package control.view;

import java.io.File;
import java.io.IOException;

import control.task.CreerVueParAppsTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import model.enums.CreerVueParAppsTaskOption;

/**
 * Gestion de l'affichage du control pour la cr�ation des vues par application
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public final class VuesApplisViewControl extends AbstractViewControl
{
    /*---------- ATTRIBUTS ----------*/
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton radioVue;
    @FXML
    private RadioButton radioFichier;
    @FXML
    private RadioButton radioAll;
    @FXML
    private Button traiter;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void afficher(ActionEvent event) throws IOException
    {
        // Pas de traitement sur cet �cran
    }

    @FXML
    public void traiter()
    {
        if (radioVue.isSelected())
            startTask(new CreerVueParAppsTask(CreerVueParAppsTaskOption.VUE, null), null);
        else if (radioFichier.isSelected())
        {
            File file = saveFileFromFileChooser(TITRE);
            startTask(new CreerVueParAppsTask(CreerVueParAppsTaskOption.FICHIERS, file), null);
        }
        else if (radioAll.isSelected())
        {
            File file = saveFileFromFileChooser(TITRE);
            startTask(new CreerVueParAppsTask(CreerVueParAppsTaskOption.ALL, file), null);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}