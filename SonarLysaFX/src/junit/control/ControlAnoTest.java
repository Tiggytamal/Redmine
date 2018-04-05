package junit.control;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import control.excel.ControlAno;
import junit.TestUtils;
import model.Anomalie;
import model.enums.Environnement;

public class ControlAnoTest
{
    private ControlAno handler;
    private File file;
    
    @Before
    public void init() throws InvalidFormatException, IOException
    {
        file = new File(getClass().getResource("/resources/Suivi_Quality_Gate.xlsx").getFile());
        handler = new ControlAno(file);
    }
    
    @Test
    public void listAnomaliesSurLotsCrees()
    {
        List<Anomalie> liste = handler.recupDonneesDepuisExcel();
        assertTrue(liste.size() == 80);
    }
    
    @Test
    public void controleKey() throws Exception
    {
        assertTrue(TestUtils.callPrivate("controleKey", handler, Boolean.class, "a", "a"));
        assertTrue(TestUtils.callPrivate("controleKey", handler, Boolean.class, "A", "a"));
        assertFalse(TestUtils.callPrivate("controleKey", handler, Boolean.class, "A", "b"));
        assertTrue(TestUtils.callPrivate("controleKey", handler, Boolean.class, "BEF000", "BEF0009"));
        assertTrue(TestUtils.callPrivate("controleKey", handler, Boolean.class, "T7004360", "T7004360E"));       
    }
    
    @Test
    public void createSheetError()
    {
        // Intialisation
        List<Anomalie> anoAcreer = new ArrayList<>();
        List<Anomalie> anoDejacrees = new ArrayList<>();
        String nomSheet = "E30";
        
        // Test ano déja abandonnée - Retour normalement vide
        Anomalie ano = new Anomalie();
        ano.setLot("Lot 305388");
        ano.setEnvironnement(Environnement.NOUVEAU);
        anoAcreer.add(ano);
        List<Anomalie> liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
        assertTrue(liste.size() == 0);
        
        // Test nouvelle ano - Retour normalement à 1
        ano = new Anomalie();
        ano.setLot("Lot 305128");
        ano.setEnvironnement(Environnement.NOUVEAU);
        anoAcreer.add(ano);
        liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
        assertTrue(liste.size() == 1);
    }
    
    @Test(expected = IOException.class)
    public void sauvegardeFichierException() throws IOException
    {
        handler.sauvegardeFichier(file.getName());      
    }
    
    @Test
    public void sauvegardeFichier() throws Exception
    {
        FileOutputStream stream = new FileOutputStream(file.getAbsolutePath() + file.getName() + "-");
        PowerMockito.whenNew(FileOutputStream.class).withArguments(Mockito.anyString()).thenReturn(stream);
        Sheet sheet = handler.sauvegardeFichier(file.getName());
        assertTrue(sheet.getLastRowNum() == 1);
    }
}