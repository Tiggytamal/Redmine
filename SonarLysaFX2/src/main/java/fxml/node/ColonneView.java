package fxml.node;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.enums.ColR;
import utilities.Statics;

/**
 * Bloc d'affichage pour paramètrer le nom d'une colonne.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 * @param <P>
 *        Enumération utilisée pour l'affichage.
 */
public class ColonneView<P extends Enum<P> & ColR> extends AbstractBaseView<P, TextField>
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private TextField texteField;
    @FXML
    private Label label;

    /*---------- CONSTRUCTEURS ----------*/

    public ColonneView(P param, String texte)
    {
        super("Colonne.fxml", param);

        // Initialisation textes
        label.setText(param.getValeur() + Statics.DEUXPOINTS);

        if (texte != null)
            texteField.setText(texte);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public TextField getField()
    {
        return texteField;
    }
}
