package fxml.dialog;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import model.bdd.AbstractBDDModele;
import model.enums.Severity;
import utilities.FunctionalException;
import utilities.Statics;

/**
 * Classe fenêtre des fenêtres pour ajouter des éléments dans un tableau et la base de données.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 * @param <T>
 *        Classe du modèle retourné par la fenêtre.
 */
public abstract class AbstractAjouterDialog<T extends AbstractBDDModele<U>, U> extends AbstractBaseDialog<T, GridPane>
{
    /*---------- ATTRIBUTS ----------*/

    // Données internes
    private int row;
    protected String donnesIncorrectes;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractAjouterDialog(String titre)
    {
        super(titre, "/fxml/dialog/AbstractAjouterDialog.fxml");
        donnesIncorrectes = Statics.EMPTY;
        init();
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Implémentation de controle pour vérifier la bonne création des objets à persister.
     * 
     * @return
     *         Vrai si le contrôle est bon.
     */
    protected abstract boolean controle();

    /**
     * Retourne l'objet créée vers le traitement qui à lancé la fenêtre.
     * 
     * @return
     *         L'objet à persister créé depuis la fenêtre.
     */
    protected abstract T retourObjet();

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    protected final void initImpl()
    {
        pane.getButtonTypes().add(ButtonType.OK);
        affCancel();

        // Converion des données en Defautqualité
        setResultConverter(button -> {
            if (button == ButtonType.OK)
            {
                if (controle())
                    return retourObjet();
                else
                    throw new FunctionalException(Severity.INFO, donnesIncorrectes);
            }
            return null;
        });
    }

    /**
     * Crée un Field dans le Dialog permettant d'enregistrer une chaîne de caractères.
     * 
     * @param id
     *                Id de la Node.
     * @param tooltip
     *                Tooltip à afficher.
     * @return
     *         Le TextField finalisé.
     */
    protected final TextField creerTextField(String id, String tooltip)
    {
        TextField retour = new TextField();
        retour.setId(id);
        GridPane.setHgrow(retour, Priority.ALWAYS);
        retour.setPromptText(tooltip);
        retour.setTooltip(new Tooltip(tooltip));
        ajouterGrid(tooltip, retour);
        return retour;
    }

    /**
     * Crée un Field dans le Dialog permettant d'enregistrer une date
     * 
     * @param id
     *              Id de la Node.
     * @param label
     *              Le texte du Label à afficher.
     * @return
     *         Le DatePicker finalisé.
     */
    protected final DatePicker creerDateField(String id, String label)
    {
        DatePicker retour = new DatePicker();
        retour.setId(id);
        ajouterGrid(label, retour);
        return retour;
    }

    /**
     * Crée un Field dans le Dialog permettant d'enregistrer un Integer
     * 
     * @param id
     *                   Id de la Node.
     * @param promptText
     *                   promptText à afficher.
     * @param label
     *                   Le texte du Label à afficher.
     * @param tooltip
     *                   Tooltip à afficher.
     * @return
     *         Le TextField finalisé.
     */
    protected final TextField creerIntegerField(String id, String promptText, String label, String tooltip)
    {
        TextField retour = new TextField();
        retour.setId(id);
        retour.setTooltip(new Tooltip(tooltip));
        retour.setPromptText(promptText);
        retour.setMaxWidth(100);

        // Utilisation d'un TextFormatter : avec StringConverter<Integer> anonyme puis contrôle qu'on ajoute que des chiffres
        retour.setTextFormatter(new TextFormatter<>(new StringConverter<Integer>() {

            @Override
            public String toString(Integer value)
            {
                if (value == null)
                    return Statics.EMPTY;
                return value.toString();
            }

            @Override
            public Integer fromString(String value)
            {
                if (value == null)
                    return null;

                value = value.trim();
                if (value.length() < 1)
                    return null;
                return Integer.valueOf(value);
            }

        }, null, change -> {
            if (change.getControlNewText().matches("[0-9]{0,6}"))
                return change;
            return null;
        }));

        ajouterGrid(label, retour);
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Ajoute la node selectionnée à la parente avec un Label.
     * 
     * @param texte
     *              Texte du label à ajouter.
     * @param field
     *              Node à ajouter.
     */
    private void ajouterGrid(String texte, Node field)
    {
        node.add(new Label(texte + Statics.DEUXPOINTS), 0, row);
        node.add(field, 1, row);
        row++;
    }

    /*---------- ACCESSEURS ----------*/
}
