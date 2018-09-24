package junit.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import control.excel.ControlApps;
import control.excel.ExcelFactory;
import dao.DaoApplication;
import junit.JunitBase;
import model.enums.TypeColApps;
import utilities.Statics;

public class TestDaoApplication extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private DaoApplication handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        handler = new DaoApplication();
        
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testReadAll()
    {
        assertNotNull(handler.readAll());
    }
    
    @Test
    @Ignore ("test manuel pour effacer la table")
    public void testResetTable()
    {
        assertEquals(handler.readAll().size(), handler.resetTable());
    }
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        // Appel fichier de test
        File file = new File(getClass().getResource(Statics.ROOT + "liste_applis.xlsx").getFile());
        
        // M�thode et r�cup�ration du nombre de lignes
        int test = handler.recupDonneesDepuisExcel(file);
        
        // Test que le nombre de lignes en registr�es est au moins �gale au nombre de lignes du ficheir excel
        ControlApps control = ExcelFactory.getReader(TypeColApps.class, file);
        assertTrue( control.recupDonneesDepuisExcel().size() <= test);        
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}