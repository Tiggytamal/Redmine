package fxml.factory;

import dao.Dao;
import fxml.bdd.ComposantPlanteBDD;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import model.bdd.ComposantErreur;
import model.fxml.ComposantPlanteFXML;

public class ComposantPlanteFXMLTableRowFactory implements Callback<TableView<ComposantPlanteFXML>, TableRow<ComposantPlanteFXML>>
{
    /*---------- ATTRIBUTS ----------*/

    private ComposantPlanteBDD control;
    private Dao<ComposantErreur, String> dao;

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantPlanteFXMLTableRowFactory(ComposantPlanteBDD control, Dao<ComposantErreur, String> dao)
    {
        this.control = control;
        this.dao = dao;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public TableRow<ComposantPlanteFXML> call(TableView<ComposantPlanteFXML> param)
    {
        return new ComposantPlanteFXMLTableRow(control, dao);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
