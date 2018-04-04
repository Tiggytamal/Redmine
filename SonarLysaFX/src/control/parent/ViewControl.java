package control.parent;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;
import view.ProgressDialog;

public abstract class ViewControl
{
    /*---------- ATTRIBUTS ----------*/
    
    @FXML
    protected GridPane backgroundPane;
    
    protected static final String TITRE = "Fichier Excel";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    protected void startTask(SonarTask task, String titre)
    {
        ProgressDialog dialog;
        if (titre != null)
            dialog = new ProgressDialog(task, titre);
        else
            dialog = new ProgressDialog(task);
        dialog.show();
        Future<?> future = dialog.startTask();
        
        try {
            future.get();
         }  catch (InterruptedException | ExecutionException e)
         {
             dialog.hide();
             Statics.logger.error(e.getCause());
             Main.gestionException(e.getCause());
         }
    }
    
    /**
     * Permet de gérer l'affichage entre les différentes options de la page
     * @param event
     */
    @FXML
    protected abstract void afficher (ActionEvent event) throws IOException;
    
    /**
     * Initialise la fenêtre de l'explorateur de fichier, et retourne le fichier selectionné.
     * 
     * @param titre
     *            Titre de la fenêtre de l'explorateur
     * @return
     */
    protected File getFileFromFileChooser(String titre)
    {
        FileChooser fc = new FileChooser();
        fc.setTitle(titre);
        fc.getExtensionFilters().add(Statics.FILTEREXCEL);
        File file = fc.showOpenDialog(backgroundPane.getScene().getWindow());
        if (file == null)
            throw new FunctionalException(Severity.SEVERITY_INFO, "Impossible de récupérer le fichier.");
        return file;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}