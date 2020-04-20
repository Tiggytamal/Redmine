package junit.model.parsing;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.enums.TypeBranche;
import model.parsing.BrancheJSON;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestBrancheJSON extends TestAbstractModele<BrancheJSON>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetType(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test getter et setter
        assertThat(objetTest.getType()).isNotNull();
        assertThat(objetTest.getType()).isEqualTo(TypeBranche.LONG);
        objetTest.setType(TypeBranche.SHORT);
        assertThat(objetTest.getType()).isEqualTo(TypeBranche.SHORT);

        // Protection null
        objetTest.setType(null);
        assertThat(objetTest.getType()).isEqualTo(TypeBranche.SHORT);

        // Protection null même par introspection
        Whitebox.getField(objetTest.getClass(), "type").set(objetTest, null);
        assertThat(objetTest.getType()).isNotNull();
        assertThat(objetTest.getType()).isEqualTo(TypeBranche.LONG);
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

        // Protection null et vide
        objetTest.setNom(null);
        assertThat(objetTest.getNom()).isEqualTo(NOM);
        objetTest.setNom(Statics.EMPTY);
        assertThat(objetTest.getNom()).isEqualTo(NOM);

        // Protection null même par introspection
        Whitebox.getField(objetTest.getClass(), "nom").set(objetTest, null);
        assertThat(objetTest.getNom()).isNotNull();
        assertThat(objetTest.getNom()).isEqualTo(Statics.EMPTY);
    }

    @Test
    public void testIsPrincipal(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isPrincipal(), b -> objetTest.setPrincipal(b));
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
