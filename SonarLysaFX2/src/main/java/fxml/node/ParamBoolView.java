package fxml.node;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import model.enums.ParamBool;
import utilities.Statics;

/**
 * Bloc d'affichage pour paramètrer une donnée de l'application de type booléen.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ParamBoolView extends AbstractBaseView<ParamBool, CheckBox>
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private CheckBox checkBox;
    @FXML
    private Label label;

    /*---------- CONSTRUCTEURS ----------*/

    public ParamBoolView(ParamBool typeBool, Boolean bool)
    {
        super("ParamBool.fxml", typeBool);

        // Initialisation textes
        if (bool == null)
            bool = Boolean.FALSE;

        label.setText(typeBool.getNom() + Statics.DEUXPOINTS);

        checkBox.setSelected(bool);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public CheckBox getField()
    {
        return checkBox;
    }
}
