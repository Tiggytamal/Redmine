package control.task;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.mchange.util.AssertException;

import control.excel.ControlSuivi;
import control.excel.ExcelFactory;
import control.rtc.ControlRTC;
import control.word.ControlRapport;
import dao.DaoAnomalie;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.DefaultQualite;
import model.bdd.ComposantSonar;
import model.bdd.LotRTC;
import model.enums.EtatDefault;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.ParamBool;
import model.enums.QG;
import model.enums.TypeColSuivi;
import model.enums.TypeInfo;
import model.enums.TypeMajSuivi;
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
    public void annulerImpl()
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
        MajLotsRTCTask task = new MajLotsRTCTask(null);
        task.affilierTache(this);
        task.call();

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
                traitementSuiviExcelToutFichiers();
                break;

            case NUIT:
                traitementSuiviExcelNuit();
                break;
        }
        return true;
    }

    /**
     * @throws Exception
     * 
     */
    private void traitementSuiviExcelNuit() throws Exception
    {
        // Mise à jour du fichier RTC à la date du jour.
        MajComposantsSonarTask task = new MajComposantsSonarTask();
        task.affilierTache(this);
        task.call();

        traitementSuiviExcelToutFichiers();
    }

    /**
     * Traitement des deux fichiers Excel de Suivi.
     * 
     * @throws IOException
     */
    private void traitementSuiviExcelToutFichiers() throws IOException
    {
        // Traitement anomalies Datastage
        majFichierSuiviExcelDataStage();

        // Traitement anomalies Java
        majFichierSuiviExcelJAVA();

        // Traitement fichier COBOL
        majFichierSuiviExcelCOBOL();
    }

    /**
     * Mise à jour du fichier Excel des suivi des anomalies pour les composants Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private Set<String> majFichierSuiviExcelDataStage() throws IOException
    {
        // Appel de la récupération des composants datastage avec les vesions en paramètre
        List<ComposantSonar> composants = recupererComposantsSonar(Matiere.DATASTAGE);

        // Mise à jour des liens des composants datastage avec le bon QG
        etapePlus();
        liensQG(composants, proprietesXML.getMapParams().get(Param.NOMQGDATASTAGE));

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
    private Set<String> majFichierSuiviExcelCOBOL() throws IOException
    {
        // Appel de la récupération des composants non datastage avec les vesions en paramètre
        List<ComposantSonar> composants = recupererComposantsSonar(Matiere.COBOL);
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
    private Set<String> majFichierSuiviExcelJAVA() throws IOException
    {
        // Appel de la récupération des composants non datastage avec les vesions en paramètre
        List<ComposantSonar> composants = recupererComposantsSonar(Matiere.JAVA);
        etapePlus();

        // Traitement du fichier de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERJAVA), Matiere.JAVA);
    }

    /**
     * Méthode de traitement pour mettre à jour les fichiers de suivi d'anomalies ainsi que la création de vue dans SonarQube. <br>
     *
     * @param composants
     * @param java
     * @param versions
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private Set<String> traitementFichierSuivi(List<ComposantSonar> composants, String fichier, Matiere matiere) throws IOException
    {

        // 1. Mise à jour de la base des anomalies et sortie des lots en erreur
        Set<String> lotsSonarQGError = lotSonarQGError(composants);

        etapePlus();
        baseMessage = "Mise à jour du fichier Excel";
        updateMessage("");
        updateProgress(-1, 1);

        // 3. Mise à jour du fichier Excel
        majFichierAnomalies(lotsSonarQGError, fichier, matiere);

        updateProgress(1, 1);
        etapePlus();

        // 4. Création des vues si le paramètrage est activé
        if (proprietesXML.getMapParamsBool().get(ParamBool.VUESSUIVI))
        {

            // Affichage et création vue parent
            String nom = fichier.replace("_", Statics.SPACE).split("\\.")[0];
            baseMessage = "Création Vue " + nom + Statics.NL;
            int i = 0;
            int size = lotsSonarQGError.size();

            Vue vueParent = creerVue(nom.replace(Statics.SPACE, Statics.EMPTY) + "Key", nom, "Vue regroupant tous les lots avec des composants en erreur", true);

            // Itération sur les lots pour créer les sous-vues
            for (String lot : lotsSonarQGError)
            {

                // Traitement + message
                String nomLot = "Lot " + lot;
                updateMessage("Ajout : " + nomLot);
                i++;
                updateProgress(i, size);
                api.ajouterSousVue(new Vue("view_lot_" + lot, nomLot), vueParent);

            }
        }
        return lotsSonarQGError;
    }

    /**
     * Met à jour et crée les nouvelles anomalies depuis les informations de la table des composants
     *
     * @param versions
     * @return
     */
    private Set<String> lotSonarQGError(List<ComposantSonar> composants)
    {
        // Création de la map de retour
        Set<String> retour = new HashSet<>();

        DaoAnomalie dao = DaoFactory.getDao(DefaultQualite.class);
        Map<String, DefaultQualite> dqsEnBase = dao.readAllMap();

        // Message
        baseMessage = "Composant : ";
        int i = 0;
        int size = composants.size();

        // Itération sur les composants pour remplir la map de retour avec les lot en erreur par version
        for (ComposantSonar compo : composants)
        {
            // Message
            i++;
            updateProgress(i, size);
            updateMessage(compo.getNom());

            traitementCompo(compo, retour, dqsEnBase);
        }

        dao.persist(dqsEnBase.values());
        return retour;
    }

    /**
     * Permet de mettre à jour le fichier des anomalies Sonar, en allant chercher les nouvelles dans Sonar et en vérifiant celles qui ne sont plus d'actualité.
     * 
     * @param lotSonarQGError
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
    private void majFichierAnomalies(Set<String> lotSonarQGError, String fichier, Matiere matiere) throws IOException
    {
        // Récupération des anomalies en base
        DaoAnomalie dao = DaoFactory.getDao(DefaultQualite.class);
        Map<String, DefaultQualite> mapDqsEnBase = dao.readAllMapMatiere(matiere);

        // Mise à jour des lots en ereur
        for (DefaultQualite dq : mapDqsEnBase.values())
        {
            if (lotSonarQGError.contains(dq.getLotRTC().getLot()))
                dq.getLotRTC().setQualityGate(QG.ERROR);
            else
                dq.getLotRTC().setQualityGate(QG.OK);
        }

        // Controleur
        String name = proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + fichier;
        ControlSuivi controlAno = ExcelFactory.getReader(TypeColSuivi.class, new File(name));
        ControlRapport controlRapport = controlAno.createControlRapport(matiere.getTypeRapport());

        // Lecture du fichier pour remonter les anomalies en cours.
        List<DefaultQualite> listeDqExcel = controlAno.recupDonneesDepuisExcel();

        // Affichage
        int size = listeDqExcel.size();
        int i = 0;
        long debut = System.currentTimeMillis();

        // Mise à jour de la base de donnée des anomalies
        for (DefaultQualite anoExcel : listeDqExcel)
        {

            DefaultQualite dqBase = mapDqsEnBase.get(anoExcel.getLotRTC().getLot());
            if (dqBase == null)
            {
                LOGPLANTAGE.error("Ano du fichier excel inconnue en base : " + anoExcel.getLotRTC().getLot());
                continue;
            }

            // Mise à jour des données depuis Excel
            dqBase.setRemarque(anoExcel.getRemarque());
            dqBase.setAction(anoExcel.getAction());
            dqBase.setDateRelance(anoExcel.getDateRelance());

            // Mise à jour des données des anomalies RTC
            ControlRTC.INSTANCE.controleAnoRTC(dqBase);

            // Gestion des action de création et de cloture des anomalies
            gestionAction(dqBase, controlRapport);

            // Affichage
            i++;
            updateProgress(i, size);
            updateMessage("" + affichageTemps(debut, i, size));
        }

        // Sauvegarde fichier et maj feuille principale
        Sheet sheet = controlAno.sauvegardeFichier(fichier);

        // Mis à jour de la feuille principale
        controlAno.majFeuillePrincipale(new ArrayList<>(mapDqsEnBase.values()), sheet, matiere);

        // Persistance des données
        dao.persist(mapDqsEnBase.values());

        controlAno.calculStatistiques(new ArrayList<>(mapDqsEnBase.values()));

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
     * @param dqEnBase
     */
    private void traitementCompo(ComposantSonar compo, Set<String> retour, Map<String, DefaultQualite> dqEnBase)
    {
        LotRTC lotRTC = compo.getLotRTC();

        // Vérification que le lot est bien valorisé et controle le QG
        if (lotRTC == null || !controleQGBloquant(compo))
            return;

        DefaultQualite dq;

        if (dqEnBase.containsKey(lotRTC.getLot()))
            dq = dqEnBase.get(lotRTC.getLot());
        else
        {
            dq = ModelFactory.getModel(DefaultQualite.class);
            dq.setLotRTC(lotRTC);
        }

        // Ajout du lot à la liste de retour s'il y a des défaults critiques ou bloquants ou de duplication de code
        retour.add(lotRTC.getLot());

        // Contrôle pour vérifier si le composant a une erreur de sécurité
        if (compo.isSecurite())
            dq.setSecurite(true);

        // Contrôle du composant pour voir s'il a une version RELEASE ou SNAPSHOT
        if (compo.isVersionRelease())
            dq.setTypeVersion(TypeVersion.RELEASE);

        dqEnBase.put(dq.getLotRTC().getLot(), dq);
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
     * Gestion des actions possibles pour une anomalie (Creer, Clôturer, Abandonner)
     * 
     * @param ano
     *            Anomalie à traiter
     * @param controlRapport
     * @param anoLot
     *            Numéro de lot l'anomalie
     * @param sheetClose
     *            Feuille des anomalies closes
     * @return {@code true} si l'on doit continuer le traitement<br>
     *         {@code false} si l'anomalie est à clôturer ou abandonner
     * @throws TeamRepositoryException 
     */
    private void gestionAction(DefaultQualite ano, ControlRapport controlRapport)
    {
        switch (ano.getAction())
        {
            case ABANDONNER:
                if (controlAno(ano))
                {
                    ano.setEtatDefault(EtatDefault.ABANDONNEE);
                    controlRapport.addInfo(TypeInfo.ANOABANDON, ano.getLotRTC().getLot(), String.valueOf(ano.getNumeroAnoRTC()));
                }
                break;

            case ASSEMBLER:
                break;

            case CLOTURER:
                if (controlAno(ano))
                {
                    ano.setEtatDefault(EtatDefault.CLOSE);
                    controlRapport.addInfo(TypeInfo.ANOABANDON, ano.getLotRTC().getLot(), String.valueOf(ano.getNumeroAnoRTC()));
                }
                break;

            case CREER:

                int numeroAno = ControlRTC.INSTANCE.creerAnoRTC(ano);
                if (numeroAno != 0)
                {
                    ano.setAction(null);
                    ano.setNumeroAnoRTC(numeroAno);
                    ano.setDateCreation(LocalDate.now());
                    ano.calculTraitee();
                    controlRapport.addInfo(TypeInfo.ANOSRTCCREES, ano.getLotRTC().getLot(), null);
                }
                break;

            case RELANCER:
                try
                {
                    ControlRTC.INSTANCE.relancerAno(ano.getNumeroAnoRTC());
                    ano.setDateRelance(LocalDate.now());
                }
                catch (TeamRepositoryException e)
                {
                    LOGPLANTAGE.error(e);
                }
                break;

            case VERIFIER:
                break;

            case VIDE:
                // Pas d'action
                break;

            default:
                throw new TechnicalException("control.task.MajSuiviExcelTask.gestionAction - type d'action inconnue : " + ano.getAction());

        }
    }

    private boolean controlAno(DefaultQualite dq)
    {
        if (dq.getNumeroAnoRTC() == 0 || Statics.ANOCLOSE.equals(dq.getEtatRTC()))
            return true;
        else
            try
            {
                return ControlRTC.INSTANCE.fermerAnoRTC(dq.getNumeroAnoRTC());
            }
            catch (TeamRepositoryException e)
            {
                LOGPLANTAGE.error(e);
                return false;
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
