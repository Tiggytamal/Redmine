package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import model.enums.TypeMajSuivi;

/**
 * Classe de test de l'énumération TypeMajSuivi
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
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
        assertEquals(7, TypeMajSuivi.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertFalse("La valeur de l'énumération est vide : " + TypeMajSuivi.COBOL, TypeMajSuivi.COBOL.getValeur().isEmpty());       
        assertFalse("La valeur de l'énumération est vide : " + TypeMajSuivi.DATASTAGE, TypeMajSuivi.DATASTAGE.getValeur().isEmpty());  
        assertFalse("La valeur de l'énumération est vide : " + TypeMajSuivi.JAVA, TypeMajSuivi.JAVA.getValeur().isEmpty());  
        assertFalse("La valeur de l'énumération est vide : " + TypeMajSuivi.MULTI, TypeMajSuivi.MULTI.getValeur().isEmpty());  
        assertFalse("La valeur de l'énumération est vide : " + TypeMajSuivi.IOS, TypeMajSuivi.IOS.getValeur().isEmpty());
        assertFalse("La valeur de l'énumération est vide : " + TypeMajSuivi.ANDROID, TypeMajSuivi.ANDROID.getValeur().isEmpty());
        assertFalse("La valeur de l'énumération est vide : " + TypeMajSuivi.NUIT, TypeMajSuivi.NUIT.getValeur().isEmpty());
    }
    
    @Test
    public void testGetNbreEtapes()
    {
        assertNotEquals(0, TypeMajSuivi.COBOL.getNbreEtapes());
        assertNotEquals(0, TypeMajSuivi.DATASTAGE.getNbreEtapes());
        assertNotEquals(0, TypeMajSuivi.JAVA.getNbreEtapes());
        assertNotEquals(0, TypeMajSuivi.MULTI.getNbreEtapes());
        assertNotEquals(0, TypeMajSuivi.IOS.getNbreEtapes());
        assertNotEquals(0, TypeMajSuivi.ANDROID.getNbreEtapes());
        assertNotEquals(0, TypeMajSuivi.NUIT.getNbreEtapes());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
