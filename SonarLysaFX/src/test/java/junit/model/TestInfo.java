package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.EMPTY;

import java.io.File;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.Info;
import utilities.Statics;

public class TestInfo extends AbstractTestModel<Info>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testControle()
    {
        String valeur = "pasvide";
        
        // Test après initialisation
        assertFalse(objetTest.controle());
        
        // Test mot de passe vide
        objetTest.setMotDePasse(EMPTY);
        assertFalse(objetTest.controle());
        
        // Test pseudo vide
        objetTest.setPseudo(EMPTY);
        objetTest.setMotDePasse(null);
        assertFalse(objetTest.controle());
        
        // Test les deux vides
        objetTest.setPseudo(EMPTY);
        objetTest.setMotDePasse(EMPTY);
        assertFalse(objetTest.controle());
        
        // Test mot de passe rempli
        objetTest.setMotDePasse(valeur);
        assertFalse(objetTest.controle());
        
        // Test pseudo rempli
        objetTest.setMotDePasse(EMPTY);
        objetTest.setPseudo(valeur);
        assertFalse(objetTest.controle());
        
        // Test les deux remplis
        objetTest.setPseudo(valeur);
        objetTest.setMotDePasse(valeur);
        assertTrue(objetTest.controle());
    }
    
    @Test
    public void testGetFile()
    {
        File file = objetTest.getFile();       
        assertEquals(new File(Statics.JARPATH + Whitebox.getInternalState(Info.class, "NOMFICHIER")), file);
    }
    
    @Test
    public void testGetRessource()
    {
        File resource = objetTest.getResource();
        
        assertEquals(new File(getClass().getResource(Whitebox.getInternalState(Info.class, "RESOURCE")).getFile()), resource);
    }
    
    @Test
    public void testControleDonnees()
    {
        // Test mdp null
        objetTest.setPseudo("a");
        assertFalse(objetTest.controleDonnees() + " - n'est pas vide", objetTest.controleDonnees().isEmpty());
        
        // Test pseudo null
        objetTest.setPseudo(null);
        objetTest.setMotDePasse("b");
        assertFalse(objetTest.controleDonnees().isEmpty());
        
        // test ok
        objetTest.setPseudo("a");
        assertEquals(0, objetTest.controleDonnees().length());
    }
    
    @Test
    public void testGetMotDePasse()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getMotDePasse());
        
        // Test setter et getter
        String mdp = "motDePasse";
        objetTest.setMotDePasse(mdp);
        assertEquals(mdp, objetTest.getMotDePasse());  
    }
    
    @Test
    public void testGetPseudo()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getPseudo());
        
        // Test setter et getter
        String pseudo = "pseudo";
        objetTest.setPseudo(pseudo);
        assertEquals(pseudo, objetTest.getPseudo());  
    }
    
    @Test
    public void testGetNom()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getNom());
        
        // Test setter et getter
        String pseudo = "pseudo";
        objetTest.setNom(pseudo);
        assertEquals(pseudo, objetTest.getNom());  
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
