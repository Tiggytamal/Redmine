package view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.enums.TypeColR;

/**
 * Bloc d'affichage pour paramétrer le nom d'une colonne
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 * @param <T>
 */
public class ColonneView<T extends Enum<T> & TypeColR> extends VBox implements ViewXML<T, TextField>
{
    /*---------- ATTRIBUTS ----------*/

    private T typeCol;
    private TextField field;
    private static final String POINT = " : ";
    private static final int WIDTH = 150;
    private static final int PADDING = 5;

    /*---------- CONSTRUCTEURS ----------*/

    public ColonneView(T typeCol, String texte)
    {
        this.typeCol = typeCol;
        ObservableList<Node> children = getChildren();
        getStylesheets().add("application.css");
        getStyleClass().add("bgimage");
        HBox box = new HBox();

        // Label
        Label label = new Label(typeCol.getValeur() + POINT);
        label.setPrefWidth(WIDTH);
        box.getChildren().add(label);

        // TextField
        field = new TextField(texte == null ? "" : texte);
        HBox.setHgrow(field, Priority.ALWAYS);
        box.getChildren().add(field);

        children.add(box);

        // Séparateur
        Separator separ = new Separator();
        separ.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        children.add(separ);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    /**
     * @return the typeCol
     */
    public T getType()
    {
        return typeCol;
    }

    /**
     * @return the field
     */
    public TextField getField()
    {
        return field;
    }
}
