package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.task.MajComposantsSonarTask;
import dao.DaoFactory;
import de.saxsys.javafx.test.JfxRunner;
import model.ModelFactory;
import model.bdd.ComposantSonar;
import model.bdd.DefaultAppli;
import model.enums.OptionMajCompos;
import model.enums.TypeMetrique;
import model.sonarapi.Composant;
import model.sonarapi.Metrique;
import model.sonarapi.Periode;

@RunWith(JfxRunner.class)
public class TestMajComposantsSonarTask extends AbstractTestTask<MajComposantsSonarTask>
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final String UN = "1.0";
    
    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init() throws IllegalAccessException
    {
        handler = new MajComposantsSonarTask(OptionMajCompos.COMPLETE);
        initAPI(MajComposantsSonarTask.class, true);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
//    @Ignore(Statics.TESTMANUEL)
    public void testCall() throws Exception
    {
        // Réinitialisation de l'API
        initAPI(MajComposantsSonarTask.class, false);
        
        // Appel de la méthode
        assertTrue(invokeMethod(handler, "call"));
    }
    @Test
    public void testCreerListeComposants() throws Exception
    {        
        // Préparation des mocks
        mockAPIGetSomething(() -> api.getComposants());
        when(api.getVersionComposant(anyString())).thenReturn("1.00");
        
        // On retourne un composant cide, sauf le premier qui sera null
        when(api.getMetriquesComposant(anyString(), any(String[].class))).thenReturn(null).thenReturn(new Composant());
        
        // La méthode SecuriteComposant retournera qu'une seule fois 1 puis 0
        when(api.getSecuriteComposant(anyString())).thenReturn(1).thenReturn(0);
        
        // Appel de la méthode
        List<ComposantSonar> retour = invokeMethod(handler, "creerListeComposants");
      
        // Contrôle
        assertNotNull(retour);
        assertFalse(retour.isEmpty());
        assertEquals(api.getComposants().size()-1, retour.size());
        
        // Contrôle qu'un seul composant a une valeur de sécurité à "true"
        int i = 0;
        for (ComposantSonar compo : retour)
        {
            if (compo.isSecurite())
                i++;
        }
        assertEquals(1, i);
    }
    
    @Test
    public void testCheckVersion() throws Exception
    {
        // Préparation retour SNAPSHOT et RELEASE de l'appel api.
        Mockito.doReturn("SNAPSHOT").doReturn("RELEASE").when(api).getVersionComposant(anyString());

        // Test que la méthode renvoit bien true et false delon RELEASE ou SNAPSHOT
        assertFalse(invokeMethod(handler, "checkVersion", "key"));
        assertTrue(invokeMethod(handler, "checkVersion", "key"));
    }
    
    @Test
    public void testRecupLeakPeriod() throws Exception
    {
        // Initialisation objets - Liste plus deux périodes avec des index différents
        List<Periode> periodes = new ArrayList<>();
        Periode periode = new Periode();
        periode.setIndex(0);
        periode.setValeur(UN);
        periodes.add(periode);
        Periode periode2 = new Periode();
        periode2.setIndex(1);
        periode2.setValeur("2.0");
        periodes.add(periode2);

        // On récupère bine la valeur qui a l'index 2.0
        assertEquals(2.0F, (float) invokeMethod(handler, "recupLeakPeriod", periodes), 0.1F);

        // Test envoit d'un paramètre null
        assertEquals(0F, (float) invokeMethod(handler, "recupLeakPeriod", new Object[] { null }), 0.1F);
    }
    
    @Test
    public void testGestionDefaultsAppli() throws Exception
    {
        List<ComposantSonar> compos = DaoFactory.getDao(ComposantSonar.class).readAll();
        Map<String, DefaultAppli> mapDefAppli = DaoFactory.getDao(DefaultAppli.class).readAllMap();
        
        for (ComposantSonar compo : compos)
        {
            Whitebox.invokeMethod(handler, "gestionDefaultsAppli", compo, mapDefAppli);
        }
        
        Whitebox.invokeMethod(handler, "majDefAppli", mapDefAppli);
    }
    
    @Test
    public void testGetListPeriode() throws Exception
    {
        // Initialisaiton des variables
        // Metrique avec une liste des périodes non vide
        Composant compo = ModelFactory.getModel(Composant.class);
        List<Periode> periodes = new ArrayList<>();
        periodes.add(new Periode(1, UN));
        List<Metrique> metriques = new ArrayList<>();
        Metrique metrique = new Metrique();
        metrique.setMetric(TypeMetrique.APPLI);
        metrique.setListePeriodes(periodes);
        metriques.add(metrique);

        // Metrique avec une liste des périodes vide
        Metrique metrique2 = new Metrique();
        metrique2.setMetric(TypeMetrique.BUGS);
        metriques.add(metrique2);       
        compo.setMetriques(metriques);
        
        // Appel avec premier metrique
        List<Periode> retour = invokeMethod(handler, "getListPeriode", compo, TypeMetrique.APPLI);
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertEquals(UN, retour.get(0).getValeur());
        assertEquals(1, retour.get(0).getIndex());

        // Appel avec metrique avec liste période non initialisée
        retour = invokeMethod(handler, "getListPeriode", compo, TypeMetrique.BUGS);
        assertNotNull(retour);
        assertEquals(0, retour.size());

        // Appel avec metrique nulle
        retour = invokeMethod(handler, "getListPeriode", compo, TypeMetrique.BLOQUANT);
        assertNotNull(retour);
        assertEquals(0, retour.size());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
