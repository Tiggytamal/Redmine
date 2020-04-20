package fxml.dialog;

import java.util.function.Consumer;

import control.task.AbstractTask;
import fxml.bdd.AbstractBDD;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import model.enums.Action;
import model.fxml.AbstractFXMLModele;

/**
 * Permet d'afficher l'avancement d'une tâche en cours
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ProgressDialog extends AbstractBaseDialog<Boolean, GridPane>
{

    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final short BARWIDTH = 100;

    @FXML
    private Label stage;
    @FXML
    private ProgressBar bar;
    @FXML
    private ProgressIndicator indicator;
    @FXML
    private Label label;
    @FXML
    private Label timer;
    @FXML
    private Button cancel;
    @FXML
    private Button ok;

    private AbstractTask task;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Contructuer du dialog utilisant le titrede la tâche.
     * 
     * @param task
     *             Tâche qui va être suivi dans le Dialog.
     */
    public ProgressDialog(AbstractTask task)
    {
        this(task, task.getTitre());
    }

    /**
     * Contructuer du dialog utilisant le titre en paramètre.
     * 
     * @param task
     *              Tâche qui va être suivi dans le Dialog.
     * @param titre
     *              Titre du Dialog.
     */
    public ProgressDialog(AbstractTask task, String titre)
    {
        super(titre, "ProgressDialog.fxml");

        this.task = task;
        init();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Rajoute l'action de rafraichir la liste des objets au bouton OK.
     * 
     * @param        <A>
     *               Type de l'action à traiter. (classe file de Action).
     * @param action
     *               Action a effectuer aprés le traitement. par exemple raffraichir la vue du tableau.
     * @param        <T>
     *               Type de l'objet à traiter. Modèle de type FXML
     * @param t
     *               Objet à traiter.
     */
    public <T extends AbstractFXMLModele<I>, A extends Action, I> void ajouterAction(Consumer<AbstractBDD<T, A, I>> action, AbstractBDD<T, A, I> t)
    {
        ok.setOnAction(event -> { action.accept(t); fermer(); });
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected void initImpl()
    {
        // Evite que la fenêtre prenne le contrôle de l'affichage
        initModality(Modality.NONE);

        // Gestion de la taille de la grille
        node.widthProperty().addListener((observable, oldValue, newValue) -> { label.setPrefWidth(newValue.doubleValue()); bar.setPrefWidth(newValue.doubleValue() - BARWIDTH); });

        // Liaison de la progression àa la tâche
        bar.setPrefWidth(node.getPrefWidth() - BARWIDTH);
        bar.progressProperty().unbind();
        bar.progressProperty().bind(task.progressProperty());
        indicator.progressProperty().unbind();
        indicator.progressProperty().bind(task.progressProperty());

        // Liaison de l'affichage à la tâche
        label.setPrefWidth(node.getPrefWidth());
        label.textProperty().bind(task.messageProperty());
        stage.setPrefWidth(node.getPrefWidth());
        stage.textProperty().bind(task.etapeProperty());
        timer.setPrefWidth(node.getPrefWidth());
        timer.textProperty().bind(task.affTimerProperty());

        // Gestion du bouton d'annulation
        cancel.setOnAction(event -> annuler());
        if (!task.isAnnulable())
        {
            cancel.setDisable(true);
            cancel.setTooltip(new Tooltip("Cette tâche ne peut pas être annulee"));
        }

        // Bouton OK actif lorsque le traitement est fini.
        ok.setOnAction(event -> fermer());
        task.setOnSucceeded(t -> { ok.setDisable(false); cancel.setDisable(true); });

        // Annulation en cas de clic sur la croix
        pane.getScene().getWindow().setOnCloseRequest(event -> {
            if (task.isCancelled())
                annuler();
            else
                fermer();
        });
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Annule un traitmeent, retire les liens, appele la methode fermer de la task et ferme la fenêtre.
     */
    @FXML
    private void annuler()
    {
        bar.progressProperty().unbind();
        label.textProperty().unbind();
        indicator.progressProperty().unbind();
        if (task.isRunning())
        {
            task.cancel(true);
            task.annuler();
        }

        fermer();
    }

    /**
     * Permet de fermer la fenêtre
     */
    @FXML
    private void fermer()
    {
        pane.getScene().getWindow().hide();
    }

    /*---------- ACCESSEURS ----------*/

}
