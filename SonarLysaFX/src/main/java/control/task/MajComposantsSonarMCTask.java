package control.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.rest.SonarAPI67;
import dao.DaoComposantSonar;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ComposantSonar;
import model.bdd.LotRTC;
import model.enums.InstanceSonar;
import model.enums.Matiere;
import model.enums.OptionMajCompos;
import model.enums.QG;
import model.enums.TypeMetrique;
import model.rest.sonarapi.Composant;
import model.rest.sonarapi.Metrique;
import model.rest.sonarapi.Periode;
import model.rest.sonarapi.Projet;

/**
 * T�che de cr�ation de la liste des composants SonarQube avec purge des composants plant�s
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public class MajComposantsSonarMCTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    /** Logger pour de debug dans la console */
    private static final Logger LOGCONSOLE = LogManager.getLogger("console-log");
    /** Nombre d'�tapes du traitement */
    private static final short ETAPES = 2;
    /** Titre de la t�che */
    private static final String TITRE = "Cr�ation liste des composants";
    private static final String LOTMONPAT = "341390";
    private static final String APPLIMONPAT = "DCPA";

    /** Options de la t�che */
    private OptionMajCompos option;

    private SonarAPI67 api67;

    /*---------- CONSTRUCTEURS ----------*/

    public MajComposantsSonarMCTask(OptionMajCompos option)
    {
        super(ETAPES, TITRE);
        this.option = option;
        annulable = true;
        api67 = SonarAPI67.INSTANCE;
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
        Map<String, LotRTC> mapLotRTC = DaoFactory.getDao(LotRTC.class).readAllMap();
        Map<String, Application> mapAppli = DaoFactory.getDao(Application.class).readAllMap();
        List<Projet> projets = api67.getComposants();

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

            if (!compoATester(compo))
                continue;

            // R�cup�ration des informations de chaque composant depuis SonarQube.
            Composant composant = api67.getMetriquesComposant(projet.getKey(), new String[] { TypeMetrique.LDC.getValeur(), TypeMetrique.SECURITY.getValeur(), TypeMetrique.VULNERABILITIES.getValeur(),
                    TypeMetrique.QG.getValeur(), TypeMetrique.DUPLICATION.getValeur(), TypeMetrique.BLOQUANT.getValeur(), TypeMetrique.CRITIQUE.getValeur() });

            // On saute les composants null et plant�s
            if (composant == null)
                continue;

            // Lot et mati�re
            initCompoLotEtMatiere(compo, mapLotRTC);

            // Code application
            String codeAppli = APPLIMONPAT;

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

            // Securite du composant
            if (api67.getSecuriteComposant(projet.getKey()) > 0)
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
            retour = ModelFactory.getModel(ComposantSonar.class);

        retour.setKey(projet.getKey());
        retour.setNom(projet.getNom());
        retour.setId(projet.getId());
        retour.setInstance(InstanceSonar.MOBILECENTER);
        return retour;
    }

    /**
     * Initialisation du lot et de la mati�ere d'un ComposantSonar
     * 
     * @param compo
     * @param mapLotRTC
     * @param composant
     */
    private void initCompoLotEtMatiere(ComposantSonar compo, Map<String, LotRTC> mapLotRTC)
    {
        // Lot RTC
        String numeroLot = LOTMONPAT;

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
     * Contr�le pour voir si l'on doit rechercher des modifications sur le composant dans SonarQube
     * 
     * @param compo
     * @return
     */
    private boolean compoATester(ComposantSonar compo)
    {
        if (!compo.getNom().contains("DCPA"))
            return false;
        if (option == OptionMajCompos.COMPLETE)
        {
            api67.initCompoVersionEtDateMaj(compo);
            return true;
        }
        return api67.initCompoVersionEtDateMaj(compo);
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
        DaoComposantSonar dao = DaoFactory.getDao(ComposantSonar.class);
        int retour = dao.persist(listeSonar);
        dao.majDateDonnee();
        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}
