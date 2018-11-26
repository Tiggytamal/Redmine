package junit.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import dao.DaoLotRTC;
import model.bdd.LotRTC;

public class TestDaoLotRTC extends AbstractTestDao<DaoLotRTC, LotRTC>
{
    /*---------- ATTRIBUTS ----------*/   
    /*---------- CONSTRUCTEURS ----------*/    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    @Override
    public void testReadAll()
    {
        assertNotNull(daoTest.readAll());
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
