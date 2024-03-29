package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.rtc.ControlRTC;
import control.task.PurgeSonarTask;
import control.word.ControlRapport;
import model.bdd.ComposantSonar;
import model.enums.ParamSpec;
import utilities.Statics;
import utilities.TechnicalException;

public class TestPurgeSonarTask extends AbstractTestTask<PurgeSonarTask>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String ID = "id";

    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init() throws IllegalAccessException
    {
        ControlRTC.INSTANCE.connexion();
        handler = new PurgeSonarTask();
        initAPI(PurgeSonarTask.class, true);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Ignore
    public void testPurgeVieuxComposants() throws Exception
    {
        mockAPIGetSomething(() -> api.getComposants());

        // 2. Appel de la m�thode depuis call
        assertTrue(Whitebox.invokeMethod(handler, "call"));
    }

    @Test
    @Ignore
    public void testCalculPurge() throws Exception
    {
        // 1. Remplacement map des composants pour test
        Map<String, ComposantSonar> mapCompos = new HashMap<>();

        // Remplissage de la liste
        List<String> listeVersion = Arrays.asList(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.VERSIONSVIEUXCOMPOS).split(";"));

        for (String string : listeVersion)
        {
            String key = "fr.ca.ts.composanta " + string;
            mapCompos.put(key, ComposantSonar.build(ID, key, "composanta " + string));
            key = "fr.ca.ts.composantb " + string;
            mapCompos.put(key, ComposantSonar.build(ID, key, "composantb " + string));
            key = "fr.ca.ts.composantc " + string;
            mapCompos.put(key, ComposantSonar.build(ID, key, "composantc " + string));
        }

        ComposantSonar end = ComposantSonar.build(ID, "fr.ca.ts.composanta 9999", "composanta 9999");
        mapCompos.put(end.getKey(), end);
        ComposantSonar a = ComposantSonar.build(ID, "fr.ca.ts.composanta 11", "composanta 11");
        mapCompos.put(a.getKey(), a);
        ComposantSonar b = ComposantSonar.build(ID, "fr.ca.ts.composanta 10", "composanta 10");
        mapCompos.put(b.getKey(), b);
        ComposantSonar c = ComposantSonar.build(ID, "fr.ca.ts.composantb 12", "composantb 12");
        mapCompos.put(c.getKey(), c);
        ComposantSonar d = ComposantSonar.build(ID, "fr.ca.ts.composantb 11", "composantb 11");
        mapCompos.put(d.getKey(), d);
        ComposantSonar e = ComposantSonar.build(ID, "fr.ca.ts.composantc 09", "composantc 09");
        mapCompos.put(e.getKey(), e);
        ComposantSonar f = ComposantSonar.build(ID, "fr.ca.ts.composantd 10", "composantd 10");
        mapCompos.put(f.getKey(), f);
        ComposantSonar g = ComposantSonar.build(ID, "fr.ca.ts.composante 13", "composante 13");
        mapCompos.put(g.getKey(), g);



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
        Whitebox.getField(ControlRapport.class, "extra").get(Whitebox.getField(PurgeSonarTask.class, "controlRapport").get(handler));

    }

    @Test
    @Ignore
    public void testCompileMapException()
    {
        
        // Appel methode dans un try catch pour r�cup�rer l'exception et pouvoir reinitialiser la map des composants
        try
        {
            Whitebox.invokeMethod(handler, "compileMap");
        } catch (Exception e)
        {
            assertTrue(e instanceof TechnicalException);
            assertEquals("Attention la liste des composants est vide - control.task.PurgeSonarTask.compileMap", e.getMessage());
        }

    }

    @Test
    @Ignore
    public void testCompileMap() throws Exception
    {
        // 1. Initialisation
        String nom = "nom";

        // Changement donn�es de la liste statique
        Map<String, ComposantSonar> mapCompos = new HashMap<>();
        ComposantSonar a = ComposantSonar.build(ID, "azerty01", nom);
        mapCompos.put(a.getKey(), a);
        ComposantSonar b = ComposantSonar.build(ID, "azerty02", nom);
        mapCompos.put(b.getKey(), b);
        ComposantSonar c = ComposantSonar.build(ID, "azerty01azeert03", nom);
        mapCompos.put(c.getKey(), c);
        ComposantSonar d = ComposantSonar.build(ID, "azerty01azeert04", nom);
        mapCompos.put(d.getKey(), d);
        ComposantSonar e = ComposantSonar.build(ID, "1234", nom);
        mapCompos.put(e.getKey(), e);

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
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}