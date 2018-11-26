package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.reflect.Whitebox.getField;
import static org.powermock.reflect.Whitebox.getMethod;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import control.excel.ControlSuivi;
import control.rtc.ControlRTC;
import dao.ListeDao;
import model.ModelFactory;
import model.bdd.DefautQualite;
import model.enums.Matiere;
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
        controlTest = Mockito.spy(controlTest);
        PowerMockito.when(controlTest, "write").thenReturn(true);

        Sheet sheet = wb.createSheet();
        Matiere matiere = Matiere.JAVA;

        // Appel méthode sans écriture du fichier
        List<DefautQualite> dqs = ListeDao.daoDefautQualite.readAll();
        controlTest.majFeuilleDefaultsQualite(ListeDao.daoDefautQualite.readAll(), sheet, matiere);
        assertEquals(wb.getActiveSheetIndex(), wb.getSheetIndex(sheet));
        assertTrue(sheet.getLastRowNum() > 2);
        assertFalse(sheet.getDataValidations().isEmpty());
        
        for (Row row : sheet)
        {
            
        }
        
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

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException1() throws IllegalAccessException
    {
        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, DefautQualite.class, IndexedColors.class);
        try
        {
            method.invoke(controlTest, null, ModelFactory.build(DefautQualite.class), IndexedColors.AQUA);

        } catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException2() throws IllegalAccessException
    {
        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, DefautQualite.class, IndexedColors.class);
        try
        {
            method.invoke(controlTest, wb.getSheetAt(0).getRow(0), null, IndexedColors.AQUA);
        } catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException3() throws IllegalAccessException
    {
        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, DefautQualite.class, IndexedColors.class);
        try
        {
            method.invoke(controlTest, wb.getSheetAt(0).getRow(0), ModelFactory.build(DefautQualite.class), null);

        } catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException4() throws IllegalAccessException
    {
        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, DefautQualite.class, IndexedColors.class);
        try
        {
            method.invoke(controlTest, null, null, null);

        } catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e.getCause();
        }
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