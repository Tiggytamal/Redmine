package fxml.dialog;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import model.fxml.DefautQualiteFXML;

/**
 * Fenêtre affichant les remarques d'un DefautQualité et permet sa modification et sa persistance.
 * 
 * @author ETP8137 - grégoire Mathon
 * @since 2.0
 *
 */
public class RemarqueDialog extends AbstractBaseDialog<String, TextArea>
{
    /*---------- ATTRIBUTS ----------*/

    private DefautQualiteFXML item;

    /*---------- CONSTRUCTEURS ----------*/

    public RemarqueDialog(DefautQualiteFXML item)
    {
        super("Remarques", "/fxml/dialog/RemarqueDialog.fxml");

        this.item = item;
        init();
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected void initImpl()
    {
        // Bouton OK
        ButtonType saveButtonType = new ButtonType("Sauvegarder", ButtonData.OK_DONE);
        pane.getButtonTypes().add(saveButtonType);
        affCancel();

        String save = item.getRemarque();

        node.setText(save);

        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType)
                return node.getText();
            else
                return save;
        });
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
