package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.InfoClarity;
import model.ModelFactory;

public class TestInfoClarity
{
    /*---------- ATTRIBUTS ----------*/

    private InfoClarity info;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        info = ModelFactory.getModel(InfoClarity.class);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testIsActif()
    {
        // test valeur vide ou nulle
        assertEquals(false, info.isActif());
        
        // Test setter et getter
        info.setActif(true);
        assertTrue(info.isActif());       
    }
    
    @Test
    public void testGetCodeClarity()
    {
        // test valeur vide ou nulle
        assertEquals("", info.getCodeClarity());
        
        // Test setter et getter
        String codeClarity = "codeClarity";
        info.setCodeClarity(codeClarity);
        assertEquals(codeClarity, info.getCodeClarity());       
    }
    
    @Test
    public void testGetLibelleProjet()
    {
        // test valeur vide ou nulle
        assertEquals("", info.getLibelleProjet());
        
        // Test setter et getter
        String libelleProjet = "libelleProjet";
        info.setLibelleProjet(libelleProjet);
        assertEquals(libelleProjet, info.getLibelleProjet());       
    }
    
    @Test
    public void testGetChefProjet()
    {
        // test valeur vide ou nulle
        assertEquals("", info.getChefProjet());
        
        // Test setter et getter
        String chefProjet = "chefProjet";
        info.setChefProjet(chefProjet);
        assertEquals(chefProjet, info.getChefProjet());       
    }
    
    @Test
    public void testGetEdition()
    {
        // test valeur vide ou nulle
        assertEquals("", info.getEdition());
        
        // Test setter et getter
        String edition = "edition";
        info.setEdition(edition);
        assertEquals(edition, info.getEdition());       
    }
    
    @Test
    public void testGetDirection()
    {
        // test valeur vide ou nulle
        assertEquals("", info.getDirection());
        
        // Test setter et getter
        String direction = "direction";
        info.setDirection(direction);
        assertEquals(direction, info.getDirection());       
    }
    
    @Test
    public void testGetDepartement()
    {
        // test valeur vide ou nulle
        assertEquals("", info.getDepartement());
        
        // Test setter et getter
        String departement = "departement";
        info.setDepartement(departement);
        assertEquals(departement, info.getDepartement());       
    }
    
    @Test
    public void testGetService()
    {
        // test valeur vide ou nulle
        assertEquals("", info.getService());
        
        // Test setter et getter
        String service = "service";
        info.setService(service);
        assertEquals(service, info.getService());       
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
