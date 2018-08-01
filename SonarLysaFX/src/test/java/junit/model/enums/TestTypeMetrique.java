package junit.model.enums;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.TypeMetrique;

public class TestTypeMetrique implements TestEnums
{
    @Test
    public void testFrom()
    {
        assertEquals(TypeMetrique.LOT, TypeMetrique.from("lot"));
        assertEquals("lot", TypeMetrique.LOT.getValeur());
        assertEquals(TypeMetrique.QG, TypeMetrique.from("alert_status"));
        assertEquals("alert_status", TypeMetrique.QG.getValeur());
        assertEquals(TypeMetrique.DUPLICATION, TypeMetrique.from("duplicated_lines_density"));
        assertEquals("duplicated_lines_density", TypeMetrique.DUPLICATION.getValeur());
        assertEquals(TypeMetrique.BLOQUANT, TypeMetrique.from("new_blocker_violations"));
        assertEquals("new_blocker_violations", TypeMetrique.BLOQUANT.getValeur());
        assertEquals(TypeMetrique.CRITIQUE, TypeMetrique.from("new_critical_violations"));
        assertEquals("new_critical_violations", TypeMetrique.CRITIQUE.getValeur());
        assertEquals(TypeMetrique.APPLI, TypeMetrique.from("application"));
        assertEquals("application", TypeMetrique.APPLI.getValeur());
        assertEquals(TypeMetrique.EDITION, TypeMetrique.from("edition"));
        assertEquals("edition", TypeMetrique.EDITION.getValeur());
        assertEquals(TypeMetrique.BUGS, TypeMetrique.from("bugs"));
        assertEquals("bugs", TypeMetrique.BUGS.getValeur());
        assertEquals(TypeMetrique.VULNERABILITIES, TypeMetrique.from("vulnerabilities"));
        assertEquals("vulnerabilities", TypeMetrique.VULNERABILITIES.getValeur());
        assertEquals(TypeMetrique.LDC, TypeMetrique.from("ncloc"));
        assertEquals("ncloc", TypeMetrique.LDC.getValeur());
        assertEquals(TypeMetrique.SECURITY, TypeMetrique.from("security_rating"));
        assertEquals("security_rating", TypeMetrique.SECURITY.getValeur());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException()
    {
        TypeMetrique.from("inconnu");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException2()
    {
        TypeMetrique.from("\0lot");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException3()
    {
        TypeMetrique.from("\0alert_status");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException4()
    {
        TypeMetrique.from("\0duplicated_lines_density");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException5()
    {
        TypeMetrique.from("\0new_blocker_violations");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException6()
    {
        TypeMetrique.from("\0new_critical_violations");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException7()
    {
        TypeMetrique.from("\0application");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException8()
    {
        TypeMetrique.from("\0edition");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException9()
    {
        TypeMetrique.from("\0bugs");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException10()
    {
        TypeMetrique.from("\0vulnerabilities");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException11()
    {
        TypeMetrique.from("\0ncloc");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFromException12()
    {
        TypeMetrique.from("\0security_rating");
    }
    
    @Test
    @Override
    public void testSize()
    {
        assertEquals(11, TypeMetrique.values().length);
    }

    @Test
    public void testValeurInstanciation() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class<Object> inner = Whitebox.getInnerClassType(TypeMetrique.class, "Valeur");
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
        assertEquals(TypeMetrique.LDC, TypeMetrique.valueOf(TypeMetrique.LDC.toString()));        }
}
