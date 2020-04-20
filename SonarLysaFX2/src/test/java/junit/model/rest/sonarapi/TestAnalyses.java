package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.Analyse;
import model.rest.sonarapi.Analyses;
import model.rest.sonarapi.Paging;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestAnalyses extends TestAbstractModele<Analyses>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetPaging(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initilaisation null
        assertThat(objetTest.getPaging()).isNull();

        Paging paging = new Paging(1, 1, 1);
        objetTest.setPaging(paging);
        assertThat(objetTest.getPaging()).isEqualTo(paging);

        // Protection null
        objetTest.setPaging(null);
        assertThat(objetTest.getPaging()).isEqualTo(paging);
    }

    @Test
    public void testGetListAnalyses(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getListAnalyses(), l -> objetTest.setListAnalyses(l), new Analyse(), "listAnalyses");
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
