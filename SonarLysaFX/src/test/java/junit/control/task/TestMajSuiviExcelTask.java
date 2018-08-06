package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.rtc.ControlRTC;
import control.sonar.SonarAPI;
import control.task.MajSuiviExcelTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;
import model.Anomalie;
import model.ComposantSonar;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.enums.Param;
import model.enums.TypeMajSuivi;
import model.enums.TypeMetrique;
import model.sonarapi.Metrique;
import model.sonarapi.Periode;
import model.sonarapi.QualityGate;
import utilities.Statics;

@RunWith(JfxRunner.class)
public class TestMajSuiviExcelTask extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private MajSuiviExcelTask handler;
    private ControlRTC control = ControlRTC.INSTANCE;
    private SonarAPI api;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init() throws IllegalArgumentException, IllegalAccessException
    {
        handler = new MajSuiviExcelTask(TypeMajSuivi.MULTI);
        control.connexion();
        api = Mockito.mock(SonarAPI.class);
        Whitebox.getField(MajSuiviExcelTask.class, "api").set(handler, api);
        
        // init du webtarget
        WebTarget webTarget = ClientBuilder.newClient().target(Statics.proprietesXML.getMapParams().get(Param.URLSONAR));
        Whitebox.getField(SonarAPI.class, "webTarget").set(api, webTarget);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    @Ignore ("pas encore prêt")
    public void testMajSuiviExcel() throws Exception
    {
        Whitebox.invokeMethod(handler, "majSuiviExcel");
    }
    
    @Test
    public void testMajFichierRTC()
    {
        
    }
    
    @Test
    public void testTraitementSuiviExcelToutFichiers()
    {
        
    }
    
    @Test 
    public void testMajFichierSuiviExcelDataStage()
    {
        
    }
    
    @Test
    public void testMajFichierSuiviExcelCOBOL()
    {
        
    }
    
    @Test
    public void testMajFichierSuiviExcelJAVA()
    {
        
    }
    
    @Test
    public void testTraitementFichierSuivi()
    {
        
    }
    
    @Test
    public void testLotSonarQGError()
    {
        
    }
    
    @Test
    public void testMajFichierAnomalies()
    {
        
    }
    
    @Test
    public void testTraitementProjet()
    {
        
    }
    
    @Test
    public void testControleQGMetriques() throws Exception
    {
        // Paramètred e base de la méthode
        Map<TypeMetrique, Metrique> metriques = new HashMap<>();
        
        // Test avec QG vide
        creerMetrique(metriques, TypeMetrique.QG, "");
        assertFalse(Whitebox.invokeMethod(handler, "controleQGMetriques", metriques));
        
        // Test avec QG pas en erreur
        creerMetrique(metriques, TypeMetrique.QG, "OK");
        assertFalse(Whitebox.invokeMethod(handler, "controleQGMetriques", metriques));
        
        // Test avec QG en erreur mais sans erreurs des métriques
        creerMetrique(metriques, TypeMetrique.QG, "ERROR");
        assertFalse(Whitebox.invokeMethod(handler, "controleQGMetriques", metriques));
        
        // Test avec QG en erreur mais avec erreurs bloquantes
        creerMetrique(metriques, TypeMetrique.BLOQUANT, "1.0");
        assertTrue(Whitebox.invokeMethod(handler, "controleQGMetriques", metriques));
        
        // Test avec QG en erreur mais avec erreurs critiques
        creerMetrique(metriques, TypeMetrique.CRITIQUE, "1.0");
        assertTrue(Whitebox.invokeMethod(handler, "controleQGMetriques", metriques));
        
        // Test avec QG en erreur mais aavec duplication de code
        creerMetrique(metriques, TypeMetrique.DUPLICATION, "5.0");
        assertTrue(Whitebox.invokeMethod(handler, "controleQGMetriques", metriques));
    }
    
    @Test
    public void testGetListPeriode() throws Exception
    {
        // Initialisaiton des variables
        // Metrique avec une liste des périodes non vide
        List<Periode> periodes = new ArrayList<>();
        periodes.add(new Periode(1, "1.0"));
        Map<TypeMetrique, Metrique> metriques = new HashMap<>();
        Metrique metrique = new Metrique();
        metrique.setMetric(TypeMetrique.APPLI);
        metrique.setListePeriodes(periodes);
        metriques.put(TypeMetrique.APPLI, metrique);
        
        // Metrique avec une liste des périodes vide
        Metrique metrique2 = new Metrique();
        metrique2.setMetric(TypeMetrique.BUGS);
        metriques.put(TypeMetrique.BUGS, metrique2);

        // Appel avec premier metrique
        List<Periode> retour = Whitebox.invokeMethod(handler, "getListPeriode", metriques, TypeMetrique.APPLI);
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertEquals("1.0", retour.get(0).getValeur());
        assertEquals(1, retour.get(0).getIndex());
        
        // Appel avec metrique avec liste période non initialisée
        retour = Whitebox.invokeMethod(handler, "getListPeriode", metriques, TypeMetrique.BUGS);
        assertNotNull(retour);
        assertEquals(0, retour.size());
        
        // Appel avec metrique nulle
        retour = Whitebox.invokeMethod(handler, "getListPeriode", metriques, TypeMetrique.BLOQUANT);
        assertNotNull(retour);
        assertEquals(0, retour.size());
    }
    
    @Test
    public void testRecupLeakPeriod() throws Exception
    {
        // Initialisation objets - Liste plus deux périodes avec des index différents
        List<Periode> periodes = new ArrayList<>();
        Periode periode = new Periode();
        periode.setIndex(0);
        periode.setValeur("1.0");
        periodes.add(periode);
        Periode periode2 = new Periode();
        periode2.setIndex(1);
        periode2.setValeur("2.0");
        periodes.add(periode2);
        
        // ONrécupère bine la valeur qui a l'index 2.0
        assertEquals(2.0F, (float)Whitebox.invokeMethod(handler, "recupLeakPeriod", periodes), 0.1F);
    }
    
    @Test
    public void testCreationNumerosLots() throws Exception
    {
        List<Anomalie> listeLotenAno = new ArrayList<>();
        Map<String, LotSuiviRTC> lotsRTC = new HashMap<>();
        String lotNumero = "123456";
        String lotNumero2 = "654321";
        
        // Ajout objets
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot(lotNumero);
        listeLotenAno.add(ano);
        
        Anomalie ano2 = ModelFactory.getModel(Anomalie.class);
        ano2.setLot(lotNumero2);
        listeLotenAno.add(ano2);
        
        LotSuiviRTC lotRTC = ModelFactory.getModel(LotSuiviRTC.class);
        lotRTC.setLot(lotNumero);
        lotRTC.setCpiProjet("cpi");
        lotRTC.setEdition("edition");
        lotsRTC.put(lotRTC.getLot(), lotRTC);
        
        // Appel méthode
        Map<String, Anomalie> retour = Whitebox.invokeMethod(handler, "creationNumerosLots", listeLotenAno, lotsRTC);
        
        // Contrôles
        // Vérification taille de la liste de retour. Il doit y avoir les deux anos.
        assertEquals(2, retour.size());
        assertNotNull(retour.get(lotNumero));
        
        // Test que la première anomalie, on a bien mis à jour les informatiosn provenant de RTC
        assertEquals("cpi", ano.getCpiProjet());
        assertEquals("edition", ano.getEdition());
        
        // Vérification de la deuxième anomalienon mise à jour
        assertNotNull(retour.get(lotNumero2));
        assertEquals("", ano2.getCpiProjet());
        assertEquals("", ano2.getEdition());
    }
    
    @Test
    public void testRelease() throws Exception
    {
        // Préparation retour SNAPSHOT et RELEASE de l'appel api.
        Mockito.doReturn("SNAPSHOT").doReturn("RELEASE").when(api).getVersionComposant(Mockito.anyString());
        
        // Test que la méthode renvoit bien true et false delon RELEASE ou SNAPSHOT
        assertFalse(Whitebox.invokeMethod(handler, "release", "key"));
        assertTrue(Whitebox.invokeMethod(handler, "release", "key"));
    }
    
    @Test
    public void testLiensQG() throws Exception
    {
        // Récupération du nom de la QualityGate DataStage
        String nomQG = Statics.proprietesXML.getMapParams().get(Param.NOMQGDATASTAGE);
        
        // Préparation retour QualityGate
        Mockito.when(api.getQualityGate(Mockito.anyString())).thenCallRealMethod();
        Mockito.when(api.getListQualitygate()).thenCallRealMethod();
        Mockito.when(api.appelWebserviceGET(Mockito.anyString())).thenCallRealMethod();
        
        // Initialisation paramètre méthode
        ComposantSonar compo = ModelFactory.getModel(ComposantSonar.class);
        List<ComposantSonar> liste = new ArrayList<>();
        Collection<List<ComposantSonar>> collec = new ArrayList<>();
        liste.add(compo);
        collec.add(liste);
        
        // Appel de la méthode
        Whitebox.invokeMethod(handler, "liensQG", collec, nomQG);
        
        // Vérification de l'appel à associerQualityGate
        Mockito.verify(api, Mockito.times(1)).associerQualitygate(Mockito.eq(compo), Mockito.any(QualityGate.class));
        
        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    private void creerMetrique(Map<TypeMetrique, Metrique> metriques, TypeMetrique type, String valeur)
    {
        Metrique metrique = new Metrique(type, valeur);
        List<Periode> periodes = new ArrayList<>();
        periodes.add(new Periode(1, valeur));
        metrique.setListePeriodes(periodes);
        metriques.put(type, metrique);        
    }
    /*---------- ACCESSEURS ----------*/

}
