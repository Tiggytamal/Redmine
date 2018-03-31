package control.view;

import control.parent.ViewControl;
import control.task.MajSuiviExcelTask;
import control.task.MajSuiviExcelTask.TypeMaj;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SuiviViewControl extends ViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private Button majSuivi;
    @FXML
    private Button majDataStage;
    @FXML
    private Button majDouble;
    
    /*---------- CONSTRUCTEURS ----------*/   
    
    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void majSuivi()
    {
        majDouble.setDisable(true);
        startTask(new MajSuiviExcelTask(TypeMaj.SUIVI), TypeMaj.SUIVI.toString());
    }
    
    @FXML
    public void majDataStage()
    {
        majDouble.setDisable(true);
        startTask(new MajSuiviExcelTask(TypeMaj.DATASTAGE), TypeMaj.DATASTAGE.toString());
    }
    
    @FXML
    public void majDouble()
    {
        majDataStage.setDisable(true);
        majSuivi.setDisable(true);
        startTask(new MajSuiviExcelTask(TypeMaj.DOUBLE), TypeMaj.DOUBLE.toString());
    }

    @Override
    protected void afficher(ActionEvent event)
    {
        // Pas de gestion de l'affichage        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}