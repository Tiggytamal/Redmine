package fxml.factory;

import dao.Dao;
import fxml.bdd.DefautQualiteBDD;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import model.bdd.DefautQualite;
import model.fxml.DefautQualiteFXML;

public class DefautQualiteFXMLTableRowFactory implements Callback<TableView<DefautQualiteFXML>, TableRow<DefautQualiteFXML>>
{
    /*---------- ATTRIBUTS ----------*/

    private DefautQualiteBDD control;
    private Dao<DefautQualite, String> dao;

    /*---------- CONSTRUCTEURS ----------*/

    public DefautQualiteFXMLTableRowFactory(DefautQualiteBDD control, Dao<DefautQualite, String> dao)
    {
        this.control = control;
        this.dao = dao;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public TableRow<DefautQualiteFXML> call(TableView<DefautQualiteFXML> param)
    {
        return new DefautQualiteFXMLTableRow(control, dao);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
