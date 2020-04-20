package fxml.factory;

import control.task.action.TraiterActionCompoPlanteTask;
import dao.Dao;
import fxml.bdd.ComposantPlanteBDD;
import model.bdd.ComposantErreur;
import model.enums.ActionCp;
import model.fxml.ComposantPlanteFXML;

public class ComposantPlanteFXMLTableRow extends FXMLTableRow<ComposantPlanteFXML, String, ActionCp, Dao<ComposantErreur, String>, ComposantErreur, ComposantPlanteBDD>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String GREEN = "-fx-background-color:lightgreen";
    private static final String YELLOW = "-fx-background-color:lightyellow";

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantPlanteFXMLTableRow(ComposantPlanteBDD control, Dao<ComposantErreur, String> dao)
    {
        super(dao, control, ActionCp.values());

        creerMenu(a -> new TraiterActionCompoPlanteTask(a, dao.recupEltParIndex(getItem().getIndex())));
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void updateItem(ComposantPlanteFXML item, boolean empty)
    {
        super.updateItem(item, empty);

        if (item == null || empty)
            return;

        // Un composant qui n'est plus dans SonarQube est blanc, un composant OK est vert et un composant plante est jaune
        if (!item.existe())
            setStyle(null);
        else if (!item.isAPurger())
            setStyle(GREEN);
        else
            setStyle(YELLOW);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
