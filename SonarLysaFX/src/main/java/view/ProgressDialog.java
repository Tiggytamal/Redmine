package view;

import control.task.AbstractTask;
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
 * Permet d'afficher l'avancement d'une t�che en cours
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public class ProgressDialog extends Dialog<Boolean>
{

    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final short GRIDWIDTH = 350;
    private static final short GRIDHEIGHT = 300;
    private static final short BASEINSET = 10;
    private static final short BARWIDTH = 100;
    
    private ProgressBar bar;
    private ProgressIndicator indicator;
    private Label label;
    private Label timer;
    private Label stage;
    private AbstractTask task;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur utilisant la variable TITRE de la SonarTask
     * 
     * @param task
     */
    public <T extends AbstractTask> ProgressDialog(T task)
    {
        this(task, task.getTitre());
    }

    public ProgressDialog(AbstractTask task, String titre)
    {
        // Initialisation
        this.task = task;
        setTitle(titre);
        setHeaderText(null);
        setResizable(true);
        initModality(Modality.NONE);
        getDialogPane().getStylesheets().add("application.css");

        // Gridpane
        GridPane grid = new GridPane();
        grid.setPrefWidth(GRIDWIDTH);
        grid.setPrefHeight(GRIDHEIGHT);
        grid.setHgap(BASEINSET);
        grid.setVgap(BASEINSET);
        grid.setPadding(new Insets(BASEINSET, BASEINSET, BASEINSET, BASEINSET));
        grid.widthProperty().addListener((observable, oldValue, newValue) -> {
            label.setPrefWidth(newValue.doubleValue());
            bar.setPrefWidth(newValue.doubleValue() - BARWIDTH); 
        });
        getDialogPane().setContent(grid);

        // Stage
        stage = new Label();
        stage.setPrefWidth(grid.getPrefWidth());
        stage.textProperty().bind(task.etapeProperty());
        grid.add(stage, 0, 0, GridPane.REMAINING, 1);

        // ProgressBar
        bar = new ProgressBar(0);
        bar.setPrefWidth(grid.getPrefWidth() - BARWIDTH);
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
        
        // Timer
        timer = new Label();
        timer.setPrefWidth(grid.getPrefWidth());
        timer.textProperty().bind(task.affTimerProperty());
        grid.add(timer, 0, 3, GridPane.REMAINING, 1);

        // Cancel
        Button cancel = new Button("Annuler");
        cancel.setOnAction(event -> annuler());
        grid.add(cancel, 1, 4);
        if (!task.isAnnulable())
        {
            cancel.setDisable(true);
            cancel.setTooltip(new Tooltip("Cette t�che ne peut pas �tre annul�e"));
        }

        // Ajout d'un bouton close cach� pour permettre la fermeture du dialog en cas de plantage de la t�che
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);

        // Bouton OK actif lorsque le traitement est fini.
        Button ok = new Button("OK");
        grid.add(ok, 0, 4);
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
     * Annule un traitmeent, retire les liens, appele la m�thode fermer de la task et ferme la den�tre
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
     * Permet de fermer la fen�tre
     */
    private void fermer()
    {
        getDialogPane().getScene().getWindow().hide();
    }

    /*---------- ACCESSEURS ----------*/

}
