package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import junit.JunitBase;
import model.InfoMail;
import model.ModelFactory;

/**
 * Classe de test de la calsse de modèle InfoMail
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class TestInfoMail extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private InfoMail handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        handler = ModelFactory.getModel(InfoMail.class);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testHashCode()
    {
        assertEquals(961, handler.hashCode());
        handler.setInfoSupp("Infosupp2");
        assertTrue(handler.hashCode() > 961*2);
        handler.setLot("lot2");
        assertTrue(handler.hashCode() > 961*3);
    }
    
    @Test
    public void testEquals()
    {
        // Passage par tous les tests de la méthode Equals
        assertTrue(handler.equals(handler));
        assertFalse(handler.equals(null));
        assertFalse(handler.equals(""));
        InfoMail objet = ModelFactory.getModelWithParams(InfoMail.class, "lot", "infoSupp");
        assertFalse(handler.equals(objet));
        handler.setInfoSupp("Infosupp2");
        assertFalse(handler.equals(objet));
        handler.setInfoSupp("infoSupp");
        assertFalse(handler.equals(objet));
        handler.setLot("lot2");
        assertFalse(handler.equals(objet));
        handler.setLot("lot");
        assertTrue(handler.equals(objet));
        handler.setLot(null);
        objet.setLot(null);
        assertTrue(handler.equals(objet));
        handler.setInfoSupp(null);
        objet.setInfoSupp(null);
        assertTrue(handler.equals(objet));
    }
    
    @Test
    public void testConstructeur()
    {
        handler = ModelFactory.getModelWithParams(InfoMail.class, "Lot", "InfoSupp");
        assertEquals("Lot", handler.getLot());
        assertEquals("InfoSupp", handler.getInfoSupp());        
    }
    
    @Test
    public void testGetLot()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getLot());
        
        // Test setter et getter
        String lot = "Lot";
        handler.setLot(lot);
        assertEquals(lot, handler.getLot());       
    }
    
    @Test
    public void testGetInfoSupp()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getInfoSupp());
        
        // Test setter et getter
        String infoSupp = "infoSupp";
        handler.setInfoSupp(infoSupp);
        assertEquals(infoSupp, handler.getInfoSupp());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/



}
