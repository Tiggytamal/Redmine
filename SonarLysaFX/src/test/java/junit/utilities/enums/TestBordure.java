package junit.utilities.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import junit.model.enums.TestEnums;
import utilities.enums.Bordure;

/**
 * Classe de test de l'�num�ration Bordure
 * 
 * @author ETP8137 - Gr�goire mathon
 * @since 1.0
 */
public class TestBordure implements TestEnums
{

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(Bordure.BAS, Bordure.valueOf(Bordure.BAS.toString()));        
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(9, Bordure.values().length);    
    }

}
