package control.view;

import java.io.File;
import java.io.IOException;

import control.task.CreerExtractVulnerabiliteTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import utilities.Statics;
import utilities.TechnicalException;

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
    private Button extraireVuln;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void extraireVuln()
    {
        File file = saveFileFromFileChooser(TITRE);
        startTask(new CreerExtractVulnerabiliteTask(file), null);
    }

    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected void afficher(ActionEvent event) throws IOException
    {
        Object source = event.getSource();
        String id = Statics.EMPTY;
        if (source instanceof RadioButton)
        {
            selectPane.getChildren().clear();

            id = ((RadioButton) source).getId();

            switch (id)
            {
                case "radioVuln":
                    selectPane.getChildren().add(extraireVuln);
                    break;

                default:
                    throw new TechnicalException("RadioButton pas géré" + id, null);
            }
        }
    }

    /*---------- ACCESSEURS ----------*/

}
