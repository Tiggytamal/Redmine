package junit.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import dao.DaoComposantSonar;
import junit.JunitBase;

public class TestDaoComposantSonar extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private DaoComposantSonar handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        handler = new DaoComposantSonar();
        
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testReadAll()
    {
        assertNotNull(handler.readAll());
    }
    
    @Test
    @Ignore ("test manuel pour effacer la table")
    public void testRestTable()
    {
        assertEquals(handler.readAll().size(), handler.resetTable());
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
