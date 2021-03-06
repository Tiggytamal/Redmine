package control.view;

import control.task.MajSuiviExcelTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import model.enums.TypeMajSuivi;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Gestionde l'affichage pour le control des fichiers de suivi.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public final class SuiviViewControl extends AbstractViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private RadioButton radioSuivi;
    @FXML
    private RadioButton radioDataStage;
    @FXML
    private RadioButton radioIOS;
    @FXML
    private RadioButton radioAndroid;
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

    private TypeMajSuivi typeMaj;

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void executer()
    {
        startTask(new MajSuiviExcelTask(typeMaj), typeMaj.getValeur());
    }

    @Override
    protected void afficher(ActionEvent event)
    {
        String id = Statics.EMPTY;
        Object source = event.getSource();
        if (source instanceof RadioButton)
            id = ((RadioButton) source).getId();

        switch (id)
        {
            case "radioSuivi":
                executer.setDisable(false);
                typeMaj = TypeMajSuivi.JAVA;
                break;

            case "radioDataStage":
                executer.setDisable(false);
                typeMaj = TypeMajSuivi.DATASTAGE;
                break;

            case "radioCobol":
                executer.setDisable(false);
                typeMaj = TypeMajSuivi.COBOL;
                break;
                
            case "radioIOS":
                executer.setDisable(false);
                typeMaj = TypeMajSuivi.IOS;
                break;
                
            case "radioAndroid":
                executer.setDisable(false);
                typeMaj = TypeMajSuivi.ANDROID;
                break;

            case "radioMulti":
                executer.setDisable(false);
                typeMaj = TypeMajSuivi.MULTI;
                break;

            default:
                throw new TechnicalException("RadioButton pas g�r�" + id, null);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
