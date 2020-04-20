package junit.model.parsing;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.parsing.ProjetJSON;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestProjetJSON extends TestAbstractModele<ProjetJSON>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetKey(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(objetTest.getKey()).isNotNull();
        assertThat(objetTest.getKey()).isEmpty();
        String idAnalyse = "idAnalyse";
        objetTest.setKey(idAnalyse);
        assertThat(objetTest.getKey()).isEqualTo(idAnalyse);

        // Protection null et vide avec introspection
        objetTest.setKey(null);
        assertThat(objetTest.getKey()).isEqualTo(idAnalyse);
        objetTest.setKey(EMPTY);
        assertThat(objetTest.getKey()).isEqualTo(idAnalyse);
        setField("key", null);
        assertThat(objetTest.getKey()).isEqualTo(EMPTY);
    }

    @Test
    public void testGetNom(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(objetTest.getNom()).isNotNull();
        assertThat(objetTest.getNom()).isEmpty();
        objetTest.setNom(NOM);
        assertThat(objetTest.getNom()).isEqualTo(NOM);

        // Protection null et vide avec introspection
        objetTest.setNom(null);
        assertThat(objetTest.getNom()).isEqualTo(NOM);
        objetTest.setNom(EMPTY);
        assertThat(objetTest.getNom()).isEqualTo(NOM);
        setField("nom", null);
        assertThat(objetTest.getNom()).isEqualTo(EMPTY);
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
