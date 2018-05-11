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
import utilities.Statics;

/**
 * Bloc d'affichage pour paramétrer une donnée de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ParamView extends VBox implements ViewXML<TypeParam, TextField>
{
    private TypeParam typeParam;
    private TextField field;

    public ParamView(TypeParam typeParam, String texte)
    {
        this.typeParam = typeParam;
        ObservableList<Node> rootChildren = getChildren();

        HBox box = new HBox();

        // Label
        Label label = new Label(typeParam.toString() + Statics.DEUXPOINTS);
        label.setPrefWidth(200);
        box.getChildren().add(label);

        // TextField
        field = new TextField(texte == null ? "" : texte.replace("\\\\", "\\"));
        HBox.setHgrow(field, Priority.ALWAYS);
        box.getChildren().add(field);

        rootChildren.add(box);

        // Séparateur
        Separator separ = new Separator();
        separ.setPadding(new Insets(5, 5, 5, 5));
        rootChildren.add(separ);
    }

    /**
     * @return the typeCol
     */
    public TypeParam getType()
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