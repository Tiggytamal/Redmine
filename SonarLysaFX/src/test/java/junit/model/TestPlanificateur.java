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
        // Test valeur apr�s initialisation
        assertFalse(handler.isLundi());
        
        // Test setter et getter
        handler.setLundi(true);
        assertTrue(handler.isLundi());       
    }
    
    @Test
    public void testIsMardi()
    {
        // Test valeur apr�s initialisation
        assertFalse(handler.isMardi());
        
        // Test setter et getter
        handler.setMardi(true);
        assertTrue(handler.isMardi());       
    }
    
    @Test
    public void testIsMercredi()
    {
        // Test valeur apr�s initialisation
        assertFalse(handler.isMercredi());
        
        // Test setter et getter
        handler.setMercredi(true);
        assertTrue(handler.isMercredi());       
    }
    
    @Test
    public void testIsJeudi()
    {
        // Test valeur apr�s initialisation
        assertFalse(handler.isJeudi());
        
        // Test setter et getter
        handler.setJeudi(true);
        assertTrue(handler.isJeudi());       
    }
    
    @Test
    public void testIsVendredi()
    {
        // Test valeur apr�s initialisation
        assertFalse(handler.isVendredi());
        
        // Test setter et getter
        handler.setVendredi(true);
        assertTrue(handler.isVendredi());       
    }
    
    @Test
    public void testGetHeure()
    {
        // Test valeur apr�s initialisation
        assertEquals(LocalTime.of(0, 0), handler.getHeure());
        
        // Test setter et getter
        handler.setHeure(LocalTime.of(10, 10));
        assertEquals(LocalTime.of(10, 10), handler.getHeure());       
    }
    
    @Test
    public void testIsActive()
    {
        // Test valeur apr�s initialisation
        assertFalse(handler.isActive());
        
        // Test setter et getter
        handler.setActive(true);
        assertTrue(handler.isActive());       
    }
    
    @Test
    public void testGetAnnees()
    {       
        // Test valeur apr�s initialisation
        assertEquals(anneeEnCours, handler.getAnnees().get(0));
        
        // Test ajout ann�e pr�cedente
        handler.addLastYear();        
        assertTrue(handler.getAnnees().contains(prec));
        assertTrue(handler.getAnnees().contains(anneeEnCours));
        
        // test ajout ann�e suivante
        handler.addNextYear();
        assertTrue(handler.getAnnees().contains(suiv)); 
        assertTrue(handler.getAnnees().contains(anneeEnCours));
        
        // Test avec nouvel objet
        handler = ModelFactory.getModel(Planificateur.class);
        
        // Test ajout ann�e pr�cedente
        handler.addLastYear();        
        assertTrue(handler.getAnnees().contains(prec));
        assertTrue(handler.getAnnees().contains(anneeEnCours));
        
        // Test avec nouvel objet
        handler = ModelFactory.getModel(Planificateur.class);
        
        // test ajout ann�e suivante
        handler.addNextYear();
        assertTrue(handler.getAnnees().contains(suiv)); 
        assertTrue(handler.getAnnees().contains(anneeEnCours));
    }
    
    @Test
    public void testAddLastYear()
    {
        // Test ajout ann�e pr�cedente
        handler.addLastYear();        
        assertTrue(handler.getAnnees().contains(prec));
        assertTrue(handler.getAnnees().contains(anneeEnCours));  
        
        // V�rification qu'on ajoute pas de doublons d'ann�e
        handler.addLastYear();  
        handler.addLastYear();  
        
        int compte = 0;
        for (String annee : handler.getAnnees())
        {
            if (annee.equals(prec))
                compte++;
        }
        assertEquals("Duplication des ann�es", 1, compte);
    }
    
    @Test
    public void testAddNextYear()
    {     
        // test ajout ann�e suivante
        handler.addNextYear();
        assertTrue(handler.getAnnees().contains(suiv)); 
        assertTrue(handler.getAnnees().contains(anneeEnCours));
        
        // V�rification qu'on ajoute pas de doublons d'ann�e
        handler.addNextYear();
        handler.addNextYear();
        
        int compte = 0;
        for (String annee : handler.getAnnees())
        {
            if (annee.equals(suiv))
                compte++;
        }
        assertEquals("Duplication des ann�es", 1, compte);
        
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
