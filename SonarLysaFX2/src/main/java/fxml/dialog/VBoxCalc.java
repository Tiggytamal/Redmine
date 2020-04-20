package fxml.dialog;

import java.io.IOException;

import fxml.node.AbstractBaseView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * VBox personnalisée pour afficher une ligne de résultat de la calculette statistiques.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class VBoxCalc extends VBox
{
    /*---------- ATTRIBUTS ----------*/

    private final StringProperty titre = new SimpleStringProperty(this, "titre", Statics.EMPTY);
    
    @FXML
    private Label texte;
    @FXML
    private Label labelValeur;

    /*---------- CONSTRUCTEURS ----------*/

    public VBoxCalc()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(AbstractBaseView.class.getResource("/fxml/dialog/VBoxCalc.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }
        catch (IOException e)
        {
            throw new TechnicalException(getClass().getName() + " - Impossible d'instancier la classe.", e);
        }

        texte.textProperty().bind(titre);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public final void setTitre(final String value)
    {
        titre.set(value);
    }

    public final String getTitre()
    {
        return titre.get();
    }

    public final StringProperty titreProperty()
    {
        return titre;
    }

    public void setValeur(String valeur)
    {
        this.labelValeur.setText(valeur);
    }

    public String getValeur()
    {
        return labelValeur.getText();
    }
}
