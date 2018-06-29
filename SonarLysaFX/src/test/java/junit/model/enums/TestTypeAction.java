package junit.model.enums;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.TypeAction;

public class TestTypeAction
{   
    @Test
    public void testSize()
    {
        assertEquals(7, TypeAction.values().length);
    }
    
    @Test
    public void testFrom()
    {
        assertEquals(TypeAction.CREER, TypeAction.from("A cr�er"));
        assertEquals("A cr�er", TypeAction.CREER.toString());
        assertEquals(TypeAction.VERIFIER, TypeAction.from("A v�rifier"));
        assertEquals("A v�rifier", TypeAction.VERIFIER.toString());
        assertEquals(TypeAction.ASSEMBLER, TypeAction.from("A assembler"));
        assertEquals("A assembler", TypeAction.ASSEMBLER.toString());
        assertEquals(TypeAction.CLOTURER, TypeAction.from("A cl�turer"));
        assertEquals("A cl�turer", TypeAction.CLOTURER.toString());
        assertEquals(TypeAction.ABANDONNER, TypeAction.from("A abandonner"));
        assertEquals("A abandonner", TypeAction.ABANDONNER.toString());
        assertEquals(TypeAction.VIDE, TypeAction.from("inconnu"));
        assertEquals("", TypeAction.VIDE.toString());
        assertEquals(TypeAction.VERIFIER, TypeAction.from("A v�rifier"));
        assertEquals("A v�rifier", TypeAction.VERIFIER.toString());
    }

    @Test
    public void testValeurInstanciation() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class<Object> inner = Whitebox.getInnerClassType(TypeAction.class, "Valeur");
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
