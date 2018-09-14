package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.task.CreerListeComposantsTask;
import de.saxsys.javafx.test.JfxRunner;
import model.ComposantSonar;
import model.ModelFactory;
import model.enums.TypeMetrique;
import model.sonarapi.Composant;
import model.sonarapi.Metrique;
import model.sonarapi.Periode;

@RunWith(JfxRunner.class)
public class TestCreerListeComposantsTask extends AbstractTestTask<CreerListeComposantsTask>
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final String UN = "1.0";
    
    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init() throws IllegalAccessException
    {
        handler = new CreerListeComposantsTask();
        initAPI(CreerListeComposantsTask.class, true);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testCreerListeComposants() throws Exception
    {        
        // Pr�paration des mocks
        mockAPIGetSomething(() -> api.getComposants());
        Mockito.when(api.getMetriquesComposant(Mockito.anyString(), Mockito.any(String[].class))).thenReturn(new Composant());
        
        // Appel de la m�thode
        Map<String, ComposantSonar> retour = Whitebox.invokeMethod(handler, "creerListeComposants");
        
        // Contr�le
        assertNotNull(retour);
        assertFalse(retour.isEmpty());
        assertEquals(api.getComposants().size(), retour.size());
    }
    
    @Test
    public void testCheckVersion() throws Exception
    {
        // Pr�paration retour SNAPSHOT et RELEASE de l'appel api.
        Mockito.doReturn("SNAPSHOT").doReturn("RELEASE").when(api).getVersionComposant(Mockito.anyString());

        // Test que la m�thode renvoit bien true et false delon RELEASE ou SNAPSHOT
        assertFalse(Whitebox.invokeMethod(handler, "checkVersion", "key"));
        assertTrue(Whitebox.invokeMethod(handler, "checkVersion", "key"));
    }
    
    @Test
    public void testRecupLeakPeriod() throws Exception
    {
        // Initialisation objets - Liste plus deux p�riodes avec des index diff�rents
        List<Periode> periodes = new ArrayList<>();
        Periode periode = new Periode();
        periode.setIndex(0);
        periode.setValeur(UN);
        periodes.add(periode);
        Periode periode2 = new Periode();
        periode2.setIndex(1);
        periode2.setValeur("2.0");
        periodes.add(periode2);

        // On r�cup�re bine la valeur qui a l'index 2.0
        assertEquals(2.0F, (float) Whitebox.invokeMethod(handler, "recupLeakPeriod", periodes), 0.1F);

        // Test envoit d'un param�tre null
        assertEquals(0F, (float) Whitebox.invokeMethod(handler, "recupLeakPeriod", new Object[] { null }), 0.1F);
    }
    
    @Test
    public void testGetListPeriode() throws Exception
    {
        // Initialisaiton des variables
        // Metrique avec une liste des p�riodes non vide
        Composant compo = ModelFactory.getModel(Composant.class);
        List<Periode> periodes = new ArrayList<>();
        periodes.add(new Periode(1, UN));
        List<Metrique> metriques = new ArrayList<>();
        Metrique metrique = new Metrique();
        metrique.setMetric(TypeMetrique.APPLI);
        metrique.setListePeriodes(periodes);
        metriques.add(metrique);

        // Metrique avec une liste des p�riodes vide
        Metrique metrique2 = new Metrique();
        metrique2.setMetric(TypeMetrique.BUGS);
        metriques.add(metrique2);       
        compo.setMetriques(metriques);
        
        // Appel avec premier metrique
        List<Periode> retour = Whitebox.invokeMethod(handler, "getListPeriode", compo, TypeMetrique.APPLI);
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertEquals(UN, retour.get(0).getValeur());
        assertEquals(1, retour.get(0).getIndex());

        // Appel avec metrique avec liste p�riode non initialis�e
        retour = Whitebox.invokeMethod(handler, "getListPeriode", compo, TypeMetrique.BUGS);
        assertNotNull(retour);
        assertEquals(0, retour.size());

        // Appel avec metrique nulle
        retour = Whitebox.invokeMethod(handler, "getListPeriode", compo, TypeMetrique.BLOQUANT);
        assertNotNull(retour);
        assertEquals(0, retour.size());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
