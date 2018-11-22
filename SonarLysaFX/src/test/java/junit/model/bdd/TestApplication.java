package junit.model.bdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import junit.model.AbstractTestModel;
import model.bdd.Application;
import utilities.Statics;

public class TestApplication extends AbstractTestModel<Application>
{
    /*---------- ATTRIBUTS ----------*/    
    /*---------- CONSTRUCTEURS ----------*/   
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetApplicationInconnue()
    {   
        objetTest = Application.getApplicationInconnue("appli");
        assertEquals("appli", objetTest.getCode());
        assertEquals(false, objetTest.isActif());
        assertEquals("Appli inconnue du référentiel", objetTest.getLibelle());
        assertEquals(true, objetTest.isOpen());
        assertEquals(false, objetTest.isMainFrame());
        assertEquals(false, objetTest.isReferentiel());
    }
    
    @Test
    public void testGetMapIndex()
    {
        assertEquals(objetTest.getCode(), objetTest.getMapIndex()); 
    }
    
    @Test
    public void testUpdate()
    {
        Application appli = Application.getApplicationInconnue("appli");
        appli.setActif(true);
        appli.setLibelle("libelle");
        appli.setOpen(false);
        appli.setMainFrame(true);
        appli.setReferentiel(true);
        
        objetTest.update(appli);
        assertEquals(Statics.EMPTY, objetTest.getCode());
        assertEquals(true, objetTest.isActif());
        assertEquals("libelle", objetTest.getLibelle());
        assertEquals(false, objetTest.isOpen());
        assertEquals(true, objetTest.isMainFrame());
        assertEquals(true, objetTest.isReferentiel());       
    }
    
    @Test
    public void testAjouterldcSonar()
    {
        // Ajout de valeurs et vérification que le total est bon
        objetTest.ajouterldcSonar(100);
        assertEquals(100, objetTest.getLdcSonar());
        objetTest.ajouterldcSonar(95);
        assertEquals(195, objetTest.getLdcSonar());
        objetTest.ajouterldcSonar(112);
        assertEquals(307, objetTest.getLdcSonar());
    }
    
    @Test
    public void testAjouterVulnerabilites()
    {
        // Ajout de valeurs et vérification que le total est bon
        objetTest.ajouterVulnerabilites(50);
        assertEquals(50, objetTest.getNbreVulnerabilites());
        objetTest.ajouterVulnerabilites(45);
        assertEquals(95, objetTest.getNbreVulnerabilites());
        objetTest.ajouterVulnerabilites(10);
        assertEquals(105, objetTest.getNbreVulnerabilites());
    }
    
    @Test
    public void testMajValSecurite()
    {
        final String A = "A";
        final String B = "B";
        final String C = "C";
        final String D = "D";
        final String E = "E";
        final String F = "F";
        
        // Mise à jour de la valeur de sécurité et vérification que l'on retrouve bine toujours la valeur maximale
        assertEquals(EMPTY, objetTest.getValSecurite());
        objetTest.majValSecurite(A);
        assertEquals(A, objetTest.getValSecurite());
        objetTest.majValSecurite(C);
        assertEquals(C, objetTest.getValSecurite());
        objetTest.majValSecurite(B);
        assertEquals(C, objetTest.getValSecurite());
        objetTest.majValSecurite(E);
        assertEquals(E, objetTest.getValSecurite());
        objetTest.majValSecurite(D);
        assertEquals(E, objetTest.getValSecurite());
        objetTest.majValSecurite(F);
        assertEquals(F, objetTest.getValSecurite());
        objetTest.majValSecurite(A);
        assertEquals(F, objetTest.getValSecurite()); 
        objetTest.majValSecurite(F);
        assertEquals(F, objetTest.getValSecurite()); 
    }
    
    @Test
    public void testToString()
    {
        objetTest.setActif(true);
        objetTest.setCode("code");
        objetTest.setLibelle("libelle");
        String string = objetTest.toString();
        assertTrue(string.contains("code=code"));
        assertTrue(string.contains("libelle=libelle"));
        assertTrue(string.contains("actif=true"));        
    }
    
    @Test
    public void testGetCode()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getCode());
        
        // Test setter et getter
        String code = "Code";
        objetTest.setCode(code);
        assertEquals(code, objetTest.getCode());       
    }
    
    @Test
    public void testIsActif()
    {
        // test valeur vide ou nulle
        assertFalse(objetTest.isActif());
        
        // Test setter et getter
        objetTest.setActif(true);
        assertTrue(objetTest.isActif());       
    }
    
    @Test
    public void testGetLibelle()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getLibelle());
        
        // Test setter et getter
        String libelle = "Libelle";
        objetTest.setLibelle(libelle);
        assertEquals(libelle, objetTest.getLibelle());       
    }
    
    @Test
    public void testIsOpen()
    {
        // test valeur vide ou nulle
        assertFalse(objetTest.isOpen());
        
        // Test setter et getter
        objetTest.setOpen(true);
        assertTrue(objetTest.isOpen());       
    }
    
    @Test
    public void testIsMainFrame()
    {
        // test valeur vide ou nulle
        assertFalse(objetTest.isMainFrame());
        
        // Test setter et getter
        objetTest.setMainFrame(true);
        assertTrue(objetTest.isMainFrame());       
    }
    
    @Test
    public void testGetValSecurite()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getValSecurite());
        
        // Test setter et getter
        String valSecurite = "Z";
        objetTest.setValSecurite(valSecurite);
        assertEquals(valSecurite, objetTest.getValSecurite());       
    }
    
    @Test
    public void testGetNbreVulnerabilites()
    {
        // test valeur vide ou nulle
        assertEquals(0, objetTest.getNbreVulnerabilites());
        
        // Test setter et getter
        int valSecurite = 100;
        objetTest.setNbreVulnerabilites(valSecurite);
        assertEquals(valSecurite, objetTest.getNbreVulnerabilites());       
    }
    
    @Test
    public void testGetLdcSonar()
    {
        // test valeur vide ou nulle
        assertEquals(0, objetTest.getLdcSonar());
        
        // Test setter et getter
        int ldcSonar = 2000;
        objetTest.setLdcSonar(ldcSonar);
        assertEquals(ldcSonar, objetTest.getLdcSonar());
    }
    
    @Test
    public void testGetLdcMainFrame()
    {
        // test valeur vide ou nulle
        assertEquals(0, objetTest.getLdcMainframe());
        
        // Test setter et getter
        int ldcMain = 150;
        objetTest.setLdcMainframe(ldcMain);
        assertEquals(ldcMain, objetTest.getLdcMainframe());       
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/



}
