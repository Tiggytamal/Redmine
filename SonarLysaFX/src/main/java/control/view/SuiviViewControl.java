package control.view;

import control.task.MajSuiviExcelTask;
import control.task.MajSuiviExcelTask.TypeMaj;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import utilities.TechnicalException;

public class SuiviViewControl extends AbstractViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private RadioButton radioSuivi;
    @FXML
    private RadioButton radioDataStage;
    @FXML
    private RadioButton radioMulti;
    @FXML
    private RadioButton radioCobol;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private VBox selectPane;
    @FXML
    private Button executer;

    private TypeMaj typeMaj;

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void executer()
    {
        startTask(new MajSuiviExcelTask(typeMaj), typeMaj.toString());
    }

    @Override
    protected void afficher(ActionEvent event)
    {
        String id = "";
        Object source = event.getSource();
        if (source instanceof RadioButton)
            id = ((RadioButton) source).getId();

        switch (id)
        {
            case "radioSuivi":
                executer.setDisable(false);
                typeMaj = TypeMaj.SUIVI;
                break;

            case "radioDataStage":
                executer.setDisable(false);
                typeMaj = TypeMaj.DATASTAGE;
                break;

            case "radioCobol":
                executer.setDisable(false);
                typeMaj = TypeMaj.COBOL;
                break;

            case "radioMulti":
                executer.setDisable(false);
                typeMaj = TypeMaj.MULTI;
                break;

            default:
                throw new TechnicalException("RadioButton pas géré" + id, null);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
