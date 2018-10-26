package control.view.fxml;

import java.io.File;

import control.view.MainScreen;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import model.fxml.ModeleFXML;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

public abstract class AbstractFXMLViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    protected TableView<ModeleFXML> table;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES ABSTRAITES ----------*/

    public abstract void initialize();
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/
    
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

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public final TableView<ModeleFXML> getTable()
    {
        return table;
    }
}
