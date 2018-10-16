package junit.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import dao.DaoComposantSonar;
import model.bdd.ComposantSonar;

public class TestDaoComposantSonar extends AbstractTestDao<DaoComposantSonar, ComposantSonar>
{
    /*---------- ATTRIBUTS ----------*/    
    /*---------- CONSTRUCTEURS ----------*/    
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
        int size = handler.resetTable();
        assertEquals(handler.readAll().size(), size);
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Test
    @Ignore ("pas d'implemntaiton dans cette classe de la méthode testée")
    @Override
    public void testRecupDonneesDepuisExcel()
    {

    }
}
