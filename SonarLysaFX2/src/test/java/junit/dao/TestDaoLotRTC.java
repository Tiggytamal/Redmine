package junit.dao;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import dao.DaoLotRTC;
import junit.AutoDisplayName;
import model.bdd.LotRTC;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestDaoLotRTC extends TestAbstractDao<DaoLotRTC, LotRTC, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testReadAll(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.readAll()).isNotNull();
    }

    @Test
    @Override
    public void testResetTable(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testRecupDonneesDepuisExcel(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // TODO Auto-generated method stub

    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
