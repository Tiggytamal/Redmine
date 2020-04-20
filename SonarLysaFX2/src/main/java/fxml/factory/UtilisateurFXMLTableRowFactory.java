package fxml.factory;

import dao.Dao;
import fxml.bdd.UtilisateurBDD;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import model.bdd.Utilisateur;
import model.fxml.UtilisateurFXML;

public class UtilisateurFXMLTableRowFactory implements Callback<TableView<UtilisateurFXML>, TableRow<UtilisateurFXML>>
{
    /*---------- ATTRIBUTS ----------*/

    private UtilisateurBDD control;
    private Dao<Utilisateur, String> dao;

    /*---------- CONSTRUCTEURS ----------*/

    public UtilisateurFXMLTableRowFactory(UtilisateurBDD control, Dao<Utilisateur, String> dao)
    {
        this.control = control;
        this.dao = dao;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public TableRow<UtilisateurFXML> call(TableView<UtilisateurFXML> param)
    {
        return new UtilisateurFXMLTableRow(control, dao);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
