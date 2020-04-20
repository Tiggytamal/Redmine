package control.watcher;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import static utilities.Statics.EMPTY;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.mchange.util.AssertException;

import application.Main;
import control.task.maj.TraiterCompoJSONFichierTask;
import fxml.Menu;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import model.enums.EtatFichierPic;
import model.enums.Param;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Classe de gestion des observers pour gérer les fichiers créer par SonarQube sur le quai de dépôt.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class ControlWatch
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantage */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");
    /** logger plantage */
    private static final Logger LOGCOMPLET = Utilities.getLogger("complet-log");

    public static final ControlWatch INSTANCE = new ControlWatch();

    private Runnable switchBoucle;
    private KeyCodeCombination raccourciFichierPic;

    private WatchService watcher;
    private Path dir;
    private boolean ok;
    private Thread boucle;

    /*---------- CONSTRUCTEURS ----------*/

    private ControlWatch()
    {
        // Controle pour éviter la création par reflexion d'une seconde instance
        if (INSTANCE != null)
            throw new AssertException();

        ok = true;
        dir = Paths.get(Statics.proprietesXML.getMapParams().get(Param.REPSONAR));

        // Switch pour démarrer ou arreter le traitement
        switchBoucle = () -> {
            if (!lancementBoucle())
            {
                stopBoucle();
                Menu.updateFichiersActif(EtatFichierPic.INACTIF);
            }
            else
                Menu.updateFichiersActif(EtatFichierPic.ACTIF);
        };
        raccourciFichierPic = new KeyCodeCombination(KeyCode.F, KeyCodeCombination.CONTROL_DOWN);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet de lancer la boucle de traitement des fichiers PIC du quai de depet.
     * Ne fait rien si la boucle n'est pas nulle et deje active.
     */
    public boolean lancementBoucle()
    {
        if (boucle != null && boucle.isAlive())
            return false;

        try
        {
            watcher = FileSystems.getDefault().newWatchService();
            dir.register(watcher, ENTRY_CREATE);
            boucle = new Thread(this::boucleWatcher);
            boucle.setUncaughtExceptionHandler((t, e) -> Main.gestionException(e));
            boucle.start();
            return true;
        }
        catch (IOException e)
        {
            throw new TechnicalException("Impossible de créer le service de surveillance du repertoire", e);
        }
    }

    // Arret de la boucle de traitement des fichiers en arrivee.
    public void stopBoucle()
    {
        ok = false;

        // Protection si l'on clic sur arrêter sans avoir démarrer une fois la boucle
        if (watcher != null)
            try
            {
                watcher.close();
            }
            catch (IOException e)
            {
                throw new TechnicalException("control.watcher.ControlWatch.stopBoucle - Impossible d'arrêter la boucle des fichiers Pic", e);
            }
    }

    /**
     * Liste tous les fichiers qui sont sur le quai de dépôt.
     * 
     * @return
     *         La liste des fichiers.
     */
    public List<File> listeFichiers()
    {
        // Liste des fichiers terminants par json
        File[] jsons = dir.toFile().listFiles((File d, String name) -> name.endsWith(".json"));

        // Création d'une arrayList depuis l'Array
        List<File> retour = Arrays.asList(jsons);

        // Tri des fichiers par date de modification
        Collections.sort(retour, (j1, j2) -> Long.compare(j1.lastModified(), j2.lastModified()));

        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Lancement de la boucle d'observation du repertoire pour le traitement des fichiers venus de la PIC.
     * Vidage du repertoire au debut du traitement.
     */
    private void boucleWatcher()
    {
        // Variables
        WatchKey key;
        ok = true;

        traitementFichiersExistants();

        do
        {
            // Récupération du dernier evenement.
            try
            {
                key = watcher.take();
            }
            catch (InterruptedException | ClosedWatchServiceException e)
            {
                Thread.currentThread().interrupt();
                LOGCOMPLET.error("Arret de la boucle de traitement des fichiers.", e);
                Platform.runLater(() -> Menu.updateFichiersActif(EtatFichierPic.INACTIF));
                return;
            }

            for (WatchEvent<?> event : key.pollEvents())
            {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == OVERFLOW)
                    continue;

                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                File file = dir.resolve(ev.context()).toFile();
                try
                {
                    // Ajout d'une sleep de 500ms pour éviter le plantage à cause de la non-fermeture du stream du fichier côté PIC.
                    Thread.sleep(500);
                    new TraiterCompoJSONFichierTask(file).call();
                }
                catch (IOException | InterruptedException e)
                {
                    LOGPLANTAGE.error(EMPTY, e);
                    Platform.runLater(() -> Menu.updateFichiersActif(EtatFichierPic.INACTIF));
                    Thread.currentThread().interrupt();
                }
            }
        }
        while (ok && key.reset() && !Thread.currentThread().isInterrupted());
    }

    /**
     * Traitement pourvérifier tous les fichiers déjà présent sûr le quaiavant le lancement de la boucle.
     */
    private void traitementFichiersExistants()
    {
        for (File file : listeFichiers())
        {
            if (!ok)
                break;
            try
            {
                new TraiterCompoJSONFichierTask(file).call();
            }
            catch (IOException e)
            {
                LOGPLANTAGE.error(EMPTY, e);
                LOGCOMPLET.error("Erreur au moment de traiter les fichiers restants du repertoire " + dir.toString());
            }
        }
    }

    /*---------- ACCESSEURS ----------*/

    public Runnable getSwitchBoucle()
    {
        return switchBoucle;
    }

    public KeyCodeCombination getRaccourciFichierPic()
    {
        return raccourciFichierPic;
    }
}
