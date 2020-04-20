package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.ModelFactory;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.Issue;
import model.rest.sonarapi.Issues;
import model.rest.sonarapi.Paging;
import model.rest.sonarapi.User;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestIssues extends TestAbstractModele<Issues>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetTotal(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getTotal(), i -> objetTest.setTotal(i));
    }

    @Test
    public void testGetP(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getP(), i -> objetTest.setP(i));
    }

    @Test
    public void testGetPs(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getPs(), i -> objetTest.setPs(i));
    }

    @Test
    public void testGetPaging(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation
        assertThat(objetTest.getPaging()).isNotNull();

        // Test getter et setter
        Paging paging = new Paging(1, 1, 1);
        objetTest.setPaging(paging);
        assertThat(objetTest.getPaging()).isEqualTo(paging);

        // Protection setter null
        objetTest.setPaging(null);
        assertThat(objetTest.getPaging()).isEqualTo(paging);
    }

    @Test
    public void testGetComposants(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getComposants(), l -> objetTest.setComposants(l), ModelFactory.build(ComposantSonar.class), "composants");
    }

    @Test
    public void testGetListIssues(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getListIssues(), l -> objetTest.setListIssues(l), new Issue(), "listIssues");
    }

    @Test
    public void testGetUtilisateurs(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getUtilisateurs(), l -> objetTest.setUtilisateurs(l), new User(), "utilisateurs");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
