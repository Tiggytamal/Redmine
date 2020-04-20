package junit.model.fxml;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import junit.AutoDisplayName;
import junit.JunitBase;
import model.fxml.FiltreurFXML;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestFiltreurFXML extends JunitBase<FiltreurFXML<String>>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String VALEUR = "Valeur";

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        objetTest = new FiltreurFXML<String>(VALEUR, "methode", (t, u) -> Statics.EMPTY, null);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testTest(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test ou la fonction renvoie vide.
        assertThat(objetTest.test(VALEUR)).isFalse();

        // Test ou la fonction renvoie VALEUR
        objetTest = new FiltreurFXML<String>(VALEUR, "methode", (t, u) -> VALEUR, null);
        assertThat(objetTest.test(VALEUR)).isTrue();

        // Test avec filtreur precedent OK
        FiltreurFXML<String> objetTPrec = new FiltreurFXML<String>(VALEUR, "methode", (t, u) -> VALEUR, null);
        objetTest = new FiltreurFXML<String>(VALEUR, "methode", (t, u) -> VALEUR, objetTPrec);
        assertThat(objetTest.test(VALEUR)).isTrue();

        // Test avec filtreur precedent KO
        objetTPrec = new FiltreurFXML<String>(VALEUR, "methode", (t, u) -> Statics.EMPTY, null);
        objetTest = new FiltreurFXML<String>(VALEUR, "methode", (t, u) -> VALEUR, objetTPrec);
        assertThat(objetTest.test(VALEUR)).isFalse();

        // Test avec filtreur precedent present mais objetTest KO
        objetTPrec = new FiltreurFXML<String>(VALEUR, "methode", (t, u) -> VALEUR, null);
        objetTest = new FiltreurFXML<String>(VALEUR, "methode", (t, u) -> Statics.EMPTY, objetTPrec);
        assertThat(objetTest.test(VALEUR)).isFalse();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
