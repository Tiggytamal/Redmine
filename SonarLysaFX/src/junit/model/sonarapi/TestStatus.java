package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.sonarapi.Status;

public class TestStatus
{
    @Test
    public void from()
    {
        assertEquals(Status.OK, Status.from("OK"));
        assertEquals("OK", Status.OK.toString());
        assertEquals(Status.WARN, Status.from("WARN"));
        assertEquals("WARN", Status.WARN.toString());
        assertEquals(Status.ERROR, Status.from("ERROR"));
        assertEquals("ERROR", Status.ERROR.toString());
        assertEquals(Status.NONE, Status.from("NONE"));
        assertEquals("NONE", Status.NONE.toString());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void fromException()
    {
        Status.from("inconnu");
    }
    
    @Test
    public void testSize()
    {
        assertEquals(4, Status.values().length);
    }

    @Test
    public void valeurInstanciation() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class<Object> inner = Whitebox.getInnerClassType(Status.class, "Valeur");
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
