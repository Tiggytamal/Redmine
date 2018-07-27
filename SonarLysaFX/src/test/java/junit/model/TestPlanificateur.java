package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import junit.JunitBase;
import model.ModelFactory;
import model.Planificateur;

public class TestPlanificateur extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private Planificateur plan;
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
    
    @Before
    public void init()
    {
        plan = ModelFactory.getModel(Planificateur.class);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testIsLundi()
    {
        // Test valeur apr�s initialisation
        assertFalse(plan.isLundi());
        
        // Test setter et getter
        plan.setLundi(true);
        assertTrue(plan.isLundi());       
    }
    
    @Test
    public void testIsMardi()
    {
        // Test valeur apr�s initialisation
        assertFalse(plan.isMardi());
        
        // Test setter et getter
        plan.setMardi(true);
        assertTrue(plan.isMardi());       
    }
    
    @Test
    public void testIsMercredi()
    {
        // Test valeur apr�s initialisation
        assertFalse(plan.isMercredi());
        
        // Test setter et getter
        plan.setMercredi(true);
        assertTrue(plan.isMercredi());       
    }
    
    @Test
    public void testIsJeudi()
    {
        // Test valeur apr�s initialisation
        assertFalse(plan.isJeudi());
        
        // Test setter et getter
        plan.setJeudi(true);
        assertTrue(plan.isJeudi());       
    }
    
    @Test
    public void testIsVendredi()
    {
        // Test valeur apr�s initialisation
        assertFalse(plan.isVendredi());
        
        // Test setter et getter
        plan.setVendredi(true);
        assertTrue(plan.isVendredi());       
    }
    
    @Test
    public void testGetHeure()
    {
        // Test valeur apr�s initialisation
        assertEquals(LocalTime.of(0, 0), plan.getHeure());
        
        // Test setter et getter
        plan.setHeure(LocalTime.of(10, 10));
        assertEquals(LocalTime.of(10, 10), plan.getHeure());       
    }
    
    @Test
    public void testIsActive()
    {
        // Test valeur apr�s initialisation
        assertFalse(plan.isActive());
        
        // Test setter et getter
        plan.setActive(true);
        assertTrue(plan.isActive());       
    }
    
    @Test
    public void testGetAnnees()
    {       
        // Test valeur apr�s initialisation
        assertEquals(anneeEnCours, plan.getAnnees().get(0));
        
        // Test ajout ann�e pr�cedente
        plan.addLastYear();        
        assertTrue(plan.getAnnees().contains(prec));
        assertTrue(plan.getAnnees().contains(anneeEnCours));
        
        // test ajout ann�e suivante
        plan.addNextYear();
        assertTrue(plan.getAnnees().contains(suiv)); 
        assertTrue(plan.getAnnees().contains(anneeEnCours));
        
        // Test avec nouvel objet
        plan = ModelFactory.getModel(Planificateur.class);
        
        // Test ajout ann�e pr�cedente
        plan.addLastYear();        
        assertTrue(plan.getAnnees().contains(prec));
        assertTrue(plan.getAnnees().contains(anneeEnCours));
        
        // Test avec nouvel objet
        plan = ModelFactory.getModel(Planificateur.class);
        
        // test ajout ann�e suivante
        plan.addNextYear();
        assertTrue(plan.getAnnees().contains(suiv)); 
        assertTrue(plan.getAnnees().contains(anneeEnCours));
    }
    
    @Test
    public void testAddLastYear()
    {
        // Test ajout ann�e pr�cedente
        plan.addLastYear();        
        assertTrue(plan.getAnnees().contains(prec));
        assertTrue(plan.getAnnees().contains(anneeEnCours));  
        
        // V�rification qu'on ajoute pas de doublons d'ann�e
        plan.addLastYear();  
        plan.addLastYear();  
        
        int compte = 0;
        for (String annee : plan.getAnnees())
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
        plan.addNextYear();
        assertTrue(plan.getAnnees().contains(suiv)); 
        assertTrue(plan.getAnnees().contains(anneeEnCours));
        
        // V�rification qu'on ajoute pas de doublons d'ann�e
        plan.addNextYear();
        plan.addNextYear();
        
        int compte = 0;
        for (String annee : plan.getAnnees())
        {
            if (annee.equals(suiv))
                compte++;
        }
        assertEquals("Duplication des ann�es", 1, compte);
        
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
