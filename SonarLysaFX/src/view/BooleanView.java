package view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.enums.TypeBool;

/**
 * Bloc d'affichage pour paramétrer une donnée de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class BooleanView extends VBox implements ViewXML<TypeBool, CheckBox>
{
    private TypeBool typeBool;
    private CheckBox checkBox;
    private static final String POINT = " : ";

    public BooleanView(TypeBool typeBool, Boolean bool)
    {
        this.typeBool = typeBool;
        ObservableList<Node> rootChildren = getChildren();

        HBox box = new HBox();

        // Label
        Label label = new Label(typeBool.toString() + POINT);
        label.setPrefWidth(200);
        box.getChildren().add(label);

        // TextField
        checkBox = new CheckBox();
        checkBox.setSelected(bool == null ? false : bool);
        box.getChildren().add(checkBox);

        rootChildren.add(box);

        // Séparateur
        Separator separ = new Separator();
        separ.setPadding(new Insets(5, 5, 5, 5));
        rootChildren.add(separ);
    }

    /**
     * @return the typeCol
     */
    public TypeBool getType()
    {
        return typeBool;
    }

    /**
     * @return the field
     */
    public CheckBox getField()
    {
        return checkBox;
    }
}