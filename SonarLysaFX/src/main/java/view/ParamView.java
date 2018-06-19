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
import model.enums.Param;
import utilities.Statics;

/**
 * Bloc d'affichage pour paramétrer une donnée de l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ParamView extends VBox implements ViewXML<Param, TextField>
{
    private Param typeParam;
    private TextField field;

    public ParamView(Param typeParam, String texte)
    {
        // Initialisation + style CSS
        this.typeParam = typeParam;
        ObservableList<Node> rootChildren = getChildren();
        getStylesheets().add("application.css");
        getStyleClass().add("bgimage");

        HBox box = new HBox();

        // Label
        Label label = new Label(typeParam.toString() + Statics.DEUXPOINTS);
        label.setPrefWidth(250);
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
    public Param getType()
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