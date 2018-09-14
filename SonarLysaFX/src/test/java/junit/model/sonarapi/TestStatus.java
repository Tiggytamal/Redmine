package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import junit.model.enums.TestEnums;
import model.enums.QG;

public class TestStatus implements TestEnums
{
    @Test
    public void testFrom()
    {
        assertEquals(QG.OK, QG.from("OK"));
        assertEquals("OK", QG.OK.getValeur());
        assertEquals(QG.WARN, QG.from("WARN"));
        assertEquals("WARN", QG.WARN.getValeur());
        assertEquals(QG.ERROR, QG.from("ERROR"));
        assertEquals("ERROR", QG.ERROR.getValeur());
        assertEquals(QG.NONE, QG.from("NONE"));
        assertEquals("NONE", QG.NONE.getValeur());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException()
    {
        QG.from("inconnu");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException2()
    {
        QG.from("\0OK");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException3()
    {
        QG.from("\0WARN");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException4()
    {
        QG.from("\0ERROR");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException5()
    {
        QG.from("\0NONE");
    }
    
    @Test
    @Override
    public void testSize()
    {
        assertEquals(4, QG.values().length);
    }

    @Test
    public void testValeurInstanciation() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class<Object> inner = Whitebox.getInnerClassType(QG.class, "Valeur");
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
        assertEquals(QG.WARN, QG.valueOf(QG.WARN.toString()));    
    }
}
