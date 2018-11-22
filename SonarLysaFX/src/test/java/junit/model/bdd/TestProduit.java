package junit.model.bdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import junit.model.AbstractTestModel;
import model.bdd.Produit;
import model.enums.GroupeProduit;

public class TestProduit extends AbstractTestModel<Produit>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void initImpl()
    {
        objetTest.setNomProjet("nom");
        objetTest.setGroupe(GroupeProduit.AUCUN);
    }
    
    @Test
    public void testGetMapIndex()
    {
        assertEquals(objetTest.getNomProjet(), objetTest.getMapIndex());
    }

    @Test
    public void testGetNomProjet()
    {
        // Test getter et setter
        assertNotNull(objetTest.getNomProjet());
        assertEquals("nom", objetTest.getNomProjet());
        
        String nomProj = "nomProj";
        objetTest.setNomProjet(nomProj);
        assertEquals(nomProj, objetTest.getNomProjet());
    }

    @Test
    public void testGetGroupe() throws IllegalArgumentException, IllegalAccessException
    {
        // Test valeur initiale
        assertNotNull(objetTest.getGroupe());
        assertEquals(GroupeProduit.AUCUN, objetTest.getGroupe());
        
        // Test protection null
        objetTest.setGroupe(null);
        assertEquals(GroupeProduit.AUCUN, objetTest.getGroupe());
        
        // Mise à null du champ
        Field field = Whitebox.getField(objetTest.getClass(), "groupe");
        field.setAccessible(true);
        field.set(objetTest, null);
        assertEquals(GroupeProduit.AUCUN, objetTest.getGroupe());
        
        // Test setter et getter
        objetTest.setGroupe(GroupeProduit.NPC);
        assertEquals(GroupeProduit.NPC, objetTest.getGroupe());
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
