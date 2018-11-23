package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.reflect.Whitebox.getField;
import static org.powermock.reflect.Whitebox.getMethod;
import static org.powermock.reflect.Whitebox.invokeMethod;
import static utilities.Statics.EMPTY;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import control.excel.ControlSuivi;
import control.rtc.ControlRTC;
import model.ModelFactory;
import model.bdd.DefautQualite;
import model.enums.Matiere;
import model.enums.TypeAction;
import model.enums.TypeColSuivi;
import model.enums.TypeRapport;
import utilities.FunctionalException;

public final class TestControlSuivi extends TestControlExcelRead<TypeColSuivi, ControlSuivi, List<DefautQualite>>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String SNAPSHOT = "SNAPSHOT";
    private static final String LOT = "Lot 315572";
    private static final String SQ = "SUIVI Défaults Qualité";
    private static final String AC = "Anomalies closes";
    private static final String CREERLIGNESQ = "creerLigneSQ";
    private static final String AJOUTERLIENS = "ajouterLiens";
    private final LocalDate today = LocalDate.now();

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlSuivi()
    {
        super(TypeColSuivi.class, "Suivi_Quality_Gate-JAVATest.xlsx");
        ControlRTC.INSTANCE.connexion();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testInitSheet() throws Exception
    {
        // Test 1 - feuille ok
        Sheet sheet = invokeMethod(controlTest, "initSheet");
        assertNotNull(sheet);
        assertEquals(wb.getSheet(SQ), sheet);
    }

    @Test(expected = FunctionalException.class)
    @Override
    public void testInitSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        removeSheet(SQ);
        invokeMethod(controlTest, "initSheet");
    }
    
    @Test
    @Override
    public void testInitEnum() throws IllegalAccessException
    {
        // Test - énumération du bon type
        assertEquals(TypeColSuivi.class, getField(ControlSuivi.class, "enumeration").get(controlTest));
    }
    
    @Test
    public void testRecupDonneesDepuisExcel()
    {
        testRecupDonneesDepuisExcel(map -> map.size() == 104); 
    }
    
    @Test
    public void testCalculStatistiques()
    {
        
    }
    
    @Test(expected = IOException.class)
    public void testSauvegardeFichierException1() throws IOException
    {
        // Envoi d'une IOException avec un nom non compatible.
        controlTest.sauvegardeFichier("@|(['{");
    }

    @Test
    public void testSauvegardeFichier() throws IOException
    {
        // Test 1 - On vérifie que la feuille renvoyée ne contient bien qu'une seule ligne.
        Sheet sheet = controlTest.sauvegardeFichier(file.getName());
        assertEquals(1, sheet.getPhysicalNumberOfRows());

        // Test 2 - test avec une feuille nulle
        removeSheet(SQ);
        sheet = controlTest.sauvegardeFichier(file.getName());
        assertEquals(1, sheet.getPhysicalNumberOfRows());
    }

    @Test
    public void testMajFeuilleDefaultsQualite() throws Exception
    {

        // Initialisation
        controlTest = PowerMockito.spy(controlTest);
        PowerMockito.doNothing().when(controlTest, "write");
        List<DefautQualite> lotsEnAno = controlTest.recupDonneesDepuisExcel();
        DefautQualite ano = ModelFactory.build(DefautQualite.class);
//        ano.setLot("Lot 225727");
        lotsEnAno.add(ano);
        ano = ModelFactory.build(DefautQualite.class);
//        ano.setLot("Lot 156452");
        ano.setEtatRTC("Close");
        lotsEnAno.add(ano);
        ano = ModelFactory.build(DefautQualite.class);
//        ano.setLot("Lot 239654");
        ano.setEtatRTC("Abandonnée");
        lotsEnAno.add(ano);
        List<DefautQualite> anoAajouter = new ArrayList<>();
        Set<String> lotsEnErreurSonar = new HashSet<>();
        Set<String> lotsSecurite = new HashSet<>();
        lotsSecurite.add("290318");
        lotsSecurite.add("225727");
        Set<String> lotsRelease = new HashSet<>();
        lotsRelease.add("225727");
        Sheet sheet = wb.createSheet();
        Matiere matiere = Matiere.JAVA;

        // Appel méthode sans écriture du fichier
        controlTest.majFeuilleDefaultsQualite(lotsEnAno, sheet, matiere);
    }

    @Test
    public void testCalculerCouleurLigne() throws Exception
    {
//        // Vérification de al couleur de sortie de la méthode
//
//        // Initialisation
//        DefautQualite dq = ModelFactory.build(DefautQualite.class);
//        String methode = "calculCouleurLigne";
//        Set<String> lotsEnErreurSonar = new HashSet<>();
//        String anoLot = "123456";
//
//        // Test 1 = Vert
//        dq.setEtatRTC(EMPTY);
//        dq.setNumeroAnoRTC(1);
//        dq.calculTraitee();
//        IndexedColors couleur = invokeMethod(controlTest, methode, dq, lotsEnErreurSonar, anoLot);
//        assertEquals(IndexedColors.LIGHT_GREEN, couleur);
//
//        // Test 3 = Blanc
//        lotsEnErreurSonar.add(anoLot);
//        couleur = invokeMethod(controlTest, methode, dq, lotsEnErreurSonar, anoLot);
//        assertEquals(IndexedColors.WHITE, couleur);
//
//        // Test 3 = Turquoise
//        dq.setAction(TypeAction.ASSEMBLER);
//        couleur = invokeMethod(controlTest, methode, dq, lotsEnErreurSonar, anoLot);
//        assertEquals(IndexedColors.LIGHT_TURQUOISE, couleur);
//
//        // Test 4 = Gris
//        dq.setAction(TypeAction.VERIFIER);
//        couleur = invokeMethod(controlTest, methode, dq, lotsEnErreurSonar, anoLot);
//        assertEquals(IndexedColors.GREY_25_PERCENT, couleur);
//
//        // Test 5 = Orange
//        dq.setNumeroAnoRTC(0);
//        dq.calculTraitee();
//        couleur = invokeMethod(controlTest, methode, dq, lotsEnErreurSonar, anoLot);
//        assertEquals(IndexedColors.LIGHT_ORANGE, couleur);
    }
    
    @Test
    public void testGestionAction() throws Exception
    {
//        // Initialisation
//        String methode = "gestionAction";
//        Logger logger = TestUtils.getMockLogger(ControlSuivi.class, "LOGGER");
//        Anomalie ano = ModelFactory.getModel(Anomalie.class);
//        ano.setLot("Lot 123456");
//        ano.setNumeroAnoRTC(329000);
//        ano.setAction(TypeAction.ABANDONNER);
//        Sheet sheet = wb.getSheet(AC);
//        String anoLot = "123456";
//        
//        // Test abandon avec anomalie en cours
//        invokeMethod(handler, methode, ano, anoLot, sheet);
//        Mockito.verify(logger, Mockito.times(1)).warn("L'anomalie 329000 n'a pas été clôturée. Impossible de la supprimer du fichier de suivi.");
//        
//        // Test clôture avec anomalie en cours
//        ano.setAction(TypeAction.CLOTURER);
//        invokeMethod(handler, methode, ano, anoLot, sheet);
//        Mockito.verify(logger, Mockito.times(2)).warn("L'anomalie 329000 n'a pas été clôturée. Impossible de la supprimer du fichier de suivi.");
//
//        // Test création anomalie avec mock de la création du Defect
//        ControlRTC mock = Mockito.mock(ControlRTC.class);
//        getField(ControlSuivi.class, "controlRTC").set(handler, mock);        
//        ano.setNumeroAnoRTC(0);
//        ano.setAction(TypeAction.CREER);
//        invokeMethod(handler, methode, ano, anoLot, sheet);
//        
//        // Test création anomalie avec simulation création anomalie
//        Mockito.when(mock.creerDefect(ano)).thenReturn(1);
//        invokeMethod(handler, methode, ano, anoLot, sheet);
//        Mockito.verify(logger, Mockito.times(1)).info("Création anomalie 1 pour le lot 123456");        
    }

    @Test
    public void testAjouterAnomaliesCloses() throws Exception
    {
//        // Vérification si une anomaie est bien rajoutée à la feuille des anomalies closes.
//
//        // Initialisation
//        Sheet sheet = wb.getSheet(AC);
//        Map<String, Anomalie> anoClose = new HashMap<>();
//        Anomalie ano = ModelFactory.getModel(Anomalie.class);
//        ano.setNumeroAnoRTC(307402);
//        ano.setLot(LOT);
//        ano.setEtatLot(EtatLot.NOUVEAU);
//        anoClose.put(ano.getLotRTC(), ano);
//        ano = ModelFactory.getModel(Anomalie.class);
//        ano.setNumeroAnoRTC(0);
//        ano.setLot("Lot 316089");
//        anoClose.put(ano.getLotRTC(), ano);
//
//        // Appel méthode et controle
//        int nbreLignes = sheet.getPhysicalNumberOfRows();
//        invokeMethod(handler, "ajouterAnomaliesCloses", sheet, anoClose);
//        assertEquals(nbreLignes + 1l, sheet.getPhysicalNumberOfRows());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException1() throws IllegalAccessException
    {
//        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, DefautQualite.class, IndexedColors.class);
//        try
//        {
//            method.invoke(controlTest, null, ModelFactory.build(DefautQualite.class), IndexedColors.AQUA);
//
//        } catch (InvocationTargetException e)
//        {
//            if (e.getCause() instanceof IllegalArgumentException)
//                throw (IllegalArgumentException) e.getCause();
//        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException2() throws IllegalAccessException
    {
//        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, DefautQualite.class, IndexedColors.class);
//        try
//        {
//            method.invoke(controlTest, wb.getSheetAt(0).getRow(0), null, IndexedColors.AQUA);
//        } catch (InvocationTargetException e)
//        {
//            if (e.getCause() instanceof IllegalArgumentException)
//                throw (IllegalArgumentException) e.getCause();
//        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException3() throws IllegalAccessException
    {
//        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, DefautQualite.class, IndexedColors.class);
//        try
//        {
//            method.invoke(controlTest, wb.getSheetAt(0).getRow(0), ModelFactory.build(DefautQualite.class), null);
//
//        } catch (InvocationTargetException e)
//        {
//            if (e.getCause() instanceof IllegalArgumentException)
//                throw (IllegalArgumentException) e.getCause();
//        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException4() throws IllegalAccessException
    {
//        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, DefautQualite.class, IndexedColors.class);
//        try
//        {
//            method.invoke(controlTest, null, null, null);
//
//        } catch (InvocationTargetException e)
//        {
//            if (e.getCause() instanceof IllegalArgumentException)
//                throw (IllegalArgumentException) e.getCause();
//        }
    }

    @Test
    public void testCreerLigneTitres() throws Exception
    {
        Sheet sheet = wb.createSheet();

        // Vérification 1 seule ligne et nombre de colonnes bon
        invokeMethod(controlTest, "creerLigneTitres", sheet);
        assertEquals(1, sheet.getPhysicalNumberOfRows());
        assertEquals(TypeColSuivi.values().length, sheet.getRow(0).getPhysicalNumberOfCells());
    }

    @Test
    public void testAjouterNouvellesAnos() throws Exception
    {
//        // Initialisation
//        Sheet sheet = wb.createSheet();
//        String methode = "ajouterNouvellesAnos";
//        List<Anomalie> anoAajouter = new ArrayList<>();
//        Map<String, Anomalie> mapAnoCloses = new HashMap<>();
//        Set<String> lotsSecurite = new HashSet<>();
//        Set<String> lotsRelease = new HashSet<>();
//        Matiere matiere = Matiere.JAVA;
//
//        // Test 1. liste vide
//        invokeMethod(handler, methode, sheet, anoAajouter, mapAnoCloses, lotsSecurite, lotsRelease, matiere);
//        assertEquals(0, sheet.getPhysicalNumberOfRows());
//
//        // Test 2. ni securite / ni release / ni close
//        Anomalie ano1 = ModelFactory.getModel(Anomalie.class);
//        ano1.setNumeroAnoRTC(307402);
//        ano1.setEtatLot(EtatLot.NOUVEAU);
//        ano1.setLot(LOT);
//        ano1.setMatieresString("JAVA");
//        anoAajouter.add(ano1);
//        invokeMethod(handler, methode, sheet, anoAajouter, mapAnoCloses, lotsSecurite, lotsRelease, matiere);
//        assertEquals(1, sheet.getPhysicalNumberOfRows());
//        assertEquals(today, ano1.getDateDetection());
//        assertEquals(EMPTY, ano1.isSecurite());
//        assertEquals(SNAPSHOT, ano1.getVersion());
//
//        // Test 3. securite / release
//        Anomalie ano = ModelFactory.getModel(Anomalie.class);
//        ano.setLot("Lot 295711");
//        anoAajouter.add(ano);
//        lotsSecurite.add("295711");
//        lotsRelease.add("295711");
//        invokeMethod(handler, methode, sheet, anoAajouter, mapAnoCloses, lotsSecurite, lotsRelease, matiere);
//        assertEquals(3, sheet.getPhysicalNumberOfRows());
//        assertEquals(today, ano.getDateDetection());
//
//        // Test 4. close
//        Anomalie anoClose = ModelFactory.getModel(Anomalie.class);
//        anoClose.setNumeroAnoRTC(307402);
//        anoClose.setLot(LOT);
//        anoClose.setDateCreation(today);
//        anoClose.setDateRelance(today);
//        anoClose.setRemarque("remarque");
//        mapAnoCloses.put(LOT, anoClose);
//        invokeMethod(handler, methode, sheet, anoAajouter, mapAnoCloses, lotsSecurite, lotsRelease, matiere);
//        assertEquals(4, sheet.getPhysicalNumberOfRows());
//        assertEquals(anoClose.getNumeroAnoRTC(), ano1.getNumeroAnoRTC());
//        assertEquals(anoClose.getLotRTC(), ano1.getLotRTC());
//        assertEquals(anoClose.getNumeroAnoRTC(), ano1.getNumeroAnoRTC());
    }

    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected void initOther()
    {
        controlTest.createControlRapport(TypeRapport.SUIVIJAVA); 
    }
    
    private void removeSheet(String nomSheet)
    {
        wb.removeSheetAt(wb.getSheetIndex(wb.getSheet(nomSheet)));
    }
    /*---------- ACCESSEURS ----------*/
}