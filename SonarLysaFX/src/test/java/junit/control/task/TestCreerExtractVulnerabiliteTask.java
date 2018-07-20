package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import control.task.CreerExtractVulnerabiliteTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;
import model.ComposantSonar;
import model.ModelFactory;
import model.Vulnerabilite;
import model.enums.TypeVulnerabilite;
import model.sonarapi.Issue;
import utilities.Statics;

@RunWith(JfxRunner.class)
public class TestCreerExtractVulnerabiliteTask extends JunitBase 
{
    /*---------- ATTRIBUTS ----------*/

    private CreerExtractVulnerabiliteTask handler;

    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init()
    {
        handler = new CreerExtractVulnerabiliteTask(new File("d:\\testExtract.xlsx")); 
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCreerExtract() throws Exception
    {
        Whitebox.invokeMethod(handler, "creerExtract");
    }
    
    @Test
    public void testConvertIssueToVul() throws Exception
    {
        Issue issue = new Issue();
        issue.setStatus("status");
        issue.setCreationDate("date");
        issue.setSeverity("severity");
        issue.setMessage("javajaf.jar");
        ComposantSonar composant = ModelFactory.getModel(ComposantSonar.class);
        composant.setNom("nom");      
        composant.setAppli("appli");
        composant.setLot("123456");
        Vulnerabilite retour = Whitebox.invokeMethod(handler, "convertIssueToVul", issue, composant);
        
        assertEquals("appli", retour.getAppli());
        assertEquals("123456", retour.getLot());
        assertEquals("nom", retour.getComposant());
        assertEquals("status", retour.getStatus());
        assertEquals("severity", retour.getSeverite());
        assertEquals("javajaf.jar", retour.getMessage());
        
    }
    
    @Test
    public void testRecupVulnerabilitesSonar() throws Exception
    {
        List<String> nomsComposPatrimoine = new ArrayList<>();
        for (ComposantSonar compo : Statics.fichiersXML.getListComposants())
        {
            nomsComposPatrimoine.add(compo.getNom());
        }
        
        List<Vulnerabilite> result = Whitebox.invokeMethod(handler, "recupVulnerabilitesSonar", TypeVulnerabilite.OUVERTE, nomsComposPatrimoine);
        for (Vulnerabilite vulnerabilite : result)
        {
            assertFalse(vulnerabilite.getStatus().equals("RESOLVED") || vulnerabilite.getStatus().equals("CLOSED"));
        }
        
        result = Whitebox.invokeMethod(handler, "recupVulnerabilitesSonar", TypeVulnerabilite.RESOLUE, nomsComposPatrimoine);
        for (Vulnerabilite vulnerabilite : result)
        {
            assertTrue(vulnerabilite.getStatus().equals("RESOLVED") || vulnerabilite.getStatus().equals("CLOSED"));
        }
    }
    
    @Test
    public void testExtractLib() throws Exception
    {
        String jar = "boudu.jar";
        assertEquals(jar, Whitebox.invokeMethod(handler, "extractLib", "Filename: boudu.jar"));
        assertEquals(jar, Whitebox.invokeMethod(handler, "extractLib", "Filename: boudu.jar | autre texte"));
        assertEquals(jar, Whitebox.invokeMethod(handler, "extractLib", "Filename: boudu.jar/autretexte"));
        assertEquals(jar, Whitebox.invokeMethod(handler, "extractLib", "boudu.jar"));
        assertEquals(jar, Whitebox.invokeMethod(handler, "extractLib", "boudu.jar/autretexte"));
        assertEquals(jar, Whitebox.invokeMethod(handler, "extractLib", "boudu.jar | autre texte"));
    }
    
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
