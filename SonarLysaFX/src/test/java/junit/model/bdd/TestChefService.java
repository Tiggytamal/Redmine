package junit.model.bdd;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import junit.model.AbstractTestModel;
import model.bdd.ChefService;
import utilities.Statics;

public class TestChefService extends AbstractTestModel<ChefService>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/   
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetApplicationInconnue()
    {   
        objetTest = ChefService.getChefServiceInconnu("inco");
        assertEquals("Chef de Service inconnu", objetTest.getNom());
        assertEquals(Statics.EMPTY, objetTest.getFiliere());
        assertEquals(Statics.EMPTY, objetTest.getDirection());
        assertEquals(Statics.EMPTY, objetTest.getDepartement());
        assertEquals("inco", objetTest.getService());
    }
    
    @Test
    public void testGetMapIndex()
    {
        assertEquals(objetTest.getService(), objetTest.getMapIndex()); 
    }
    
    @Test
    public void testUpdate()
    {
        ChefService chef = ChefService.getChefServiceInconnu("inco");
        chef.setNom("nom");
        chef.setFiliere("fil");
        chef.setDirection("direct");
        chef.setDepartement("depart");
        
        objetTest.update(chef);
        assertEquals("fil", objetTest.getFiliere());
        assertEquals("direct", objetTest.getDirection());
        assertEquals("depart", objetTest.getDepartement());       
        assertEquals("inco", objetTest.getService());     
    }
    
    @Test
    public void testGetDirection()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getDirection());
        
        // Test setter et getter
        String direction = "Direction";
        objetTest.setDirection(direction);
        assertEquals(direction, objetTest.getDirection());       
    }
    
    @Test
    public void testGetFiliere()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getFiliere());
        
        // Test setter et getter
        String filiere = "filiere";
        objetTest.setFiliere(filiere);
        assertEquals(filiere, objetTest.getFiliere());       
    }
    
    @Test
    public void testGetService()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getService());
        
        // Test setter et getter
        String service = "service";
        objetTest.setService(service);
        assertEquals(service, objetTest.getService());       
    }
    
    @Test
    public void testGetDepartement()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getDepartement());
        
        // Test setter et getter
        String departement = "departement";
        objetTest.setDepartement(departement);
        assertEquals(departement, objetTest.getDepartement());       
    }
    
    @Test
    public void testGetNom()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getNom());
        
        // Test setter et getter
        String nom = "nom";
        objetTest.setNom(nom);
        assertEquals(nom, objetTest.getNom());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
