package junit.model.enums;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.Matiere;
import model.enums.TypeRapport;

/**
 * Classe de test de l'énumération Matiere
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestMatiere implements TestEnums
{
    
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(Matiere.JAVA, Matiere.valueOf(Matiere.JAVA.toString()));        
        
    }
    
    @Test
    public void testFrom()
    {
        assertEquals(Matiere.JAVA, Matiere.from("JAVA"));
        assertEquals("JAVA", Matiere.JAVA.getValeur());
        assertEquals(Matiere.DATASTAGE, Matiere.from("DATASTAGE"));
        assertEquals("DATASTAGE", Matiere.DATASTAGE.getValeur());
        assertEquals(Matiere.COBOL, Matiere.from("COBOL"));
        assertEquals("COBOL", Matiere.COBOL.getValeur());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException()
    {
        Matiere.from("inconnu");
    }
    
    @Test
    public void testGetTypeRapport()
    {
        assertEquals(TypeRapport.SUIVIJAVA, Matiere.JAVA.getTypeRapport());
        assertEquals(TypeRapport.SUIVIDATASTAGE, Matiere.DATASTAGE.getTypeRapport());
        assertEquals(TypeRapport.SUIVICOBOL, Matiere.COBOL.getTypeRapport());
    }
    
    @Test
    @Override
    public void testSize()
    {
        assertEquals(3, Matiere.values().length);
    }

    @Test
    public void testValeurInstanciation() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class<Object> inner = Whitebox.getInnerClassType(Matiere.class, "Valeur");
        Constructor<Object> constructor = Whitebox.getConstructor(inner);
        constructor.setAccessible(true);
        try
        {
            constructor.newInstance();
        } catch (InvocationTargetException e)
        {
            assertEquals(AssertionError.class, e.getCause().getClass());
        }
    }
}