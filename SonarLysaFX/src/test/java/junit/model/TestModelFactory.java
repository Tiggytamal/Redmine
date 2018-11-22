package junit.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.Colonne;
import model.ModelFactory;
import model.interfaces.AbstractModele;
import utilities.TechnicalException;

public class TestModelFactory
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test(expected = TechnicalException.class)
    public void testGetModelException()
    {
        // Test avec classe sans constructeru par défaut.
        ModelFactory.build(ModelTest.class);
    }

    @Test
    public void testGetModel()
    {
        // Test avec classe sans constructeru par défaut.
        assertEquals(Colonne.class, ModelFactory.build(Colonne.class).getClass());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    /*---------- CLASSE PRIVEE ----------*/

    private class ModelTest extends AbstractModele
    {
        private ModelTest(String nom)
        {

        }
    }
}
