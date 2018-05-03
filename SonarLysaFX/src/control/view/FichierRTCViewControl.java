package control.view;

import control.task.LaunchTask;
import control.task.MajFichierRTCTask;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

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
        startTask(new MajFichierRTCTask(datePicker.getValue(), true), "Initialisation fichier lots RTC");        
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}