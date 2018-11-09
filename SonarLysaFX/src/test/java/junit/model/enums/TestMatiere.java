package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
        assertEquals(Matiere.ANDROID, Matiere.from("ANDROID"));
        assertEquals("ANDROID", Matiere.ANDROID.getValeur());
        assertEquals(Matiere.IOS, Matiere.from("IOS"));
        assertEquals("IOS", Matiere.IOS.getValeur());
        
        // Test lancement exception
        List<String> listeException = new ArrayList<>();
        listeException.add("\0JAVA");
        listeException.add("\0DATASTAGE");
        listeException.add("\0COBOL");
        listeException.add("\0ANDROID");
        listeException.add("\0IOS");

        for (String string : listeException)
        {
            try
            {
                Matiere.from(string);
            }
            catch (IllegalArgumentException e)
            {
                assertEquals("model.enums.Matiere.from - matière envoyée inconnue : " + string, e.getMessage());
                continue;
            }
            fail("Pas d'envoi d'esception avec " + string);
        }
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
        assertEquals(TypeRapport.ANDROID, Matiere.ANDROID.getTypeRapport());
        assertEquals(TypeRapport.IOS, Matiere.IOS.getTypeRapport());
    }
    
    @Test
    @Override
    public void testSize()
    {
        assertEquals(5, Matiere.values().length);
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