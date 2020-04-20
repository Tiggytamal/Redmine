package control.task.maj;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import control.parsing.ControlJSON;
import control.task.AbstractTask;
import control.watcher.ControlWatch;
import model.parsing.ComposantJSON;

/**
 * Traitement de purge des fichiers JSON obsolètes, c'est-à-dire des fichiers qui proviennent d'analyses supprimées.
 * SonarQube ne garde qu'une seule analyse par jour.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class PurgerFichiersJSONTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Purge des fichiers JSON obsolètes";
    private static final int ETAPES = 1;
    private boolean stop;

    /*---------- CONSTRUCTEURS ----------*/

    public PurgerFichiersJSONTask()
    {
        super(ETAPES, TITRE);
        annulable = true;
        stop = false;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        stop = true;
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        List<File> fichiers = ControlWatch.INSTANCE.listeFichiers();
        ControlJSON control = new ControlJSON();

        // affichage
        baseMessage = "Traitements des fichiers obsolètes :\n";
        int i = 0;
        int size = fichiers.size();

        // Itération sur tous les fichiers du repertoires, pour supprimer ceux dont la date d'analyse est nulle. 
        // C'est-à-dire que l'analyse n'est plus sur le serveur.
        for (File file : fichiers)
        {
            ComposantJSON compoJSON = control.parsingCompoJSON(file);

            // Affichage
            i++;
            updateMessage("analyse : " + compoJSON.getIdAnalyse());
            updateProgress(i, size);

            if (compoJSON.getNumeroLot() == 0 || api.getAnalyseQGInfos(compoJSON.getIdAnalyse()) == null)
                Files.delete(file.toPath());

            if (stop)
                break;
        }

        updateMessage("Terminé");
        updateProgress(1, 1);

        return Boolean.TRUE;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
