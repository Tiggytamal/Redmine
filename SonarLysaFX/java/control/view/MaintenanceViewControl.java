package control.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import control.task.CreerVueCHCCDMTask;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.enums.CHCouCDM;
import utilities.TechnicalException;

public class MaintenanceViewControl extends AbstractViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private HBox checkBoxPane;
    @FXML
    private Button creer;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton radioCHC;
    @FXML
    private RadioButton radioCHCCDM;
    @FXML
    private VBox selectPane;
    @FXML
    private CheckBox suivante;
    @FXML
    private CheckBox precedente;

    private CHCouCDM chccdm;
    private String titreTask;
    private final LocalDate today = LocalDate.now();

    /*---------- CONSTRUCTEURS ----------*/

    @FXML
    public void initialize()
    {
        selectPane.getChildren().clear();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void afficher(ActionEvent event)
    {
        Object source = event.getSource();
        String id = EMPTY;
        if (source instanceof RadioButton)
        {
            ObservableList<Node> children = selectPane.getChildren();
            children.clear();

            id = ((RadioButton) source).getId();

            switch (id)
            {
                case "radioCHC" :
                    children.add(checkBoxPane);
                    children.add(creer);
                    chccdm = CHCouCDM.CHC;
                    titreTask = "Vues CHC";
                    break;

                case "radioCHCCDM" :
                    children.add(checkBoxPane);
                    children.add(creer);
                    chccdm = CHCouCDM.CDM;
                    titreTask = "Vues CHC_CDM";
                    break;

                default :
                    throw new TechnicalException("RadioButton pas g�r�" + id, null);
            }
        }
    }

    @FXML
    public void creerVues()
    {
        // Cr�ation de la liste des ann�es
        List<String> annees = new ArrayList<>();
        annees.add(String.valueOf(today.getYear()));
        if (suivante.isSelected())
            annees.add(String.valueOf(today.getYear() + 1));
        if (precedente.isSelected())
            annees.add(String.valueOf(today.getYear() - 1));

        // Lancement de la task
        CreerVueCHCCDMTask task = new CreerVueCHCCDMTask(annees, chccdm);
        startTask(task, titreTask);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
