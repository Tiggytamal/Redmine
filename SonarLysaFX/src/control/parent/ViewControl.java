package control.parent;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

public abstract class ViewControl extends LaunchTask
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    protected GridPane backgroundPane;

    protected static final String TITRE = "Fichier Excel";

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet de gérer l'affichage entre les différentes options de la page
     * 
     * @param event
     */
    @FXML
    protected abstract void afficher(ActionEvent event) throws IOException;

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