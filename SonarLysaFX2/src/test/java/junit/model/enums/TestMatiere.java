package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import junit.AutoDisplayName;
import model.enums.Matiere;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestMatiere extends TestAbstractEnum<Matiere>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(Matiere.valueOf(Matiere.JAVA.toString())).isEqualTo(Matiere.JAVA);
    }

    @Test
    public void testFrom(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(Matiere.from("JAVA")).isEqualTo(Matiere.JAVA);
        assertThat(Matiere.from("DATASTAGE")).isEqualTo(Matiere.DATASTAGE);
        assertThat(Matiere.from("COBOL")).isEqualTo(Matiere.COBOL);
        assertThat(Matiere.from("ANDROID")).isEqualTo(Matiere.ANDROID);
        assertThat(Matiere.from("IOS")).isEqualTo(Matiere.IOS);
        assertThat(Matiere.from("ANGULAR")).isEqualTo(Matiere.ANGULAR);
    }

    @ParameterizedTest(name = "from Exception : {index} - {0}")
    @ValueSource(strings = { "\0JAVA", "\0DATASTAGE", "\0COBOL", "\0ANDROID", "\0IOS", "\0ANGULAR", "autre" })
    void testFrom_Exception(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Matiere.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.Matiere.from - valeur non gérée : " + valeur);
    }

    @ParameterizedTest(name = "from Exception vide et null : {index} - {0}")
    @NullAndEmptySource
    void testFrom_Exception_vide_et_null(String valeur, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Matiere.from(valeur));
        assertThat(e.getMessage()).isEqualTo("model.enums.Matiere.from - valeur envoyée nulle ou vide.");
    }
    
    @Test
    public void testTestMatiereCompoMC_ANGULAR()
    {
        // Test composant Angular
        assertThat(Matiere.testMatiereCompoMC("Compo Angular")).isEqualTo(Matiere.ANGULAR);
        assertThat(Matiere.testMatiereCompoMC("Angular-Compo")).isEqualTo(Matiere.ANGULAR);
        
        // Test composant non Angular
        assertThat(Matiere.testMatiereCompoMC("Compo Ios")).isNotEqualTo(Matiere.ANGULAR);
        assertThat(Matiere.testMatiereCompoMC("Compo Android")).isNotEqualTo(Matiere.ANGULAR);
    }
    
    @Test
    public void testTestMatiereCompoMC_IOS()
    {
        // Test composant Angular
        assertThat(Matiere.testMatiereCompoMC("Compo iOS")).isEqualTo(Matiere.IOS);
        assertThat(Matiere.testMatiereCompoMC("iOS-Compo")).isEqualTo(Matiere.IOS);
        
        // Test composant non Angular
        assertThat(Matiere.testMatiereCompoMC("Compo Angular")).isNotEqualTo(Matiere.IOS);
        assertThat(Matiere.testMatiereCompoMC("Compo Android")).isNotEqualTo(Matiere.IOS);
    }
    
    @Test
    public void testTestMatiereCompoMC_ANDROID()
    {
        // Test composant Angular
        assertThat(Matiere.testMatiereCompoMC("Compo Android")).isEqualTo(Matiere.ANDROID);
        assertThat(Matiere.testMatiereCompoMC("Android-Compo")).isEqualTo(Matiere.ANDROID);
        
        // Test composant non Angular
        assertThat(Matiere.testMatiereCompoMC("Compo Ios")).isNotEqualTo(Matiere.ANDROID);
        assertThat(Matiere.testMatiereCompoMC("Compo Angular")).isNotEqualTo(Matiere.ANDROID);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        assertThat(Matiere.JAVA.getValeur()).isEqualTo("JAVA");
        assertThat(Matiere.DATASTAGE.getValeur()).isEqualTo("DATASTAGE");
        assertThat(Matiere.COBOL.getValeur()).isEqualTo("COBOL");
        assertThat(Matiere.ANDROID.getValeur()).isEqualTo("ANDROID");
        assertThat(Matiere.IOS.getValeur()).isEqualTo("IOS");
        assertThat(Matiere.ANGULAR.getValeur()).isEqualTo("ANGULAR");
    }

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(Matiere.values().length).isEqualTo(7);
    }

    @Test
    @DisplayName("protection instanciation classe interne Valeur")
    public void testValeurInstanciation(TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        testValeurInstanciation(Matiere.class, testInfo);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
