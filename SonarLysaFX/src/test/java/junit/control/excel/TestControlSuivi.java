package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.reflect.Whitebox.getField;
import static org.powermock.reflect.Whitebox.getMethod;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import control.excel.ControlSuivi;
import control.rtc.ControlRTC;
import control.word.ControlRapport;
import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.enums.EtatDefaut;
import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.QG;
import model.enums.TypeAction;
import model.enums.TypeColSuivi;
import model.enums.TypeRapport;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;

public final class TestControlSuivi extends TestControlExcelRead<TypeColSuivi, ControlSuivi, List<DefautQualite>>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String SQ = "SUIVI Défaults Qualité";
    private static final String CREERLIGNESQ = "creerLigneSQ";
    private final Method methodCreerLigneSQ;

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlSuivi()
    {
        super(TypeColSuivi.class, "Suivi_Quality_Gate-JAVATest.xlsx");
        ControlRTC.INSTANCE.connexion();
        methodCreerLigneSQ = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, DefautQualite.class, IndexedColors.class, FillPatternType.class);
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
    public void testCreerLigneTitres() throws Exception
    {
        Sheet sheet = wb.createSheet();

        // Vérification 1 seule ligne et nombre de colonnes bon
        invokeMethod(controlTest, "creerLigneTitres", sheet);
        assertEquals(1, sheet.getPhysicalNumberOfRows());
        assertEquals(TypeColSuivi.values().length, sheet.getRow(0).getPhysicalNumberOfCells());
    }

    @Test
    public void testRetcupDonneesDepuisExcel()
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
        controlTest.majFeuilleDefaultsQualite(DaoFactory.getDao(DefautQualite.class).readAll(), sheet, matiere);
        assertEquals(wb.getActiveSheetIndex(), wb.getSheetIndex(sheet));
        assertTrue(sheet.getLastRowNum() > 2);
        assertFalse(sheet.getDataValidations().isEmpty());

        // Test avec rapport et fichier vide
        sheet = wb.createSheet();
        controlTest.createControlRapport(TypeRapport.ANDROID);
        controlTest.majFeuilleDefaultsQualite(new ArrayList<>(), sheet, matiere);
        assertEquals(wb.getActiveSheetIndex(), wb.getSheetIndex(sheet));
        assertEquals(0, sheet.getLastRowNum());
        assertTrue(sheet.getDataValidations().isEmpty());

    }

    @Test
    public void testCalculDonnee()
    {

    }

    @Test
    public void testCalculEnCours()
    {

    }

    @Test
    public void testTestAnoOK() throws Exception
    {
        // Initialisation
        String methode = "testAnoOK";
        DefautQualite dq = ModelFactory.build(DefautQualite.class);
        LotRTC lot = LotRTC.getLotRTCInconnu("123456");
        dq.setLotRTC(lot);

        // Retour vrai pour le anomalies abandonnées
        dq.setEtatDefaut(EtatDefaut.ABANDONNE);
        assertTrue(invokeMethod(controlTest, methode, dq));

        // Test des défauts non repris
        dq.getLotRTC().setEtatLot(EtatLot.EDITION);
        dq.setEtatDefaut(EtatDefaut.CLOS);
        assertTrue(invokeMethod(controlTest, methode, dq));
        dq.getLotRTC().setEtatLot(EtatLot.TERMINE);
        assertTrue(invokeMethod(controlTest, methode, dq));

        // Test anomalies closes à reprendre
        dq.setEtatDefaut(EtatDefaut.CLOS);
        dq.getLotRTC().setEtatLot(EtatLot.MOA);
        dq.getLotRTC().setQualityGate(QG.ERROR);
        assertFalse(invokeMethod(controlTest, methode, dq));

        // Test autres cas
        dq.setEtatDefaut(EtatDefaut.NOUVEAU);
        assertFalse(invokeMethod(controlTest, methode, dq));

        dq.setEtatDefaut(EtatDefaut.CLOS);
        dq.getLotRTC().setQualityGate(QG.NONE);
        assertTrue(invokeMethod(controlTest, methode, dq));

        dq.getLotRTC().setEtatLot(EtatLot.EDITION);
        dq.setEtatDefaut(EtatDefaut.NOUVEAU);
        assertFalse(invokeMethod(controlTest, methode, dq));

    }

    @Test
    public void testCreerTitresStats()
    {

    }

    @Test
    public void testCalculerCouleurLigne() throws Exception
    {
        // Vérification de al couleur de sortie de la méthode

        // Initialisation
        DefautQualite dq = ModelFactory.build(DefautQualite.class);
        dq.setLotRTC(LotRTC.getLotRTCInconnu("123456"));
        String methode = "calculCouleurLigne";

        // Test 1 = Vert
        dq.getLotRTC().setQualityGate(QG.OK);
        dq.setRemarque("rem");
        dq.calculTraitee();
        IndexedColors couleur = invokeMethod(controlTest, methode, dq);
        assertEquals(IndexedColors.LIGHT_GREEN, couleur);

        // Test 3 = Blanc
        dq.setRemarque("rem");
        dq.getLotRTC().setQualityGate(QG.ERROR);
        couleur = invokeMethod(controlTest, methode, dq);
        assertEquals(IndexedColors.WHITE, couleur);

        // Test 3 = Turquoise
        dq.setAction(TypeAction.ASSEMBLER);
        couleur = invokeMethod(controlTest, methode, dq);
        assertEquals(IndexedColors.LIGHT_TURQUOISE, couleur);

        // Test 4 = Gris
        dq.setAction(TypeAction.VERIFIER);
        couleur = invokeMethod(controlTest, methode, dq);
        assertEquals(IndexedColors.GREY_25_PERCENT, couleur);

        // Test 5 = Orange
        dq.setNumeroAnoRTC(0);
        dq.setRemarque(Statics.EMPTY);
        dq.setEtatDefaut(EtatDefaut.NOUVEAU);
        dq.setAction(TypeAction.VIDE);
        couleur = invokeMethod(controlTest, methode, dq);
        assertEquals(IndexedColors.LIGHT_ORANGE, couleur);

        // Test 6 - Jaune
        dq.setDateMepPrev(LocalDate.now().plusWeeks(2L));
        dq.setAction(TypeAction.ABANDONNER);
        couleur = invokeMethod(controlTest, methode, dq);
        assertEquals(IndexedColors.LIGHT_YELLOW, couleur);
    }

    @Test
    public void testCalculMotif()
    {

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException1() throws IllegalAccessException
    {
        try
        {
            methodCreerLigneSQ.invoke(controlTest, null, ModelFactory.build(DefautQualite.class), IndexedColors.AQUA, FillPatternType.ALT_BARS);

        }
        catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException2() throws IllegalAccessException
    {
        try
        {
            methodCreerLigneSQ.invoke(controlTest, wb.getSheetAt(0).getRow(0), null, IndexedColors.AQUA, FillPatternType.ALT_BARS);
        }
        catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException3() throws IllegalAccessException
    {
        try
        {
            methodCreerLigneSQ.invoke(controlTest, wb.getSheetAt(0).getRow(0), ModelFactory.build(DefautQualite.class), null, FillPatternType.ALT_BARS);

        }
        catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException4() throws IllegalAccessException
    {
        try
        {
            methodCreerLigneSQ.invoke(controlTest, wb.getSheetAt(0).getRow(0), ModelFactory.build(DefautQualite.class), IndexedColors.AQUA, null);

        }
        catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException5() throws IllegalAccessException
    {
        try
        {
            methodCreerLigneSQ.invoke(controlTest, null, null, null, null);

        }
        catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e.getCause();
        }
    }

    @Test
    public void testCreerFormule()
    {

    }

    @Test
    public void testCreerLigneCalcul()
    {

    }

    @Test
    public void testCreerLigneTotalAno()
    {

    }

    @Test
    public void testCreerDqDepuisExcel()
    {

    }

    @Test
    public void testAjouterDataValidation() throws Exception
    {
        String methode = "ajouterDataValidation";
        // Test avec une ancienne feuille excel
        HSSFWorkbook wbOld = new HSSFWorkbook();
        HSSFSheet sheet = wbOld.createSheet();
        Whitebox.invokeMethod(controlTest, methode, sheet);
        assertTrue(sheet.getDataValidations().isEmpty());
        wbOld.close();

        Sheet newSheet = wb.getSheet(SQ);
        Whitebox.invokeMethod(controlTest, methode, newSheet);
        assertFalse(newSheet.getDataValidations().isEmpty());
        assertEquals(1, newSheet.getDataValidations().get(0).getRegions().getCellRangeAddresses()[0].getFirstRow());
        assertEquals(newSheet.getLastRowNum(), newSheet.getDataValidations().get(0).getRegions().getCellRangeAddresses()[0].getLastRow());
        int column = (int) Whitebox.getField(ControlSuivi.class, "colAction").get(controlTest);
        assertEquals(column, newSheet.getDataValidations().get(0).getRegions().getCellRangeAddresses()[0].getFirstColumn());
        assertEquals(column, newSheet.getDataValidations().get(0).getRegions().getCellRangeAddresses()[0].getLastColumn());
    }

    @Test(expected = TechnicalException.class)
    public void testGetControlRapport()
    {
        // Test getter objet null
        assertNull(controlTest.getControlRapport());

        // Test setter et getter
        ControlRapport rapport = controlTest.createControlRapport(TypeRapport.ANDROID);
        assertEquals(rapport, controlTest.getControlRapport());

        // Test exception type null
        controlTest.createControlRapport(null);
    }

    /*---------- METHODES PRIVEES ----------*/

    private void removeSheet(String nomSheet)
    {
        wb.removeSheetAt(wb.getSheetIndex(wb.getSheet(nomSheet)));
    }
    /*---------- ACCESSEURS ----------*/
}
