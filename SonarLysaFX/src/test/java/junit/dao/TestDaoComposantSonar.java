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
        assertNotNull(daoTest.readAll());
    }
    
    @Test
    @Ignore ("test manuel pour effacer la table")
    public void testResetTable()
    {
        int size = daoTest.resetTable();
        assertEquals(daoTest.readAll().size(), size);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Test
    @Override
    public void testRecupDonneesDepuisExcel()
    {
        assertEquals(0, daoTest.recupDonneesDepuisExcel(null));
    }
    
    @Test
    public void testRecupLotsAvecComposants()
    {
        daoTest.recupLotsAvecComposants();
    }
}
