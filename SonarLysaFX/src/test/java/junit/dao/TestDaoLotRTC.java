package junit.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import dao.DaoFactory;
import dao.DaoLotRTC;
import model.bdd.LotRTC;

public class TestDaoLotRTC extends AbstractTestDao<DaoLotRTC, LotRTC>
{
    /*---------- ATTRIBUTS ----------*/
    
    private DaoLotRTC handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        handler = DaoFactory.getDao(LotRTC.class);        
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    @Override
    public void testReadAll()
    {
        assertNotNull(handler.readAll());
    }

    @Test
    @Override
    public void testResetTable()
    {
        
    }

    @Test
    @Override
    public void testRecupDonneesDepuisExcel()
    {
        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
