package control.task;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import application.Main;
import javafx.application.Platform;
import model.enums.Action;
import model.fxml.AbstractFXMLModele;
import utilities.AbstractToStringImpl;
import fxml.bdd.AbstractBDD;
import fxml.dialog.ProgressDialog;

/**
 * Classe lancement des tâches. Ne comprend que les méthodes statiques pour lancer celles-ci ainsi que les fenêtres de progression.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public abstract class LaunchTask extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    private LaunchTask()
    {}

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet de lancer une tâche avec le titre choisi.
     * 
     * @param task
     *              Tâche à executer.
     * 
     * @param titre
     *              Le titre affiché sur la fenêtre.
     */
    public static void startTask(AbstractTask task, String titre)
    {
        Platform.runLater(() -> { ProgressDialog dialog = createDialog(task, titre); dialog.show(); });
    }

    /**
     * Permet de lancer une tâche avec le titre par défaut de la tâche.
     * 
     * @param task
     *             La tâche à lancer. Implementation concrète de {@code AbstractSonarTask}.
     */
    public static void startTask(AbstractTask task)
    {
        startTask(task, null);
    }

    /**
     * Permet de lancer une tâche avec le titre choisi et d'effectuer la tâche prévue à la suite.
     * 
     * @param task
     *               La tâche à lancer. Implementation concrète de {@code AbstractTask}.
     * @param titre
     *               Le titre affiche sur la fenêtre.
     * @param action
     *               Ection à effectuer aprés le traitement.
     * @param t
     *               Controleur de la vue qui lance la tâche.
     * @param        <T>
     *               Classe du modèle FXML.
     * @param        <A>
     *               Classe de l'action à traiter.
     */
    public static <T extends AbstractFXMLModele<I>, A extends Action, I> void startTaskWithAction(AbstractTask task, String titre, Consumer<AbstractBDD<T, A, I>> action,
            AbstractBDD<T, A, I> t)
    {
        ProgressDialog dialog = createDialog(task, titre);
        dialog.ajouterAction(action, t);
        dialog.show();
    }

    /**
     * Permet de lancer une tâche avec le titre par défaut de la tâche et d'effectuer la tâche prévue à la suite.
     * 
     * @param task
     *               La tâche à lancer. Implementation concrète de {@code AbstractTask}.
     * @param action
     *               Action à effectuer aprés le traitement. La plupart du temps, ce sera un rafrichissement de la liste des éléments : {@code t -> refreshList(getPredicate())}.
     * @param t
     *               Controleur de la vue qui lance la tâche.
     * @param        <T>
     *               Classe du modèle FXML.
     * @param        <A>
     *               Classe de l'action à traiter.
     */
    public static <T extends AbstractFXMLModele<I>, A extends Action, I> void startTaskWithAction(AbstractTask task, Consumer<AbstractBDD<T, A, I>> action, AbstractBDD<T, A, I> t)
    {
        startTaskWithAction(task, null, action, t);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Création de la fenêtre de progression avec lancement de la tâche.
     * 
     * @param task
     *              La tâche à lancer. Implementation concrète de {@code AbstractTask}.
     * @param titre
     *              Titre de la fenêtre de progression.
     * @return
     *         Le dialog responsable de l'affichage de la progression de la tâche.
     */
    private static ProgressDialog createDialog(AbstractTask task, String titre)
    {
        ProgressDialog dialog;

        // Si le titre est null on prend celui du dialogue.
        if (titre != null)
            dialog = new ProgressDialog(task, titre);
        else
            dialog = new ProgressDialog(task);

        // Lancement du thread de contrôle. Celui-ci va attendre le fin du thread de la tâche et propagee les erreurs au besoin.
        new Thread(() -> {
            try
            {
                task.get();
            }
            catch (InterruptedException | ExecutionException e)
            {
                Platform.runLater(dialog::close);
                Main.gestionException(e);
                Thread.currentThread().interrupt();
            }
        }).start();

        // Lancement du thread de la tâche.
        new Thread(task).start();
        return dialog;
    }

    /*---------- ACCESSEURS ----------*/
}
