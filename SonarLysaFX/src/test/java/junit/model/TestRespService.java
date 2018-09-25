package junit.model;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import model.bdd.ChefService;

public class TestRespService extends AbstractTestModel<ChefService>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/   
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetDirection()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getDirection());
        
        // Test setter et getter
        String direction = "Direction";
        handler.setDirection(direction);
        assertEquals(direction, handler.getDirection());       
    }
    
    @Test
    public void testGetFiliere()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getFiliere());
        
        // Test setter et getter
        String filiere = "filiere";
        handler.setFiliere(filiere);
        assertEquals(filiere, handler.getFiliere());       
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
    public void testGetNom()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getNom());
        
        // Test setter et getter
        String nom = "nom";
        handler.setNom(nom);
        assertEquals(nom, handler.getNom());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
