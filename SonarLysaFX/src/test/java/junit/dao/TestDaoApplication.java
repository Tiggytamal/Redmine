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
import model.bdd.Application;
import model.enums.TypeColApps;
import utilities.Statics;

public class TestDaoApplication extends AbstractTestDao<DaoApplication, Application>
{
    /*---------- ATTRIBUTS ----------*/    
    /*---------- CONSTRUCTEURS ----------*/    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    @Override
    public void testReadAll()
    {
        assertNotNull(handler.readAll());
    }
    
    @Test
    @Ignore ("test manuel pour effacer la table")
    @Override
    public void testResetTable()
    {
        assertEquals(handler.readAll().size(), handler.resetTable());
    }
    
    @Test
    @Override
    public void testRecupDonneesDepuisExcel()
    {
        // Appel fichier de test
        File file = new File(getClass().getResource(Statics.ROOT + "liste_applis.xlsx").getFile());
        
        // Méthode et récupération du nombre de lignes
        int test = handler.recupDonneesDepuisExcel(file);
        
        // Test que le nombre de lignes en registrées est au moins égale au nombre de lignes du ficheir excel
        ControlApps control = ExcelFactory.getReader(TypeColApps.class, file);
        assertTrue( control.recupDonneesDepuisExcel().size() <= test);        
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
