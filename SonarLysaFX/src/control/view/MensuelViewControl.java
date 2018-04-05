package control.view;

import java.io.File;
import java.time.LocalDate;

import control.task.CreerVueProductionTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utilities.FunctionalException;
import utilities.TechnicalException;
import utilities.enums.Severity;

public class MensuelViewControl extends ViewControl
{
    /* ---------- ATTIBUTS ---------- */

    /* Attributs FXML */

    @FXML
    private HBox listeMoisHBox;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private HBox dateDebutHBox;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private HBox dateFinHBox;
    @FXML
    private Button charger;
    @FXML
    private Button creer;
    @FXML
    private RadioButton radioExcel;
    @FXML
    private RadioButton radioDate;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private VBox selectPane;

    /* ---------- CONSTRUCTEURS ---------- */

    @FXML
    public void initialize()
    {
        selectPane.getChildren().clear();
        backgroundPane.getChildren().remove(creer);
    }

    /* ---------- METHODES PUBLIQUES ---------- */

    /**
     * Création de la vue depuis un fichier Excel
     */
    @FXML
    public void chargerExcel()
    {
        File file = getFileFromFileChooser(TITRE);
        CreerVueProductionTask task = new CreerVueProductionTask(file);
        startTask(task, null);
    }

    /**
     * Switch entre les radioButtons
     * 
     * @param event
     */
    @Override
    public void afficher(ActionEvent event)
    {
        String id = "";
        Object source = event.getSource();
        if (source instanceof RadioButton)
            id = ((RadioButton) source).getId();

        switch (id)
        {
            case "radioExcel" :
                selectPane.getChildren().clear();
                selectPane.getChildren().add(charger);
                break;

            case "radioDate" :
                selectPane.getChildren().clear();
                selectPane.getChildren().add(dateDebutHBox);
                selectPane.getChildren().add(dateFinHBox);
                selectPane.getChildren().add(creer);
                break;
                
            default :
                throw new TechnicalException("RadioButton pas géré" + id, null);
        }
    }

    /**
     * Création de la vue depuis Sonar
     */
    @FXML
    public void creerVue()
    {
        // Contrôle sur les dates
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();
        if (dateDebut == null || dateFin == null || dateFin.isBefore(dateDebut))
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Les dates sont mal renseignées");

        // Traitement
        startTask(new CreerVueProductionTask(dateDebut, dateFin), null);
    }
}