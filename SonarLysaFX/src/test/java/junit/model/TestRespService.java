package junit.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import junit.JunitBase;
import model.ModelFactory;
import model.RespService;

public class TestRespService extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private RespService respService;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        respService = ModelFactory.getModel(RespService.class);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetDirection()
    {
        // test valeur vide ou nulle
        assertEquals("", respService.getDirection());
        
        // Test setter et getter
        String direction = "Direction";
        respService.setDirection(direction);
        assertEquals(direction, respService.getDirection());       
    }
    
    @Test
    public void testGetFiliere()
    {
        // test valeur vide ou nulle
        assertEquals("", respService.getFiliere());
        
        // Test setter et getter
        String filiere = "filiere";
        respService.setFiliere(filiere);
        assertEquals(filiere, respService.getFiliere());       
    }
    
    @Test
    public void testGetService()
    {
        // test valeur vide ou nulle
        assertEquals("", respService.getService());
        
        // Test setter et getter
        String service = "service";
        respService.setService(service);
        assertEquals(service, respService.getService());       
    }
    
    @Test
    public void testGetDepartement()
    {
        // test valeur vide ou nulle
        assertEquals("", respService.getDepartement());
        
        // Test setter et getter
        String departement = "departement";
        respService.setDepartement(departement);
        assertEquals(departement, respService.getDepartement());       
    }
    
    @Test
    public void testGetNom()
    {
        // test valeur vide ou nulle
        assertEquals("", respService.getNom());
        
        // Test setter et getter
        String nom = "nom";
        respService.setNom(nom);
        assertEquals(nom, respService.getNom());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
