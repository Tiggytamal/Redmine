package view;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;

public class ProgressDialog extends Dialog<Boolean>
{
    
    /*---------- ATTRIBUTS ----------*/

    private ProgressBar bar;
    private Label label;
    private Task<Object> worker;
    
    /*---------- CONSTRUCTEURS ----------*/

    public ProgressDialog(Task<Object> worker, String titre)
    {
        // Initialisation
        this.worker = worker;        
        setTitle(titre);
        setHeaderText(null);
        
        // Gridpane
        GridPane grid = new GridPane();
        grid.setPrefWidth(300);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 80, 10, 10));  
        getDialogPane().setContent(grid);
        
        // ProgressBar
        bar = new ProgressBar(0);
        bar.setPrefWidth(grid.getPrefWidth());
        grid.add(bar, 0, 0);
        bar.progressProperty().unbind();
        bar.progressProperty().bind(worker.progressProperty()); 
        
        //label
        label = new Label();
        label.setPrefWidth(grid.getPrefWidth());
        grid.add(label, 0, 1);
        label.textProperty().bind(worker.messageProperty());
        
        // Cancel
        Button cancel = new Button("Annuler");
        grid.add(cancel, 0, 2);
        cancel.setOnAction(event -> annuler());        
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    public void start()
    {
        new Thread(worker).start();
    }
    /*---------- METHODES PRIVEES ----------*/
    
    private void annuler()
    {
        worker.cancel(true);
        bar.progressProperty().unbind();
        label.textProperty().unbind();
    }
    
    /*---------- ACCESSEURS ----------*/
    
}
