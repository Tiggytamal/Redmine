package fxml;

import control.task.LaunchTask;
import control.task.portfolio.CreerPortfolioCompoTU;
import control.task.portfolio.CreerPortfolioDirectionTask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import utilities.AbstractToStringImpl;
import utilities.TechnicalException;

/**
 * Controleur de la page de gestion des autres portfolios SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public final class AutresPortfolios extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private Button creer;
    @FXML
    private RadioButton radioCompoTU;
    @FXML
    private RadioButton radioDirection;
    @FXML
    private ToggleGroup tg;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES FXML ----------*/

    @FXML
    private void creer()
    {
        if (radioCompoTU.isSelected())
            LaunchTask.startTask(new CreerPortfolioCompoTU());
        else if (radioDirection.isSelected())
            LaunchTask.startTask(new CreerPortfolioDirectionTask());
        else
            throw new TechnicalException("fxml.AutresPortfolios.creer - RadioButton pas géré : " + ((Node) tg.getSelectedToggle()).getId());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
