package junit.model.enums;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.enums.Environnement;

public class TestEnvironnement
{
    @Test
    public void testSize()
    {
        assertEquals(10, Environnement.values().length);
    }
    @Test
    public void from()
    {
        String inconnu = "INCONNU";
        assertEquals(Environnement.ABANDONNE, Environnement.from("Abandonné"));
        assertEquals("Abandonné", Environnement.ABANDONNE.toString());
        assertEquals(Environnement.NOUVEAU, Environnement.from("Nouveau"));
        assertEquals("Nouveau", Environnement.NOUVEAU.toString());
        assertEquals(Environnement.DEVTU, Environnement.from("En DEV-TU"));
        assertEquals("En DEV-TU", Environnement.DEVTU.toString());
        assertEquals(Environnement.TFON, Environnement.from("TFON"));
        assertEquals("TFON", Environnement.TFON.toString());
        assertEquals(Environnement.VMOE, Environnement.from("En Vérification MOE"));
        assertEquals("En Vérification MOE", Environnement.VMOE.toString());
        assertEquals(Environnement.VMOA, Environnement.from("En Validation MOA"));
        assertEquals("En Validation MOA", Environnement.VMOA.toString());
        assertEquals(Environnement.EDITION, Environnement.from("Livré à l'Edition"));
        assertEquals("Livré à l'Edition", Environnement.EDITION.toString());
        assertEquals(Environnement.TERMINE, Environnement.from("Terminé"));
        assertEquals("Terminé", Environnement.TERMINE.toString());
        assertEquals(Environnement.MOA, Environnement.from("Candidat pour la Validation MOA"));
        assertEquals("Candidat pour la Validation MOA", Environnement.MOA.toString());
        assertEquals(Environnement.INCONNU, Environnement.from("inconnu"));
        assertEquals(inconnu, Environnement.INCONNU.toString());
        assertEquals(Environnement.INCONNU, Environnement.from("autre"));
        assertEquals(inconnu, Environnement.INCONNU.toString());
        assertEquals(Environnement.INCONNU, Environnement.from(null));
        assertEquals(inconnu, Environnement.INCONNU.toString());
        assertEquals(Environnement.INCONNU, Environnement.from(""));
        assertEquals(inconnu, Environnement.INCONNU.toString());
    }

    @Test
    public void valeurInstanciation() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class<Object> inner = Whitebox.getInnerClassType(Environnement.class, "Valeur");
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
