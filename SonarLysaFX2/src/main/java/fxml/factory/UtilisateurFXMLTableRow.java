package fxml.factory;

import dao.Dao;
import fxml.bdd.UtilisateurBDD;
import model.bdd.Utilisateur;
import model.enums.ActionU;
import model.fxml.UtilisateurFXML;

public class UtilisateurFXMLTableRow extends FXMLTableRow<UtilisateurFXML, String, ActionU, Dao<Utilisateur, String>, Utilisateur, UtilisateurBDD>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String YELLOW = "-fx-background-color:lightyellow";

    /*---------- CONSTRUCTEURS ----------*/

    public UtilisateurFXMLTableRow(UtilisateurBDD control, Dao<Utilisateur, String> dao)
    {
        super(dao, control, ActionU.values());
    }
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void updateItem(UtilisateurFXML item, boolean empty)
    {
        super.updateItem(item, empty);

        if (item == null || empty)
            setStyle(null);

        // Ligne jaune si l'utilisateur est désactivé
        else if (!item.isActive())
            setStyle(YELLOW);
        else
            setStyle(null);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
