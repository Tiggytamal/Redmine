package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.EMPTY;
import static utilities.Statics.proprietesXML;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.excel.ControlSuivi;
import control.rtc.ControlRTC;
import control.task.MajSuiviExcelTask;
import model.Anomalie;
import model.ComposantSonar;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.QG;
import model.enums.TypeMajSuivi;
import model.sonarapi.QualityGate;
import utilities.Statics;

public class TestMajSuiviExcelTask extends AbstractTestTask<MajSuiviExcelTask>
{
    /*---------- ATTRIBUTS ----------*/

    private ControlRTC control = ControlRTC.INSTANCE;
    private static final String NUMEROLOT1 = "123456";

    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init() throws Exception
    {
        // Cr�ation handler
        handler = new MajSuiviExcelTask(TypeMajSuivi.MULTI);
        
        // spy pour annuler la m�thode write et �viter de modifier les fichiers de test
        handler = Mockito.spy(handler);        
        Mockito.doReturn(true).when(handler).write(Mockito.any(ControlSuivi.class));
        
        // Connexion � RTC et initialisation API Sonar
        control.connexion();
        initAPI(MajSuiviExcelTask.class, true);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Ignore("pas encore pr�t")
    public void testMajSuiviExcel() throws Exception
    {
        assertTrue(Whitebox.invokeMethod(handler, "call"));
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
        handler = Mockito.spy(handler);
    }

    @Test
    public void testMajFichierAnomalies() throws Exception
    {
        Map<String, Set<String>> mapLotsSonar = new HashMap<>();
        Set<String> lotsSecurite = new HashSet<>();
        Set<String> lotRelease = new HashSet<>();
        String fichier = proprietesXML.getMapParams().get(Param.NOMFICHIERJAVA);
        Matiere matiere = Matiere.JAVA;        
        Whitebox.invokeMethod(handler, "majFichierAnomalies", mapLotsSonar, lotsSecurite, lotRelease, fichier, matiere);       
    }

    @Test
    public void testTraitementProjet() throws Exception
    {
        // Initialisation des variables
        ComposantSonar compo = ModelFactory.getModelWithParams(ComposantSonar.class, "id", "pweb.metier.gpar:BOREAL_Metier_GPAR_Build:15", "nom");
        HashMap<String, Set<String>> retour = new HashMap<>();
        String entryKey = "15";
        retour.put(entryKey, new HashSet<>());
        Set<String> lotSecurite = new HashSet<>();
        Set<String> lotRelease = new HashSet<>();
        String base = EMPTY;

        // Premi�re invocation avec lot vide
        Whitebox.invokeMethod(handler, "traitementProjet", compo, retour, entryKey, lotSecurite, lotRelease, base);

        // Toutes les listes sont vides
        assertEquals(0, lotSecurite.size());
        assertEquals(0, lotRelease.size());
        assertEquals(1, retour.size());
        assertEquals(0, retour.get(entryKey).size());

        // Initialisation du composant pour tester les cas avec un composant en erreur
       compo.setQualityGate(QG.ERROR);
       compo.setBloquants(1.0F);

        // Invocation avec lot vide mais metriques initialis�s
        Whitebox.invokeMethod(handler, "traitementProjet", compo, retour, entryKey, lotSecurite, lotRelease, base);

        // Toutes les listes sont vides
        assertEquals(0, lotSecurite.size());
        assertEquals(0, lotRelease.size());
        assertEquals(1, retour.size());
        assertEquals(0, retour.get(entryKey).size());

        // Initialisation du lot
        compo.setLot(NUMEROLOT1);

        // Invocation avec composant en erreur ni s�curit� ni release
        Whitebox.invokeMethod(handler, "traitementProjet", compo, retour, entryKey, lotSecurite, lotRelease, base);

        // Toutes les listes sont vides
        assertEquals(0, lotSecurite.size());
        assertEquals(0, lotRelease.size());
        assertEquals(1, retour.size());
        assertEquals(1, retour.get(entryKey).size());

        // Invocation avec composant en erreur et s�curit�
        compo.setSecurite(true);
        Whitebox.invokeMethod(handler, "traitementProjet", compo, retour, entryKey, lotSecurite, lotRelease, base);

        // Contr�le
        assertEquals(1, lotSecurite.size());
        assertEquals(0, lotRelease.size());
        assertEquals(1, retour.size());
        assertEquals(1, retour.get(entryKey).size());
        lotSecurite.clear();

        // Invocation avec composant en erreur et release
        compo.setVersionRelease(true);
        compo.setSecurite(false);
        Whitebox.invokeMethod(handler, "traitementProjet", compo, retour, entryKey, lotSecurite, lotRelease, base);

        // Contr�le
        assertEquals(0, lotSecurite.size());
        assertEquals(1, lotRelease.size());
        assertEquals(1, retour.size());
        assertEquals(1, retour.get(entryKey).size());
        lotRelease.clear();

        // Invocation avec composant en erreur et s�curit�, release
        compo.setSecurite(true);
        Whitebox.invokeMethod(handler, "traitementProjet", compo, retour, entryKey, lotSecurite, lotRelease, base);

        // Contr�le
        assertEquals(1, lotSecurite.size());
        assertEquals(1, lotRelease.size());
        assertEquals(1, retour.size());
        assertEquals(1, retour.get(entryKey).size());
    }

    @Test
    public void testCreationNumerosLots() throws Exception
    {
        List<Anomalie> listeLotenAno = new ArrayList<>();
        Map<String, LotSuiviRTC> lotsRTC = new HashMap<>();
        String numerolot2 = "654321";

        // Ajout objets
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot(NUMEROLOT1);
        listeLotenAno.add(ano);

        Anomalie ano2 = ModelFactory.getModel(Anomalie.class);
        ano2.setLot("Lot " + numerolot2);
        listeLotenAno.add(ano2);

        LotSuiviRTC lotRTC = ModelFactory.getModel(LotSuiviRTC.class);
        lotRTC.setLot(NUMEROLOT1);
        lotRTC.setCpiProjet("cpi");
        lotRTC.setEdition("edition");
        lotsRTC.put(lotRTC.getLot(), lotRTC);

        // Appel m�thode
        Map<String, Anomalie> retour = Whitebox.invokeMethod(handler, "creationNumerosLots", listeLotenAno, lotsRTC);

        // Contr�les
        // V�rification taille de la liste de retour. Il doit y avoir les deux anos.
        assertEquals(2, retour.size());
        assertNotNull(retour.get(NUMEROLOT1));

        // Test que la premi�re anomalie, on a bien mis � jour les informatiosn provenant de RTC
        assertEquals("cpi", ano.getCpiProjet());
        assertEquals("edition", ano.getEdition());

        // V�rification de la deuxi�me anomalienon mise � jour
        assertNotNull(retour.get(numerolot2));
        assertEquals(EMPTY, ano2.getCpiProjet());
        assertEquals(EMPTY, ano2.getEdition());
    }

    @Test
    public void testLiensQG() throws Exception
    {
        // R�cup�ration du nom de la QualityGate DataStage
        String nomQG = Statics.proprietesXML.getMapParams().get(Param.NOMQGDATASTAGE);

        // Pr�paration retour QualityGate
        Mockito.when(api.getQualityGate(Mockito.anyString())).thenCallRealMethod();
        Mockito.when(api.getListQualitygate()).thenCallRealMethod();
        Mockito.when(api.appelWebserviceGET(Mockito.anyString())).thenCallRealMethod();

        // Initialisation param�tre m�thode
        ComposantSonar compo = ModelFactory.getModel(ComposantSonar.class);
        List<ComposantSonar> liste = new ArrayList<>();
        Collection<List<ComposantSonar>> collec = new ArrayList<>();
        liste.add(compo);
        collec.add(liste);

        // Appel de la m�thode
        Whitebox.invokeMethod(handler, "liensQG", collec, nomQG);

        // V�rification de l'appel � associerQualityGate
        Mockito.verify(api, Mockito.times(1)).associerQualitygate(Mockito.eq(compo), Mockito.any(QualityGate.class));

    }
    
    @Test
    public void testControleQGBloquant() throws Exception
    {
        final String METHODE = "controleQGBloquant";

        // Param�tre de base de la m�thode        
        ComposantSonar compo = ModelFactory.getModel(ComposantSonar.class);

        // Appel liste vide
        assertFalse(Whitebox.invokeMethod(handler, METHODE, compo));

        // Test avec QG vide
        compo.setQualityGate(QG.NONE);
        assertFalse(Whitebox.invokeMethod(handler, METHODE, compo));

        // Test avec QG pas en erreur
        compo.setQualityGate(QG.OK);
        assertFalse(Whitebox.invokeMethod(handler, METHODE, compo));

        // Test avec QG en erreur mais sans erreurs des m�triques
        compo.setQualityGate(QG.ERROR);
        assertFalse(Whitebox.invokeMethod(handler, METHODE, compo));

        // Test avec QG en erreur mais avec erreurs bloquantes
        compo.setBloquants(1.0F);
        assertTrue(Whitebox.invokeMethod(handler, METHODE, compo));

        // Test avec QG en erreur mais avec erreurs critiques
        compo.setCritiques(1.0F);
        compo.setBloquants(0.0F);
        assertTrue(Whitebox.invokeMethod(handler, METHODE, compo));

        // Test avec QG en erreur mais avec duplication de code
        compo.setDuplication(5.0F);
        compo.setCritiques(0.0F);
        assertTrue(Whitebox.invokeMethod(handler, METHODE, compo));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
