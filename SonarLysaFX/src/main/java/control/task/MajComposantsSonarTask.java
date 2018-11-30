package control.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.rest.ControlRepack;
import control.word.ControlRapport;
import dao.DaoEdition;
import dao.ListeDao;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ComposantSonar;
import model.bdd.DefautAppli;
import model.bdd.LotRTC;
import model.enums.EtatDefaut;
import model.enums.EtatLot;
import model.enums.InstanceSonar;
import model.enums.Matiere;
import model.enums.OptionMajCompos;
import model.enums.ParamSpec;
import model.enums.QG;
import model.enums.TypeInfo;
import model.enums.TypeMetrique;
import model.enums.TypeRapport;
import model.rest.repack.RepackREST;
import model.rest.sonarapi.Composant;
import model.rest.sonarapi.Metrique;
import model.rest.sonarapi.Periode;
import model.rest.sonarapi.Projet;
import utilities.Statics;

/**
 * T�che de cr�ation de la liste des composants SonarQube avec purge des composants plant�s
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public class MajComposantsSonarTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    /** Logger pour de debug dans la console */
    private static final Logger LOGCONSOLE = LogManager.getLogger("console-log");
    /** Logger g�n�ral de l'application */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    /** Nombre d'�tapes du traitement */
    private static final short ETAPES = 2;
    /** Titre de la t�che */
    private static final String TITRE = "Cr�ation liste des composants";
    /** Num�ro de lot inconnu */
    private static final String LOT0 = "000000";

    private static final boolean PURGE = true;

    /** Liste des clefs des composants plant�s dans SonarQube */
    private List<String> keysComposPlantes;
    /** Options de la t�che */
    private OptionMajCompos option;
    /** Rapport de la purge */
    private ControlRapport controlR;

    /*---------- CONSTRUCTEURS ----------*/

    public MajComposantsSonarTask(OptionMajCompos option)
    {
        super(ETAPES, TITRE);
        this.option = option;
        annulable = true;
        keysComposPlantes = new ArrayList<>();
        controlR = new ControlRapport(TypeRapport.PURGESONAR);
        startTimers();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        List<ComposantSonar> listeSonar = creerListeComposants();
        return sauvegarde(listeSonar) >= listeSonar.size();
    }

    @Override
    public void annulerImpl()
    {
        // Pas de traitement � l'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private List<ComposantSonar> creerListeComposants()
    {
        // Affichage
        baseMessage = "Cr�ation de la liste des composants :\n";
        updateMessage("R�cup�ration des composants depuis Sonar...\n");
        updateProgress(0, -1);

        // R�cup�ration des donn�es de la base de donn�es et de Sonar
        List<ComposantSonar> retour = new ArrayList<>();
        Map<String, Application> mapAppli = ListeDao.daoAppli.readAllMap();
        Map<String, LotRTC> mapLotRTC = ListeDao.daoLotRTC.readAllMap();
        Map<String, DefautAppli> mapDefAppli = ListeDao.daoDefautAppli.readAllMap();
        List<Projet> projets = api.getComposants();

        // Affichage
        updateMessage("R�cup�ration OK.");
        etapePlus();
        int i = 0;
        int size = projets.size();
        long debut = System.currentTimeMillis();

        for (Projet projet : projets)
        {
            // Arr�t de a boucle en cas d'annulation
            if (isCancelled())
                break;

            // Affichage et logging
            i++;
            calculTempsRestant(debut, i, size);
            updateMessage(projet.getNom());
            updateProgress(i, size);
            LOGCONSOLE.debug(new StringBuilder("Traitement composants Sonar ").append(projet.getNom()).append(" : ").append(i).append(" - ").append(size).toString());

            // Initialisation composant
            ComposantSonar compo = initCompoDepuisProjet(mapCompos, projet);

            // On ne recalcule pas les composants avec une ancienne version ou ceux qui n'ont pas �t� mis � jour depuis la derni�re mise � jour,
            // mais seulement dans le cas o� l'option de mise � jour partielle est activ�e.
            List<String> oldVersion = Arrays.asList(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.VERSIONSVIEUXCOMPOS).split(";"));
            if (!compoATester(compo, oldVersion))
                continue;

            // R�cup�ration des informations de chaque composant depuis SonarQube.
            Composant composant = api.getMetriquesComposant(projet.getKey(),
                    new String[] { TypeMetrique.LOT.getValeur(), TypeMetrique.APPLI.getValeur(), TypeMetrique.LDC.getValeur(), TypeMetrique.SECURITY.getValeur(),
                            TypeMetrique.VULNERABILITIES.getValeur(), TypeMetrique.QG.getValeur(), TypeMetrique.DUPLICATION.getValeur(), TypeMetrique.BLOQUANT.getValeur(),
                            TypeMetrique.CRITIQUE.getValeur() });

            // On saute les composants null et plant�s
            if (composant == null || !testCompoPlante(composant))
                continue;

            // Lot et mati�re
            initCompoLotEtMatiere(compo, composant, mapLotRTC);

            // Repack
            initDateRepack(compo);

            // Code application
            String codeAppli = getValueMetrique(composant, TypeMetrique.APPLI, Statics.EMPTY).toUpperCase();

            // On r�cup�re le code appli des infos du composant et si on ne trouve pas le code application dans la base de donn�es,
            // on cr�e une nouvelle en sp�cifiant qu'elle ne fait pas partie du r�f�rentiel. Les composants sans code appli auront une application nulle.
            if (mapAppli.containsKey(codeAppli))
                compo.setAppli(mapAppli.get(codeAppli));
            else
            {
                Application appli = Application.getApplicationInconnue(codeAppli);
                compo.setAppli(appli);
                mapAppli.put(codeAppli, appli);
            }

            // Mise � jour la table des d�fauts sur les codes application
            gestionDefautsAppli(compo, mapDefAppli);

            // Securite du composant
            if (api.getSecuriteComposant(projet.getKey()) > 0)
                compo.setSecurite(true);

            // Donn�es restantes
            compo.setQualityGate(getValueMetrique(composant, TypeMetrique.QG, QG.NONE.getValeur()));
            compo.setLdc(getValueMetrique(composant, TypeMetrique.LDC, "0"));
            compo.setSecurityRatingDepuisSonar(getValueMetrique(composant, TypeMetrique.SECURITY, "0"));
            compo.setVulnerabilites(getValueMetrique(composant, TypeMetrique.VULNERABILITIES, "0"));
            compo.setBloquants(recupLeakPeriod(getListPeriode(composant, TypeMetrique.BLOQUANT)));
            compo.setCritiques(recupLeakPeriod(getListPeriode(composant, TypeMetrique.CRITIQUE)));
            compo.setDuplication(recupLeakPeriod(getListPeriode(composant, TypeMetrique.DUPLICATION)));
            retour.add(compo);            
        }

        // Ecriture de la liste des composants purg�s
        controlR.creerFichier();

        // Purge dans la ltable des composants plant�s
        purgeComposPlantes(keysComposPlantes);

        // Mise � jour des d�faults des codes application
        majDefAppli(mapDefAppli);

        return retour;
    }

    /**
     * Initialisation d'un ComposantSonar depuis les informations d'un obejt Projet
     * 
     * @param mapCompos
     * @param projet
     * @return
     */
    private ComposantSonar initCompoDepuisProjet(Map<String, ComposantSonar> mapCompos, Projet projet)
    {
        ComposantSonar retour;
        if (mapCompos.containsKey(projet.getKey()))
            retour = mapCompos.get(projet.getKey());
        else
            retour = ModelFactory.build(ComposantSonar.class);

        retour.setKey(projet.getKey());
        retour.setNom(projet.getNom());
        retour.setId(projet.getId());
        retour.setInstance(InstanceSonar.LEGACY);
        return retour;
    }

    /**
     * Initialisation du lot et de la mati�ere d'un ComposantSonar
     * 
     * @param compo
     * @param composant
     * @param mapLotRTC
     */
    private void initCompoLotEtMatiere(ComposantSonar compo, Composant composant, Map<String, LotRTC> mapLotRTC)
    {
        // Lot RTC
        String numeroLot = getValueMetrique(composant, TypeMetrique.LOT, LOT0);

        // Gestion de la mati�re
        Matiere matiere = testMatiereCompo(compo.getNom());
        compo.setMatiere(matiere);

        // On r�cup�re le num�ro de lot des infos du composant et si on ne trouve pas la valeur dans la base de donn�es,
        // on cr�e un nouveau lot en sp�cifiant qu'il ne fait pas parti du r�f�rentiel. Les composants sans num�ro de lot auront un lotRTC inconnu.
        if (mapLotRTC.containsKey(numeroLot))
        {
            compo.setLotRTC(mapLotRTC.get(numeroLot));
            compo.getLotRTC().addMatiere(matiere);
        }
        else
        {
            LotRTC lotRTC = LotRTC.getLotRTCInconnu(numeroLot);
            lotRTC.addMatiere(matiere);
            compo.setLotRTC(lotRTC);
            mapLotRTC.put(numeroLot, lotRTC);
        }
    }

    /**
     * Initilisation de la date de repack du composant ainsi que de la mise � jour de celle de son lot
     * 
     * @param compo
     */
    private void initDateRepack(ComposantSonar compo)
    {
        // R�cup�ration de tous les repacks du composant
        List<RepackREST> repacks = ControlRepack.INSTANCE.getRepacksComposant(compo);

        // R�cup�ratoin de la date de chaque repack pour garder la plus recente de toute
        LocalDate date = Statics.DATEINCONNUE;
        for (RepackREST repackREST : repacks)
        {
            if (repackREST.getNomGc() != null && repackREST.getNomGc() != "BOREAL_Packaging")
            {
                LocalDate temp = calculDateRepack(repackREST.getNomGc());
                if (temp.isAfter(date))
                    date = temp;
            }
        }

        if (date != Statics.DATEINCONNUE)
            compo.setDateRepack(date);

        traitementLot(compo);
    }

    /**
     * Calcul de la date de Repack depuis le nom des groupes de correction
     * 
     * @param nomGc
     * @return
     */
    private LocalDate calculDateRepack(String nomGc)
    {
        if (nomGc == null)
            return Statics.DATEINCONNUE;

        DaoEdition dao = ListeDao.daoEdition;
        if (nomGc.matches("^E[2-9]\\d_GC\\d\\d[A-Z]?$"))
            return dao.readAllMap().get(nomGc.substring(0, 3)).getDateMEP();

        if (nomGc.matches("^E[2-9]\\d_CDMS\\d\\d$"))
            return dao.recupDateEditionDepuisRepack(nomGc);

        if (nomGc.matches("^E[2.9]\\d_.*$"))
            return dao.readAllMap().get(nomGc.substring(0, 3)).getDateMEP();

        return Statics.DATEINCONNUE;
    }

    /**
     * Calcul de la date de repack du lot en prenant la date la plus r�cente des repacks des composants
     * 
     * @param compo
     */
    private void traitementLot(ComposantSonar compo)
    {
        LotRTC lot = compo.getLotRTC();
        LocalDate repackCompo = compo.getDateRepack();

        if (repackCompo != null && Statics.DATEINCO2099.equals(lot.getEdition().getDateMEP()) && (lot.getDateRepack() == null || repackCompo.isAfter(lot.getDateRepack())))
            lot.setDateRepack(repackCompo);
    }

    /**
     * Contr�le pour voir si l'on doit rechercher des modifications sur le composant dans SonarQube
     * 
     * @param compo
     * @param oldVersion
     * @return
     */
    private boolean compoATester(ComposantSonar compo, List<String> oldVersion)
    {
        if (option == OptionMajCompos.COMPLETE)
        {
            api.initCompoVersionEtDateMaj(compo);
            return true;
        }
        return testVersion(compo, oldVersion) && api.initCompoVersionEtDateMaj(compo);
    }

    /**
     * Test si la version d'un composant est r�cente ou non. Utilise le param�tre de l'application {@code ParamSpec.VERSIONSVIEUXCOMPOS}.
     * 
     * @param compo
     * @param oldVersions
     * @return vrai si la version est r�cente faux si c'est uen ancienne version
     */
    private boolean testVersion(ComposantSonar compo, List<String> oldVersions)
    {
        for (String version : oldVersions)
        {
            String nom = compo.getNom();
            if (nom.substring(nom.length() - 2, nom.length()).equals(version))
                return false;
        }
        return true;
    }

    /**
     * Mise � jour de la table des d�faults des applications
     * 
     * @param mapDefAppli
     */
    private void majDefAppli(Map<String, DefautAppli> mapDefAppli)
    {
        ListeDao.daoDefautAppli.persist(mapDefAppli.values());
        ListeDao.daoDefautAppli.majDateDonnee();
    }

    /**
     * Contr�le des composants et cr�ation d'un d�fault dans le cas d'un code applicatif �rron�.
     * 
     * @param compo
     * @param mapDefAppli
     */
    private void gestionDefautsAppli(ComposantSonar compo, Map<String, DefautAppli> mapDefAppli)
    {
        Application appli = compo.getAppli();
        LotRTC lotRTC = compo.getLotRTC();

        if (!appli.isReferentiel() && !mapDefAppli.containsKey(compo.getNom()))
        {
            DefautAppli defAppli = ModelFactory.build(DefautAppli.class);
            defAppli.setCompo(compo);
            mapDefAppli.put(compo.getNom(), defAppli);
        }
        else if (mapDefAppli.containsKey(compo.getNom()))
        {
            DefautAppli defAppli = mapDefAppli.get(compo.getNom());

            if (appli.isReferentiel())
                defAppli.setEtatDefaut(EtatDefaut.CORRIGE);
            else if (lotRTC.getEtatLot() == EtatLot.ABANDONNE || lotRTC.getEtatLot() == EtatLot.TERMINE || lotRTC.getEtatLot() == EtatLot.EDITION)
                defAppli.setEtatDefaut(EtatDefaut.LOTCLOS);
        }
    }

    /**
     * Test pour trouver les composants plant� dans SonarQube
     * 
     * @param composant
     * @return
     */
    private boolean testCompoPlante(Composant composant)
    {
        boolean retour = true;
        if (LOT0.equals(getValueMetrique(composant, TypeMetrique.LOT, LOT0)))
        {
            keysComposPlantes.add(composant.getKey());
            controlR.addInfo(TypeInfo.COMPOPURGE, composant.getKey(), "");
            retour = false;
        }
        return retour;
    }

    /**
     * Remonte une liste de p�riode en prot�geant des nullPointer
     * 
     * @param metriques
     * @param type
     * @return
     */
    private List<Periode> getListPeriode(Composant compo, TypeMetrique type)
    {
        return compo.getMapMetriques().computeIfAbsent(type, t -> new Metrique(type, null)).getListePeriodes();
    }

    /**
     * Remonte la valeur du premier index de la p�riode qui correspond � la leakPeriod
     * 
     * @param periodes
     * @return
     */
    private float recupLeakPeriod(List<Periode> periodes)
    {
        if (periodes == null)
            return 0F;

        for (Periode periode : periodes)
        {
            if (periode.getIndex() == 1)
                return Float.valueOf(periode.getValeur());
        }
        return 0F;
    }

    /**
     * Sauvegarde les donn�es en base avec retour du nombre de lignes ajout�es
     * 
     * @param listeSonar
     */
    private int sauvegarde(List<ComposantSonar> listeSonar)
    {
        // Ajout des donn�es et mise � jour de la date de modification de la table
        int retour = ListeDao.daoCompo.persist(listeSonar);
        ListeDao.daoCompo.majDateDonnee();
        return retour;
    }

    /**
     * Purge des composantsSoanr plant�s dans SonarQube et dans la base de donn�es
     * 
     * @param keysComposPlantes
     */
    private void purgeComposPlantes(List<String> keysComposPlantes)
    {

        for (String key : keysComposPlantes)
        {
            // Suppression dans SonarQube
            api.supprimerProjet(key, false);
            api.supprimerVue(key, false);

            LOGGER.debug("suppression composant : {}", key);

            // Suppression dans la base
            if (PURGE)
                ListeDao.daoCompo.delete(ListeDao.daoCompo.recupEltParIndex(key));
        }

    }

    /*---------- ACCESSEURS ----------*/
}
