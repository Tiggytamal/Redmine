package fxml.factory;

import dao.Dao;
import fxml.bdd.ComposantBDD;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import model.bdd.ComposantBase;
import model.fxml.ComposantFXML;

public class ComposantFXMLTableRowFactory implements Callback<TableView<ComposantFXML>, TableRow<ComposantFXML>>
{
    /*---------- ATTRIBUTS ----------*/

    private ComposantBDD control;
    private Dao<ComposantBase, String> dao;

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantFXMLTableRowFactory(ComposantBDD control, Dao<ComposantBase, String> dao)
    {
        this.control = control;
        this.dao = dao;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public TableRow<ComposantFXML> call(TableView<ComposantFXML> param)
    {
        return new ComposantFXMLTableRow(control, dao);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
