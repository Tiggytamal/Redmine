package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.Composants;
import model.rest.sonarapi.Paging;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestComposants extends TestAbstractModele<Composants>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetPaging(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initilisation
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
    public void testGetListeComposants(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getListeComposants(), l -> objetTest.setListeComposants(l), new ComposantSonar(), "listeComposants");
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
