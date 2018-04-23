package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import model.Info;
import model.ModelFactory;
import utilities.Statics;

public class TestInfo
{
    /*---------- ATTRIBUTS ----------*/

    private Info info;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        info = ModelFactory.getModel(Info.class);
    }
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void controle()
    {
        // Test après initialisation
        assertFalse(info.controle());
        
        // Test mot de passe vide
        info.setMotDePasse("");
        assertFalse(info.controle());
        
        // Test pseudo vide
        info.setPseudo("");
        info.setMotDePasse(null);
        assertFalse(info.controle());
        
        // Test les deux vides
        info.setPseudo("");
        info.setMotDePasse("");
        assertFalse(info.controle());
        
        // Test mot de passe rempli
        info.setMotDePasse("pasvide");
        assertFalse(info.controle());
        
        // Test pseudo rempli
        info.setMotDePasse("");
        info.setPseudo("pasvide");
        assertFalse(info.controle());
        
        // Test les deux remplis
        info.setPseudo("pasvide");
        info.setMotDePasse("pasvide");
        assertTrue(info.controle());
    }
    
    @Test
    public void getFile()
    {
        File file = info.getFile();       
        assertEquals(new File(Statics.JARPATH + Whitebox.getInternalState(Info.class, "NOMFICHIER")), file);
    }
    
    @Test
    public void getRessource()
    {
        File resource = info.getResource();
        
        assertEquals(new File(getClass().getResource(Whitebox.getInternalState(Info.class, "RESOURCE")).getFile()), resource);
    }
    
    @Test
    public void controleDonnees()
    {
        // Test mdp null
        info.setPseudo("a");
        assertTrue(!info.controleDonnees().isEmpty());
        
        // Test pseudo null
        info.setPseudo(null);
        info.setMotDePasse("b");
        assertTrue(!info.controleDonnees().isEmpty());
        
        // test ok
        info.setPseudo("a");
        assertTrue(info.controleDonnees().isEmpty());
    }
    
    @Test
    public void getMotDePasse()
    {
        // test valeur vide ou nulle
        assertEquals("", info.getMotDePasse());
        
        // Test setter et getter
        String motDePasse = "motDePasse";
        info.setMotDePasse(motDePasse);
        assertEquals(motDePasse, info.getMotDePasse());  
    }
    
    @Test
    public void getPseudo()
    {
        // test valeur vide ou nulle
        assertEquals("", info.getPseudo());
        
        // Test setter et getter
        String pseudo = "pseudo";
        info.setPseudo(pseudo);
        assertEquals(pseudo, info.getPseudo());  
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
