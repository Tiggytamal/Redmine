package control.view;

import static utilities.Statics.proprietesXML;

import javax.xml.bind.JAXBException;

import org.quartz.SchedulerException;

import control.ControlXML;
import control.quartz.ControlJob;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import model.Planificateur;
import model.enums.TypePlan;
import view.TimeSpinner;
import view.TrayIconView;

/**
 * Controleur de la vue des planificateurs
 * 
 * @author ETP8137 - Gr�goire Mathon
 *
 */
public class PlanificateurViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private Button demarrer;
    @FXML
    private Button arreter;
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
    private CheckBox activeBox;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton radioSuivi;
    @FXML
    private RadioButton radioChc;
    @FXML
    private RadioButton radioCdm;
    @FXML
    private HBox hboxPane;
    
    private ControlJob control;
    private TimeSpinner spinner;
    private Planificateur planificateur;

    /*---------- CONSTRUCTEURS ----------*/

    public PlanificateurViewControl() throws SchedulerException
    {
        control = new ControlJob();
    }

    @FXML
    public void initialize()
    {
        planificateur = proprietesXML.getMapPlans().get(TypePlan.SUIVIHEBDO);
        setInfos(planificateur);
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
    public void sauvegarder() throws JAXBException
    {
        planificateur.setHeure(spinner.getValue());
        planificateur.setLundi(lundiBox.isSelected());
        planificateur.setMardi(mardiBox.isSelected());
        planificateur.setMercredi(mercrediBox.isSelected());
        planificateur.setJeudi(jeudiBox.isSelected());
        planificateur.setVendredi(vendrediBox.isSelected());
        planificateur.setActive(activeBox.isSelected());
        planificateur.setHeure(spinner.getValue());
        new ControlXML().saveParam(proprietesXML);
    }

    @FXML
    public void afficherPlanificateur(ActionEvent event)
    {
        String id = "";
        Object source = event.getSource();
        if (source instanceof Node)
            id = ((Node)source).getId();

        switch (id)
        {
            case "radioSuivi":
                planificateur = proprietesXML.getMapPlans().get(TypePlan.SUIVIHEBDO);
                break;
            case "radioChc":
                planificateur = proprietesXML.getMapPlans().get(TypePlan.VUECHC);
                break;
            case "radioCdm":
                planificateur = proprietesXML.getMapPlans().get(TypePlan.VUECDM);
                break;
            default:
                break;
        }        
        setInfos(planificateur);
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
        activeBox.setSelected(planificateur.isActive());
        hboxPane.getChildren().remove(spinner);
        spinner = new TimeSpinner(planificateur.getHeure());
        hboxPane.getChildren().add(spinner);
    }

    /*---------- ACCESSEURS ----------*/
}