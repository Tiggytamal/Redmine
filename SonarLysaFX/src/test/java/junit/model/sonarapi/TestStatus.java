package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import junit.model.enums.TestEnums;
import model.sonarapi.Status;

public class TestStatus implements TestEnums
{
    @Test
    public void testFrom()
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
    public void testFromException()
    {
        Status.from("inconnu");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException2()
    {
        Status.from("\0OK");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException3()
    {
        Status.from("\0WARN");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException4()
    {
        Status.from("\0ERROR");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException5()
    {
        Status.from("\0NONE");
    }
    
    @Test
    @Override
    public void testSize()
    {
        assertEquals(4, Status.values().length);
    }

    @Test
    public void testValeurInstanciation() throws ClassNotFoundException, InstantiationException, IllegalAccessException
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

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(Status.WARN, Status.valueOf(Status.WARN.toString()));    
    }
}
