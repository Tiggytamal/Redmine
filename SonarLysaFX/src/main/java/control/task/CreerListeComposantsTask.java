package control.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import dao.DaoApplication;
import dao.DaoComposantSonar;
import model.Application;
import model.ComposantSonar;
import model.ModelFactory;
import model.enums.QG;
import model.enums.TypeMetrique;
import model.sonarapi.Composant;
import model.sonarapi.Metrique;
import model.sonarapi.Periode;
import model.sonarapi.Projet;
import utilities.Utilities;

/**
 * T�che de cr�ation de la liste des composants SonarQube avec sauvegarde sous forme de fichier XML
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public class CreerListeComposantsTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final Logger LOGCONSOLE = LogManager.getLogger("console-log");
    private static final short ETAPES = 2;
    private static final String TITRE = "Cr�ation liste des composants";

    /*---------- CONSTRUCTEURS ----------*/

    public CreerListeComposantsTask()
    {
        super(ETAPES, TITRE);
        annulable = false;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        Map<String, ComposantSonar> mapSonar = creerListeComposants();
        return sauvegarde(mapSonar) >= mapSonar.size();
    }

    @Override
    public void annuler()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private Map<String, ComposantSonar> creerListeComposants()
    {
        // Affichage
        String base = "Cr�ation de la liste des composants :\n";
        updateMessage(base + "R�cup�ration des composants depuis Sonar...\n");
        updateProgress(0, -1);

        // R�cup�ration des composants Sonar
        Map<String, ComposantSonar> retour = new HashMap<>();
        Map<String, Application> mapAppli = new DaoApplication().readAllMap();
        @SuppressWarnings("unchecked")
        List<Projet> projets = Utilities.recuperation(Main.DESER, List.class, "composants.ser", () -> api.getComposants());

        // Affichage
        updateMessage(base + "R�cup�ration OK.");
        etapePlus();
        int i = 0;
        int size = projets.size();
        long debut = System.currentTimeMillis();

        for (Projet projet : projets)
        {
            LOGCONSOLE.debug("Traitement composants Sonar : " + i + " - " + size);

            // R�cup�ration du num�ro de lot et de l'applicaitond e chaque composant.
            Composant composant = api.getMetriquesComposant(projet.getKey(),
                    new String[] { TypeMetrique.LOT.getValeur(), TypeMetrique.APPLI.getValeur(), TypeMetrique.EDITION.getValeur(), TypeMetrique.LDC.getValeur(), TypeMetrique.SECURITY.getValeur(),
                            TypeMetrique.VULNERABILITIES.getValeur(), TypeMetrique.QG.getValeur(), TypeMetrique.DUPLICATION.getValeur(), TypeMetrique.BLOQUANT.getValeur(),
                            TypeMetrique.CRITIQUE.getValeur() });
            if (composant == null)
                continue;

            ComposantSonar composantSonar = ModelFactory.getModel(ComposantSonar.class);
            composantSonar.setKey(projet.getKey());
            composantSonar.setNom(projet.getNom());
            composantSonar.setId(projet.getId());
            composantSonar.setLot(getValueMetrique(composant, TypeMetrique.LOT, null));

            // Code application
            String codeAppli = getValueMetrique(composant, TypeMetrique.APPLI, null);
            // On r�cup�re le code appli des infos du composant et si on ne trouve pas le code application dans la base de donn�es, 
            // on cr�e une nouvelle en sp�cifiant qu'elle ne fait pas partie du r�f�rentiel
            composantSonar.setAppli(mapAppli.computeIfAbsent(codeAppli, code -> ModelFactory.getModelWithParams(Application.class, code)));
                        
            composantSonar.setEdition(getValueMetrique(composant, TypeMetrique.EDITION, null));
            composantSonar.setLdc(getValueMetrique(composant, TypeMetrique.LDC, "0"));
            composantSonar.setSecurityRating((int) Float.parseFloat(getValueMetrique(composant, TypeMetrique.SECURITY, "0")));
            composantSonar.setQualityGate(getValueMetrique(composant, TypeMetrique.QG, QG.NONE.getValeur()));
            composantSonar.setVulnerabilites(getValueMetrique(composant, TypeMetrique.VULNERABILITIES, "0"));
            composantSonar.setBloquants(recupLeakPeriod(getListPeriode(composant, TypeMetrique.BLOQUANT)));
            composantSonar.setCritiques(recupLeakPeriod(getListPeriode(composant, TypeMetrique.CRITIQUE)));
            composantSonar.setDuplication(recupLeakPeriod(getListPeriode(composant, TypeMetrique.DUPLICATION)));
            composantSonar.setVersionRelease(checkVersion(projet.getKey()));
            retour.put(composantSonar.getKey(), composantSonar);

            if (api.getSecuriteComposant(projet.getKey()) > 0)
                composantSonar.setSecurite(true);
            
            // Affichage
            i++;
            updateMessage(base + projet.getNom() + affichageTemps(debut, i, size));
            updateProgress(i, size);
        }

        // Sauvegarde des donn�es
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
     * Test si un composant a une version release ou snapshot.
     *
     * @param key
     *          clef du composant
     * @return
     *          vrai si le composant est RELEASE, FAUX si SNAPSHOT
     */
    private boolean checkVersion(String key)
    {
        String version = api.getVersionComposant(key);
        return !version.contains("SNAPSHOT");
    }

    /**
     * Sauvegarde les donn�es en base avec retour ud nombre de lignes ajout�es
     * 
     * @param mapSonar
     */
    private int sauvegarde(Map<String, ComposantSonar> mapSonar)
    {        
        // Controleur de persistance SQL
        DaoComposantSonar dao = new DaoComposantSonar();
        
        // Initialisation de la table et ajout des donn�es
        dao.resetTable();
        return dao.save(mapSonar.values());
    }

    /*---------- ACCESSEURS ----------*/
}
