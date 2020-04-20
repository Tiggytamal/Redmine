package fxml.node;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import utilities.TechnicalException;

/**
 * Classe mère abstraite de toutes les éléments fxml spécifiques à l'application.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public abstract class AbstractBaseView<P extends Enum<P>, F extends Node> extends VBox implements ViewXML<P, F>
{
    /*---------- ATTRIBUTS ----------*/

    protected P param;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractBaseView(String fxml, P param)
    {
        this.param = param;

        try
        {
            FXMLLoader loader = new FXMLLoader(AbstractBaseView.class.getResource(fxml));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }
        catch (IOException e)
        {
            throw new TechnicalException(getClass().getName() + " - Impossible d'instancier la classe.", e);
        }
    }
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Override
    public final P getType()
    {
        return param;
    }
}
