package fxml;

import java.time.LocalDate;

import control.task.LaunchTask;
import control.task.maj.MajLotsRTCTask;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import model.enums.Severity;
import utilities.AbstractToStringImpl;
import utilities.FunctionalException;

/**
 * Contrôle de la mise à jour des lots RTC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public final class MajLotsRTC extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private DatePicker datePicker;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    
    @FXML
    private void majLotsRTC()
    {
        if (datePicker.getValue() != null && datePicker.getValue().isBefore(LocalDate.now()))
            LaunchTask.startTask(new MajLotsRTCTask(datePicker.getValue()), "Initialisation fichier lots RTC");
        else
            throw new FunctionalException(Severity.ERROR, "La date limite de création des lots doit être renseignée et antérieure à la date du jour.");
    }

    /*---------- ACCESSEURS ----------*/
}
