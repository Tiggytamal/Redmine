package junit.model;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ComposantSonar;
import model.bdd.LotRTC;

public class TestComposantSonar extends AbstractTestModel<ComposantSonar>
{
    /*---------- ATTRIBUTS ----------*/    
    /*---------- CONSTRUCTEURS ----------*/    
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
        assertEquals(EMPTY, handler.getNom());
        
        // Test setter et getter
        String nom = "Nom";
        handler.setNom(nom);
        assertEquals(nom, handler.getNom());       
    }
    
    @Test
    public void testGetLot()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getLotRTC());
        
        // Test setter et getter
        String lot = "Lot";
        LotRTC lotRTC = ModelFactory.getModel(LotRTC.class);
        lotRTC.setLot(lot);
        handler.setLotRTC(lotRTC);
        assertEquals(lot, handler.getLotRTC());       
    }
    
    @Test
    public void testGetKey()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getKey());
        
        // Test setter et getter
        String key = "Key";
        handler.setKey(key);
        assertEquals(key, handler.getKey());       
    }
    
    @Test
    public void testGetId()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getId());
        
        // Test setter et getter
        String id = "Id";
        handler.setId(id);
        assertEquals(id, handler.getId());       
    }
    
    @Test
    public void testGetAppli()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getAppli());
        
        // Test setter et getter
        Application appli = ModelFactory.getModel(Application.class);
        appli.setCode("Appli");
        handler.setAppli(appli);
        assertEquals(appli, handler.getAppli());       
    }
    
    @Test
    public void testGetEdition()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getEdition());
        
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
        assertEquals(0, handler.getSecurityRating());
        
        // Test setter et getter
        String securiteRat = "B";
        handler.setSecurityRating(securiteRat);
        assertEquals(securiteRat, handler.getSecurityRating());       
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
