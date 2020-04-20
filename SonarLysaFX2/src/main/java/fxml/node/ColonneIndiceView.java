package fxml.node;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.enums.ColW;
import utilities.Statics;

/**
 * Bloc d'affichage pour paramètrer le nom d'une colonne
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 * @param <P>
 *        Enumération à afficher.
 */
public class ColonneIndiceView<P extends Enum<P> & ColW> extends AbstractBaseView<P, TextField>
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private TextField texteField;
    @FXML
    private Label label;
    @FXML
    private TextField indiceField;

    /*---------- CONSTRUCTEURS ----------*/

    public ColonneIndiceView(P param, String texte, String indice)
    {
        super("ColonneIndice.fxml", param);

        // Initialisation textes
        label.setText(param.getValeur() + Statics.DEUXPOINTS);

        if (indice != null)
            indiceField.setText(indice);

        if (texte != null)
            texteField.setText(texte);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    @Override
    public TextField getField()
    {
        return texteField;
    }
    
    public TextField getIndice()
    {
        return indiceField;
    }
}
