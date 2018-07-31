package control.view;

import control.task.LaunchTask;
import control.task.MajFichierRTCTask;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import utilities.FunctionalException;
import utilities.enums.Severity;

public class FichierRTCViewControl extends LaunchTask
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private DatePicker datePicker;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void majFichierRTC()
    {
        if (datePicker.getValue() != null)
            startTask(new MajFichierRTCTask(datePicker.getValue(), true), "Initialisation fichier lots RTC");
        else
            throw new FunctionalException(Severity.ERROR, "La date limite de création des lots doit être renseignée.");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
