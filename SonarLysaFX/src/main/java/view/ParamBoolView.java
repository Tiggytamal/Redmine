package view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.enums.ParamBool;

/**
 * Bloc d'affichage pour paramétrer une donnée de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ParamBoolView extends VBox implements ViewXML<ParamBool, CheckBox>
{
    private ParamBool typeBool;
    private CheckBox checkBox;
    private static final String POINT = " : ";
    private static final short LABELWIDTH = 300;
    private static final short BASEPADDING = 5;

    public ParamBoolView(ParamBool typeBool, Boolean bool)
    {
        // Initialisation + style CSS
        this.typeBool = typeBool;
        ObservableList<Node> rootChildren = getChildren();
        getStylesheets().add("application.css");
        getStyleClass().add("bgimage");

        HBox box = new HBox();

        // Label
        Label label = new Label(typeBool.toString() + POINT); 
        label.setPrefWidth(LABELWIDTH);
        box.getChildren().add(label);

        // TextField
        checkBox = new CheckBox();
        checkBox.setSelected(bool == null ? false : bool);
        box.getChildren().add(checkBox);

        rootChildren.add(box);

        // Séparateur
        Separator separ = new Separator();
        separ.setPadding(new Insets(BASEPADDING, BASEPADDING, BASEPADDING, BASEPADDING));
        rootChildren.add(separ);
    }

    /**
     * @return the typeCol
     */
    public ParamBool getType()
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
