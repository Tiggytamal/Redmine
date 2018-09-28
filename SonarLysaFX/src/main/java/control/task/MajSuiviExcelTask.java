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
import model.sonarapi.QualityGate;
import model.sonarapi.Vue;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * T�che permettant de mettre � jour les fichiers Excel de suivi des anomalies Sonar.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public class MajSuiviExcelTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Maj Suivi anomalies";

    /** logger applications non list�e dans le r�f�rentiel */
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
        // Mise � jour du fichier RTC � la date du jour.
        new MajLotsRTCTask(null).call();

        updateProgress(-1, 0);
        etapePlus();

        // Mise � jour d'un fichier ou des deux , selon le type de mise � jour.
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
                // Appel de la mise � jour des composants Sonar
                try
                {
                    new CreerListeComposantsTask().call();
                }
                catch (Exception e)
                {
                    LOGPLANTAGE.error(e);
                    throw new TechnicalException("Plantage mise � jour des composants Sonar", e);
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
        // R�cup�ration anomalies Datastage
        List<String> anoDatastage = majFichierSuiviExcelDataStage();

        // R�cup�ration anomalies Java
        initEtape(ETAPES - 1);
        List<String> anoJava = majFichierSuiviExcelJAVA();

        // Liste des anomalies sur plusieures mati�res
        List<String> anoMultiple = new ArrayList<>();
        for (String string : anoDatastage)
        {
            if (anoJava.contains(string))
                anoMultiple.add(string);
        }

        // Mise � jour des fichiers Excel
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
     * Mise � jour du fichier Excel des suivi des anomalies pour les composants Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private List<String> majFichierSuiviExcelDataStage() throws IOException
    {
        // Appel de la r�cup�ration des composants datastage avec les vesions en param�tre
        Map<String, List<ComposantSonar>> composants = recupererComposantsSonarVersion(Matiere.DATASTAGE);

        // Mise � jour des liens des composants datastage avec le bon QG
        etapePlus();
        liensQG(composants.values(), proprietesXML.getMapParams().get(Param.NOMQGDATASTAGE));

        // Traitement du fichier datastage de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERDATASTAGE), Matiere.DATASTAGE);
    }

    /**
     * Mise � jour du fichier Excel du suivi des anomalies pour tous les composants COBOL
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private List<String> majFichierSuiviExcelCOBOL() throws IOException
    {
        // Appel de la r�cup�ration des composants non datastage avec les vesions en param�tre
        Map<String, List<ComposantSonar>> composants = recupererComposantsSonarVersion(Matiere.COBOL);
        etapePlus();

        // Traitement du fichier de suivi

        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERCOBOL), Matiere.COBOL);
    }

    /**
     * Mise � jour du fichier Excel des suivis d'anomalies pour tous les composants non Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private List<String> majFichierSuiviExcelJAVA() throws IOException
    {
        // Appel de la r�cup�ration des composants non datastage avec les vesions en param�tre
        Map<String, List<ComposantSonar>> composants = recupererComposantsSonarVersion(Matiere.JAVA);
        etapePlus();

        // Traitement du fichier de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERJAVA), Matiere.JAVA);
    }

    /**
     * M�thode de traitement pour mettre � jour les fichiers de suivi d'anomalies ainsi que la cr�ation de vue dans SonarQube. <br>
     * Retourne une liste de tous les lots Sonar en erreur.
     *
     * @param composants
     * @param java
     * @param versions
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private List<String> traitementFichierSuivi(Map<String, List<ComposantSonar>> composants, String fichier, Matiere matiere) throws IOException
    {
        // 1. R�cup�ration des donn�es depuis les fichiers Excel.

        // 2. R�cup�ration des lots Sonar en erreur.

        Set<String> lotsSecurite = new HashSet<>();
        Set<String> lotsRelease = new HashSet<>();

        etapePlus();

        Map<String, Set<String>> lotsSonarQGError = lotSonarQGError(composants, lotsSecurite, lotsRelease);

        etapePlus();
        updateMessage("Mise � jour du fichier Excel...");
        updateProgress(-1, 1);

        // 3. Supression des lots d�j� cr��s et cr�ation des feuille Excel avec les nouvelles erreurs
        majFichierAnomalies(lotsSonarQGError, lotsSecurite, lotsRelease, fichier, matiere);

        updateProgress(1, 1);
        etapePlus();

        // 4. Cr�ation des vues si le param�trage est activ�
        if (proprietesXML.getMapParamsBool().get(ParamBool.VUESSUIVI))
        {
            for (Map.Entry<String, Set<String>> entry : lotsSonarQGError.entrySet())
            {
                // Cr�ation de la vue, gestion du message et ajout � la liste de vues cr��es en cas d'annulation
                String nom = fichier.replace("_", Statics.SPACE).split("\\.")[0];
                String nomVue = nom + " - Edition " + entry.getKey();
                String base = "Cr�ation Vue " + nomVue + Statics.NL;
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
     * R�cup�re tous les composants Sonar des versions choisies avec une qualityGate en erreur.<br>
     * la clef de la map correspond � la version, et la valeur, � la liste des lots en erreur de cette version.
     *
     * @param versions
     * @return
     */
    private Map<String, Set<String>> lotSonarQGError(Map<String, List<ComposantSonar>> composants, Set<String> lotsSecurite, Set<String> lotsRelease)
    {
        // Cr�ation de la map de retour
        HashMap<String, Set<String>> retour = new HashMap<>();

        // It�ration sur les composants pour remplir la map de retour avec les lot en erreur par version
        for (Map.Entry<String, List<ComposantSonar>> entry : composants.entrySet())
        {
            String entryKey = entry.getKey();
            retour.put(entryKey, new TreeSet<>());

            String base = "Traitement Version : " + entryKey + Statics.NL;
            updateMessage(base);
            int i = 0;
            int size = entry.getValue().size();

            // Iteration sur la liste des projets
            for (ComposantSonar compo : entry.getValue())
            {
                traitementProjet(compo, retour, entryKey, lotsSecurite, lotsRelease, base);
                i++;
                updateProgress(i, size);
            }
        }
        return retour;
    }

    /**
     * Permet de mettre � jour le fichier des anomalies Sonar, en allant chercher les nouvelles dans Sonar et en v�rifiant celles qui ne sont plus d'actualit�.
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
    private void majFichierAnomalies(Map<String, Set<String>> lotsSonarQGError, Set<String> lotsSecurite, Set<String> lotRelease, String fichier, Matiere matiere) throws IOException
    {
        // Fichier des lots �dition
        Map<String, LotRTC> lotsRTC = DaoFactory.getDao(LotRTC.class).readAllMap();
        Map<String, Anomalie> anoEnBase = DaoFactory.getDao(Anomalie.class).readAllMap();

        // Controleur
        String name = proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + fichier;
        ControlSuivi controlAno = ExcelFactory.getReader(TypeColSuivi.class, new File(name));
        controlAno.createControlRapport(matiere.getTypeRapport());

        // Lecture du fichier pour remonter les anomalies en cours.
        List<Anomalie> listeAnoExcel = controlAno.recupDonneesDepuisExcel();

        // Liste des anomalies � ajouter apr�s traitement
        List<Anomalie> anoAajouter = new ArrayList<>();

        // Mise dans un Set de tous les lots en erreur venus de Sonar ind�pendement de la version des composants.
        Set<String> lotsEnErreur = new TreeSet<>();
        for (Set<String> value : lotsSonarQGError.values())
        {
            lotsEnErreur.addAll(value);
        }

        // It�ration sur les lots en erreurs venant de Sonar pour chaque version de composants (13, 14, ...)
        for (Entry<String, Set<String>> entry : lotsSonarQGError.entrySet())
        {
            List<Anomalie> anoACreer = new ArrayList<>();
            List<Anomalie> anoDejacrees = new ArrayList<>();

            // Iteration sur toutes les anomalies venant de Sonar pour chaque version
            for (String numeroLot : entry.getValue())
            {
                // On va chercher les informations de ce lot dans le fichier des lots de la PIC. Si on ne les trouve pas, il faudra mettre � jour ce fichier
                LotRTC lot = lotsRTC.get(numeroLot);
                if (lot == null)
                {
                    LOGNONLISTEE.warn("Un lot Sonar n'est pas connu dasn RTC : " + numeroLot);
                    controlAno.getControlRapport().addInfo(TypeInfo.LOTNONRTC, numeroLot, null);
                    continue;
                }

                // On ajoute, soit le lot dans la liste des anos d�j� cr��es soit, on ajoute une nouvelle anomalie dans la liste des anoACeer.
                if (anoEnBase.containsKey(numeroLot))
                    anoDejacrees.add(anoEnBase.get(numeroLot));
                else
                    anoACreer.add(ModelFactory.getModelWithParams(Anomalie.class, lot));
            }

            // Mise � jour de la feuille des anomalies pour chaque version de composants
            anoAajouter.addAll(controlAno.createSheetError(entry.getKey(), anoACreer, anoDejacrees));
            write(controlAno);
        }

        // Sauvegarde fichier et maj feuille principale
        Sheet sheet = controlAno.sauvegardeFichier(fichier);

        // Mis � jour de la feuille principale
        controlAno.majFeuillePrincipale(listeAnoExcel, anoAajouter, lotsEnErreur, lotsSecurite, lotRelease, sheet, matiere);

        // Ecriture et Fermeture controleur
        write(controlAno);
        controlAno.close();
    }

    /**
     * Traitement Sonar d'un projet
     *
     * @param compo
     *            Coposant � traiter
     * @param retour
     *            Map de retour avec les lots en erreur par version
     * @param entryKey
     *            Num�ro de version
     * @param lotsSecurite
     *            Lots qui ont un probl�me de s�curit�
     * @param lotsRelease
     *            Lots qui sont en version RELEASE
     * @param base
     *            Base pour l'affichage du message dans la fen�tre d'execution
     */
    private void traitementProjet(ComposantSonar compo, Map<String, Set<String>> retour, String entryKey, Set<String> lotsSecurite, Set<String> lotsRelease, String base)
    {
        LotRTC lotRTC = compo.getLotRTC();
        updateMessage(base + compo.getNom());

        // V�rification que le lot est bien valoris� et controle le QG
        if (lotRTC != null && controleQGBloquant(compo))
        {
            // Ajout du lot � la liste de retour s'il y a des d�faults critiques ou bloquants ou de duplication de code
            retour.get(entryKey).add(lotRTC.getLot());

            // Contr�le pour v�rifier si le composant a une erreur de s�curit�, ce qui ajoute le lot � la listeSecurite
            if (compo.isSecurite())
                lotsSecurite.add(lotRTC.getLot());

            // Contr�le du composant pour voir s'il a une version release ou SNAPSHOT
            if (compo.isVersionRelease())
                lotsRelease.add(lotRTC.getLot());
        }
    }

    /**
     * Controle si le QG est en erreur, et v�rifie qu'il y a bien au moins une erreur bloquante, critique, ou une duplication de code.
     * 
     * @param metriques
     * @return
     */
    private boolean controleQGBloquant(ComposantSonar compo)
    {
        return compo.getQualityGate() == QG.ERROR && (compo.getBloquants() > 0 || compo.getCritiques() > 0 || compo.getDuplication() > DUPLI);
    }

    /**
     * Permet de lier tous les composants sonar � une QG particuli�re
     *
     * @param composants
     * @param nomQG
     */
    private void liensQG(Collection<List<ComposantSonar>> composants, String nomQG)
    {
        // R�cup�ration de l'Id de la QualityGate
        QualityGate qg = api.getQualityGate(nomQG);

        // Pr�paration message
        String base = "Association avec le QG DataStage :" + Statics.NL;
        int i = 0;
        int size = 0;
        for (List<ComposantSonar> liste : composants)
        {
            size += liste.size();
        }

        // Iteration sur tous les composants pour les associer au QualityGate
        for (List<ComposantSonar> liste : composants)
        {
            for (ComposantSonar compo : liste)
            {
                // Message
                updateMessage(base + compo.getNom());
                i++;
                updateProgress(i, size);

                api.associerQualitygate(compo, qg);
            }
        }
    }

    /**
     * M�thode d'�criture des fichier pour premettre les tests.
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
