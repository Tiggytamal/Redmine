package view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import model.enums.OptionMajCompos;

/**
 * Dialogue permettant de tester la connexion au serveur Sonar
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ComposantsDialog extends Dialog<OptionMajCompos>
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final int BASEGAP = 10;
    private static final int TOPINSET = 20;
    private static final int RIGHTINSET = 80;

    private Node loginButton;

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantsDialog()
    {
        setTitle("Mise à jour des composants");
        setHeaderText(null);
        getDialogPane().getStylesheets().add("application.css");

        // Boutons
        ButtonType buttonOK = new ButtonType("ok", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(buttonOK, ButtonType.CANCEL);

        // Gridpane
        GridPane grid = new GridPane();
        grid.setHgap(BASEGAP);
        grid.setVgap(BASEGAP);
        grid.setPadding(new Insets(TOPINSET, RIGHTINSET, BASEGAP, BASEGAP));

        // Option
        Label label = new Label("Choisissez le type de mise à jour :");
        ToggleGroup group = new ToggleGroup();
        RadioButton partielle = new RadioButton("Partielle. temps < 20 mins");
        group.getToggles().add(partielle);        
        RadioButton complete = new RadioButton("Complète. temps > 1h");
        group.getToggles().add(complete);

        grid.add(label, 0, 0, 2, 1);
        grid.add(partielle, 1, 1);
        grid.add(complete, 1, 2);

        // Contrôle affichage du bouton
        loginButton = getDialogPane().lookupButton(buttonOK);
        loginButton.setDisable(false);

        // Mise ne place du panel
        getDialogPane().setContent(grid);

        // Renvoi du résultat
        setResultConverter(dialogButton -> {
            if (dialogButton.equals(buttonOK))
            {
                if (partielle.isSelected())
                    return OptionMajCompos.PARTIELLE;
                else
                    return OptionMajCompos.COMPLETE;
            }
            return null;
        });
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
