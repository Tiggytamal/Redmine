package junit.dao;

import static com.google.common.truth.Truth.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import dao.DaoComposantBase;
import junit.AutoDisplayName;
import model.bdd.ComposantBase;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestDaoComposantBase extends TestAbstractDao<DaoComposantBase, ComposantBase, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testReadAll(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.readAll()).isNotNull();
    }

    @Test
    @Disabled(TESTMANUEL)
    public void testResetTable(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        int size = objetTest.resetTable();
        assertThat(size).isEqualTo(objetTest.readAll().size());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Test
    @Override
    public void testRecupDonneesDepuisExcel(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(objetTest.recupDonneesDepuisExcel(null)).isEqualTo(0);
    }

    @Test
    public void testRecupLotsAvecComposants(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.recupLotsAvecComposants();
    }

    @Test
    public void testReadAllMap(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        Map<String, ComposantBase> map = objetTest.readAllMap();
        ComposantBase compo = map.get("fr.ca.cat.apimanager.apis:APIM_AgregationLinxo_Buildmaster");
        assertThat(compo).isNotNull();
    }
}
