package control.task;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import dao.DaoDefautQualite;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.ComposantSonar;
import model.bdd.DefautAppli;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.enums.EtatDefaut;
import model.enums.InstanceSonar;
import model.enums.Matiere;
import model.enums.OptionMajCompos;
import model.enums.Param;
import model.enums.QG;
import model.enums.TypeAction;
import model.enums.TypeColSuivi;
import model.enums.TypeDefaut;
import model.enums.TypeInfo;
import model.enums.TypeMajSuivi;
import model.enums.TypeVersion;
import model.rest.sonarapi.QualityProfile;
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

    private static final short DUPLI = 3;

    private TypeMajSuivi typeMaj;
    private DaoDefautQualite daoDefautQualite;

    /*---------- CONSTRUCTEURS ----------*/

    public MajSuiviExcelTask(TypeMajSuivi typeMaj)
    {
        super(typeMaj.getNbreEtapes(), TITRE);
        this.typeMaj = typeMaj;
        daoDefautQualite = DaoFactory.getDao(DefautQualite.class);
        annulable = false;
        startTimers();
    }

    /**
     * Constructeur pour les tests, ne pas utiliser. Lance une exception
     */
    public MajSuiviExcelTask()
    {
        super(0, TITRE);
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
                majSuiviDefautsAppli();
                break;

            case DATASTAGE:
                majFichierSuiviExcelDataStage();
                break;

            case COBOL:
                majFichierSuiviExcelCOBOL();
                break;

            case ANDROID:
                MajComposantsSonarMCTask recupCompos = new MajComposantsSonarMCTask(OptionMajCompos.COMPLETE);
                recupCompos.affilierTache(this);
                recupCompos.call();
                majFichierSuiviExcelAndroid();
                break;

            case IOS:
                recupCompos = new MajComposantsSonarMCTask(OptionMajCompos.COMPLETE);
                recupCompos.affilierTache(this);
                recupCompos.call();
                majFichierSuiviExcelIOS();
                break;

            case MULTI:
                traitementSuiviExcelToutFichiers();
                majSuiviDefautsAppli();
                break;

            case NUIT:
                traitementSuiviExcelNuit();
                majSuiviDefautsAppli();
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
        MajComposantsSonarTask task = new MajComposantsSonarTask(OptionMajCompos.COMPLETE);
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

        // Traitement fichier Androïd
        majFichierSuiviExcelAndroid();

        // Traitement fichier iOS
        majFichierSuiviExcelIOS();
    }

    /**
     * Mise à jour du fichier Excel des suivi des anomalies pour les composants Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private void majFichierSuiviExcelDataStage() throws IOException
    {
        // Appel de la récupération des composants datastage avec les vesions en paramètre
        List<ComposantSonar> composants = recupererComposantsSonar(Matiere.DATASTAGE, InstanceSonar.LEGACY);

        // Mise à jour des liens des composants datastage avec le bon QG
        etapePlus();
        liensQG(composants, proprietesXML.getMapParams().get(Param.NOMQGDATASTAGE));

        // Traitement du fichier datastage de suivi
        traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERDATASTAGE), Matiere.DATASTAGE);
    }

    /**
     * Mise à jour du fichier Excel du suivi des anomalies pour tous les composants COBOL
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private void majFichierSuiviExcelCOBOL() throws IOException
    {
        // Appel de la récupération des composants non datastage avec les vesions en paramètre
        List<ComposantSonar> composants = recupererComposantsSonar(Matiere.COBOL, InstanceSonar.LEGACY);
        etapePlus();

        // Traitement du fichier de suivi

        traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERCOBOL), Matiere.COBOL);
    }

    /**
     * Mise à jour du fichier Excel des suivis d'anomalies pour tous les composants non Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private void majFichierSuiviExcelJAVA() throws IOException
    {
        // Appel de la récupération des composants non datastage avec les vesions en paramètre
        List<ComposantSonar> composants = recupererComposantsSonar(Matiere.JAVA, InstanceSonar.LEGACY);
        etapePlus();

        // Traitement du fichier de suivi
        traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERJAVA), Matiere.JAVA);
    }

    private void majFichierSuiviExcelAndroid() throws IOException
    {
        // Appel de la récupération des composants non datastage avec les vesions en paramètre
        List<ComposantSonar> composants = recupererComposantsSonar(Matiere.JAVA, InstanceSonar.MOBILECENTER);
        etapePlus();

        // Traitement du fichier de suivi
        traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERANDROID), Matiere.ANDROID);
    }

    private void majFichierSuiviExcelIOS() throws IOException
    {
        // Appel de la récupération des composants non datastage avec les vesions en paramètre
        List<ComposantSonar> composants = recupererComposantsSonar(Matiere.JAVA, InstanceSonar.MOBILECENTER);
        etapePlus();

        // Traitement du fichier de suivi
        traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERIOS), Matiere.IOS);
    }

    private void majSuiviDefautsAppli() throws Exception
    {
        MajSuiviAppsTask majSuiviAppsTask = new MajSuiviAppsTask();
        majSuiviAppsTask.affilierTache(this);
        majSuiviAppsTask.call();

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
    private void traitementFichierSuivi(List<ComposantSonar> composants, String fichier, Matiere matiere) throws IOException
    {

        // 1. Mise à jour de la base des anomalies et des lots en erreur
        lotSonarQGError(composants);

        etapePlus();
        baseMessage = "Mise à jour du fichier Excel";
        updateMessage("");
        updateProgress(-1, 1);

        // 3. Mise à jour du fichier Excel
        majFichierAnomalies(fichier, matiere);
    }

    /**
     * Met à jour et crée les nouvelles anomalies depuis les informations de la table des composants
     *
     * @param versions
     * @return
     */
    private void lotSonarQGError(List<ComposantSonar> composants)
    {
        Map<String, DefautQualite> dqsEnBase = daoDefautQualite.readAllMap();
        List<DefautQualite> dqInit = new ArrayList<>();
        List<LotRTC> lotsTraites = new ArrayList<>();

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

            traitementCompo(compo, dqsEnBase, dqInit, lotsTraites);
        }

        daoDefautQualite.persist(dqsEnBase.values());
        DaoFactory.getDao(LotRTC.class).persist(lotsTraites);
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
    private void majFichierAnomalies(String fichier, Matiere matiere) throws IOException
    {
        // Récupération des anomalies en base
        Map<String, DefautQualite> mapDqsEnBase = daoDefautQualite.readAllMapMatiere(matiere);

        // Controleur
        String name = proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + fichier;
        ControlSuivi controlSuivi = ExcelFactory.getReader(TypeColSuivi.class, new File(name));
        ControlRapport controlRapport = controlSuivi.createControlRapport(matiere.getTypeRapport());

        // Lecture du fichier pour remonter les anomalies en cours.
        List<DefautQualite> listeDqExcel = controlSuivi.recupDonneesDepuisExcel();

        // Affichage
        int size = listeDqExcel.size();
        int i = 0;
        long debut = System.currentTimeMillis();

        // Mise à jour de la base de donnée des anomalies
        for (DefautQualite dqExcel : listeDqExcel)
        {

            DefautQualite dqBase = mapDqsEnBase.get(dqExcel.getLotRTC().getLot());
            if (dqBase == null)
            {
                LOGPLANTAGE.error("Ano du fichier excel inconnue en base : " + dqExcel.getLotRTC().getLot());
                continue;
            }

            // Mise à jour des données depuis Excel
            dqBase.setRemarque(dqExcel.getRemarque());
            dqBase.setAction(dqExcel.getAction());
            dqBase.setDateRelance(dqExcel.getDateRelance());

            // Mise à jour des données des anomalies RTC
            ControlRTC.INSTANCE.controleAnoRTC(dqBase);

            // Gestion des action de création et de cloture des anomalies
            gestionAction(dqBase, controlRapport);

            // Affichage
            i++;
            calculTempsRestant(debut, i, size);
            updateProgress(i, size);
            updateMessage("");
        }

        // Sauvegarde fichier et maj feuille principale
        Sheet sheet = controlSuivi.sauvegardeFichier(fichier);

        // Mis à jour de la feuille principale
        controlSuivi.majFeuilleDefaultsQualite(new ArrayList<>(mapDqsEnBase.values()), sheet, matiere);

        // Persistance des données
        daoDefautQualite.persist(mapDqsEnBase.values());

        controlSuivi.calculStatistiques(new ArrayList<>(mapDqsEnBase.values()));

        // Ecriture et Fermeture controleur
        write(controlSuivi);
        controlSuivi.close();
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
     * @param dqInit
     * @param lotsTraites
     * @param dasEnBase
     */
    private void traitementCompo(ComposantSonar compo, Map<String, DefautQualite> dqEnBase, List<DefautQualite> dqInit, List<LotRTC> lotsTraites)
    {
        LotRTC lotRTC = compo.getLotRTC();

        // Vérification que le lot est bien valorisé et controle le QG
        TypeDefaut type = controleQGAppliBloquant(compo, lotsTraites);
        if (lotRTC == null || type == null)
            return;

        DefautQualite dq;

        if (dqEnBase.containsKey(lotRTC.getLot()))
        {
            dq = dqEnBase.get(lotRTC.getLot());

            // Initialisation d'un défault avec les valeurs à faux pour la sécurité, à SNAPSHOT pour la version, et avec le typeDefaut du composant
            // On effectue cette opération qu'une fois pour le premier composant qui mets à jour l'anomalie.
            if (!dqInit.contains(dq))
            {
                dqInit.add(dq);
                dq.setSecurite(false);
                dq.setTypeVersion(TypeVersion.SNAPSHOT);
                dq.setTypeDefaut(type);
            }
        }
        else
        {
            dq = ModelFactory.build(DefautQualite.class);
            dq.setLotRTC(lotRTC);
        }

        // Contrôle pour vérifier si le composant a une erreur de sécurité
        if (compo.isSecurite())
            dq.setSecurite(true);

        // Contrôle du composant pour voir s'il a une version RELEASE ou SNAPSHOT
        if (compo.isVersionRelease())
            dq.setTypeVersion(TypeVersion.RELEASE);

        // Mise à jour de la date de repack
        if (dq.getLotRTC().getDateRepack() != null)
            dq.setDateMepPrev(dq.getLotRTC().getDateRepack());
        else
            dq.setDateMepPrev(dq.getLotRTC().getEdition().getDateMEP());

        // Mise à jour du type de défaut
        gestionTypeDefaut(dq, type);

        // Ajout du défaut application
        if (type == TypeDefaut.MIXTE || type == TypeDefaut.APPLI)
        {
            dq.getDefautsAppli().add(compo.getDefautAppli());
            compo.getDefautAppli().setDefautQualite(dq);
        }

        dqEnBase.put(dq.getLotRTC().getLot(), dq);
    }

    /**
     * Controle si le QG est en erreur, et vérifie qu'il y a bien au moins une erreur bloquante, critique, ou une duplication de code. Contrôle si le composant à
     * une erreur de code application.
     * 
     * @param lotsTraites
     * 
     * @param dasEnBase
     * 
     * @param metriques
     * @return
     */
    private TypeDefaut controleQGAppliBloquant(ComposantSonar compo, List<LotRTC> lotsTraites)
    {
        // Mise à OK des lots la première fois puis ajout à la liste des lots traités.
        LotRTC lot = compo.getLotRTC();
        if (!lotsTraites.contains(lot))
        {
            lot.setQualityGate(QG.OK);
            lotsTraites.add(lot);
        }

        boolean avecErreurs = compo.getVraisDefauts() > 0;

        boolean qgBloquant = compo.getQualityGate() == QG.ERROR && (compo.getBloquants() > 0 || compo.getCritiques() > 0 || compo.getDuplication() > DUPLI);

        boolean appli = compo.getDefautAppli() != null;

        // Calcul QG lot
        if (qgBloquant && avecErreurs)
            lot.setQualityGate(QG.ERROR);
        else if (qgBloquant && lot.getQualityGate() != QG.ERROR)
            lot.setQualityGate(QG.WARN);

        // Retour type défaut
        if (qgBloquant && avecErreurs && appli)
            return TypeDefaut.MIXTE;
        if (qgBloquant && avecErreurs)
            return TypeDefaut.SONAR;
        if (appli)
            return TypeDefaut.APPLI;
        return null;
    }

    /**
     * Mets à jour le type du défaut qualité selon les erreurs du composants (APPLI, SONAR ou MIXTE)
     * 
     * @param dq
     * @param type
     */
    private void gestionTypeDefaut(DefautQualite dq, TypeDefaut type)
    {
        switch (dq.getTypeDefaut())
        {
            case APPLI:
                if (type == TypeDefaut.SONAR || type == TypeDefaut.MIXTE)
                    dq.setTypeDefaut(TypeDefaut.MIXTE);
                break;

            case INCONNU:
                dq.setTypeDefaut(type);
                break;

            case MIXTE:
                break;

            case SONAR:
                if (type == TypeDefaut.APPLI || type == TypeDefaut.MIXTE)
                    dq.setTypeDefaut(TypeDefaut.MIXTE);
                break;

            default:
                break;
        }
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
        QualityProfile qg = api.getQualityGate(nomQG);

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
     * @param dq
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
    private void gestionAction(DefautQualite dq, ControlRapport controlRapport)
    {
        // Atention plusieurs cases n'ont pas d'action et sont regroupés
        switch (dq.getAction())
        {
            case ABANDONNER:
                clotureAno(dq, controlRapport, EtatDefaut.ABANDONNE);
                break;

            case CLOTURER:
                clotureAno(dq, controlRapport, EtatDefaut.CLOS);
                break;

            case CREER:
                creationAno(dq, controlRapport);
                break;

            case AJOUTCOMM:
                ajoutercomm(dq);
                break;

            case RELANCER:
                relancerAno(dq);
                break;

            case REOUV:
                rouvrirAno(dq);
                break;

            case ASSEMBLER:
            case VERIFIER:
            case VIDE:
                dq.controleLiens();
                break;

            default:
                throw new TechnicalException("control.task.MajSuiviExcelTask.gestionAction - type d'action inconnue : " + dq.getAction());
        }
    }

    /**
     * Relance d'une anomalie
     * 
     * @param dq
     */
    private void relancerAno(DefautQualite dq)
    {
        try
        {
            if (dq.getNumeroAnoRTC() != 0)
            {
                ControlRTC.INSTANCE.relancerAno(dq.getNumeroAnoRTC());
                dq.setDateRelance(LocalDate.now());
            }
            dq.setAction(TypeAction.VIDE);
        }
        catch (TeamRepositoryException e)
        {
            LOGPLANTAGE.error(e);
        }
        dq.controleLiens();
    }

    /**
     * Ajout d'un commentaire de code applicaiton sur une anomalie RTC
     * 
     * @param dq
     */
    private void ajoutercomm(DefautQualite dq)
    {
        if (dq.getNumeroAnoRTC() != 0)
        {
            for (DefautAppli da : dq.getDefautsAppli())
            {
                if (da.getEtatDefaut() == EtatDefaut.NOUVEAU && ControlRTC.INSTANCE.ajoutCommAppliAnoRTC(da, dq.getNumeroAnoRTC()))
                    da.setEtatDefaut(EtatDefaut.TRAITE);
            }
        }
    }

    /**
     * Création d'une anomalie RTC avec les commentaire sur les codes application
     * 
     * @param dq
     * @param controlRapport
     */
    private void creationAno(DefautQualite dq, ControlRapport controlRapport)
    {
        int numeroAno = ControlRTC.INSTANCE.creerAnoRTC(dq);
        if (numeroAno != 0)
        {
            dq.setAction(TypeAction.VIDE);
            dq.setNumeroAnoRTC(numeroAno);
            dq.setDateCreation(LocalDate.now());
            dq.calculTraitee();
            controlRapport.addInfo(TypeInfo.ANOSRTCCREES, dq.getLotRTC().getLot(), null);
            for (DefautAppli da : dq.getDefautsAppli())
            {
                if (da.getEtatDefaut() == EtatDefaut.NOUVEAU && ControlRTC.INSTANCE.ajoutCommAppliAnoRTC(da, numeroAno))
                    da.setEtatDefaut(EtatDefaut.TRAITE);
            }
        }
    }

    /**
     * Réouverture d'une anomalie
     * 
     * @param dq
     */
    private void rouvrirAno(DefautQualite dq)
    {
        try
        {
            ControlRTC.INSTANCE.reouvrirAnoRTC(dq.getNumeroAnoRTC());
            dq.setDateReouv(LocalDate.now());
            dq.setAction(TypeAction.VIDE);
        }
        catch (TeamRepositoryException e)
        {
            LOGPLANTAGE.error(e);
        }
    }

    /**
     * Cloture ou abadon d'un anomalie RTC
     * 
     * @param dq
     * @param controlRapport
     * @param clos
     */
    private void clotureAno(DefautQualite dq, ControlRapport controlRapport, EtatDefaut etat)
    {
        if (controlEtFermetureAno(dq))
        {
            dq.setEtatDefaut(etat);
            for (DefautAppli da : dq.getDefautsAppli())
            {
                da.setEtatDefaut(etat);
            }
            dq.setAction(TypeAction.VIDE);
            controlRapport.addInfo(TypeInfo.ANOABANDON, dq.getLotRTC().getLot(), String.valueOf(dq.getNumeroAnoRTC()));
        }
        dq.controleLiens();
    }

    private boolean controlEtFermetureAno(DefautQualite dq)
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
