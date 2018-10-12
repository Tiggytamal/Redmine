package junit.control.excel;

import java.util.List;

import org.junit.Test;

import control.excel.ControlApps;
import control.xml.ControlXML;
import model.SwitchXML;
import model.bdd.Application;
import model.enums.Switch;
import model.enums.TypeColApps;

public class TestControlApps extends TestControlExcelRead<TypeColApps, ControlApps, List<Application>>
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
        testRecupDonneesDepuisExcel(liste -> liste.size() == 1897);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}