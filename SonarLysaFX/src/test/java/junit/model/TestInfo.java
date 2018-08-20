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
        assertFalse(handler.controle());
        
        // Test mot de passe vide
        handler.setMotDePasse(EMPTY);
        assertFalse(handler.controle());
        
        // Test pseudo vide
        handler.setPseudo(EMPTY);
        handler.setMotDePasse(null);
        assertFalse(handler.controle());
        
        // Test les deux vides
        handler.setPseudo(EMPTY);
        handler.setMotDePasse(EMPTY);
        assertFalse(handler.controle());
        
        // Test mot de passe rempli
        handler.setMotDePasse(valeur);
        assertFalse(handler.controle());
        
        // Test pseudo rempli
        handler.setMotDePasse(EMPTY);
        handler.setPseudo(valeur);
        assertFalse(handler.controle());
        
        // Test les deux remplis
        handler.setPseudo(valeur);
        handler.setMotDePasse(valeur);
        assertTrue(handler.controle());
    }
    
    @Test
    public void testGetFile()
    {
        File file = handler.getFile();       
        assertEquals(new File(Statics.JARPATH + Whitebox.getInternalState(Info.class, "NOMFICHIER")), file);
    }
    
    @Test
    public void testGetRessource()
    {
        File resource = handler.getResource();
        
        assertEquals(new File(getClass().getResource(Whitebox.getInternalState(Info.class, "RESOURCE")).getFile()), resource);
    }
    
    @Test
    public void testControleDonnees()
    {
        // Test mdp null
        handler.setPseudo("a");
        assertFalse(handler.controleDonnees() + " - n'est pas vide", handler.controleDonnees().isEmpty());
        
        // Test pseudo null
        handler.setPseudo(null);
        handler.setMotDePasse("b");
        assertFalse(handler.controleDonnees().isEmpty());
        
        // test ok
        handler.setPseudo("a");
        assertEquals(0, handler.controleDonnees().length());
    }
    
    @Test
    public void testGetMotDePasse()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getMotDePasse());
        
        // Test setter et getter
        String mdp = "motDePasse";
        handler.setMotDePasse(mdp);
        assertEquals(mdp, handler.getMotDePasse());  
    }
    
    @Test
    public void testGetPseudo()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getPseudo());
        
        // Test setter et getter
        String pseudo = "pseudo";
        handler.setPseudo(pseudo);
        assertEquals(pseudo, handler.getPseudo());  
    }
    
    @Test
    public void testGetNom()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getNom());
        
        // Test setter et getter
        String pseudo = "pseudo";
        handler.setNom(pseudo);
        assertEquals(pseudo, handler.getNom());  
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
