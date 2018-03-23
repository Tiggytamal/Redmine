package control.view;

import org.quartz.SchedulerException;

import control.quartz.ControlJob;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import view.TrayIconView;

public class PlanificateurViewControl
{
    /*---------- ATTRIBUTS ----------*/
    
    @FXML
    private Button demarrer;
    @FXML
    private Button arreter;
    @FXML
    private GridPane backgroundPane;
    @FXML
    private CheckBox lundiBox;
    @FXML
    private CheckBox mardiBox;
    @FXML
    private CheckBox mercrediBox;
    @FXML
    private CheckBox jeudiBox;
    @FXML
    private CheckBox vendrediBox;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton radioSuivi;
    @FXML
    private RadioButton radioChc;
    
    private ControlJob control;

    /*---------- CONSTRUCTEURS ----------*/
    
    public PlanificateurViewControl() throws SchedulerException
    {
        control = new ControlJob();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void demarrer() throws SchedulerException
    {
        MainScreen.changeImageTray(TrayIconView.imageRed);
        control.creationJobsSonar();
    }
    
    @FXML
    public void arreter() throws SchedulerException
    {
        MainScreen.changeImageTray(TrayIconView.imageBase);
        control.fermeturePlanificateur();
    }
    
    @FXML
    public void afficherSuivi()
    {
        
    }
    
    @FXML
    public void afficherChc()
    {
        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
