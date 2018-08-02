package junit.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import junit.JunitBase;
import model.ModelFactory;
import model.Vulnerabilite;

/**
 * Classe de test de la classe de modèle Vulnerabilite
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class TestVulnerabilite extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private Vulnerabilite handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        handler = ModelFactory.getModel(Vulnerabilite.class);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetSeverite()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getSeverite());
        
        // Test setter et getter
        String severite = "Sev";
        handler.setSeverite(severite);
        assertEquals(severite, handler.getSeverite());       
    }
    
    @Test
    public void testGetComposant()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getComposant());
        
        // Test setter et getter
        String compo = "Compo";
        handler.setComposant(compo);
        assertEquals(compo, handler.getComposant());       
    }
    
    @Test
    public void testGetStatus()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getStatus());
        
        // Test setter et getter
        String status = "Status";
        handler.setStatus(status);
        assertEquals(status, handler.getStatus());       
    }
    
    @Test
    public void testGetMessage()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getMessage());
        
        // Test setter et getter
        String message = "Message";
        handler.setMessage(message);
        assertEquals(message, handler.getMessage());       
    }
    
    @Test
    public void testGetDateCreation()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getDateCreation());
        
        // Test setter et getter
        String date = "10-12-2018";
        handler.setDateCreation(date);
        assertEquals(date, handler.getDateCreation());       
    }
    
    @Test
    public void testGetLot()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getLot());
        
        // Test setter et getter
        String lot = "123456";
        handler.setLot(lot);
        assertEquals(lot, handler.getLot());       
    }
    
    @Test
    public void testGetClarity()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getClarity());
        
        // Test setter et getter
        String clarity = "SVRP_qsdfhjl";
        handler.setClarity(clarity);
        assertEquals(clarity, handler.getClarity());       
    }
    
    @Test
    public void testGetAppli()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getAppli());
        
        // Test setter et getter
        String appli = "ABCD";
        handler.setAppli(appli);
        assertEquals(appli, handler.getAppli());       
    }
    
    @Test
    public void testGetLib()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getLib());
        
        // Test setter et getter
        String lib = "Lib";
        handler.setLib(lib);
        assertEquals(lib, handler.getLib());       
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/



}
