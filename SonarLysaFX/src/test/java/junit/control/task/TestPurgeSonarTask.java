package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
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
import model.ComposantSonar;
import model.ModelFactory;
import model.enums.Param;
import model.enums.ParamSpec;
import model.sonarapi.Parametre;
import utilities.Statics;
import utilities.TechnicalException;

@RunWith(JfxRunner.class)
public class TestPurgeSonarTask extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private PurgeSonarTask handler;
    private static final String ID = "id";
    
    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init()
    {
        ControlRTC.INSTANCE.connexion();
        handler = new PurgeSonarTask();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

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
        
        // 1. Remplacement map des composants pour test
        Map<String, ComposantSonar> mapCompos = new HashMap<>();

        // Remplissage de la liste
        List<String> listeVersion = Arrays.asList(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.VERSIONSCOMPOSANTS).split(";"));
                
        for (String string : listeVersion)
        {
            String key = "fr.ca.ts.composanta " + string;
            mapCompos.put(key, ModelFactory.getModelWithParams(ComposantSonar.class, ID, key, "composanta " + string));
            key = "fr.ca.ts.composantb " + string;
            mapCompos.put(key, ModelFactory.getModelWithParams(ComposantSonar.class, ID, key, "composantb " + string));
            key = "fr.ca.ts.composantc " + string;
            mapCompos.put(key, ModelFactory.getModelWithParams(ComposantSonar.class, ID, key, "composantc " + string));
        }
        
        ComposantSonar end = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "fr.ca.ts.composanta 9999", "composanta 9999");
        mapCompos.put(end.getKey(), end);       
        ComposantSonar a = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "fr.ca.ts.composanta 11", "composanta 11");
        mapCompos.put(a.getKey(), a);  
        ComposantSonar b = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "fr.ca.ts.composanta 10", "composanta 10");
        mapCompos.put(b.getKey(), b);  
        ComposantSonar c = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "fr.ca.ts.composantb 12", "composantb 12");
        mapCompos.put(c.getKey(), c);  
        ComposantSonar d = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "fr.ca.ts.composantb 11", "composantb 11");
        mapCompos.put(d.getKey(), d);  
        ComposantSonar e = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "fr.ca.ts.composantc 09", "composantc 09");
        mapCompos.put(e.getKey(), e);  
        ComposantSonar f = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "fr.ca.ts.composantd 10", "composantd 10");
        mapCompos.put(f.getKey(), f);  
        ComposantSonar g = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "fr.ca.ts.composante 13", "composante 13");
        mapCompos.put(g.getKey(), g);  
        
        // Changement données de la liste statique
        Statics.fichiersXML.getMapComposSonar().clear();
        Statics.fichiersXML.getMapComposSonar().putAll(mapCompos);
        
        // 2. Appel de la méthode        
        List<ComposantSonar> liste = Whitebox.invokeMethod(handler, "calculPurge");
        
        // 3. Contrôle des données
        assertEquals(5, liste.size());
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
        Statics.fichiersXML.getMapComposSonar().clear();
        Whitebox.invokeMethod(handler, "compileMap");
    }
    
    @Test
    public void testCompileMap() throws Exception
    {
        // 1. Initialisation
        String nom = "nom";

        
        // Changement données de la liste statique
        Map<String, ComposantSonar> mapCompos = new HashMap<>();
        ComposantSonar a = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "azerty01", nom);
        mapCompos.put(a.getKey(), a);
        ComposantSonar b = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "azerty02", nom);
        mapCompos.put(b.getKey(), b);
        ComposantSonar c = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "azerty01azeert03", nom);
        mapCompos.put(c.getKey(), c);
        ComposantSonar d = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "azerty01azeert04", nom);
        mapCompos.put(d.getKey(), d);
        ComposantSonar e = ModelFactory.getModelWithParams(ComposantSonar.class, ID, "1234", nom);
        mapCompos.put(e.getKey(), e);  
        Statics.fichiersXML.getMapComposSonar().clear();
        Statics.fichiersXML.getMapComposSonar().putAll(mapCompos);
        
        // 2. Appel de la méthode
        Map<String, List<ComposantSonar>> map = Whitebox.invokeMethod(handler, "compileMap");
        
        // 3. Contrôle map
        assertFalse(map == null);
        assertFalse(map.isEmpty());
        assertTrue(map.size() == 2);
        
        // 4. Contrôle des listes de la map
        List<ComposantSonar> liste1 = map.get("azerty");
        assertTrue(liste1 != null);
        assertFalse(liste1.isEmpty());
        assertTrue(liste1.size() == 2);
        assertTrue(liste1.contains(a));
        assertTrue(liste1.contains(b));
        
        List<ComposantSonar> liste2 = map.get("azerty01azeert");
        assertTrue(liste2 != null);
        assertFalse(liste2.isEmpty());
        assertTrue(liste2.size() == 2);
        assertTrue(liste2.contains(c));
        assertTrue(liste2.contains(d));      
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}