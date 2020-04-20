package fxml.node;

import static utilities.Statics.proprietesXML;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import model.enums.ParamSpec;
import utilities.Statics;

/**
 * Classe gérant l'affichage d'un paramètre sous forme de texte.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 *
 */
public class ParamTextView extends AbstractBaseView<ParamSpec, TextArea>
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final Pattern PATTERNRC = Pattern.compile("[\\n\\r]");

    private double nbreLignes;

    @FXML
    private Label label;
    @FXML
    private TextArea textArea;

    /*---------- CONSTRUCTEURS ----------*/

    public ParamTextView(ParamSpec param)
    {
        super("ParamText.fxml", param);

        // Initialisation
        label.setText(param.getNom() + Statics.DEUXPOINTS);

        // Recupération texte selon paramètre personnel ou non
        String text;
        if (!param.isPerso())
            text = proprietesXML.getMapParamsSpec().computeIfAbsent(param, p -> Statics.EMPTY).replace("\\n", Statics.NL);
        else
            text = Statics.propPersoXML.getParamsSpec().computeIfAbsent(param, p -> Statics.EMPTY).replace("\\n", Statics.NL);
        textArea.setText(text);

        // Gestion hauteur de la zone
        gestionHauteur();
        textArea.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            nbreLignes = 1L;
            Matcher matcher = PATTERNRC.matcher(textArea.getText());
            while (matcher.find())
            {
                nbreLignes++;
            }
            textArea.setPrefHeight(nbreLignes * Statics.ROW_HEIGHT + 2);
        });

        textArea.setWrapText(true);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/

    private void gestionHauteur()
    {
        nbreLignes = 1L;
        Matcher matcher = PATTERNRC.matcher(textArea.getText());
        while (matcher.find())
        {
            nbreLignes++;
        }
        textArea.setPrefHeight(nbreLignes * Statics.ROW_HEIGHT + 2);
    }
    /*---------- ACCESSEURS ----------*/

    @Override
    public TextArea getField()
    {
        return textArea;
    }

}
