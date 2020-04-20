package fxml.factory;

import control.task.action.TraiterActionCompoTask;
import dao.Dao;
import fxml.bdd.ComposantBDD;
import model.bdd.ComposantBase;
import model.enums.ActionC;
import model.fxml.ComposantFXML;

public class ComposantFXMLTableRow extends FXMLTableRow<ComposantFXML, String, ActionC, Dao<ComposantBase, String>, ComposantBase, ComposantBDD>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String YELLOW = "-fx-background-color:lightyellow";
    private static final String ORANGE = "-fx-background-color:orange";

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantFXMLTableRow(ComposantBDD control, Dao<ComposantBase, String> dao)
    {
        super(dao, control, ActionC.values());

        creerMenu(a -> new TraiterActionCompoTask(a, dao.recupEltParIndex(getItem().getIndex())));
    }
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void updateItem(ComposantFXML item, boolean empty)
    {
        super.updateItem(item, empty);

        if (item == null || empty)
            return;

        // Ligne orange si le composant est un doublon
        if (item.isDoublon())
            setStyle(ORANGE);

        // Ligne jaune si le composant n'a pas la dernière version possible
        else if (!item.isVersionOK())
            setStyle(YELLOW);
        else
            setStyle(null);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
