package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.enums.TypeBranche;
import model.rest.sonarapi.Branche;
import model.rest.sonarapi.StatutBranche;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestBranche extends TestAbstractModele<Branche>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testIsPrincipale(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isPrincipale(), b -> objetTest.setPrincipale(b));
    }

    @Test
    public void testGetType(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation
        assertThat(objetTest.getType()).isNotNull();
        assertThat(objetTest.getType()).isEqualTo(TypeBranche.LONG);

        // Test getter et setter
        objetTest.setType(TypeBranche.SHORT);
        assertThat(objetTest.getType()).isEqualTo(TypeBranche.SHORT);

        // Protection null
        objetTest.setType(null);
        assertThat(objetTest.getType()).isEqualTo(TypeBranche.SHORT);

        // Protection null avec introspection
        setField("type", null);
        assertThat(objetTest.getType()).isEqualTo(TypeBranche.LONG);

    }

    @Test
    public void testGetStatut(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation null
        assertThat(objetTest.getStatut()).isNull();

        // Test getter et setter
        StatutBranche statut = new StatutBranche();
        objetTest.setStatut(statut);
        assertThat(objetTest.getStatut()).isEqualTo(statut);

        // Protection null
        objetTest.setStatut(null);
        assertThat(objetTest.getStatut()).isEqualTo(statut);
    }

    @Test
    public void testGetDateAnalyse(TestInfo testInfo)
    {
        testSimpleLocalDateTime(testInfo, () -> objetTest.getDateAnalyse(), dt -> objetTest.setDateAnalyse(dt));
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
