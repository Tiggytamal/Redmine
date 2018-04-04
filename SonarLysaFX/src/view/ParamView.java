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
import model.enums.TypeParam;

public class ParamView extends VBox
{
    private TypeParam typeParam;
    private TextField field;
    private static final String POINT = " : ";

    public ParamView(TypeParam typeParam, String texte)
    {
        this.typeParam = typeParam;
        ObservableList<Node> rootChildren = getChildren();

        HBox box = new HBox();

        // Label
        Label label = new Label(typeParam.toString() + POINT);
        label.setPrefWidth(200);
        box.getChildren().add(label);

        // TextField
        field = new TextField(texte == null ? "" : texte.replace("\\\\", "\\"));
        HBox.setHgrow(field, Priority.ALWAYS);
        box.getChildren().add(field);

        rootChildren.add(box);

        // SÚparateur
        Separator separ = new Separator();
        separ.setPadding(new Insets(5, 5, 5, 5));
        rootChildren.add(separ);
    }

    /**
     * @return the typeCol
     */
    public TypeParam getTypeParam()
    {
        return typeParam;
    }

    /**
     * @return the field
     */
    public TextField getField()
    {
        return field;
    }
}