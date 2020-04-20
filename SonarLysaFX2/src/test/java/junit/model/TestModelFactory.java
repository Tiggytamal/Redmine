package junit.model;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.JunitBase;
import model.AbstractModele;
import model.LotSuiviPic;
import model.ModelFactory;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestModelFactory extends JunitBase<ModelFactory>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init()
    {
        // Pas d'initialisation particuliere
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testBuild(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Création Modele depuis Factory
        LotSuiviPic lot = ModelFactory.build(LotSuiviPic.class);
        assertThat(lot).isNotNull();

        // Test exception lance si le constructeur n'est pas accéssible
        assertThrows(TechnicalException.class, () -> ModelFactory.build(ModeletTest.class));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    /*---------- CLASSES PRIVEES ----------*/

    /**
     * Classe de Modele pour tester les constructeurs non accéssibles
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private class ModeletTest extends AbstractModele
    {
        private ModeletTest()
        {}
    }
}
