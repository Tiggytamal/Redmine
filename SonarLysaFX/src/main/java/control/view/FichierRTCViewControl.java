package control.view;

import control.task.AbstractLaunchTask;
import control.task.MajLotsRTCTask;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import utilities.FunctionalException;
import utilities.enums.Severity;

/**
 * Contr�le de la mise � jour du fichier RTC.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public final class FichierRTCViewControl extends AbstractLaunchTask
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
            startTask(new MajLotsRTCTask(datePicker.getValue(), true), "Initialisation fichier lots RTC");
        else
            throw new FunctionalException(Severity.ERROR, "La date limite de cr�ation des lots doit �tre renseign�e.");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
