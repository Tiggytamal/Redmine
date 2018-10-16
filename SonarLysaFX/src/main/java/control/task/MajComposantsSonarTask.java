package control.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.DaoComposantSonar;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ComposantSonar;
import model.bdd.LotRTC;
import model.enums.Matiere;
import model.enums.QG;
import model.enums.TypeMetrique;
import model.sonarapi.Composant;
import model.sonarapi.Metrique;
import model.sonarapi.Periode;
import model.sonarapi.Projet;
import utilities.Statics;

/**
 * Tâche de création de la liste des composants SonarQube avec sauvegarde sous forme de fichier XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class MajComposantsSonarTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final Logger LOGCONSOLE = LogManager.getLogger("console-log");
    private static final short ETAPES = 2;
    private static final String TITRE = "Création liste des composants";
    private static final String LOT0 = "000000";

    /*---------- CONSTRUCTEURS ----------*/

    public MajComposantsSonarTask()
    {
        super(ETAPES, TITRE);
        annulable = true;
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
        // Pas de traitement à l'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private List<ComposantSonar> creerListeComposants()
    {
        // Affichage
        baseMessage = "Création de la liste des composants :\n";
        updateMessage("Récupération des composants depuis Sonar...\n");
        updateProgress(0, -1);

        // Récupération des données de la base de données et de Sonar
        List<ComposantSonar> retour = new ArrayList<>();
        List<ComposantSonar> composPlantes = new ArrayList<>();
        Map<String, Application> mapAppli = DaoFactory.getDao(Application.class).readAllMap();
        Map<String, LotRTC> mapLotRTC = DaoFactory.getDao(LotRTC.class).readAllMap();
        List<Projet> projets = api.getComposants();

        // Réinitialisation des matières des lots pour le cas d'un composant qui serait retiré et qui enléverait un type de matière.
        for (LotRTC lotRTC : mapLotRTC.values())
        {
            lotRTC.getMatieres().clear();
        }

        // Affichage
        updateMessage("Récupération OK.");
        etapePlus();
        int i = 0;
        int size = projets.size();
        long debut = System.currentTimeMillis();

        for (Projet projet : projets)
        {
            // Arrêt de a boucle en cas d'annulation
            if (isCancelled())
                break;

            LOGCONSOLE.debug("Traitement composants Sonar : " + i + " - " + size);

            // Récupération du numéro de lot et de l'applicaiton de chaque composant.
            Composant composant = api.getMetriquesComposant(projet.getKey(),
                    new String[] { TypeMetrique.LOT.getValeur(), TypeMetrique.APPLI.getValeur(), TypeMetrique.EDITION.getValeur(), TypeMetrique.LDC.getValeur(), TypeMetrique.SECURITY.getValeur(),
                            TypeMetrique.VULNERABILITIES.getValeur(), TypeMetrique.QG.getValeur(), TypeMetrique.DUPLICATION.getValeur(), TypeMetrique.BLOQUANT.getValeur(),
                            TypeMetrique.CRITIQUE.getValeur() });

            if (composant == null)
                continue;
            
            
            // Initialisation composant
            ComposantSonar composantSonar = initCompoDepuisProjet(mapCompos, projet);
            
            // Lot RTC
            String numeroLot = getValueMetrique(composant, TypeMetrique.LOT, LOT0);

            // Gestion de la matière
            Matiere matiere = testMatiereCompo(composantSonar.getNom());
            composantSonar.setMatiere(matiere);

            // On récupère le numéro de lot des infos du composant et si on ne trouve pas la valeur dans la base de données
            // on crée un nouveau lot en spécifiant qu'il ne fait pas parti du référentiel. Les composants sans numéro de lot auront un lotRTC nul.
            if (mapLotRTC.containsKey(numeroLot))
            {
                composantSonar.setLotRTC(mapLotRTC.get(numeroLot));
                composantSonar.getLotRTC().addMatiere(matiere);
            }
            else
            {
                LotRTC lotRTC = LotRTC.getLotRTCInconnu(numeroLot);
                lotRTC.addMatiere(matiere);
                composantSonar.setLotRTC(lotRTC);
                mapLotRTC.put(numeroLot, lotRTC);
            }

            // Code application
            String codeAppli = getValueMetrique(composant, TypeMetrique.APPLI, Statics.EMPTY);

            // On récupère le code appli des infos du composant et si on ne trouve pas le code application dans la base de données,
            // on crée une nouvelle en spécifiant qu'elle ne fait pas partie du référentiel. Les composants sans code appli auront une application nulle.
            if (mapAppli.containsKey(codeAppli))
                composantSonar.setAppli(mapAppli.get(codeAppli));
            else
            {
                Application appli = Application.getApplicationInconnue(codeAppli);
                composantSonar.setAppli(appli);
                mapAppli.put(codeAppli, appli);
            }
            
            // Securite du composant
            if (api.getSecuriteComposant(projet.getKey()) > 0)
                composantSonar.setSecurite(true);

            // Données restantes
            composantSonar.setQualityGate(getValueMetrique(composant, TypeMetrique.QG, QG.NONE.getValeur()));
            composantSonar.setEdition(getValueMetrique(composant, TypeMetrique.EDITION, null));
            composantSonar.setLdc(getValueMetrique(composant, TypeMetrique.LDC, "0"));
            composantSonar.setSecurityRatingDepuisSonar(getValueMetrique(composant, TypeMetrique.SECURITY, "0"));
            composantSonar.setVulnerabilites(getValueMetrique(composant, TypeMetrique.VULNERABILITIES, "0"));
            composantSonar.setBloquants(recupLeakPeriod(getListPeriode(composant, TypeMetrique.BLOQUANT)));
            composantSonar.setCritiques(recupLeakPeriod(getListPeriode(composant, TypeMetrique.CRITIQUE)));
            composantSonar.setDuplication(recupLeakPeriod(getListPeriode(composant, TypeMetrique.DUPLICATION)));
            retour.add(composantSonar);

            if (LOT0.equals(composantSonar.getLotRTC().getLot()))
                    composPlantes.add(composantSonar);

            // Affichage
            i++;
            calculTempsRestant(debut, i, size);
            updateMessage(projet.getNom());
            updateProgress(i, size);
        }
        
        for (ComposantSonar compo : composPlantes)
        {
            LOGCONSOLE.info(compo.getLotRTC().getLot());
        }

        // Sauvegarde des données
        return retour;
    }

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
        retour.setVersionRelease(checkVersion(projet.getKey()));
        
        return retour;
    }

    /**
     * Remonte une liste de période en protégeant des nullPointer
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
     * Remonte la valeur du premier index de la période qui correspond à la leakPeriod
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
     * Test si un composant a une version release ou snapshot.
     *
     * @param key
     *            clef du composant
     * @return vrai si le composant est RELEASE, FAUX si SNAPSHOT
     */
    private boolean checkVersion(String key)
    {
        String version = api.getVersionComposant(key);
        return !version.contains("SNAPSHOT");
    }

    /**
     * Sauvegarde les donnèes en base avec retour du nombre de lignes ajoutèes
     * 
     * @param listeSonar
     */
    private int sauvegarde(List<ComposantSonar> listeSonar)
    {
        // Ajout des donnèes et mise à jour de la date de modification de la table
        DaoComposantSonar dao = DaoFactory.getDao(ComposantSonar.class);
        int retour = dao.persist(listeSonar);
        dao.majDateDonnee();
        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}
