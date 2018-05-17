package junit.model.enums;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.EtatLot;

public class TestEnvironnement
{
    @Test
    public void testSize()
    {
        assertEquals(10, EtatLot.values().length);
    }
    @Test
    public void from()
    {
        String inconnu = "INCONNU";
        assertEquals(EtatLot.ABANDONNE, EtatLot.from("Abandonné"));
        assertEquals("Abandonné", EtatLot.ABANDONNE.toString());
        assertEquals(EtatLot.NOUVEAU, EtatLot.from("Nouveau"));
        assertEquals("Nouveau", EtatLot.NOUVEAU.toString());
        assertEquals(EtatLot.DEVTU, EtatLot.from("En DEV-TU"));
        assertEquals("En DEV-TU", EtatLot.DEVTU.toString());
        assertEquals(EtatLot.TFON, EtatLot.from("TFON"));
        assertEquals("TFON", EtatLot.TFON.toString());
        assertEquals(EtatLot.VMOE, EtatLot.from("En Vérification MOE"));
        assertEquals("En Vérification MOE", EtatLot.VMOE.toString());
        assertEquals(EtatLot.VMOA, EtatLot.from("En Validation MOA"));
        assertEquals("En Validation MOA", EtatLot.VMOA.toString());
        assertEquals(EtatLot.EDITION, EtatLot.from("Livré à l'Edition"));
        assertEquals("Livré à l'Edition", EtatLot.EDITION.toString());
        assertEquals(EtatLot.TERMINE, EtatLot.from("Terminé"));
        assertEquals("Terminé", EtatLot.TERMINE.toString());
        assertEquals(EtatLot.MOA, EtatLot.from("Candidat pour la Validation MOA"));
        assertEquals("Candidat pour la Validation MOA", EtatLot.MOA.toString());
        assertEquals(EtatLot.INCONNU, EtatLot.from("inconnu"));
        assertEquals(inconnu, EtatLot.INCONNU.toString());
        assertEquals(EtatLot.INCONNU, EtatLot.from("autre"));
        assertEquals(inconnu, EtatLot.INCONNU.toString());
        assertEquals(EtatLot.INCONNU, EtatLot.from(null));
        assertEquals(inconnu, EtatLot.INCONNU.toString());
        assertEquals(EtatLot.INCONNU, EtatLot.from(""));
        assertEquals(inconnu, EtatLot.INCONNU.toString());
    }

    @Test
    public void valeurInstanciation() throws ClassNotFoundException, InstantiationException, IllegalAccessException
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
}
