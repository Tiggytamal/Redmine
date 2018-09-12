package control.view;

import java.io.File;
import java.io.IOException;

import control.task.CreerExtractComposantsSonarTask;
import control.task.CreerExtractVulnerabiliteTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import model.enums.OptionExtract;
import utilities.TechnicalException;

/**
 * Gestion de l'affichage pour la création de sextractions SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public final class ExtractionViewControl extends AbstractViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private VBox selectPane;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton radioVuln;
    @FXML
    private RadioButton radioCompo;
    @FXML
    private Button extraire;
    
    private OptionExtract option;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void extraire()
    {
        File file = saveFileFromFileChooser(TITRE);
        switch (option)
        {
            case COMPOSANTS:
                startTask(new CreerExtractComposantsSonarTask(file));
                break;
                
            case VULNERABILITES:
                startTask(new CreerExtractVulnerabiliteTask(file));
                break;            
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected void afficher(ActionEvent event) throws IOException
    {
        Object source = event.getSource();

        if (source instanceof RadioButton)
        {
            selectPane.getChildren().clear();

            String id = ((RadioButton) source).getId();

            switch (id)
            {
                case "radioVuln":
                    selectPane.getChildren().clear();
                    selectPane.getChildren().add(extraire);
                    option = OptionExtract.VULNERABILITES;
                    break;
                    
                case "radioCompo":
                    selectPane.getChildren().clear();
                    selectPane.getChildren().add(extraire);
                    option = OptionExtract.COMPOSANTS;
                    break;

                default:
                    throw new TechnicalException("RadioButton pas géré" + id);
            }
        }
    }

    /*---------- ACCESSEURS ----------*/

}
