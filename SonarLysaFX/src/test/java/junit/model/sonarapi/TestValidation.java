package junit.model.sonarapi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Validation;

public class TestValidation
{
    /*---------- ATTRIBUTS ----------*/

    private Validation modele;
    private Validation modeleNull;
    private static final boolean VALID = true;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Validation(VALID);
        modeleNull = new Validation();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
       
    @Test
    public void isValid()
    {
        assertTrue(modele.isValid());
        assertFalse(modeleNull.isValid());
    }
    
    @Test
    public void setValid()
    {
        modele.setValid(false);
        assertFalse(modele.isValid());
        modeleNull.setValid(true);
        assertTrue(modeleNull.isValid());
    }
    
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
