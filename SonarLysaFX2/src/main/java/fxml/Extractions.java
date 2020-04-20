package fxml;

import java.io.File;

import control.task.LaunchTask;
import control.task.extraction.ExtractionComposantsSonarTask;
import control.task.extraction.ExtractionVulnerabiliteTask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import utilities.AbstractToStringImpl;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Gestion de l'affichage pour la création des extractions SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public final class Extractions extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private RadioButton radioVuln;
    @FXML
    private RadioButton radioCompo;
    @FXML
    private ToggleGroup tg;
    @FXML
    private Button extraire;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES FXML ----------*/

    @FXML
    private void extraire()
    {
        if (radioCompo.isSelected())
        {
            File file = Utilities.saveFileFromFileChooser("Fichier Extraction Composants");
            LaunchTask.startTask(new ExtractionComposantsSonarTask(file));
        }

        else if (radioVuln.isSelected())
        {
            File file = Utilities.saveFileFromFileChooser("Fichier Extraction Vulnérabilités");
            LaunchTask.startTask(new ExtractionVulnerabiliteTask(file));
        }
        else
            throw new TechnicalException("fxml.Extractions.extraire - radioBouton non géré : " + ((Node) tg.getSelectedToggle()).getId());
    }

    /*---------- METHODES PRIVEES ----------*/

    /*---------- ACCESSEURS ----------*/
}
