package view;

import java.io.IOException;

import control.SonarTask;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;

public class ProgressDialog extends Dialog<Boolean>
{
    
    /*---------- ATTRIBUTS ----------*/

    private ProgressBar bar;
    private ProgressIndicator indicator;
    private Label label;
    private SonarTask task;
    
    @FXML
    private GridPane backgroundPane;
    
    /*---------- CONSTRUCTEURS ----------*/

    public ProgressDialog(SonarTask task, String titre) throws IOException
    {
        backgroundPane = FXMLLoader.load(getClass().getResource("/view/tesPane.fxml"));
        backgroundPane.setPrefWidth(500);
        // Initialisation
        this.task = task;  
        setTitle(titre);
        setHeaderText(null);
        setResizable(true);
        
        // Gridpane
        GridPane grid = new GridPane();
        grid.setPrefWidth(350);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));  
        grid.widthProperty().addListener((observable,  oldValue, newValue) ->
            {
                label.setPrefWidth(newValue.doubleValue());
                bar.setPrefWidth(newValue.doubleValue()-100);
            });
        getDialogPane().setContent(grid);
        
        // ProgressBar
        bar = new ProgressBar(0);
        bar.setPrefWidth(grid.getPrefWidth()-100);
        grid.add(bar, 0, 0);
        bar.progressProperty().unbind();
        bar.progressProperty().bind(task.progressProperty()); 
        
        // Progress Indicator
        indicator = new ProgressIndicator(0);
        grid.add(indicator, 1, 0);
        indicator.progressProperty().unbind();
        indicator.progressProperty().bind(task.progressProperty());
        
        //label
        label = new Label();
        label.setPrefWidth(grid.getPrefWidth());
        grid.add(label, 0, 1, GridPane.REMAINING, 1);
        label.textProperty().bind(task.messageProperty());
        
        // Cancel
        Button cancel = new Button("Annuler");
        grid.add(cancel, 1, 2);
        cancel.setOnAction(event -> annuler());  
        
        // Bouton OK actif lorsque le traitement est fini.
        Button ok = new Button("OK");
        grid.add(ok, 0, 2);
        ok.setOnAction(event -> fermer());
        GridPane.setHalignment(ok, HPos.RIGHT);
        ok.setDisable(true);
        task.setOnSucceeded(t -> 
        {
            ok.setDisable(false);
            cancel.setDisable(true);
        });
        
        // Annulation en cas de clic sur la croix
        getDialogPane().getScene().getWindow().setOnCloseRequest(event -> 
        {
            if (task.isCancelled())
                annuler();
            else
                fermer();
        });

    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    public void startTask()
    {
        new Thread(task).start();
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * Annule un traitmeent, retire les liens, appele la méthode fermer de la task et ferme la denêtre
     */
    private void annuler()
    {
        task.cancel(true);
        bar.progressProperty().unbind();
        label.textProperty().unbind();
        indicator.progressProperty().unbind();
        task.annuler();
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