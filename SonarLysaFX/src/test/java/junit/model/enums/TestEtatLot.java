package junit.model.enums;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.EtatLot;
import utilities.Statics;

public class TestEtatLot implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(10, EtatLot.values().length);
    }
    @Test
    public void testFrom()
    {
        String inconnu = "INCONNU";
        assertEquals(EtatLot.ABANDONNE, EtatLot.from("Abandonné"));
        assertEquals(EtatLot.INCONNU, EtatLot.from("\0Abandonné"));
        assertEquals("Abandonné", EtatLot.ABANDONNE.getValeur());
        assertEquals(EtatLot.NOUVEAU, EtatLot.from("Nouveau"));
        assertEquals(EtatLot.INCONNU, EtatLot.from("\0Nouveau"));
        assertEquals("Nouveau", EtatLot.NOUVEAU.getValeur());
        assertEquals(EtatLot.DEVTU, EtatLot.from("En DEV-TU"));
        assertEquals(EtatLot.INCONNU, EtatLot.from("\0En DEV-TU"));
        assertEquals("En DEV-TU", EtatLot.DEVTU.getValeur());
        assertEquals(EtatLot.TFON, EtatLot.from("TFON"));
        assertEquals(EtatLot.INCONNU, EtatLot.from("\0TFON"));
        assertEquals("TFON", EtatLot.TFON.getValeur());
        assertEquals(EtatLot.VMOE, EtatLot.from("En Vérification MOE"));
        assertEquals(EtatLot.INCONNU, EtatLot.from("\0En Vérification MOE"));
        assertEquals("En Vérification MOE", EtatLot.VMOE.getValeur());
        assertEquals(EtatLot.VMOA, EtatLot.from("En Validation MOA"));
        assertEquals(EtatLot.INCONNU, EtatLot.from("\0En Validation MOA"));
        assertEquals("En Validation MOA", EtatLot.VMOA.getValeur());
        assertEquals(EtatLot.EDITION, EtatLot.from("Livré à l'Edition"));
        assertEquals(EtatLot.INCONNU, EtatLot.from("\0Livré à l'Edition"));
        assertEquals("Livré à l'Edition", EtatLot.EDITION.getValeur());
        assertEquals(EtatLot.TERMINE, EtatLot.from("Terminé"));
        assertEquals(EtatLot.INCONNU, EtatLot.from("\0Terminé"));
        assertEquals("Terminé", EtatLot.TERMINE.getValeur());
        assertEquals(EtatLot.MOA, EtatLot.from("Candidat pour la Validation MOA"));
        assertEquals(EtatLot.INCONNU, EtatLot.from("\0Candidat pour la Validation MOA"));
        assertEquals("Candidat pour la Validation MOA", EtatLot.MOA.getValeur());
        assertEquals(EtatLot.INCONNU, EtatLot.from("inconnu"));
        assertEquals(EtatLot.INCONNU, EtatLot.from("\0inconnu"));
        assertEquals(inconnu, EtatLot.INCONNU.getValeur());
        assertEquals(EtatLot.INCONNU, EtatLot.from("autre"));
        assertEquals(inconnu, EtatLot.INCONNU.getValeur());
        assertEquals(EtatLot.INCONNU, EtatLot.from(null));
        assertEquals(inconnu, EtatLot.INCONNU.getValeur());
        assertEquals(EtatLot.INCONNU, EtatLot.from(Statics.EMPTY));
        assertEquals(inconnu, EtatLot.INCONNU.getValeur());
    }

    @Test
    public void testValeurInstanciation() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class<Object> inner = Whitebox.getInnerClassType(EtatLot.class, "Valeur");
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
        assertEquals(EtatLot.ABANDONNE, EtatLot.valueOf(EtatLot.ABANDONNE.toString()));        
    }
}
