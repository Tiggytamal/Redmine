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
        assertFalse(info.controle());
        
        // Test mot de passe vide
        info.setMotDePasse(EMPTY);
        assertFalse(info.controle());
        
        // Test pseudo vide
        info.setPseudo(EMPTY);
        info.setMotDePasse(null);
        assertFalse(info.controle());
        
        // Test les deux vides
        info.setPseudo(EMPTY);
        info.setMotDePasse(EMPTY);
        assertFalse(info.controle());
        
        // Test mot de passe rempli
        info.setMotDePasse(valeur);
        assertFalse(info.controle());
        
        // Test pseudo rempli
        info.setMotDePasse(EMPTY);
        info.setPseudo(valeur);
        assertFalse(info.controle());
        
        // Test les deux remplis
        info.setPseudo(valeur);
        info.setMotDePasse(valeur);
        assertTrue(info.controle());
    }
    
    @Test
    public void testGetFile()
    {
        File file = info.getFile();       
        assertEquals(new File(Statics.JARPATH + Whitebox.getInternalState(Info.class, "NOMFICHIER")), file);
    }
    
    @Test
    public void testGetRessource()
    {
        File resource = info.getResource();
        
        assertEquals(new File(getClass().getResource(Whitebox.getInternalState(Info.class, "RESOURCE")).getFile()), resource);
    }
    
    @Test
    public void testControleDonnees()
    {
        // Test mdp null
        info.setPseudo("a");
        assertFalse(info.controleDonnees().isEmpty());
        
        // Test pseudo null
        info.setPseudo(null);
        info.setMotDePasse("b");
        assertFalse(info.controleDonnees().isEmpty());
        
        // test ok
        info.setPseudo("a");
        assertEquals(0, info.controleDonnees().length());
    }
    
    @Test
    public void testGetMotDePasse()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, info.getMotDePasse());
        
        // Test setter et getter
        String mdp = "motDePasse";
        info.setMotDePasse(mdp);
        assertEquals(mdp, info.getMotDePasse());  
    }
    
    @Test
    public void testGetPseudo()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, info.getPseudo());
        
        // Test setter et getter
        String pseudo = "pseudo";
        info.setPseudo(pseudo);
        assertEquals(pseudo, info.getPseudo());  
    }
    
    @Test
    public void testGetNom()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, info.getNom());
        
        // Test setter et getter
        String pseudo = "pseudo";
        info.setNom(pseudo);
        assertEquals(pseudo, info.getNom());  
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
