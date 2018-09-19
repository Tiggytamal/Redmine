package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.excel.ControlExtractVul;
import control.task.CreerExtractVulnerabiliteTask;
import dao.DaoComposantSonar;
import model.ComposantSonar;
import model.ModelFactory;
import model.Vulnerabilite;
import model.enums.TypeVulnerabilite;
import model.sonarapi.Issue;
import utilities.Statics;

public class TestCreerExtractVulnerabiliteTask extends AbstractTestTask<CreerExtractVulnerabiliteTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init()
    {
        handler = new CreerExtractVulnerabiliteTask(new File(Statics.RESSTEST + "testExtract.xlsx"));
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCreerExtract() throws Exception
    {   
        // Appel de la méthode call qui ne sert qu'à appeler la méthode privée.
        // Controle du bon retour à true. Mock du controleur pour éviter écriture du fichier.
        ControlExtractVul control = Mockito.mock(ControlExtractVul.class);
        Mockito.when(control.write()).thenReturn(true);
        Whitebox.getField(CreerExtractVulnerabiliteTask.class, "control").set(handler, control);
        assertTrue(Whitebox.invokeMethod(handler, "call"));
    }
    
    @Test
    public void testConvertIssueToVul() throws Exception
    {
        // Initialisation
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
        
        // Controleur que les valeurs sont bonnes après conversion
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
        
        // Compteur pour limiter la taille de la liste et le temps de traitement.
        int i = 0;
        for (ComposantSonar compo : new DaoComposantSonar().readAll())
        {
            nomsComposPatrimoine.add(compo.getNom());
            if (++i == 20)
                break;
        }
        
        // Test que la liste des vulnérabilités ouvertes ne contient pas de résolue ou close.
        List<Vulnerabilite> result = Whitebox.invokeMethod(handler, "recupVulnerabilitesSonar", TypeVulnerabilite.OUVERTE, nomsComposPatrimoine);
        for (Vulnerabilite vulnerabilite : result)
        {
            assertFalse(vulnerabilite.getStatus().equals("RESOLVED") || vulnerabilite.getStatus().equals("CLOSED"));
        }
        
        // Test que la liste des vulnérabilités résolues ne contient que des résolues ou closes.
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
