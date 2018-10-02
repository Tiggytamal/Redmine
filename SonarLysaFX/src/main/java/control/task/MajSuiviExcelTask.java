package control.task;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.mchange.util.AssertException;

import control.excel.ControlSuivi;
import control.excel.ExcelFactory;
import dao.DaoAnomalie;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.Anomalie;
import model.bdd.ComposantSonar;
import model.bdd.LotRTC;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.ParamBool;
import model.enums.QG;
import model.enums.TypeColSuivi;
import model.enums.TypeInfo;
import model.enums.TypeMajSuivi;
import model.enums.TypeRapport;
import model.enums.TypeVersion;
import model.sonarapi.QualityGate;
import model.sonarapi.Vue;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Tâche permettant de mettre à jour les fichiers Excel de suivi des anomalies Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class MajSuiviExcelTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Maj Suivi anomalies";

    /** logger applications non listée dans le référentiel */
    private static final Logger LOGNONLISTEE = LogManager.getLogger("nonlistee-log");
    /** logger plantage */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    private static final short ETAPES = 6;
    private static final short DUPLI = 3;

    private TypeMajSuivi typeMaj;

    /*---------- CONSTRUCTEURS ----------*/

    public MajSuiviExcelTask(TypeMajSuivi typeMaj)
    {
        super(ETAPES, TITRE);
        this.typeMaj = typeMaj;
        annulable = false;
    }

    /**
     * Constructeur pour les tests, ne pas utiliser. Lance une exception
     */
    public MajSuiviExcelTask()
    {
        super(ETAPES, TITRE);
        throw new AssertException();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return majSuiviExcel();
    }

    @Override
    public void annuler()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * 
     * @return
     * @throws Exception
     * @throws JAXBException
     */
    private boolean majSuiviExcel() throws Exception
    {
        // Mise à jour du fichier RTC à la date du jour.
        new MajLotsRTCTask(null).call();

        updateProgress(-1, 0);
        etapePlus();

        // Mise à jour d'un fichier ou des deux , selon le type de mise à jour.
        switch (typeMaj)
        {
            case JAVA:
                majFichierSuiviExcelJAVA();
                break;

            case DATASTAGE:
                majFichierSuiviExcelDataStage();
                break;

            case COBOL:
                majFichierSuiviExcelCOBOL();
                break;

            case MULTI:
                // Appel de la mise à jour des composants Sonar
                try
                {
                    new CreerListeComposantsTask().call();
                }
                catch (Exception e)
                {
                    LOGPLANTAGE.error(e);
                    throw new TechnicalException("Plantage mise à jour des composants Sonar", e);
                }
                traitementSuiviExcelToutFichiers();
                break;
        }
        return true;
    }

    /**
     * Traitement des deux fichiers Excel de Suivi.
     * 
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private void traitementSuiviExcelToutFichiers() throws IOException
    {
        // Récupération anomalies Datastage
        List<String> anoDatastage = majFichierSuiviExcelDataStage();

        // Récupération anomalies Java
        initEtape(ETAPES - 1);
        List<String> anoJava = majFichierSuiviExcelJAVA();

        // Liste des anomalies sur plusieures matières
        List<String> anoMultiple = new ArrayList<>();
        for (String string : anoDatastage)
        {
            if (anoJava.contains(string))
                anoMultiple.add(string);
        }

        // Mise à jour des fichiers Excel
        ControlSuivi controlAnoJava = ExcelFactory.getReader(TypeColSuivi.class,
                new File(proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + proprietesXML.getMapParams().get(Param.NOMFICHIERJAVA)));
        controlAnoJava.createControlRapport(TypeRapport.SUIVIJAVA);
        controlAnoJava.majMultiMatiere(anoMultiple);
        write(controlAnoJava);
        controlAnoJava.close();

        ControlSuivi controlAnoDataStage = ExcelFactory.getReader(TypeColSuivi.class,
                new File(proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + proprietesXML.getMapParams().get(Param.NOMFICHIERDATASTAGE)));
        controlAnoDataStage.createControlRapport(TypeRapport.SUIVIDATASTAGE);
        controlAnoDataStage.majMultiMatiere(anoMultiple);
        write(controlAnoDataStage);
        controlAnoDataStage.close();
    }

    /**
     * Mise à jour du fichier Excel des suivi des anomalies pour les composants Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private List<String> majFichierSuiviExcelDataStage() throws IOException
    {
        // Appel de la récupération des composants datastage avec les vesions en paramètre
        Map<String, ComposantSonar> composants = recupererComposantsSonar(Matiere.DATASTAGE);

        // Mise à jour des liens des composants datastage avec le bon QG
        etapePlus();
        liensQG(composants.values(), proprietesXML.getMapParams().get(Param.NOMQGDATASTAGE));

        // Traitement du fichier datastage de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERDATASTAGE), Matiere.DATASTAGE);
    }

    /**
     * Mise à jour du fichier Excel du suivi des anomalies pour tous les composants COBOL
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private List<String> majFichierSuiviExcelCOBOL() throws IOException
    {
        // Appel de la récupération des composants non datastage avec les vesions en paramètre
        Map<String, ComposantSonar> composants = recupererComposantsSonar(Matiere.COBOL);
        etapePlus();

        // Traitement du fichier de suivi

        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERCOBOL), Matiere.COBOL);
    }

    /**
     * Mise à jour du fichier Excel des suivis d'anomalies pour tous les composants non Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private List<String> majFichierSuiviExcelJAVA() throws IOException
    {
        // Appel de la récupération des composants non datastage avec les vesions en paramètre
        Map<String, ComposantSonar> composants = recupererComposantsSonar(Matiere.JAVA);
        etapePlus();

        // Traitement du fichier de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERJAVA), Matiere.JAVA);
    }

    /**
     * Méthode de traitement pour mettre à jour les fichiers de suivi d'anomalies ainsi que la création de vue dans SonarQube. <br>
     * Retourne une liste de tous les lots Sonar en erreur.
     *
     * @param composants
     * @param java
     * @param versions
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private List<String> traitementFichierSuivi(Map<String, ComposantSonar> composants, String fichier, Matiere matiere) throws IOException
    {

        // 1. Mise à jour de la base des anomalies
        lotSonarQGError(composants);

        etapePlus();
        updateMessage("Mise à jour du fichier Excel...");
        updateProgress(-1, 1);

        // 3. Supression des lots déjà créés et création des feuille Excel avec les nouvelles erreurs
        majFichierAnomalies(fichier, matiere);

        updateProgress(1, 1);
        etapePlus();

        // 4. Création des vues si le paramètrage est activé
        if (proprietesXML.getMapParamsBool().get(ParamBool.VUESSUIVI))
        {
            
            for (Map.Entry<String, Set<String>> entry : lotsSonarQGError.entrySet())
            {
                // Création de la vue, gestion du message et ajout à la liste de vues créées en cas d'annulation
                String nom = fichier.replace("_", Statics.SPACE).split("\\.")[0];
                String nomVue = nom + " - Edition " + entry.getKey();
                String base = "Création Vue " + nomVue + Statics.NL;
                updateMessage(base);
                Vue vueParent = creerVue(nom.replace(Statics.SPACE, Statics.EMPTY) + "Key" + entry.getKey(), nomVue, "Vue regroupant tous les lots avec des composants en erreur", true);

                // Ajout des sous-vue
                int i = 0;
                int size = entry.getValue().size();
                for (String lot : entry.getValue())
                {
                    // Traitement + message
                    String nomLot = "Lot " + lot;
                    updateMessage(base + "Ajout : " + nomLot);
                    i++;
                    updateProgress(i, size);
                    api.ajouterSousVue(new Vue("view_lot_" + lot, nomLot), vueParent);
                }
            }
        }

        // Traitement liste de retour
        List<String> retour = new ArrayList<>();
        for (Set<String> liste : lotsSonarQGError.values())
        {
            retour.addAll(liste);
        }
        return retour;
    }

    /**
     * Met à jour et crée les nouvelles anomalies depuis les informations de la table des composants
     *
     * @param versions
     * @return
     */
    private Set<String> lotSonarQGError(Map<String, ComposantSonar> composants)
    {
        // Création de la map de retour
        Set<String> retour = new HashSet<>();

        DaoAnomalie dao = DaoFactory.getDao(Anomalie.class);
        Map<String, Anomalie> anoEnBase = dao.readAllMap();
        Collection<ComposantSonar> collec = composants.values();

        // Message
        baseMessage = "Composant : ";
        int i = 0;
        int size = collec.size();

        // Itération sur les composants pour remplir la map de retour avec les lot en erreur par version
        for (ComposantSonar compo : collec)
        {
            // Message
            i++;
            updateProgress(i, size);
            updateMessage(compo.getNom());

            traitementCompo(compo, retour, anoEnBase);
        }

        dao.persist(anoEnBase.values());
        return retour;
    }

    /**
     * Permet de mettre à jour le fichier des anomalies Sonar, en allant chercher les nouvelles dans Sonar et en vérifiant celles qui ne sont plus d'actualité.
     *
     * @param lotsSonarQGError
     *            map des lots Sonar avec une quality Gate en erreur
     * @param lotsSecurite
     * @param lotRelease
     * @param matiere
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private void majFichierAnomalies(String fichier, Matiere matiere) throws IOException
    {
        // Récupération des anomalies en base
        DaoAnomalie dao = DaoFactory.getDao(Anomalie.class);
        Map<String, Anomalie> anoEnBase = dao.readAllMap();

        // Controleur
        String name = proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + fichier;
        ControlSuivi controlAno = ExcelFactory.getReader(TypeColSuivi.class, new File(name));
        controlAno.createControlRapport(matiere.getTypeRapport());

        // Lecture du fichier pour remonter les anomalies en cours.
        List<Anomalie> listeAnoExcel = controlAno.recupDonneesDepuisExcel();

        // Mise à jour de la base de donnée depuis les information du fichier Excel
        for (Anomalie anoExcel : listeAnoExcel)
        {
            Anomalie anoBase = anoEnBase.get(anoExcel.getLotRTC().getLot());
            if (anoBase == null)
            {
                System.out.println("Ano du fichier excel inconnue en base : " + anoExcel.getLotRTC().getLot());
                continue;
            }

            anoBase.setRemarque(anoExcel.getRemarque());
            anoBase.setAction(anoExcel.getAction());
            anoBase.setDateRelance(anoExcel.getDateRelance());
        }

        // Persistance des données
        dao.persist(anoEnBase.values());

        // Sauvegarde fichier et maj feuille principale
        Sheet sheet = controlAno.sauvegardeFichier(fichier);

        // Mis à jour de la feuille principale
        controlAno.majFeuillePrincipale(anoEnBase.values(), sheet, matiere);

        // Ecriture et Fermeture controleur
        write(controlAno);
        controlAno.close();
    }

    /**
     * Traitement Sonar d'un projet
     *
     * @param compo
     *            Coposant à traiter
     * @param retour
     *            Map de retour avec les lots en erreur par version
     * @param entryKey
     *            Numéro de version
     * @param lotsSecurite
     *            Lots qui ont un problème de sécurité
     * @param lotsRelease
     *            Lots qui sont en version RELEASE
     * @param base
     *            Base pour l'affichage du message dans la fenêtre d'execution
     * @param anoEnBase
     */
    private void traitementCompo(ComposantSonar compo, Set<String> retour, Map<String, Anomalie> anoEnBase)
    {
        LotRTC lotRTC = compo.getLotRTC();

        // Vérification que le lot est bien valorisé et controle le QG
        if (lotRTC == null || !controleQGBloquant(compo))
            return;

        Anomalie ano;

        if (anoEnBase.containsKey(lotRTC.getLot()))
            ano = anoEnBase.get(lotRTC.getLot());
        else
        {
            ano = ModelFactory.getModel(Anomalie.class);
            ano.setLotRTC(lotRTC);
        }

        // Ajout du lot à la liste de retour s'il y a des défaults critiques ou bloquants ou de duplication de code
        retour.add(lotRTC.getLot());

        // Contrôle pour vérifier si le composant a une erreur de sécurité
        if (compo.isSecurite())
            ano.setSecurite(true);

        // Contrôle du composant pour voir s'il a une version RELEASE ou SNAPSHOT
        if (compo.isVersionRelease())
            ano.setTypeVersion(TypeVersion.RELEASE);

        anoEnBase.put(ano.getLotRTC().getLot(), ano);
    }

    /**
     * Controle si le QG est en erreur, et vérifie qu'il y a bien au moins une erreur bloquante, critique, ou une duplication de code.
     * 
     * @param metriques
     * @return
     */
    private boolean controleQGBloquant(ComposantSonar compo)
    {
        return compo.getQualityGate() == QG.ERROR && (compo.getBloquants() > 0 || compo.getCritiques() > 0 || compo.getDuplication() > DUPLI);
    }

    /**
     * Permet de lier tous les composants sonar à une QG particulière
     *
     * @param composants
     * @param nomQG
     */
    private void liensQG(Collection<ComposantSonar> composants, String nomQG)
    {
        // Récupération de l'Id de la QualityGate
        QualityGate qg = api.getQualityGate(nomQG);

        // Préparation message
        String base = "Association avec le QG DataStage :" + Statics.NL;
        int i = 0;
        int size = composants.size();

        // Iteration sur tous les composants pour les associer au QualityGate
        for (ComposantSonar compo : composants)
        {
            // Message
            updateMessage(base + compo.getNom());
            i++;
            updateProgress(i, size);

            api.associerQualitygate(compo, qg);
        }

    }

    /**
     * Méthode d'écriture des fichier pour premettre les tests.
     * 
     * @param control
     * @return
     */
    private boolean write(ControlSuivi control)
    {
        return control.write();
    }

    /*---------- ACCESSEURS ----------*/
}
