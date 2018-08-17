package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.EMPTY;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.TypeAction;

public class TestTypeAction implements TestEnums
{   
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeAction.CREER, TypeAction.valueOf(TypeAction.CREER.toString()));          
    }
    
    @Test
    @Override
    public void testSize()
    {
        assertEquals(7, TypeAction.values().length);
    }
    
    @Test
    public void testFrom()
    {
        assertEquals(TypeAction.CREER, TypeAction.from("A cr�er"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0A cr�er"));
        assertEquals("A cr�er", TypeAction.CREER.getValeur());
        assertEquals(TypeAction.VERIFIER, TypeAction.from("A v�rifier"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0A v�rifier"));
        assertEquals("A v�rifier", TypeAction.VERIFIER.getValeur());
        assertEquals(TypeAction.ASSEMBLER, TypeAction.from("A assembler"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0A assembler"));
        assertEquals("A assembler", TypeAction.ASSEMBLER.getValeur());
        assertEquals(TypeAction.CLOTURER, TypeAction.from("A cl�turer"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0A cl�turer"));
        assertEquals("A cl�turer", TypeAction.CLOTURER.getValeur());
        assertEquals(TypeAction.ABANDONNER, TypeAction.from("A abandonner"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0A abandonner"));
        assertEquals("A abandonner", TypeAction.ABANDONNER.getValeur());
        assertEquals(TypeAction.RELANCER, TypeAction.from("A relancer"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0A relancer"));
        assertEquals("A relancer", TypeAction.RELANCER.getValeur());
        assertEquals(TypeAction.VIDE, TypeAction.from("inconnu"));
        assertEquals(EMPTY, TypeAction.VIDE.getValeur());

        assertEquals(TypeAction.VIDE, TypeAction.from(EMPTY));
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
