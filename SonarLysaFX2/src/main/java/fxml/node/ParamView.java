package fxml.node;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.enums.Param;
import utilities.Statics;

/**
 * Bloc d'affichage pour paramètrer une donnée de l'application.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ParamView extends AbstractBaseView<Param, TextField>
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private TextField texteField;
    @FXML
    private Label label;

    /*---------- CONSTRUCTEURS ----------*/

    public ParamView(Param param, String texte)
    {
        super("Param.fxml", param);

        // Initialition textes
        label.setText(param.getNom() + Statics.DEUXPOINTS);
        
        if (texte != null)
            texteField.setText(texte.replace("\\\\", "\\"));
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public TextField getField()
    {
        return texteField;
    }
}
