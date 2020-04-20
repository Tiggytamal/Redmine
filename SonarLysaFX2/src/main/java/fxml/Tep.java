package fxml;

import java.io.File;
import java.time.LocalDate;

import control.task.LaunchTask;
import control.task.portfolio.CreerPortfolioTrimestrielTask;
import control.task.portfolio.CreerPortfolioTrimetrielDepuisPicTask;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.enums.OptionPortfolioTrimestriel;
import model.enums.Severity;
import utilities.AbstractToStringImpl;
import utilities.FunctionalException;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Gestion de l'affichage de la création des portfolios trimestriel de mise en production.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public final class Tep extends AbstractToStringImpl
{
    /* ---------- ATTIBUTS ---------- */

    /* Attributs FXML */

    @FXML
    private HBox listeMoisHBox;
    @FXML
    private HBox dateDebutHBox;
    @FXML
    private Button creer;
    @FXML
    private RadioButton radioAll;
    @FXML
    private RadioButton radioDataStage;
    @FXML
    private RadioButton radioPic;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private VBox selectPane;

    private DatePicker datePicker;

    /* ---------- CONSTRUCTEURS ---------- */

    @FXML
    public void initialize()
    {
        datePicker = new DatePicker();
        datePicker.setDayCellFactory((DatePicker param) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty)
            {
                super.updateItem(item, empty);

                // On désactive par défaut la cellule
                setDisable(true);

                // On active le jour 3 si le premier jour est un samedi, le jour 2 si c'est un dimanche, sinon le jour 1
                if (item.getDayOfMonth() == 1)
                    setDisable(false);
            }
        });
        dateDebutHBox.getChildren().add(datePicker);
    }

    /* ---------- METHODES PUBLIQUES ---------- */

    /**
     * Création de la vue depuis Sonar
     */
    @FXML
    public void creerVue()
    {
        // Contrôle sur les dates
        LocalDate dateDebut = datePicker.getValue();

        // Contrôle de la date
        if (dateDebut == null)
            throw new FunctionalException(Severity.ERROR, "La date de début doit être renseignée.");

        // Appel méthode selon option choisie
        if (radioAll.isSelected())
            LaunchTask.startTask(new CreerPortfolioTrimestrielTask(dateDebut, OptionPortfolioTrimestriel.ALL));
        else if (radioDataStage.isSelected())
            LaunchTask.startTask(new CreerPortfolioTrimestrielTask(dateDebut, OptionPortfolioTrimestriel.DATASTAGE));
        else if (radioPic.isSelected())
        {
            File file = Utilities.getFileFromFileChooser("Charger Fichier Pic");
            LaunchTask.startTask(new CreerPortfolioTrimetrielDepuisPicTask(file, dateDebut));
        }
        else
            throw new TechnicalException("fxml.Tep.creerVue : RadioButton inconnu selectionné.");
    }
}
