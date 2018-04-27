package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.TODAY;

import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import model.ModelFactory;
import model.Planificateur;

public class TestPlanificateur
{
    /*---------- ATTRIBUTS ----------*/

    private Planificateur plan;
    private String anneeEnCours;
    private String prec;
    private String suiv;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public TestPlanificateur()
    {
        anneeEnCours = String.valueOf(TODAY.getYear());
        prec = String.valueOf(TODAY.getYear() - 1);
        suiv = String.valueOf(TODAY.getYear() + 1);
    }
    
    @Before
    public void init()
    {
        plan = ModelFactory.getModel(Planificateur.class);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void isLundi()
    {
        // Test valeur après initialisation
        assertEquals(false, plan.isLundi());
        
        // Test setter et getter
        plan.setLundi(true);
        assertTrue(plan.isLundi());       
    }
    
    @Test
    public void isMardi()
    {
        // Test valeur après initialisation
        assertEquals(false, plan.isMardi());
        
        // Test setter et getter
        plan.setMardi(true);
        assertTrue(plan.isMardi());       
    }
    
    @Test
    public void isMercredi()
    {
        // Test valeur après initialisation
        assertEquals(false, plan.isMercredi());
        
        // Test setter et getter
        plan.setMercredi(true);
        assertTrue(plan.isMercredi());       
    }
    
    @Test
    public void isJeudi()
    {
        // Test valeur après initialisation
        assertEquals(false, plan.isJeudi());
        
        // Test setter et getter
        plan.setJeudi(true);
        assertTrue(plan.isJeudi());       
    }
    
    @Test
    public void isVendredi()
    {
        // Test valeur après initialisation
        assertEquals(false, plan.isVendredi());
        
        // Test setter et getter
        plan.setVendredi(true);
        assertTrue(plan.isVendredi());       
    }
    
    @Test
    public void getHeure()
    {
        // Test valeur après initialisation
        assertEquals(LocalTime.of(0, 0), plan.getHeure());
        
        // Test setter et getter
        plan.setHeure(LocalTime.of(10, 10));
        assertEquals(LocalTime.of(10, 10), plan.getHeure());       
    }
    
    @Test
    public void isActive()
    {
        // Test valeur après initialisation
        assertEquals(false, plan.isActive());
        
        // Test setter et getter
        plan.setActive(true);
        assertTrue(plan.isActive());       
    }
    
    @Test
    public void getAnnees()
    {       
        // Test valeur après initialisation
        assertEquals(anneeEnCours, plan.getAnnees().get(0));
        
        // Test ajout annèe précedente
        plan.addLastYear();        
        assertTrue(plan.getAnnees().contains(prec));
        assertTrue(plan.getAnnees().contains(anneeEnCours));
        
        // test ajout année suivante
        plan.addNextYear();
        assertTrue(plan.getAnnees().contains(suiv)); 
        assertTrue(plan.getAnnees().contains(anneeEnCours));
        
        // Test avec nouvel objet
        plan = ModelFactory.getModel(Planificateur.class);
        
        // Test ajout annèe précedente
        plan.addLastYear();        
        assertTrue(plan.getAnnees().contains(prec));
        assertTrue(plan.getAnnees().contains(anneeEnCours));
        
        // Test avec nouvel objet
        plan = ModelFactory.getModel(Planificateur.class);
        
        // test ajout année suivante
        plan.addNextYear();
        assertTrue(plan.getAnnees().contains(suiv)); 
        assertTrue(plan.getAnnees().contains(anneeEnCours));
    }
    
    @Test
    public void addLastYear()
    {
        // Test ajout annèe précedente
        plan.addLastYear();        
        assertTrue(plan.getAnnees().contains(prec));
        assertTrue(plan.getAnnees().contains(anneeEnCours));  
        
        // Vérification qu'on ajoute pas de doublons d'année
        plan.addLastYear();  
        plan.addLastYear();  
        
        int compte = 0;
        for (String annee : plan.getAnnees())
        {
            if (annee.equals(prec))
                compte++;
        }
        assertEquals("Duplication des annèes", 1, compte);
    }
    
    @Test
    public void addNextYear()
    {     
        // test ajout année suivante
        plan.addNextYear();
        assertTrue(plan.getAnnees().contains(suiv)); 
        assertTrue(plan.getAnnees().contains(anneeEnCours));
        
        // Vérification qu'on ajoute pas de doublons d'année
        plan.addNextYear();
        plan.addNextYear();
        
        int compte = 0;
        for (String annee : plan.getAnnees())
        {
            if (annee.equals(suiv))
                compte++;
        }
        assertEquals("Duplication des annèes", 1, compte);
        
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
