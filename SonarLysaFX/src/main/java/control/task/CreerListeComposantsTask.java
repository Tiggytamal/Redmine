package control.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import control.xml.ControlXML;
import model.ComposantSonar;
import model.ModelFactory;
import model.enums.TypeFichier;
import model.enums.TypeMetrique;
import model.sonarapi.Composant;
import model.sonarapi.Metrique;
import model.sonarapi.Projet;
import utilities.Statics;
import utilities.Utilities;

public class CreerListeComposantsTask extends AbstractSonarTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final Logger LOGCONSOLE = LogManager.getLogger("console-log");
    private static final short ETAPES = 2;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerListeComposantsTask()
    {
        super(ETAPES);
        annulable = false;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return creerListeComposants();
    }
    
    @Override
    public void annuler()
    {
        // Pas de traitement d'annulation        
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean creerListeComposants()
    {
        // Affichage
        String base = "Création de la liste des composants :\n";
        updateMessage(base + "Récupération des composants depuis Sonar...\n");
        updateProgress(0, -1);

        // Récupération des composants Sonar
        Map<String, ComposantSonar> mapSonar = new HashMap<>();
        @SuppressWarnings("unchecked")
        List<Projet> projets = Utilities.recuperation(Main.DESER, List.class, "composants.ser", () -> api.getComposants());

        // Affichage
        updateMessage(base + "Récupération OK.");
        etapePlus();
        int i = 0;
        int size = projets.size();

        for (Projet projet : projets)
        {
            // Affichage
            updateMessage(base + projet.getNom());
            i++;
            updateProgress(i, size);
            
            LOGCONSOLE.debug("Traitement composants Sonar : " + i + " - " + size);

            // Récupération du numéro de lot et de l'applicaitond e chaque composant.
            Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] { TypeMetrique.LOT.getValeur(), TypeMetrique.APPLI.getValeur(), TypeMetrique.EDITION.getValeur(),
                    TypeMetrique.LDC.getValeur(), TypeMetrique.SECURITY.getValeur(), TypeMetrique.VULNERABILITIES.getValeur() });
            if (composant == null)
                continue;
            ComposantSonar composantSonar = ModelFactory.getModel(ComposantSonar.class);
            composantSonar.setKey(projet.getKey());
            composantSonar.setNom(projet.getNom());
            composantSonar.setId(projet.getId());
            composantSonar.setLot(composant.getMapMetriques().computeIfAbsent(TypeMetrique.LOT, t -> new Metrique(TypeMetrique.LOT, null)).getValue());
            composantSonar.setAppli(composant.getMapMetriques().computeIfAbsent(TypeMetrique.APPLI, t -> new Metrique(TypeMetrique.APPLI, null)).getValue());
            composantSonar.setEdition(composant.getMapMetriques().computeIfAbsent(TypeMetrique.EDITION, t -> new Metrique(TypeMetrique.EDITION, null)).getValue());
            composantSonar.setLdc(Integer.parseInt(composant.getMapMetriques().computeIfAbsent(TypeMetrique.LDC, t -> new Metrique(TypeMetrique.LDC, "0")).getValue())); 
            composantSonar.setSecurity((int) Float.parseFloat(composant.getMapMetriques().computeIfAbsent(TypeMetrique.SECURITY, t -> new Metrique(TypeMetrique.SECURITY, "0")).getValue()));
            composantSonar
                    .setVulnerabilites(Integer.parseInt(composant.getMapMetriques().computeIfAbsent(TypeMetrique.VULNERABILITIES, t -> new Metrique(TypeMetrique.VULNERABILITIES, "0")).getValue()));
            mapSonar.put(composantSonar.getKey(), composantSonar);
        }

        // Sauvegarde des données
        new ControlXML().saveParam(Statics.fichiersXML.majMapDonnees(TypeFichier.SONAR, mapSonar));
        return true;
    }

    /*---------- ACCESSEURS ----------*/
}
