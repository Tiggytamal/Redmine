package fxml;

import static utilities.Statics.proprietesXML;

import org.quartz.SchedulerException;

import application.MainScreen;
import control.job.ControlJob;
import control.parsing.ControlXML;
import fxml.node.TimeSpinner;
import fxml.node.TrayIconView;
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
import utilities.AbstractToStringImpl;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Controleur de la fenêtre des planificateurs.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public final class Planification extends AbstractToStringImpl implements SubView
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final short COL2 = 2;

    // Object FXML
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
    private RadioButton radioChc;
    @FXML
    private RadioButton radioCdm;
    @FXML
    private RadioButton radioStats;
    @FXML
    private HBox hboxPane;

    private ControlJob control;
    private TimeSpinner spinner;
    private Planificateur planificateur;

    /*---------- CONSTRUCTEURS ----------*/

    public Planification()
    {
        control = new ControlJob();
    }

    @FXML
    public void initialize()
    {
        planificateur = initPlan(TypePlan.VUECDM);
        vboxPane.getChildren().remove(anneePane);
        setInfos(planificateur);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void afficher(ActionEvent event)
    {
        String id = Statics.EMPTY;
        Object source = event.getSource();
        if (source instanceof Node)
            id = ((Node) source).getId();
        vboxPane.getChildren().remove(anneePane);

        switch (id)
        {
            case "radioChc":
                planificateur = initPlan(TypePlan.VUECHC);
                break;

            case "radioCdm":
                planificateur = initPlan(TypePlan.VUECDM);
                break;
                
            case "radioStats":
                planificateur = initPlan(TypePlan.STATS);
                break;

            default:
                throw new TechnicalException("fxml.Planification.afficher - RadioButton pas géré : " + id, null);
        }

        vboxPane.getChildren().add(COL2, anneePane);
        setInfos(planificateur);
    }

    /*---------- METHODES FXML ----------*/

    @FXML
    private void demarrer() throws SchedulerException
    {
        MainScreen.changeImageTray(TrayIconView.imageRed);
        sauvegarder();
        control.creationJobsSonar();
    }

    @FXML
    private void arreter() throws SchedulerException
    {
        MainScreen.changeImageTray(TrayIconView.imageBase);
        control.fermeturePlanificateur();
    }

    @FXML
    private void sauvegarder()
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

        new ControlXML().saveXML(proprietesXML);
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

        // Gestion de l'heure de declenchement
        hboxPane.getChildren().remove(spinner);
        spinner = new TimeSpinner(planificateur.getHeure());
        hboxPane.getChildren().add(spinner);

        // Gestion des cases à cocher

        suivanteBox.setSelected(planificateur.getAnnees().contains(String.valueOf(Statics.TODAY.getYear() + 1)));

        precedenteBox.setSelected(planificateur.getAnnees().contains(String.valueOf(Statics.TODAY.getYear() - 1)));
    }

    private Planificateur initPlan(TypePlan type)
    {
        return proprietesXML.getMapPlans().computeIfAbsent(type, k -> ModelFactory.build(Planificateur.class));
    }

    /*---------- ACCESSEURS ----------*/
}
