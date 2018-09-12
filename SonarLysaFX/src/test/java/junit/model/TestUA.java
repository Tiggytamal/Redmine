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
        assertEquals(EMPTY, handler.getCodeUA());
        
        // Test setter et getter
        String string = "CodeUA";
        handler.setCodeUA(string);
        assertEquals(string, handler.getCodeUA());       
    }
    
    @Test
    public void testGetCodeAppli()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getCodeAppli());
        
        // Test setter et getter
        String string = "CodeAppli";
        handler.setCodeAppli(string);
        assertEquals(string, handler.getCodeAppli());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
