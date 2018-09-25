package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import model.bdd.ProjetClarity;

public class TestInfoClarity extends AbstractTestModel<ProjetClarity>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/    
    /*---------- METHODES PUBLIQUES ----------*/
    
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
    public void testGetCodeClarity()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getCode());
        
        // Test setter et getter
        String codeClarity = "codeClarity";
        handler.setCode(codeClarity);
        assertEquals(codeClarity, handler.getCode());       
    }
    
    @Test
    public void testGetLibelleProjet()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getLibelleProjet());
        
        // Test setter et getter
        String libelleProjet = "libelleProjet";
        handler.setLibelleProjet(libelleProjet);
        assertEquals(libelleProjet, handler.getLibelleProjet());       
    }
    
    @Test
    public void testGetChefProjet()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getChefProjet());
        
        // Test setter et getter
        String chefProjet = "chefProjet";
        handler.setChefProjet(chefProjet);
        assertEquals(chefProjet, handler.getChefProjet());       
    }
    
    @Test
    public void testGetEdition()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getEdition());
        
        // Test setter et getter
        String edition = "edition";
        handler.setEdition(edition);
        assertEquals(edition, handler.getEdition());       
    }
    
    @Test
    public void testGetDirection()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getDirection());
        
        // Test setter et getter
        String direction = "direction";
        handler.setDirection(direction);
        assertEquals(direction, handler.getDirection());       
    }
    
    @Test
    public void testGetDepartement()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getDepartement());
        
        // Test setter et getter
        String departement = "departement";
        handler.setDepartement(departement);
        assertEquals(departement, handler.getDepartement());       
    }
    
    @Test
    public void testGetService()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getService());
        
        // Test setter et getter
        String service = "service";
        handler.setService(service);
        assertEquals(service, handler.getService());       
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
