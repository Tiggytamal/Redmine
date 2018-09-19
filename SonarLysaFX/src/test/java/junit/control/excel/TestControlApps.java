package junit.control.excel;

import java.util.Map;

import org.junit.Test;

import control.excel.ControlApps;
import control.xml.ControlXML;
import model.Application;
import model.SwitchXML;
import model.enums.Switch;
import model.enums.TypeColApps;

public class TestControlApps extends TestControlExcelRead<TypeColApps, ControlApps, Map<String, Application>>
{
    /*---------- ATTRIBUTS ----------*/
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public TestControlApps()
    { 
        super(TypeColApps.class, "liste_applis.xlsx");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne est utilisée.
        testCalculIndiceColonnes(1);
        
        SwitchXML switchXML = new SwitchXML();
        switchXML.setSwitch(Switch.BDD);
        new ControlXML().saveParam(switchXML);
    }
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        testRecupDonneesDepuisExcel(map -> map.size() == 1897);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}