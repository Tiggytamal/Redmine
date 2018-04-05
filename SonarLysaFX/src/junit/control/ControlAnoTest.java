package junit.control;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import control.ControlAno;
import junit.TestUtils;
import model.Anomalie;

public class ControlAnoTest
{
    private ControlAno handler;
    
    @Before
    public void init() throws InvalidFormatException, IOException
    {
        handler = new ControlAno(new File(getClass().getResource("/resources/Suivi_Quality_Gate.xlsx").getFile()));
    }
    
    @Test
    public void listAnomaliesSurLotsCrees()
    {
        List<Anomalie> liste = handler.recupDonneesDepuisExcel();
        Assert.assertTrue(liste.size() == 80);
    }
    
    @Test
    public void controleKey() throws Exception
    {
        Assert.assertTrue(TestUtils.callPrivate("controleKey", handler, Boolean.class, "a", "a"));
        Assert.assertTrue(TestUtils.callPrivate("controleKey", handler, Boolean.class, "A", "a"));
        Assert.assertFalse(TestUtils.callPrivate("controleKey", handler, Boolean.class, "A", "b"));
        Assert.assertTrue(TestUtils.callPrivate("controleKey", handler, Boolean.class, "BEF000", "BEF0009"));
        Assert.assertTrue(TestUtils.callPrivate("controleKey", handler, Boolean.class, "T7004360", "T7004360E"));       
    }
    
    @Test
    public void sauvegardeFichier()
    {
        
    }
    
    
    public void creerAnodepuisExcel()
    {

        
    }
}