package junit.model.enums;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.Matiere;
import model.enums.TypeMail;

public class TestMatiere
{
    @Test
    public void testFrom()
    {
        assertEquals(Matiere.JAVA, Matiere.from("JAVA"));
        assertEquals("JAVA", Matiere.JAVA.toString());
        assertEquals(Matiere.DATASTAGE, Matiere.from("DATASTAGE"));
        assertEquals("DATASTAGE", Matiere.DATASTAGE.toString());
        assertEquals(Matiere.COBOL, Matiere.from("COBOL"));
        assertEquals("COBOL", Matiere.COBOL.toString());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException()
    {
        Matiere.from("inconnu");
    }
    
    public void testGetTypeMail()
    {
        assertEquals(TypeMail.SUIVIJAVA, Matiere.JAVA.getTypeMail());
        assertEquals(TypeMail.SUIVIDATASTAGE, Matiere.DATASTAGE.getTypeMail());
        assertEquals(TypeMail.SUIVICOBOL, Matiere.COBOL.getTypeMail());
    }
    
    @Test
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