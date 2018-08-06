package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import model.enums.TypeMajSuivi;

public class TestTypeMajSuivi implements TestEnums
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeMajSuivi.COBOL, TypeMajSuivi.valueOf(TypeMajSuivi.COBOL.toString()));    
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(4, TypeMajSuivi.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertFalse("La valeur de l'énumération est vide : " + TypeMajSuivi.COBOL, TypeMajSuivi.COBOL.getValeur().isEmpty());       
        assertFalse("La valeur de l'énumération est vide : " + TypeMajSuivi.DATASTAGE, TypeMajSuivi.DATASTAGE.getValeur().isEmpty());  
        assertFalse("La valeur de l'énumération est vide : " + TypeMajSuivi.JAVA, TypeMajSuivi.JAVA.getValeur().isEmpty());  
        assertFalse("La valeur de l'énumération est vide : " + TypeMajSuivi.MULTI, TypeMajSuivi.MULTI.getValeur().isEmpty());  
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
