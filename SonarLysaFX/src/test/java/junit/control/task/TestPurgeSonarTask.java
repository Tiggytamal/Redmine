package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
    private Map<String, ComposantSonar> save;

    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init()
    {
        ControlRTC.INSTANCE.connexion();
        handler = new PurgeSonarTask();
        save = new HashMap<>();
        save.putAll(Statics.fichiersXML.getMapComposSonar());
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testPurgeVieuxComposants() throws Exception
    {
        // 1. Pr�paration du mock
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

        // 2. Appel de la m�thode
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

        // Changement donn�es de la liste statique
        Statics.fichiersXML.getMapComposSonar().clear();
        Statics.fichiersXML.getMapComposSonar().putAll(mapCompos);

        // 2. Appel de la m�thode
        List<ComposantSonar> liste = Whitebox.invokeMethod(handler, "calculPurge");

        // 3. Contr�le des donn�es
        assertEquals(5, liste.size());
        assertTrue(liste.contains(a));
        assertTrue(liste.contains(b));
        assertTrue(liste.contains(c));
        assertTrue(liste.contains(d));
        assertTrue(liste.contains(e));

        // Test de la zone extra du controlMail.
        Whitebox.getField(ControlMail.class, "extra").get(Whitebox.getField(PurgeSonarTask.class, "controlMail").get(handler))
                .equals("Composants uniques : 5\nComposants solo : 2\nTotal composants sonar : 14\nSuppressions : 5\n");

        // Reinstanciation de la map des composants
        Statics.fichiersXML.getMapComposSonar().clear();
        Statics.fichiersXML.getMapComposSonar().putAll(save);
    }

    @Test
    public void testCompileMapException()
    {
        // Initialisation
        Statics.fichiersXML.getMapComposSonar().clear();
        
        // Appel methode dans un try catch pour r�cup�rer l'exception et pouvoir reinitialiser la map des composants
        try
        {
            Whitebox.invokeMethod(handler, "compileMap");
        } catch (Exception e)
        {
            assertTrue(e instanceof TechnicalException);
            assertEquals("Attention la liste des composants est vide - control.task.PurgeSonarTask.compileMap", e.getMessage());
        } finally
        {
            Statics.fichiersXML.getMapComposSonar().putAll(save);
        }
    }

    @Test
    public void testCompileMap() throws Exception
    {
        // 1. Initialisation
        String nom = "nom";

        // Changement donn�es de la liste statique
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

        // 2. Appel de la m�thode
        Map<String, List<ComposantSonar>> map = Whitebox.invokeMethod(handler, "compileMap");

        // 3. Contr�le map
        assertNotNull(map);
        assertFalse(map.isEmpty());
        assertEquals(2, map.size());

        // 4. Contr�le des listes de la map
        List<ComposantSonar> liste1 = map.get("azerty");
        assertNotNull(liste1);
        assertFalse(liste1.isEmpty());
        assertEquals(2, liste1.size());
        assertTrue(liste1.contains(a));
        assertTrue(liste1.contains(b));

        List<ComposantSonar> liste2 = map.get("azerty01azeert");
        assertNotNull(liste2);
        assertFalse(liste2.isEmpty());
        assertEquals(2, liste2.size());
        assertTrue(liste2.contains(c));
        assertTrue(liste2.contains(d));

        // Reinstanciation de la map des composants
        Statics.fichiersXML.getMapComposSonar().clear();
        Statics.fichiersXML.getMapComposSonar().putAll(save);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}