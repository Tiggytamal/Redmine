package junit.model.utilities;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import junit.JunitBase;
import model.LotSuiviPic;
import model.ModelFactory;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.interfaces.AbstractModele;
import utilities.TechnicalException;

/**
 * Classe de test de Modelfactory
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public class TestModelFactory extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init()
    {
        // Pas d'initialisation particuli�re
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test(expected = TechnicalException.class)
    public void testGetModelException()
    {
        // Test exception lanc� si le constructeur n'est pas accessible
        ModelFactory.build(ModeletTest.class);
    }

    @Test
    public void testGetModel()
    {
        // Cr�ation Modele depuis Factory
        LotSuiviPic lot = ModelFactory.build(LotSuiviPic.class);
        assertNotNull(lot);
    }

    @Test
    public void testGetModelWithParams()
    {
        // Test cr�ation objet depuis constructeur avec param�tres
        DefautQualite ano = DefautQualite.build(ModelFactory.build(LotRTC.class));
        assertNotNull(ano);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    /*---------- CLASSES PRIVEES ----------*/

    /**
     * Classe de Modele pour tester les constructeurs non accessibles
     * 
     * @author ETP8137 - Gr�goire Mathon
     * @since 1.0
     */
    private class ModeletTest extends AbstractModele
    {
        private ModeletTest()
        {
        }
    }
}
