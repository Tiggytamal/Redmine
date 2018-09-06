package control.view;

import java.io.File;
import java.io.IOException;

import control.task.AbstractLaunchTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

/**
 * Classe m�re des controleurs de l'affichage de l'application
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public abstract class AbstractViewControl extends AbstractLaunchTask
{
    /*---------- ATTRIBUTS ----------*/

    protected static final String TITRE = "Fichier Excel";
    
    @FXML
    protected GridPane backgroundPane;

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet de g�rer l'affichage entre les diff�rentes options de la page
     * 
     * @param event
     */
    @FXML
    protected abstract void afficher(ActionEvent event) throws IOException;

    /**
     * Initialise la fen�tre de l'explorateur de fichier, et retourne le fichier selectionn�.
     * 
     * @param titre
     *            Titre de la fen�tre de l'explorateur
     * @return
     */
    protected File getFileFromFileChooser(String titre)
    {
        FileChooser fc = new FileChooser();
        fc.setTitle(titre);
        fc.getExtensionFilters().add(Statics.FILTEREXCEL);
        File file = fc.showOpenDialog(MainScreen.getRoot().getScene().getWindow());
        if (file == null)
            throw new FunctionalException(Severity.INFO, "Impossible de r�cup�rer le fichier.");
        return file;
    }

    /**
     * Initialise la fen�tre de l'explorateur de fichier, et retourne le fichier selectionn�.
     * 
     * @param titre
     *            Titre de la fen�tre de l'explorateur
     * @return
     */
    protected File saveFileFromFileChooser(String titre)
    {
        FileChooser fc = new FileChooser();
        fc.setTitle(titre);
        fc.getExtensionFilters().add(Statics.FILTEREXCEL);
        File file = fc.showSaveDialog(MainScreen.getRoot().getScene().getWindow());
        if (file == null)
            throw new FunctionalException(Severity.INFO, "Impossible de r�cup�rer le fichier.");
        return file;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}