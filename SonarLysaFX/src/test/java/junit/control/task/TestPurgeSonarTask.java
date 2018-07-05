package junit.control.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.mail.ControlMail;
import control.rtc.ControlRTC;
import control.sonar.SonarAPI;
import control.task.PurgeSonarTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;
import model.enums.Param;
import model.enums.ParamSpec;
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
        ControlRTC.INSTANCE.connexion();
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
        String id = "id";
        
        // 1. Préparation du mock
        SonarAPI mock = Mockito.mock(SonarAPI.class);
        
        // init du webtarget
        WebTarget webTarget = ClientBuilder.newClient().target(Statics.proprietesXML.getMapParams().get(Param.URLSONAR));
        Whitebox.getField(SonarAPI.class, "webTarget").set(mock, webTarget);
        
        // Vrai appel webservice
        Parametre param = new Parametre("search", "composant ");
        Mockito.when(mock.appelWebserviceGET(Mockito.anyString(), Mockito.refEq(param))).thenCallRealMethod();
        
        // Préparation appel getComposant.
        List<Projet> retourMock = new ArrayList<>();
        List<String> listeVersion = Arrays.asList(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.VERSIONSCOMPOSANTS).split(";"));
        
        for (String string : listeVersion)
        {
            retourMock.add(new Projet(id, "fr.ca.ts.composanta " + string, "composanta " + string, null, null, null));
            retourMock.add(new Projet(id, "fr.ca.ts.composantb " + string, "composantb " + string, null, null, null));
            retourMock.add(new Projet(id, "fr.ca.ts.composantc " + string, "composantc " + string, null, null, null));
        }
        
        Projet end = new Projet(id, "fr.ca.ts.composanta 9999", "composanta 9999", null, null, null);
        retourMock.add(end);       
        Projet a = new Projet(id, "fr.ca.ts.composanta 11", "composanta 11", null, null, null);
        retourMock.add(a);
        Projet b = new Projet(id, "fr.ca.ts.composanta 10", "composanta 10", null, null, null);
        retourMock.add(b);
        Projet c = new Projet(id, "fr.ca.ts.composantb 12", "composantb 12", null, null, null);
        retourMock.add(c);
        Projet d = new Projet(id, "fr.ca.ts.composantb 11", "composantb 11", null, null, null);
        retourMock.add(d);
        Projet e = new Projet(id, "fr.ca.ts.composantc 09", "composantc 9", null, null, null);
        retourMock.add(e);
        Projet f = new Projet(id, "fr.ca.ts.composantd 10", "composantd 10", null, null, null);
        retourMock.add(f);
        Projet g = new Projet(id, "fr.ca.ts.composante 13", "composante 13", null, null, null);
        retourMock.add(g);
        Collections.sort(retourMock, (p1,p2) -> p1.getNom().compareTo(p2.getNom()));
        
        Mockito.when(mock.getComposants()).thenReturn(retourMock);
        
        // Remplacement api par le mock
        Whitebox.getField(PurgeSonarTask.class, "api").set(handler, mock);
        
        // 2. Appel de la méthode        
        List<Projet> liste = Whitebox.invokeMethod(handler, "calculPurge");
        
        // 3. Contrôle des données
        assertTrue(liste.size() == 5);
        assertTrue(liste.contains(a));
        assertTrue(liste.contains(b));
        assertTrue(liste.contains(c));
        assertTrue(liste.contains(d));
        assertTrue(liste.contains(e));  
        
        // Test de la zone extra du controlMail.
        Whitebox.getField(ControlMail.class, "extra").get(Whitebox.getField(PurgeSonarTask.class, "controlMail").get(handler))
        .equals("Composants uniques : 5\nComposants solo : 2\nTotal composants sonar : 14\nSuppressions : 5\n");
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