package junit.control.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.sonar.SonarAPI;
import control.task.PurgeSonarTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;
import model.enums.Param;
import model.sonarapi.Parametre;
import model.sonarapi.Projet;
import utilities.Statics;
import utilities.TechnicalException;

@RunWith(JfxRunner.class)
public class TestPurgeSonarTask extends JunitBase
{
    private PurgeSonarTask handler;

    @Before
    public void init()
    {
        handler = new PurgeSonarTask();
    }
    
    @Test
    public void testPurgeVieuxComposants() throws Exception
    {
        // 1. Préparation du mock
        SonarAPI mock = Mockito.mock(SonarAPI.class);
        
        // init du webtarget
        WebTarget webTarget = ClientBuilder.newClient().target(Statics.proprietesXML.getMapParams().get(Param.URLSONAR));
        Whitebox.getField(SonarAPI.class, "webTarget").set(mock, webTarget);
        
        // Vrai appel getComposant
        Mockito.when(mock.getComposants()).thenCallRealMethod();
        
        // Vrai appel webservice
        Parametre param = new Parametre("search", "composant ");
        Mockito.when(mock.appelWebserviceGET(Mockito.anyString(), Mockito.refEq(param))).thenCallRealMethod();
        
        // Remplacement api par le mock
        Whitebox.getField(PurgeSonarTask.class, "api").set(handler, mock);
        
        // 2. Appel de la méthode
        assertTrue(Whitebox.invokeMethod(handler, "purgeVieuxComposants"));
    }

    @Test
    public void testCalculPurge() throws Exception
    {
        List<Projet> liste = Whitebox.invokeMethod(handler, "calculPurge");
        
    }


    
    @Test (expected = TechnicalException.class)
    public void testCompileMapException1() throws Exception
    {
        List<Projet> entree = null;
        Whitebox.invokeMethod(handler, "compileMap", entree);
    }
    
    @Test (expected = TechnicalException.class)
    public void testCompileMapException2() throws Exception
    {
        List<Projet> entree = new ArrayList<>();
        Whitebox.invokeMethod(handler, "compileMap", entree);
    }
    
    @Test
    public void testCompileMap() throws Exception
    {
        // Initialisation
        List<Projet> entree = new ArrayList<>();
        Projet projet = new Projet("id", "azerty01", "nom", "sc", "qu", "lot");
        Projet projet2 = new Projet("id", "azerty02", "nom", null, null, null);
        Projet projet3 = new Projet("id", "azerty01azeert03", "nom", null, null, null);
        Projet projet4 = new Projet("id", "azerty01azeert04", "nom", null, null, null);
        Projet projet5 = new Projet("id", "1234", null, null, null, null);
        entree.add(projet5);
        entree.add(projet4);
        entree.add(projet3);
        entree.add(projet2);
        entree.add(projet);   
        
        // Appel de la méthode
        Map<String, List<Projet>> map = Whitebox.invokeMethod(handler, "compileMap", entree);
        
        // Contrôle map
        assertFalse(map == null);
        assertFalse(map.isEmpty());
        assertTrue(map.size() == 2);
        
        // Contrôle des listes de la map
        List<Projet> liste1 = map.get("azerty");
        assertTrue(liste1 != null);
        assertFalse(liste1.isEmpty());
        assertTrue(liste1.size() == 2);
        assertTrue(liste1.contains(projet));
        assertTrue(liste1.contains(projet2));
        
        List<Projet> liste2 = map.get("azerty01azeert");
        assertTrue(liste2 != null);
        assertFalse(liste2.isEmpty());
        assertTrue(liste2.size() == 2);
        assertTrue(liste2.contains(projet3));
        assertTrue(liste2.contains(projet4));      
    }
}
