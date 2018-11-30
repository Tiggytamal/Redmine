package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.EMPTY;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.TypeAction;

/**
 * Classe de test de l'énumération TypeAction
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
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
        assertEquals(9, TypeAction.values().length);
    }
    
    @Test
    public void testFrom()
    {
        assertEquals(TypeAction.CREER, TypeAction.from("Créer"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0ACréer"));
        assertEquals("Créer", TypeAction.CREER.getValeur());
        assertEquals(TypeAction.AJOUTCOMM, TypeAction.from("Ajouter com."));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0Ajouter com."));
        assertEquals("Ajouter com.", TypeAction.AJOUTCOMM.getValeur());
        assertEquals(TypeAction.VERIFIER, TypeAction.from("Vérifier"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0Vérifier"));
        assertEquals("Vérifier", TypeAction.VERIFIER.getValeur());
        assertEquals(TypeAction.ASSEMBLER, TypeAction.from("Assembler"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0Assembler"));
        assertEquals("Assembler", TypeAction.ASSEMBLER.getValeur());
        assertEquals(TypeAction.CLOTURER, TypeAction.from("Clôturer"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0Clôturer"));
        assertEquals("Clôturer", TypeAction.CLOTURER.getValeur());
        assertEquals(TypeAction.ABANDONNER, TypeAction.from("Abandonner"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0Abandonner"));
        assertEquals("Abandonner", TypeAction.ABANDONNER.getValeur());
        assertEquals(TypeAction.RELANCER, TypeAction.from("Relancer"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0Relancer"));
        assertEquals("Relancer", TypeAction.RELANCER.getValeur());
        assertEquals(TypeAction.REOUV, TypeAction.from("Réouvrir"));
        assertEquals(TypeAction.VIDE, TypeAction.from("\0Réouvrir"));
        assertEquals("Réouvrir", TypeAction.REOUV.getValeur());
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
