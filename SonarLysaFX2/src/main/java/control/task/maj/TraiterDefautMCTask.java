package control.task.maj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.rest.ControlAppCATS;
import control.rest.SonarAPI;
import control.rtc.ControlRTC;
import control.task.AbstractTask;
import control.word.ControlSimpleFile;
import dao.Dao;
import dao.DaoApplication;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.bdd.ProjetMobileCenter;
import model.enums.InstanceSonar;
import model.enums.Matiere;
import model.enums.Metrique;
import model.enums.OptionGetCompos;
import model.enums.QG;
import model.enums.TypeDefautQualite;
import model.rest.sonarapi.ComposantSonar;
import utilities.Statics;

/**
 * Tâche de traitement pour les défauts qualité venant du Mobile Center.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class TraiterDefautMCTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    /** Nombre d'etapes du traitement */
    private static final short ETAPES = 2;

    /** Titre de la tâche */
    private static final String TITRE = "Traitement des défauts du Mobile Center";

    private static final Pattern PATTERNSPLIT = Pattern.compile(" - ");
    private static final int APPLILENGTH = 4;

    private SonarAPI apiMC;
    private List<ComposantBase> compos;

    /*---------- CONSTRUCTEURS ----------*/

    public TraiterDefautMCTask()
    {
        super(ETAPES, TITRE);
        startTimers();
        apiMC = SonarAPI.build(InstanceSonar.MOBILECENTER);
        compos = new ArrayList<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        // Mise à jour de la liste des composants
        majComposantsSonarMCTask();
        sauvegarde(compos);

        return traiterDefauts();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Création ou mise à jour du Défaut Qualité
     * 
     * @return
     *         Vrai s'il n'y a pas eu de plantage.
     */
    private boolean traiterDefauts()
    {
        Dao<DefautQualite, String> dao = DaoFactory.getMySQLDao(DefautQualite.class);

        for (ComposantBase compoBase : compos)
        {
            DefautQualite dq = dao.recupEltParIndex(compoBase.getMapIndex() + compoBase.getLotRTC().getNumero());

            if (dq == null && compoBase.getQualityGate() == QG.ERROR)
                dq = creerDefaut(compoBase);

            if (dq != null)
            {
                dq.setTypeDefaut(TypeDefautQualite.SONAR);

                // Mise à jour des informations de l'anomalie
                ControlRTC.getInstance().controleAnoRTC(dq);
                sauvegarde(dao, dq);
            }
        }

        return true;
    }

    /**
     * Mise à jour des composant SonarQube depuis le serveur SonarQube du Mobile Center.
     * On va chercher tous les composant du serveur et on met à jour les données depuis toutes les sources.
     * Utilisation de la base des projets Mobile Center pour obtenir les numéros de lot en ignorant les composants non répertoriés dedans.
     * 
     * @throws TeamRepositoryException
     *                                 Exception venant depuis RTC.
     */
    private void majComposantsSonarMCTask() throws TeamRepositoryException
    {
        // Affichage
        baseMessage = "Création de la liste des composants :\n";
        updateMessage("Récupération des composants depuis Sonar...\n");
        updateProgress(0, -1);

        // Récupération des données de la base de données et de Sonar
        Map<String, LotRTC> mapLotRTC = DaoFactory.getMySQLDao(LotRTC.class).readAllMap();
        Map<String, ProjetMobileCenter> mapPMC = DaoFactory.getMySQLDao(ProjetMobileCenter.class).readAllMap();
        List<ComposantSonar> composants = apiMC.getComposants(OptionGetCompos.MINIMALE, this);

        // Affichage
        int i = 0;
        int size = composants.size();
        long debut = System.currentTimeMillis();

        for (ComposantSonar composant : composants)
        {
            // Affichage
            i++;
            calculTempsRestant(debut, i, size);
            updateMessage(composant.getNom());
            updateProgress(i, size);

            // Initialisation composant
            ComposantBase compoBase = initCompoDepuisSonar(getMapCompo(), composant);

            // On ne prend que les composants non plantés qui sont dans les projets Mobile Center
            if (!mapPMC.containsKey(compoBase.getNom()) || compoBase.getDerniereAnalyse() == null)
                continue;

            // Récupération des informations de chaque composant depuis SonarQube.
            ComposantSonar compo = apiMC.getMesuresComposant(composant.getKey(), composant.getBranche(), new String[]
            { Metrique.LDC.getValeur(), Metrique.SECURITY.getValeur(), Metrique.QG.getValeur() });
            ComposantSonar compoShow = apiMC.getDetailsComposant(composant.getKey(), composant.getBranche());

            // Lot et matière
            initCompoLotEtMatiere(compoBase, mapLotRTC, mapPMC);

            // Code application
            initCodeAppli(compoBase);

            // Donnees restantes
            compoBase.setDerniereAnalyse(compoShow.getDateAnalyse());
            compoBase.setVersion(compoShow.getVersion());

            // Gestion de la version
            // Mise à jour de la version max si on progresse de version
            if (compoBase.getVersionMax().isEmpty() || compoBase.isVersionOK())
                compoBase.setVersionMax(compoShow.getVersion());
            else
            {
                // Création d'une ligne dans le fichier d'erreur s'il y a une régression de version
                ControlSimpleFile control = ControlSimpleFile.majFichierVersion();
                control.ajouterErreurCompo(compoBase);
                control.fermeture();
            }

            compoBase.setQualityGate(compo.getValueMetrique(Metrique.QG, QG.NONE.getValeur()));
            compoBase.setLdc(compo.getValueMetrique(Metrique.LDC, "0"));
            compoBase.setSecurityRatingDepuisSonar(compo.getValueMetrique(Metrique.SECURITY, "0"));
            compos.add(compoBase);
        }

        // Affichage
        updateMessage("Récupération OK.");
        etapePlus();
    }

    /**
     * Initialisation d'un ComposantSonar depuis les informations d'un objet Composant.
     * 
     * @param mapCompos
     *                  Map des composants en base.
     * @param compo
     *                  Informations provenant de SonarQube.
     * @return
     *         Composant à traiter.
     */
    private ComposantBase initCompoDepuisSonar(Map<String, ComposantBase> mapCompos, ComposantSonar compo)
    {
        ComposantBase retour;
        if (mapCompos.containsKey(compo.getKey() + compo.getBranche()))
            retour = mapCompos.get(compo.getKey() + compo.getBranche());
        else
            retour = ModelFactory.build(ComposantBase.class);

        retour.setKey(compo.getKey());
        retour.setNom(compo.getNom());
        retour.setBranche(compo.getBranche());
        retour.setInstance(InstanceSonar.MOBILECENTER);
        retour.setDerniereAnalyse(compo.getDateAnalyse());
        retour.setQualityGate(compo.getQualityGate());
        return retour;
    }

    /**
     * Initialisation du lot et de la matière d'un ComposantBase
     * 
     * @param compo
     *                  composant à traiter
     * @param mapLotRTC
     *                  Map des lots RTC en base
     * @param mapPMC
     *                  Map des projets MobileCenter en base
     * @throws TeamRepositoryException
     *                                 Exception lancée depuis RTC
     */
    private void initCompoLotEtMatiere(ComposantBase compo, Map<String, LotRTC> mapLotRTC, Map<String, ProjetMobileCenter> mapPMC) throws TeamRepositoryException
    {
        // On récupère le numero de lot depuis le projet Mobile Center.
        String numeroLot = String.valueOf(mapPMC.get(compo.getNom()).getNumeroLot());

        // Gestion de la matière
        compo.setMatiere(Matiere.testMatiereCompoMC(compo.getNom()));

        // Recherche de la mise à jour du lot depuis RTC
        LotRTC update = ControlRTC.getInstance().creerLotSuiviRTCDepuisItem(ControlRTC.getInstance().recupWorkItemDepuisId(Integer.parseInt(numeroLot)));
        new MajLotsRTCTask(null, this).majLotRTC(update);

        // Mise à jour ou création du lot selon le cas
        LotRTC lot = mapLotRTC.get(numeroLot);
        if (lot != null)
            compo.setLotRTC(lot.update(update));
        else
        {
            compo.setLotRTC(update);
            mapLotRTC.put(numeroLot, update);
        }
    }

    /**
     * Initialisation du code application d'un composant
     * 
     * @param compo
     *              Composant à traiter
     */
    private void initCodeAppli(ComposantBase compo)
    {
        String codeAppli = PATTERNSPLIT.split(compo.getNom())[1].substring(0, APPLILENGTH);

        DaoApplication dao = DaoFactory.getMySQLDao(Application.class);

        Application appli = dao.recupEltParIndex(codeAppli);

        boolean appliExiste = ControlAppCATS.INSTANCE.testApplicationExiste(codeAppli);

        // On récupère le code appli des infos du composant et si on ne trouve pas le code application dans la base de données,
        // on crée une nouvelle en specifiant qu'elle ne fait pas partie du référentiel.
        if (appli != null)
            appli.setReferentiel(appliExiste);
        else
            appli = Application.getApplication(codeAppli, appliExiste);

        compo.setAppli(appli);
    }

    /**
     * Sauvegarde les données en base avec retour du nombre de lignes ajoutées.
     * 
     * @param listeSonar
     *                   Liste des composant à sauvegarder en base de données.
     * @return
     *         Nombre de composants sauvegardés.
     */
    private int sauvegarde(List<ComposantBase> listeSonar)
    {
        // Ajout des données et mise à jour
        return sauvegarde(daoCompo, listeSonar);
    }

    /**
     * Création d'un nouveau défaut avec les informations essentielles.
     * 
     * @param compoBase
     *                  Composant à traiter.
     * @return
     *         Défaut Qualité correspondant.
     */
    private DefautQualite creerDefaut(ComposantBase compoBase)
    {
        // Initialisation avec le composant et le lot liés
        DefautQualite retour = ModelFactory.build(DefautQualite.class);
        retour.setCompo(compoBase);
        retour.setLotRTC(compoBase.getLotRTC());
        retour.initMapIndex();
        retour.setAnalyseId(Statics.INCONNUE);

        // Mise à jour de la date de repack
        if (retour.getLotRTC().getDateRepack() != null)
            retour.setDateMepPrev(retour.getLotRTC().getDateRepack());
        else
            retour.setDateMepPrev(retour.getLotRTC().getEdition().getDateMEP());

        return retour;
    }

    /*---------- ACCESSEURS ----------*/

}
