package junit.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import model.Anomalie;
import model.LotSuiviPic;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.Modele;
import utilities.TechnicalException;

/**
 * Classe de test de Modelfactory
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TestModelFactory
{
    @Test (expected = TechnicalException.class)
    public void getModelException()
    {
        // Test exception lancé si le constructeur n'est pas accessible
        ModelFactory.getModel(ModeletTest.class);
    }
    
    @Test
    public void getModel()
    {
        // Création Modele depuis Factory
        LotSuiviPic lot = ModelFactory.getModel(LotSuiviPic.class);
        assertNotNull(lot);
    }
    
    @Test
    public void getModelWithParams()
    {
        // Test création objet depuis constructeur avec paramètres
        Anomalie ano = ModelFactory.getModelWithParams(Anomalie.class, ModelFactory.getModel(LotSuiviRTC.class));
        assertNotNull(ano);
    }
    
    @Test (expected = TechnicalException.class)
    public void getModelWithParamsException()
    {
        // Test exception lancée si le contructeur avec paramètres n'éxiste pas
        ModelFactory.getModelWithParams(ModeletTest.class, new Object());
    }
    
    
    /**
     * Classe de Modele pour tester les constructeurs non accessibles
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private class ModeletTest implements Modele
    {
        private ModeletTest() {}
    }
}
