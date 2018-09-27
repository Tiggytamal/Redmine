package junit.model.utilities;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import junit.JunitBase;
import model.LotSuiviPic;
import model.ModelFactory;
import model.bdd.Anomalie;
import model.bdd.LotRTC;
import model.utilities.AbstractModele;
import utilities.TechnicalException;

/**
 * Classe de test de Modelfactory
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TestModelFactory extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init()
    {
        // Pas d'initialisation particulière       
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Test (expected = TechnicalException.class)
    public void testGetModelException()
    {
        // Test exception lancé si le constructeur n'est pas accessible
        ModelFactory.getModel(ModeletTest.class);
    }
    
    @Test
    public void testGetModel()
    {
        // Création Modele depuis Factory
        LotSuiviPic lot = ModelFactory.getModel(LotSuiviPic.class);
        assertNotNull(lot);
    }
    
    @Test
    public void testGetModelWithParams()
    {
        // Test création objet depuis constructeur avec paramètres
        Anomalie ano = ModelFactory.getModelWithParams(Anomalie.class, ModelFactory.getModel(LotRTC.class));
        assertNotNull(ano);
    }
    
    @Test (expected = TechnicalException.class)
    public void testGetModelWithParamsException()
    {
        // Test exception lancée si le contructeur avec paramètres n'éxiste pas
        ModelFactory.getModelWithParams(ModeletTest.class, new Object());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    /*---------- CLASSES PRIVEES ----------*/
    
    /**
     * Classe de Modele pour tester les constructeurs non accessibles
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private class ModeletTest extends AbstractModele
    {
        private ModeletTest() {}
    }
}
