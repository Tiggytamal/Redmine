package control.view;

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
import utilities.Statics;
import utilities.TechnicalException;
import utilities.enums.Severity;

/**
 * Gestion de l'affichage de la création des vues menseulles et trimestrielles de mise ne production
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public final class MensuelViewControl extends AbstractViewControl
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
    private Button creer;
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
     * Switch entre les radioButtons
     * 
     * @param event
     */
    @Override
    public void afficher(ActionEvent event)
    {
        String id = Statics.EMPTY;
        Object source = event.getSource();
        if (source instanceof RadioButton)
            id = ((RadioButton) source).getId();

        if ("radioDate".equals(id))
        {
            selectPane.getChildren().clear();
            selectPane.getChildren().add(dateDebutHBox);
            selectPane.getChildren().add(dateFinHBox);
            selectPane.getChildren().add(creer);
        }
        else
            throw new TechnicalException("RadioButton pas géré" + id, null);

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
            throw new FunctionalException(Severity.ERROR, "Les dates sont mal renseignées");

        // Traitement
        startTask(new CreerVueProductionTask(dateDebut, dateFin), CreerVueProductionTask.TITRE);
    }
}
