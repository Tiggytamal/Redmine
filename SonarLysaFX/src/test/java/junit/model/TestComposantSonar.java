package junit.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import junit.JunitBase;
import model.ComposantSonar;
import model.ModelFactory;

public class TestComposantSonar extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private ComposantSonar handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        handler = ModelFactory.getModel(ComposantSonar.class);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testConstructeur()
    {
        handler = ModelFactory.getModelWithParams(ComposantSonar.class, "id", "key", "nom");
        assertEquals("nom", handler.getNom());
        assertEquals("key", handler.getKey());
        assertEquals("id", handler.getId());
    }
    
    @Test
    public void testGetNom()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getNom());
        
        // Test setter et getter
        String nom = "Nom";
        handler.setNom(nom);
        assertEquals(nom, handler.getNom());       
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
    public void testGetKey()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getKey());
        
        // Test setter et getter
        String key = "Key";
        handler.setKey(key);
        assertEquals(key, handler.getKey());       
    }
    
    @Test
    public void testGetId()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getId());
        
        // Test setter et getter
        String id = "Id";
        handler.setId(id);
        assertEquals(id, handler.getId());       
    }
    
    @Test
    public void testGetAppli()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getAppli());
        
        // Test setter et getter
        String appli = "Appli";
        handler.setAppli(appli);
        assertEquals(appli, handler.getAppli());       
    }
    
    @Test
    public void testGetEdition()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getEdition());
        
        // Test setter et getter
        String edition = "Edition";
        handler.setEdition(edition);
        assertEquals(edition, handler.getEdition());       
    }
    
    @Test
    public void testGetLdc()
    {
        // test valeur vide ou nulle
        assertEquals(0, handler.getLdc());
        
        // Test setter et getter
        int ldc = 123456;
        handler.setLdc(ldc);
        assertEquals(ldc, handler.getLdc());       
    }
    
    @Test
    public void testSecurity()
    {
        // test valeur vide ou nulle
        assertEquals(0, handler.getSecurity());
        
        // Test setter et getter
        int securite = 12345;
        handler.setSecurity(securite);
        assertEquals(securite, handler.getSecurity());       
    }
    
    @Test
    public void testGetVulnerabilites()
    {
        // test valeur vide ou nulle
        assertEquals(0, handler.getVulnerabilites());
        
        // Test setter et getter
        int vulnerabilites = 1234;
        handler.setVulnerabilites(vulnerabilites);
        assertEquals(vulnerabilites, handler.getVulnerabilites());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/



}
