package fxml.factory;

import dao.Dao;
import fxml.bdd.DefautQualiteHistoriqueBDD;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import model.bdd.DefautQualite;
import model.fxml.DefautQualiteFXML;

public class DefautQualiteHistoriqueFXMLTableRowFactory implements Callback<TableView<DefautQualiteFXML>, TableRow<DefautQualiteFXML>>
{
    /*---------- ATTRIBUTS ----------*/

    private DefautQualiteHistoriqueBDD control;
    private Dao<DefautQualite, String> dao;

    /*---------- CONSTRUCTEURS ----------*/

    public DefautQualiteHistoriqueFXMLTableRowFactory(DefautQualiteHistoriqueBDD control, Dao<DefautQualite, String> dao)
    {
        this.control = control;
        this.dao = dao;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public TableRow<DefautQualiteFXML> call(TableView<DefautQualiteFXML> param)
    {
        return new DefautQualiteHistoriqueFXMLTableRow(control, dao);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
