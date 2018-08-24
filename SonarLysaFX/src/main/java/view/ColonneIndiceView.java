package view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.enums.TypeColW;
import utilities.Statics;

/**
 * Bloc d'affichage pour paramétrer le nom d'une colonne
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 * @param <T>
 */
public class ColonneIndiceView<T extends Enum<T> & TypeColW> extends VBox implements ViewXML<T, TextField>
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final String POINT = " : ";
    private static final short REGIONWIDTH = 20;
    private static final int WIDTHLABEL = 100;
    private static final int WIDTHINDICE = 30;
    private static final int PADDING = 5;
    
    private T typeCol;
    private TextField texteField;
    private TextField indiceField;

    /*---------- CONSTRUCTEURS ----------*/

    public ColonneIndiceView(T typeCol, String texte, String indice)
    {
        this.typeCol = typeCol;
        ObservableList<Node> children = getChildren();
        getStylesheets().add("application.css");
        getStyleClass().add("bgimage");
        HBox box = new HBox();

        // Label
        Label label = new Label(typeCol.getValeur() + POINT);
        label.setPrefWidth(WIDTHLABEL);
        box.getChildren().add(label);

        indiceField = new TextField(indice == null ? Statics.EMPTY : indice);
        indiceField.setPrefWidth(WIDTHINDICE);
        box.getChildren().add(indiceField);
        
        // Region
        Region region = new Region();
        region.setPrefWidth(REGIONWIDTH);
        region.setPrefHeight(REGIONWIDTH);
        box.getChildren().add(region);
               
        // TextField
        texteField = new TextField(texte == null ? Statics.EMPTY : texte);
        HBox.setHgrow(texteField, Priority.ALWAYS);
        box.getChildren().add(texteField);

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
    @Override
    public T getType()
    {
        return typeCol;
    }

    /**
     * @return the field
     */
    @Override
    public TextField getField()
    {
        return texteField;
    }
    
    public TextField getIndice()
    {
        return indiceField;
    }   
}
