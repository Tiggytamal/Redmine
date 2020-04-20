package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.Paging;
import model.rest.sonarapi.Regle;
import model.rest.sonarapi.Regles;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestRegles extends TestAbstractModele<Regles>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testTotal(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getTotal(), i -> objetTest.setTotal(i));
    }

    @Test
    public void testP(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getP(), i -> objetTest.setP(i));
    }

    @Test
    public void testPs(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getPs(), i -> objetTest.setPs(i));
    }

    @Test
    public void testGetPaging(TestInfo testInfo) throws IllegalAccessException
    {
        // Test initialisation
        assertThat(objetTest.getPaging()).isNotNull();

        // Test getter et setter
        Paging paging = new Paging(1, 1, 1);
        objetTest.setPaging(paging);
        assertThat(objetTest.getPaging()).isEqualTo(paging);

        // Protection setter null
        objetTest.setPaging(null);
        assertThat(objetTest.getPaging()).isEqualTo(paging);

        // Protection introspection null
        setField("paging", null);
        assertThat(objetTest.getPaging()).isNotNull();
    }

    @Test
    public void testGetListRegles(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getListRegles(), l -> objetTest.setListRegles(l), new Regle(), "listRegles");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
