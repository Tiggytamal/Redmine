package fxml.factory;

import dao.Dao;
import fxml.bdd.AnomalieRTCBDD;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import model.bdd.AnomalieRTC;
import model.fxml.AnomalieRTCFXML;

public class AnomalieRTCFXMLTableRowFactory implements Callback<TableView<AnomalieRTCFXML>, TableRow<AnomalieRTCFXML>>
{
    /*---------- ATTRIBUTS ----------*/

    private AnomalieRTCBDD control;
    private Dao<AnomalieRTC, Integer> dao;

    /*---------- CONSTRUCTEURS ----------*/

    public AnomalieRTCFXMLTableRowFactory(AnomalieRTCBDD control, Dao<AnomalieRTC, Integer> dao)
    {
        this.control = control;
        this.dao = dao;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public TableRow<AnomalieRTCFXML> call(TableView<AnomalieRTCFXML> param)
    {
        return new AnomalieRTCFXMLTableRow(control, dao);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
