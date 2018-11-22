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
        assertFalse(objetTest.isLundi());
        
        // Test setter et getter
        objetTest.setLundi(true);
        assertTrue(objetTest.isLundi());       
    }
    
    @Test
    public void testIsMardi()
    {
        // Test valeur apr�s initialisation
        assertFalse(objetTest.isMardi());
        
        // Test setter et getter
        objetTest.setMardi(true);
        assertTrue(objetTest.isMardi());       
    }
    
    @Test
    public void testIsMercredi()
    {
        // Test valeur apr�s initialisation
        assertFalse(objetTest.isMercredi());
        
        // Test setter et getter
        objetTest.setMercredi(true);
        assertTrue(objetTest.isMercredi());       
    }
    
    @Test
    public void testIsJeudi()
    {
        // Test valeur apr�s initialisation
        assertFalse(objetTest.isJeudi());
        
        // Test setter et getter
        objetTest.setJeudi(true);
        assertTrue(objetTest.isJeudi());       
    }
    
    @Test
    public void testIsVendredi()
    {
        // Test valeur apr�s initialisation
        assertFalse(objetTest.isVendredi());
        
        // Test setter et getter
        objetTest.setVendredi(true);
        assertTrue(objetTest.isVendredi());       
    }
    
    @Test
    public void testGetHeure()
    {
        // Test valeur apr�s initialisation
        assertEquals(LocalTime.of(0, 0), objetTest.getHeure());
        
        // Test setter et getter
        objetTest.setHeure(LocalTime.of(10, 10));
        assertEquals(LocalTime.of(10, 10), objetTest.getHeure());       
    }
    
    @Test
    public void testIsActive()
    {
        // Test valeur apr�s initialisation
        assertFalse(objetTest.isActive());
        
        // Test setter et getter
        objetTest.setActive(true);
        assertTrue(objetTest.isActive());       
    }
    
    @Test
    public void testGetAnnees()
    {       
        // Test valeur apr�s initialisation
        assertEquals(anneeEnCours, objetTest.getAnnees().get(0));
        
        // Test ajout ann�e pr�cedente
        objetTest.addLastYear();        
        assertTrue(objetTest.getAnnees().contains(prec));
        assertTrue(objetTest.getAnnees().contains(anneeEnCours));
        
        // test ajout ann�e suivante
        objetTest.addNextYear();
        assertTrue(objetTest.getAnnees().contains(suiv)); 
        assertTrue(objetTest.getAnnees().contains(anneeEnCours));
        
        // Test avec nouvel objet
        objetTest = ModelFactory.build(Planificateur.class);
        
        // Test ajout ann�e pr�cedente
        objetTest.addLastYear();        
        assertTrue(objetTest.getAnnees().contains(prec));
        assertTrue(objetTest.getAnnees().contains(anneeEnCours));
        
        // Test avec nouvel objet
        objetTest = ModelFactory.build(Planificateur.class);
        
        // test ajout ann�e suivante
        objetTest.addNextYear();
        assertTrue(objetTest.getAnnees().contains(suiv)); 
        assertTrue(objetTest.getAnnees().contains(anneeEnCours));
    }
    
    @Test
    public void testAddLastYear()
    {
        // Test ajout ann�e pr�cedente
        objetTest.addLastYear();        
        assertTrue(objetTest.getAnnees().contains(prec));
        assertTrue(objetTest.getAnnees().contains(anneeEnCours));  
        
        // V�rification qu'on ajoute pas de doublons d'ann�e
        objetTest.addLastYear();  
        objetTest.addLastYear();  
        
        int compte = 0;
        for (String annee : objetTest.getAnnees())
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
        objetTest.addNextYear();
        assertTrue(objetTest.getAnnees().contains(suiv)); 
        assertTrue(objetTest.getAnnees().contains(anneeEnCours));
        
        // V�rification qu'on ajoute pas de doublons d'ann�e
        objetTest.addNextYear();
        objetTest.addNextYear();
        
        int compte = 0;
        for (String annee : objetTest.getAnnees())
        {
            if (annee.equals(suiv))
                compte++;
        }
        assertEquals("Duplication des ann�es", 1, compte);
        
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
