package junit.control;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import control.excel.ControlClarity;

public class ControlClarityTest
{
    private ControlClarity handler;

    @Before
    public void init() throws InvalidFormatException, IOException
    {
        // handler = new ControlSonar();
        handler = new ControlClarity(new File("d:\\Referentiel_Projets.xlsm"));
    }
    
    @Test
    public void tesTrecupInfosClarityExcel()
    {
        handler.recupDonneesDepuisExcel();
    }
}