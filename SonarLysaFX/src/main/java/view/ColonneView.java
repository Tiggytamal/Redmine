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
import model.enums.TypeCol;

/**
 * Bloc d'affichage pour paramétrer le nom d'une colonne
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 * @param <T>
 */
public class ColonneView<T extends Enum<T> & TypeCol> extends VBox implements ViewXML<T, TextField>
{
    private T typeCol;
    private TextField field;
    private static final String POINT = " : ";

    public ColonneView(T typeCol, String texte)
    {
        this.typeCol = typeCol;
        ObservableList<Node> children = getChildren();
        getStylesheets().add("application.css");
        getStyleClass().add("bgimage");
        HBox box = new HBox();

        // Label
        Label label = new Label(typeCol.getValeur() + POINT);
        label.setPrefWidth(150);
        box.getChildren().add(label);

        // TextField
        field = new TextField(texte == null ? "" : texte);
        HBox.setHgrow(field, Priority.ALWAYS);
        box.getChildren().add(field);

        children.add(box);

        // Séparateur
        Separator separ = new Separator();
        separ.setPadding(new Insets(5, 5, 5, 5));
        children.add(separ);
    }

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