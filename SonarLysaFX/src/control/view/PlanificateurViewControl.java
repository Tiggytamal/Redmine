package control.view;

import static utilities.Statics.proprietesXML;

import java.io.IOException;
import java.time.LocalTime;

import org.quartz.SchedulerException;

import control.quartz.ControlJob;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Planificateur;
import model.enums.TypePlan;
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
    @FXML
    private RadioButton radioCdm;
    @FXML
    private Spinner<LocalTime> heureSpin;
    @FXML
    private HBox hboxPane;
    
    private ControlJob control;
    private Planificateur planificateur;

    /*---------- CONSTRUCTEURS ----------*/

    public PlanificateurViewControl() throws SchedulerException
    {
        control = new ControlJob();
    }

    @FXML
    public void initialize() throws IOException
    {
        planificateur = proprietesXML.getMapPlans().get(TypePlan.SUIVIHEBDO);
//        FXMLLoader loader = new FXMLLoader();
//        planificateur = new Planificateur();
//        planificateur.setHeure(LocalTime.now());
//        loader.getNamespace().put("init", planificateur.getHeure());
//        loader.setLocation(getClass().getResource("/view/PlanInfos.fxml"));
//        hboxPane.getChildren().add(loader.load());
//        setInfos(planificateur);
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

    @FXML
    public void afficherCdm()
    {

    }

    @FXML
    public void afficherPlanificateur(ActionEvent event)
    {
        Object source = event.getSource();

    }
    /*---------- METHODES PRIVEES ----------*/

    private void setInfos(Planificateur planificateur)
    {
        if (planificateur == null)
            return;
        lundiBox.setSelected(planificateur.isLundi());
        mardiBox.setSelected(planificateur.isMardi());
        mercrediBox.setSelected(planificateur.isMercredi());
        jeudiBox.setSelected(planificateur.isJeudi());
        vendrediBox.setSelected(planificateur.isVendredi());
    }

    /*---------- ACCESSEURS ----------*/
}
