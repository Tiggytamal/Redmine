package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import junit.JunitBase;
import model.Application;
import model.ModelFactory;

public class TestApplication extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private Application handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        handler = ModelFactory.getModel(Application.class);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testAjouterldcSonar()
    {
        // Ajout de valeurs et v�rification que le total est bon
        handler.ajouterldcSonar(100);
        assertEquals(100, handler.getLDCSonar());
        handler.ajouterldcSonar(95);
        assertEquals(195, handler.getLDCSonar());
        handler.ajouterldcSonar(112);
        assertEquals(307, handler.getLDCSonar());
    }
    
    @Test
    public void testAjouterVulnerabilites()
    {
        // Ajout de valeurs et v�rification que le total est bon
        handler.ajouterVulnerabilites(50);
        assertEquals(50, handler.getNbreVulnerabilites());
        handler.ajouterVulnerabilites(45);
        assertEquals(95, handler.getNbreVulnerabilites());
        handler.ajouterVulnerabilites(10);
        assertEquals(105, handler.getNbreVulnerabilites());
    }
    
    @Test
    public void testMajValSecurite()
    {
        // Mise � jour de la valeur de s�curit� et v�rification que l'on retrouve bine toujours la valeur maximale
        assertEquals("", handler.getValSecurite());
        handler.majValSecurite(1);
        assertEquals("A", handler.getValSecurite());
        handler.majValSecurite(3);
        assertEquals("C", handler.getValSecurite());
        handler.majValSecurite(2);
        assertEquals("C", handler.getValSecurite());
        handler.majValSecurite(5);
        assertEquals("E", handler.getValSecurite());
        handler.majValSecurite(4);
        assertEquals("E", handler.getValSecurite());
        handler.majValSecurite(6);
        assertEquals("F", handler.getValSecurite());
        handler.majValSecurite(0);
        assertEquals("F", handler.getValSecurite()); 
        handler.majValSecurite(12);
        assertEquals("F", handler.getValSecurite()); 
    }
    
    @Test
    public void testGetCode()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getCode());
        
        // Test setter et getter
        String code = "Code";
        handler.setCode(code);
        assertEquals(code, handler.getCode());       
    }
    
    @Test
    public void testIsActif()
    {
        // test valeur vide ou nulle
        assertFalse(handler.isActif());
        
        // Test setter et getter
        handler.setActif(true);
        assertTrue(handler.isActif());       
    }
    
    @Test
    public void testGetLibelle()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getLibelle());
        
        // Test setter et getter
        String libelle = "Libelle";
        handler.setLibelle(libelle);
        assertEquals(libelle, handler.getLibelle());       
    }
    
    @Test
    public void testIsOpen()
    {
        // test valeur vide ou nulle
        assertFalse(handler.isOpen());
        
        // Test setter et getter
        handler.setOpen(true);
        assertTrue(handler.isOpen());       
    }
    
    @Test
    public void testIsMainFrame()
    {
        // test valeur vide ou nulle
        assertFalse(handler.isMainFrame());
        
        // Test setter et getter
        handler.setMainFrame(true);
        assertTrue(handler.isMainFrame());       
    }
    
    @Test
    public void testGetValSecurite()
    {
        // test valeur vide ou nulle
        assertEquals("", handler.getValSecurite());
        
        // Test setter et getter
        String valSecurite = "Z";
        handler.setValSecurite(valSecurite);
        assertEquals(valSecurite, handler.getValSecurite());       
    }
    
    @Test
    public void testGetNbreVulnerabilites()
    {
        // test valeur vide ou nulle
        assertEquals(0, handler.getNbreVulnerabilites());
        
        // Test setter et getter
        int valSecurite = 100;
        handler.setNbreVulnerabilites(valSecurite);
        assertEquals(valSecurite, handler.getNbreVulnerabilites());       
    }
    
    @Test
    public void testGetLdcSonar()
    {
        // test valeur vide ou nulle
        assertEquals(0, handler.getLDCSonar());
        
        // Test setter et getter
        int ldcSonar = 2000;
        handler.setLDCSonar(ldcSonar);
        assertEquals(ldcSonar, handler.getLDCSonar());       
    }
    
    @Test
    public void testGetLdcMainFrame()
    {
        // test valeur vide ou nulle
        assertEquals(0, handler.getLDCMainframe());
        
        // Test setter et getter
        int ldcMain = 150;
        handler.setLDCMainframe(ldcMain);
        assertEquals(ldcMain, handler.getLDCMainframe());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/



}
