package junit.control;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import control.ControlEdition;

public class ControlEditionTest
{
 private ControlEdition handler;
    
    @Before
    public void init() throws InvalidFormatException, IOException
    {
        handler = new ControlEdition(new File("d:\\Codification des Editions.xls"));
    }
    
    @Test
    public void recupVersionDepuisExcel()
    {
        List<String> liste = new ArrayList<>();
        liste.add("2018");
        handler.setAnnees(liste);
        Map<String, Map<String, String>> map =  handler.recupDonneesDepuisExcel();
        Map<String, String> chc = map.get("CHC");
        Map<String, String> cdm = map.get("CDM");
        assertTrue(!map.isEmpty());
        assertTrue(!chc.isEmpty());
        assertTrue(!cdm.isEmpty());
        
        for (Map.Entry<String, String> entry : chc.entrySet())
        {
            assertTrue(entry.getKey().matches("^([0-9]{2}\\.){3}[0-9]{2}$"));
            assertTrue(entry.getValue().matches("^CHC20[12][0-9]\\-S[0-5][0-9]$"));            
        }
        
        for (Map.Entry<String, String> entry : cdm.entrySet())
        {
            assertTrue(entry.getKey().matches("^([0-9]{2}\\.){3}[0-9]{2}$"));
            assertTrue(entry.getValue().matches("^CHC_CDM20[12][0-9]\\-S[0-5][0-9]$"));            
        }
    }
}