package view;

import static utilities.Statics.proprietesXML;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.enums.ParamSpec;
import utilities.Statics;

public class ParamTextView extends VBox
{
    /*---------- ATTRIBUTS ----------*/

    private ParamSpec param;
    private TextArea textArea;
    private double nbreLignes;
    private static final short BASEPADDING = 10;

    /*---------- CONSTRUCTEURS ----------*/

    public ParamTextView(ParamSpec param)
    {
        // Initialisation + style CSS
        this.param = param;
        ObservableList<Node> rootChildren = getChildren();
        getStylesheets().add("application.css");
        getStyleClass().add("bgimage");

        // ----- 1. Label -----
        Label label = new Label(param.toString() + Statics.DEUXPOINTS);
        rootChildren.add(label);

        // ----- 2.Region -----
        Region region = new Region();
        region.setPrefHeight(BASEPADDING);
        rootChildren.add(region);

        // ----- 3. TextArea -----
        String text = proprietesXML.getMapParamsSpec().get(param).replace("\\n", Statics.NL);
        textArea = new TextArea(text);

        // Gestion hauteur de la zone
        gestionHauteur();
        textArea.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            nbreLignes = 1L;
            Matcher matcher = Pattern.compile("[\\n\\r]").matcher(textArea.getText());
            while (matcher.find())
            {
                nbreLignes++;
            }
            textArea.setPrefHeight(nbreLignes * Statics.ROW_HEIGHT + 2);
        });

        textArea.setWrapText(true);
        rootChildren.add(textArea);

        // ----- 4. Séparateur -----
        Separator separ = new Separator();
        separ.setPadding(new Insets(BASEPADDING, BASEPADDING/2, BASEPADDING, BASEPADDING/2));
        rootChildren.add(separ);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void sauverValeurs()
    {
        proprietesXML.getMapParamsSpec().put(param, textArea.getText());
    }

    /*---------- METHODES PRIVEES ----------*/

    private void gestionHauteur()
    {
        nbreLignes = 1L;
        Matcher matcher = Pattern.compile("[\\n\\r]").matcher(textArea.getText());
        while (matcher.find())
        {
            nbreLignes++;
        }
        textArea.setPrefHeight(nbreLignes * Statics.ROW_HEIGHT + 2);
    }
    /*---------- ACCESSEURS ----------*/

}
