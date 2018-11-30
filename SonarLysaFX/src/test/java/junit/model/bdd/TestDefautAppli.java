package junit.model.bdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import junit.model.AbstractTestModel;
import model.ModelFactory;
import model.bdd.ComposantSonar;
import model.bdd.DefautAppli;
import model.enums.EtatDefaut;

public class TestDefautAppli extends AbstractTestModel<DefautAppli>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void initImpl()
    {
        ComposantSonar compo = ModelFactory.build(ComposantSonar.class);
        compo.setNom("nom");
        compo.setKey("key");
        compo.setId("id");
        objetTest.setCompo(compo);
    }

    @Test
    public void testGetMapIndex()
    {
        // Test avec compo non null
        assertEquals(objetTest.getCompo().getNom(), objetTest.getMapIndex());

        // Test avec compo null
        objetTest = ModelFactory.build(DefautAppli.class);
        objetTest.setNomComposant("nomCompo");
        assertEquals(objetTest.getNomComposant(), objetTest.getMapIndex());
    }

    @Test
    public void testGetCompo()
    {
        // Test avec composant
        assertNotNull(objetTest.getCompo());
        assertEquals("nom", objetTest.getCompo().getNom());

        // Test protection compo null
        objetTest.setCompo(null);
        assertNotNull(objetTest.getCompo());
        assertEquals("nom", objetTest.getCompo().getNom());

        // Test setter et getter
        ComposantSonar compo = ModelFactory.build(ComposantSonar.class);
        objetTest.setCompo(compo);
        assertEquals(compo, objetTest.getCompo());
    }

    @Test
    public void testGetAppliCorrigee()
    {
        // Test sans instanciation
        assertNotNull(objetTest.getAppliCorrigee());
        assertTrue(objetTest.getAppliCorrigee().isEmpty());

        // Test getter et setter
        String string = "appli";
        objetTest.setAppliCorrigee(string);
        assertEquals(string, objetTest.getAppliCorrigee());
        objetTest.setAppliCorrigee(null);
        assertTrue(objetTest.getAppliCorrigee().isEmpty());
    }

    @Test
    public void testGetEtatDefaut() throws IllegalAccessException
    {
        // Test valeur initiale
        assertNotNull(objetTest.getEtatDefaut());
        assertEquals(EtatDefaut.NOUVEAU, objetTest.getEtatDefaut());
        
        // Test protection null
        objetTest.setEtatDefaut(null);
        assertEquals(EtatDefaut.NOUVEAU, objetTest.getEtatDefaut());
        
        // Mise à null du champ
        Field field = Whitebox.getField(objetTest.getClass(), "etatDefaut");
        field.setAccessible(true);
        field.set(objetTest, null);
        assertEquals(EtatDefaut.NOUVEAU, objetTest.getEtatDefaut());
        
        // Test setter et getter
        objetTest.setEtatDefaut(EtatDefaut.ABANDONNE);
        assertEquals(EtatDefaut.ABANDONNE, objetTest.getEtatDefaut());
    }

    @Test
    public void testGetNomComposant()
    {
        // Test getter et setter
        assertNotNull(objetTest.getNomComposant());
        assertTrue(objetTest.getNomComposant().isEmpty());
        
        String nom = "nom";
        objetTest.setNomComposant(nom);
        assertEquals(nom, objetTest.getNomComposant());
        objetTest.setNomComposant(null);
        assertTrue(objetTest.getNomComposant().isEmpty());
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
