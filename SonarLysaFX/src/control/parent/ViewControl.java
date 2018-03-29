package control.parent;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import view.ProgressDialog;

public abstract class ViewControl
{
    /*---------- ATTRIBUTS ----------*/
    
    @FXML
    protected GridPane backgroundPane;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    protected void startTask(SonarTask task, String titre) throws IOException
    {
        ProgressDialog dialog = new ProgressDialog(task, titre);
        dialog.show();
        dialog.startTask();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}