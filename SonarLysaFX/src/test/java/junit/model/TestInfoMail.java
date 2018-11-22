package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import model.InfoMail;

/**
 * Classe de test de la calsse de modèle InfoMail
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class TestInfoMail extends AbstractTestModel<InfoMail>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testHashCode()
    {
        assertEquals(961, objetTest.hashCode());
        objetTest.setInfoSupp("Infosupp2");
        assertTrue(objetTest.hashCode() > 961*2);
        objetTest.setLot("lot2");
        assertTrue(objetTest.hashCode() > 961*3);
    }
    
    @Test
    public void testEquals()
    {
        // Passage par tous les tests de la méthode Equals
        assertTrue(objetTest.equals(objetTest));
        assertFalse(objetTest.equals(null));
        assertFalse(objetTest.equals(EMPTY));
        InfoMail objet = InfoMail.build("lot", "infoSupp");
        assertFalse(objetTest.equals(objet));
        objetTest.setInfoSupp("Infosupp2");
        assertFalse(objetTest.equals(objet));
        objetTest.setInfoSupp("infoSupp");
        assertFalse(objetTest.equals(objet));
        objetTest.setLot("lot2");
        assertFalse(objetTest.equals(objet));
        objetTest.setLot("lot");
        assertTrue(objetTest.equals(objet));
        objetTest.setLot(null);
        objet.setLot(null);
        assertTrue(objetTest.equals(objet));
        objetTest.setInfoSupp(null);
        objet.setInfoSupp(null);
        assertTrue(objetTest.equals(objet));
    }
    
    @Test
    public void testConstructeur()
    {
        objetTest = InfoMail.build("Lot", "InfoSupp");
        assertEquals("Lot", objetTest.getLot());
        assertEquals("InfoSupp", objetTest.getInfoSupp());        
    }
    
    @Test
    public void testGetLot()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getLot());
        
        // Test setter et getter
        String lot = "Lot";
        objetTest.setLot(lot);
        assertEquals(lot, objetTest.getLot());       
    }
    
    @Test
    public void testGetInfoSupp()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getInfoSupp());
        
        // Test setter et getter
        String infoSupp = "infoSupp";
        objetTest.setInfoSupp(infoSupp);
        assertEquals(infoSupp, objetTest.getInfoSupp());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/



}
