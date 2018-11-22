package junit.model;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import model.UA;

public class TestUA extends AbstractTestModel<UA>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetCodeUA()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getCodeUA());
        
        // Test setter et getter
        String string = "CodeUA";
        objetTest.setCodeUA(string);
        assertEquals(string, objetTest.getCodeUA());       
    }
    
    @Test
    public void testGetCodeAppli()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getCodeAppli());
        
        // Test setter et getter
        String string = "CodeAppli";
        objetTest.setCodeAppli(string);
        assertEquals(string, objetTest.getCodeAppli());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
