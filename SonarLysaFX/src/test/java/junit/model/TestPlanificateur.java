package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import model.ModelFactory;
import model.Planificateur;

public class TestPlanificateur extends AbstractTestModel<Planificateur>
{
    /*---------- ATTRIBUTS ----------*/

    private String anneeEnCours;
    private String prec;
    private String suiv;
    private final LocalDate today = LocalDate.now();
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public TestPlanificateur()
    {
        anneeEnCours = String.valueOf(today.getYear());
        prec = String.valueOf(today.getYear() - 1);
        suiv = String.valueOf(today.getYear() + 1);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testIsLundi()
    {
        // Test valeur après initialisation
        assertFalse(objetTest.isLundi());
        
        // Test setter et getter
        objetTest.setLundi(true);
        assertTrue(objetTest.isLundi());       
    }
    
    @Test
    public void testIsMardi()
    {
        // Test valeur après initialisation
        assertFalse(objetTest.isMardi());
        
        // Test setter et getter
        objetTest.setMardi(true);
        assertTrue(objetTest.isMardi());       
    }
    
    @Test
    public void testIsMercredi()
    {
        // Test valeur après initialisation
        assertFalse(objetTest.isMercredi());
        
        // Test setter et getter
        objetTest.setMercredi(true);
        assertTrue(objetTest.isMercredi());       
    }
    
    @Test
    public void testIsJeudi()
    {
        // Test valeur après initialisation
        assertFalse(objetTest.isJeudi());
        
        // Test setter et getter
        objetTest.setJeudi(true);
        assertTrue(objetTest.isJeudi());       
    }
    
    @Test
    public void testIsVendredi()
    {
        // Test valeur après initialisation
        assertFalse(objetTest.isVendredi());
        
        // Test setter et getter
        objetTest.setVendredi(true);
        assertTrue(objetTest.isVendredi());       
    }
    
    @Test
    public void testGetHeure()
    {
        // Test valeur après initialisation
        assertEquals(LocalTime.of(0, 0), objetTest.getHeure());
        
        // Test setter et getter
        objetTest.setHeure(LocalTime.of(10, 10));
        assertEquals(LocalTime.of(10, 10), objetTest.getHeure());       
    }
    
    @Test
    public void testIsActive()
    {
        // Test valeur après initialisation
        assertFalse(objetTest.isActive());
        
        // Test setter et getter
        objetTest.setActive(true);
        assertTrue(objetTest.isActive());       
    }
    
    @Test
    public void testGetAnnees()
    {       
        // Test valeur après initialisation
        assertEquals(anneeEnCours, objetTest.getAnnees().get(0));
        
        // Test ajout annèe précedente
        objetTest.addLastYear();        
        assertTrue(objetTest.getAnnees().contains(prec));
        assertTrue(objetTest.getAnnees().contains(anneeEnCours));
        
        // test ajout année suivante
        objetTest.addNextYear();
        assertTrue(objetTest.getAnnees().contains(suiv)); 
        assertTrue(objetTest.getAnnees().contains(anneeEnCours));
        
        // Test avec nouvel objet
        objetTest = ModelFactory.build(Planificateur.class);
        
        // Test ajout annèe précedente
        objetTest.addLastYear();        
        assertTrue(objetTest.getAnnees().contains(prec));
        assertTrue(objetTest.getAnnees().contains(anneeEnCours));
        
        // Test avec nouvel objet
        objetTest = ModelFactory.build(Planificateur.class);
        
        // test ajout année suivante
        objetTest.addNextYear();
        assertTrue(objetTest.getAnnees().contains(suiv)); 
        assertTrue(objetTest.getAnnees().contains(anneeEnCours));
    }
    
    @Test
    public void testAddLastYear()
    {
        // Test ajout annèe précedente
        objetTest.addLastYear();        
        assertTrue(objetTest.getAnnees().contains(prec));
        assertTrue(objetTest.getAnnees().contains(anneeEnCours));  
        
        // Vérification qu'on ajoute pas de doublons d'année
        objetTest.addLastYear();  
        objetTest.addLastYear();  
        
        int compte = 0;
        for (String annee : objetTest.getAnnees())
        {
            if (annee.equals(prec))
                compte++;
        }
        assertEquals("Duplication des annèes", 1, compte);
    }
    
    @Test
    public void testAddNextYear()
    {     
        // test ajout année suivante
        objetTest.addNextYear();
        assertTrue(objetTest.getAnnees().contains(suiv)); 
        assertTrue(objetTest.getAnnees().contains(anneeEnCours));
        
        // Vérification qu'on ajoute pas de doublons d'année
        objetTest.addNextYear();
        objetTest.addNextYear();
        
        int compte = 0;
        for (String annee : objetTest.getAnnees())
        {
            if (annee.equals(suiv))
                compte++;
        }
        assertEquals("Duplication des annèes", 1, compte);
        
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
