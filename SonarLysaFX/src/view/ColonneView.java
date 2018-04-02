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


public class ColonneView<T extends Enum<T> & TypeCol> extends VBox
{
    private T typeCol;
    private TextField field;


    private static final String POINT = " : "; 
    
    public ColonneView(T typeCol, String valeur)
    {        
        this.typeCol = typeCol;   
        ObservableList<Node> children = getChildren(); 
                
        HBox box = new HBox();

        // Label
        Label label = new Label(typeCol.toString() + POINT);
        label.setPrefWidth(150);
        box.getChildren().add(label);

        // TextField
        field = new TextField(valeur);
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
    public T getTypeCol()
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
