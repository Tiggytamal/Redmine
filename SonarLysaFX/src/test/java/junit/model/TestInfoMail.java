package junit.model;

import static org.junit.Assert.assertEquals;

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
