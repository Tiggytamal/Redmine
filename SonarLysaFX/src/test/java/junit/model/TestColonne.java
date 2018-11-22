package junit.model;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import model.Colonne;

/**
 * Classe de test de la calsse de modèle Colonne
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TestColonne extends AbstractTestModel<Colonne>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Test
    public void testGetNom()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getNom());

        // Test setter et getter
        String string = "nom";
        objetTest.setNom(string);
        assertEquals(string, objetTest.getNom());
    }
    
    @Test
    public void testGetIndice()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getIndice());

        // Test setter et getter
        String string = "indice";
        objetTest.setIndice(string);
        assertEquals(string, objetTest.getIndice());
    }
}
