package control.view;

import static utilities.Statics.proprietesXML;

import java.time.LocalDate;

import org.quartz.SchedulerException;

import control.quartz.ControlJob;
import control.xml.ControlXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.ModelFactory;
import model.Planificateur;
import model.enums.TypePlan;
import utilities.TechnicalException;
import view.TimeSpinner;
import view.TrayIconView;

/**
 * Controleur de la vue des planificateurs
 * 
 * @author ETP8137 - Grégoire Mathon
 */
public class PlanificateurViewControl extends AbstractViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private Button demarrer;
    @FXML
    private Button arreter;
    @FXML
    private VBox vboxPane;
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
    private CheckBox suivanteBox;
    @FXML
    private VBox anneePane;
    @FXML
    private CheckBox precedenteBox;
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
    private final LocalDate today = LocalDate.now();
    private static final short COL2 = 2;

    /*---------- CONSTRUCTEURS ----------*/

    public PlanificateurViewControl() throws SchedulerException
    {
        control = new ControlJob();
    }

    @FXML
    public void initialize()
    {
        planificateur = initPlan(TypePlan.SUIVIHEBDO);
        vboxPane.getChildren().remove(anneePane);
        setInfos(planificateur);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void demarrer() throws SchedulerException
    {
        MainScreen.changeImageTray(TrayIconView.imageRed);
        sauvegarder();
        control.creationJobsSonar();
    }

    @FXML
    public void arreter() throws SchedulerException
    {
        MainScreen.changeImageTray(TrayIconView.imageBase);
        control.fermeturePlanificateur();
    }

    @FXML
    public void sauvegarder()
    {
        planificateur.getAnnees().clear();
        planificateur.setHeure(spinner.getValue());
        planificateur.setLundi(lundiBox.isSelected());
        planificateur.setMardi(mardiBox.isSelected());
        planificateur.setMercredi(mercrediBox.isSelected());
        planificateur.setJeudi(jeudiBox.isSelected());
        planificateur.setVendredi(vendrediBox.isSelected());
        planificateur.setActive(activeBox.isSelected());

        if (suivanteBox.isVisible() && suivanteBox.isSelected())
            planificateur.addNextYear();

        if (precedenteBox.isVisible() && precedenteBox.isSelected())
            planificateur.addLastYear();

        new ControlXML().saveParam(proprietesXML);
    }

    @Override
    public void afficher(ActionEvent event)
    {
        String id = "";
        Object source = event.getSource();
        if (source instanceof Node)
            id = ((Node) source).getId();
        vboxPane.getChildren().remove(anneePane);

        switch (id)
        {
            case "radioSuivi":
                planificateur = initPlan(TypePlan.SUIVIHEBDO);
                break;

            case "radioChc":
                planificateur = initPlan(TypePlan.VUECHC);
                vboxPane.getChildren().add(COL2, anneePane);
                break;

            case "radioCdm":
                planificateur = initPlan(TypePlan.VUECDM);
                vboxPane.getChildren().add(COL2, anneePane);
                break;

            default:
                throw new TechnicalException("RadioButton pas géré" + id, null);
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

        // Gestion de l'heure de déclenchement
        hboxPane.getChildren().remove(spinner);
        spinner = new TimeSpinner(planificateur.getHeure());
        hboxPane.getChildren().add(spinner);

        // Gestion des cases à cocher
        if (planificateur.getAnnees().contains(String.valueOf(today.getYear() + 1)))
            suivanteBox.setSelected(true);
        else
            suivanteBox.setSelected(false);

        if (planificateur.getAnnees().contains(String.valueOf(today.getYear() - 1)))
            precedenteBox.setSelected(true);
        else
            precedenteBox.setSelected(false);

    }

    private Planificateur initPlan(TypePlan typePlan)
    {
        return proprietesXML.getMapPlans().computeIfAbsent(typePlan, k -> ModelFactory.getModel(Planificateur.class));
    }

    /*---------- ACCESSEURS ----------*/
}
