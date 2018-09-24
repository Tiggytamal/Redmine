package junit.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
//    @Ignore ("test manuel pour effacer la table")
    public void testResetTable()
    {
        int size = handler.resetTable();
        assertEquals(handler.readAll().size(), size);
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}