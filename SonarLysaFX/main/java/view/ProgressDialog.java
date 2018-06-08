package view;

import control.task.SonarTask;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

/**
 * Permet d'afficher l'avancement d'une tâche en cours
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ProgressDialog extends Dialog<Boolean>
{

    /*---------- ATTRIBUTS ----------*/

    private ProgressBar bar;
    private ProgressIndicator indicator;
    private Label label;
    private Label stage;
    private SonarTask task;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur utilisant la variable TITRE de la SonarTask
     * 
     * @param task
     */
    public <T extends SonarTask> ProgressDialog(T task)
    {
        this(task, T.TITRE);
    }

    public ProgressDialog(SonarTask task, String titre)
    {
        // Initialisation
        this.task = task;
        setTitle(titre);
        setHeaderText(null);
        setResizable(true);
        initModality(Modality.NONE);

        // Gridpane
        GridPane grid = new GridPane();
        grid.setPrefWidth(350);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.widthProperty().addListener((observable, oldValue, newValue) -> {
            label.setPrefWidth(newValue.doubleValue());
            bar.setPrefWidth(newValue.doubleValue() - 100);
        });
        getDialogPane().setContent(grid);

        // Stage
        stage = new Label();
        stage.setPrefWidth(grid.getPrefWidth());
        stage.textProperty().bind(task.etapeProperty());
        grid.add(stage, 0, 0, GridPane.REMAINING, 1);
        
        // ProgressBar
        bar = new ProgressBar(0);
        bar.setPrefWidth(grid.getPrefWidth() - 100);
        bar.progressProperty().unbind();
        bar.progressProperty().bind(task.progressProperty());
        grid.add(bar, 0, 1);

        // Progress Indicator
        indicator = new ProgressIndicator(0);
        indicator.progressProperty().unbind();
        indicator.progressProperty().bind(task.progressProperty());
        grid.add(indicator, 1, 1);

        // label
        label = new Label();
        label.setPrefWidth(grid.getPrefWidth());
        label.textProperty().bind(task.messageProperty());
        grid.add(label, 0, 2, GridPane.REMAINING, 1);

        // Cancel
        Button cancel = new Button("Annuler");
        cancel.setOnAction(event -> annuler());
        grid.add(cancel, 1, 3);
        if (!task.isAnnulable())
        {
            cancel.setDisable(true);
            cancel.setTooltip(new Tooltip("Cette tâche ne peut pas être annulée"));
        }
        
        // Ajout d'un bouton close caché pour permettre la fermeture du dialog en cas de plantage de la tâche
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);

        // Bouton OK actif lorsque le traitement est fini.
        Button ok = new Button("OK");
        grid.add(ok, 0, 3);
        ok.setOnAction(event -> fermer());
        GridPane.setHalignment(ok, HPos.RIGHT);
        ok.setDisable(true);
        task.setOnSucceeded(t -> {
            ok.setDisable(false);
            cancel.setDisable(true);
        });

        // Annulation en cas de clic sur la croix
        getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
            if (task.isCancelled())
                annuler();
            else
                fermer();
        });
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Annule un traitmeent, retire les liens, appele la méthode fermer de la task et ferme la denêtre
     */
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
    private void fermer()
    {
        getDialogPane().getScene().getWindow().hide();
    }

    /*---------- ACCESSEURS ----------*/

}