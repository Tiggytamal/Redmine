package fxml;

import java.util.ArrayList;
import java.util.List;

import control.task.LaunchTask;
import control.task.portfolio.CreerPortfolioCHCCDMTask;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import model.enums.TypeEdition;
import utilities.AbstractToStringImpl;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Gestion de l'affichage de la création des portfolios de maintenance (CHC - CDM)
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public final class Maintenance extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private ToggleGroup tg;
    @FXML
    private RadioButton radioCHC;
    @FXML
    private RadioButton radioCHCCDM;
    @FXML
    private CheckBox suivante;
    @FXML
    private CheckBox precedente;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/

    @FXML
    private void creer()
    {
        // Création de la liste des années
        List<String> annees = new ArrayList<>();
        annees.add(String.valueOf(Statics.TODAY.getYear()));
        if (suivante.isSelected())
            annees.add(String.valueOf(Statics.TODAY.getYear() + 1));
        if (precedente.isSelected())
            annees.add(String.valueOf(Statics.TODAY.getYear() - 1));

        // Lancement de la tâche selon le raiobouton selectionné
        if (radioCHC.isSelected())
            LaunchTask.startTask(new CreerPortfolioCHCCDMTask(annees, TypeEdition.CHC), "Vues CHC");
        else if (radioCHCCDM.isSelected())
            LaunchTask.startTask(new CreerPortfolioCHCCDMTask(annees, TypeEdition.CDM), "Vues CHC_CDM");
        else
            throw new TechnicalException("fxml.Maintenance.creer : RadioButton inconnu selectionné.");
    }

    /*---------- ACCESSEURS ----------*/
}
