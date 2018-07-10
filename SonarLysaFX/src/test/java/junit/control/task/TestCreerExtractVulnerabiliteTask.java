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
import model.Vulnerabilite;
import model.enums.TypeMetrique;
import model.enums.TypeVulnerabilite;
import model.sonarapi.Composant;
import model.sonarapi.Issue;
import model.sonarapi.Metrique;

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
        Composant composant = new Composant();
        composant.setNom("nom");
        List<Metrique> metriques = new ArrayList<>();
        metriques.add(new Metrique(TypeMetrique.LOT, "lot"));
        metriques.add(new Metrique(TypeMetrique.APPLI, "appli"));       
        composant.setMetriques(metriques);
        Vulnerabilite retour = Whitebox.invokeMethod(handler, "convertIssueToVul", issue, composant);
        
    }
    
    @Test
    public void testRecupVulnerabilitesSonar() throws Exception
    {
        List<Vulnerabilite> result = Whitebox.invokeMethod(handler, "recupVulnerabilitesSonar", TypeVulnerabilite.OUVERTE);
        for (Vulnerabilite vulnerabilite : result)
        {
            assertFalse(vulnerabilite.getStatus().equals("RESOLVED") || vulnerabilite.getStatus().equals("CLOSED"));
        }
        
        result = Whitebox.invokeMethod(handler, "recupVulnerabilitesSonar", TypeVulnerabilite.RESOLUE);
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
