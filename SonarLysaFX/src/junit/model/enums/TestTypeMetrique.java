package junit.model.enums;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.TypeMetrique;

public class TestTypeMetrique
{
    @Test
    public void from()
    {
        assertEquals(TypeMetrique.LOT, TypeMetrique.from("lot"));
        assertEquals("lot", TypeMetrique.LOT.toString());
        assertEquals(TypeMetrique.QG, TypeMetrique.from("alert_status"));
        assertEquals("alert_status", TypeMetrique.QG.toString());
        assertEquals(TypeMetrique.DUPLICATION, TypeMetrique.from("duplicated_lines_density"));
        assertEquals("duplicated_lines_density", TypeMetrique.DUPLICATION.toString());
        assertEquals(TypeMetrique.BLOQUANT, TypeMetrique.from("new_blocker_violations"));
        assertEquals("new_blocker_violations", TypeMetrique.BLOQUANT.toString());
        assertEquals(TypeMetrique.CRITIQUE, TypeMetrique.from("new_critical_violations"));
        assertEquals("new_critical_violations", TypeMetrique.CRITIQUE.toString());
        assertEquals(TypeMetrique.APPLI, TypeMetrique.from("application"));
        assertEquals("application", TypeMetrique.APPLI.toString());
        assertEquals(TypeMetrique.EDITION, TypeMetrique.from("edition"));
        assertEquals("edition", TypeMetrique.EDITION.toString());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void fromException()
    {
        TypeMetrique.from("inconnu");
    }
    
    @Test
    public void testSize()
    {
        assertEquals(7, TypeMetrique.values().length);
    }

    @Test
    public void valeurInstanciation() throws ClassNotFoundException, InstantiationException, IllegalAccessException
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
}
