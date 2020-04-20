package junit.fxml.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import dao.Dao;
import dao.DaoFactory;
import fxml.bdd.AnomalieRTCBDD;
import fxml.factory.AnomalieRTCFXMLTableRow;
import junit.AutoDisplayName;
import model.bdd.AnomalieRTC;
import model.enums.ActionA;
import model.fxml.AnomalieRTCFXML;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestAnomalieRTCFXMLTableRow extends TestAbstractTableRow<AnomalieRTCFXMLTableRow, AnomalieRTCFXML, Integer, ActionA, Dao<AnomalieRTC, Integer>, AnomalieRTC, AnomalieRTCBDD>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    @BeforeEach
    public void init() throws Exception
    {
        objetTest = new AnomalieRTCFXMLTableRow(new AnomalieRTCBDD(), DaoFactory.getMySQLDao(AnomalieRTC.class));
        fxml = new AnomalieRTCFXML();        
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testUpdateItem(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());
        
        
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
