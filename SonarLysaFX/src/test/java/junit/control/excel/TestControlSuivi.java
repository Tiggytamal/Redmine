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

import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import control.excel.ControlSuivi;
import control.rtc.ControlRTC;
import junit.TestUtils;
import model.ModelFactory;
import model.bdd.DefautQualite;
import model.enums.EtatLot;
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
    private static final String SQ = "SUIVI Qualit�";
    private static final String AC = "Anomalies closes";
    private static final String CREERLIGNESQ = "creerLigneSQ";
    private static final String AJOUTERLIENS = "ajouterLiens";
    private final LocalDate today = LocalDate.now();

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlSuivi()
    {
        super(TypeColSuivi.class, "Suivi_Quality_GateTest.xlsx");
        ControlRTC.INSTANCE.connexion();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testRecupDonneesDepuisExcel()
    {
        testRecupDonneesDepuisExcel(map -> map.size() == 27); 
    }

    @Test
    public void testCreateSheetError()
    {
//        // Intialisation
//        List<Anomalie> anoAcreer = new ArrayList<>();
//        List<Anomalie> anoDejacrees = new ArrayList<>();
//        String nomSheet = "E30";
//
//        // Test 1 - ano d�ja abandonn�e - Retour normalement vide
//        Anomalie ano = ModelFactory.getModel(Anomalie.class);
//        ano.setLot("Lot 305388");
//        ano.setEtatLot(EtatLot.NOUVEAU);
//        anoAcreer.add(ano);
//        List<Anomalie> liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
//        assertEquals(0, liste.size());
//
//        // Test 2 - nouvelle ano - Retour normalement � 1
//        ano = ModelFactory.getModel(Anomalie.class);
//        ano.setLot("Lot 305128");
//        ano.setEtatLot(EtatLot.NOUVEAU);
//        ano = ModelFactory.getModel(Anomalie.class);
//        ano.setLot("Lot 307128");
//        ano.setEtatLot(EtatLot.NOUVEAU);
//
//        anoAcreer.add(ano);
//        liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
//        assertEquals(1, liste.size());
//
//        // Test 3 - feuille nulle - Retour normalement � 2
//        removeSheet(nomSheet);
//        liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
//        assertEquals(2, liste.size());
//
//        // Test 4 - feuille nulle - anomalie d�j� cr��e
//        removeSheet(nomSheet);
//        ano = ModelFactory.getModel(Anomalie.class);
//        ano.setLot("Lot 305129");
//        ano.setEdition("E31");
//        ano.setAction(TypeAction.ABANDONNER);
//        anoDejacrees.add(ano);
//        ano.setAction(TypeAction.RELANCER);
//        anoDejacrees.add(ano);        
//        liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
//        assertEquals(2, liste.size());
//        assertEquals(5, wb.getSheet(nomSheet).getPhysicalNumberOfRows());

    }

    @Test(expected = IOException.class)
    public void testSauvegardeFichierException1() throws IOException
    {
        // Envoi d'une IOException avec un nom non compatible.
        handler.sauvegardeFichier("@|(['{");
    }

    @Test
    public void testSauvegardeFichier() throws IOException
    {
        // Test 1 - On v�rifie que la feuille renvoy�e ne contient bien qu'une seule ligne.
        Sheet sheet = handler.sauvegardeFichier(file.getName());
        assertEquals(1, sheet.getPhysicalNumberOfRows());

        // Test 2 - test avec une feuille nulle
        removeSheet(SQ);
        sheet = handler.sauvegardeFichier(file.getName());
        assertEquals(1, sheet.getPhysicalNumberOfRows());
    }

    @Test
    public void testMajFeuillePrincipale() throws Exception
    {
//
//        // Initialisation
//        handler = PowerMockito.spy(handler);
//        PowerMockito.doNothing().when(handler, "write");
//        List<Anomalie> lotsEnAno = handler.recupDonneesDepuisExcel();
//        Anomalie ano = ModelFactory.getModel(Anomalie.class);
//        ano.setLot("Lot 225727");
//        lotsEnAno.add(ano);
//        ano = ModelFactory.getModel(Anomalie.class);
//        ano.setLot("Lot 156452");
//        ano.setEtatRTC("Close");
//        lotsEnAno.add(ano);
//        ano = ModelFactory.getModel(Anomalie.class);
//        ano.setLot("Lot 239654");
//        ano.setEtatRTC("Abandonn�e");
//        lotsEnAno.add(ano);
//        List<Anomalie> anoAajouter = new ArrayList<>();
//        Set<String> lotsEnErreurSonar = new HashSet<>();
//        Set<String> lotsSecurite = new HashSet<>();
//        lotsSecurite.add("290318");
//        lotsSecurite.add("225727");
//        Set<String> lotsRelease = new HashSet<>();
//        lotsRelease.add("225727");
//        Sheet sheet = wb.createSheet();
//        Matiere matiere = Matiere.JAVA;
//
//        // Appel m�thode sans �criture du fichier
//        handler.majFeuillePrincipale(lotsEnAno, anoAajouter, lotsEnErreurSonar, lotsSecurite, lotsRelease, sheet, matiere);
    }

    @Test
    public void testCalculerCouleurLigne() throws Exception
    {
        // V�rification de al couleur de sortie de la m�thode

        // Initialisation
        DefautQualite dq = ModelFactory.getModel(DefautQualite.class);
        String methode = "calculCouleurLigne";
        Set<String> lotsEnErreurSonar = new HashSet<>();
        String anoLot = "123456";

        // Test 1 = Vert
        dq.setEtatRTC(EMPTY);
        dq.setNumeroAnoRTC(1);
        dq.calculTraitee();
        IndexedColors couleur = invokeMethod(handler, methode, dq, lotsEnErreurSonar, anoLot);
        assertEquals(IndexedColors.LIGHT_GREEN, couleur);

        // Test 3 = Blanc
        lotsEnErreurSonar.add(anoLot);
        couleur = invokeMethod(handler, methode, dq, lotsEnErreurSonar, anoLot);
        assertEquals(IndexedColors.WHITE, couleur);

        // Test 3 = Turquoise
        dq.setAction(TypeAction.ASSEMBLER);
        couleur = invokeMethod(handler, methode, dq, lotsEnErreurSonar, anoLot);
        assertEquals(IndexedColors.LIGHT_TURQUOISE, couleur);

        // Test 4 = Gris
        dq.setAction(TypeAction.VERIFIER);
        couleur = invokeMethod(handler, methode, dq, lotsEnErreurSonar, anoLot);
        assertEquals(IndexedColors.GREY_25_PERCENT, couleur);

        // Test 5 = Orange
        dq.setNumeroAnoRTC(0);
        dq.calculTraitee();
        couleur = invokeMethod(handler, methode, dq, lotsEnErreurSonar, anoLot);
        assertEquals(IndexedColors.LIGHT_ORANGE, couleur);
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
//        Mockito.verify(logger, Mockito.times(1)).warn("L'anomalie 329000 n'a pas �t� cl�tur�e. Impossible de la supprimer du fichier de suivi.");
//        
//        // Test cl�ture avec anomalie en cours
//        ano.setAction(TypeAction.CLOTURER);
//        invokeMethod(handler, methode, ano, anoLot, sheet);
//        Mockito.verify(logger, Mockito.times(2)).warn("L'anomalie 329000 n'a pas �t� cl�tur�e. Impossible de la supprimer du fichier de suivi.");
//
//        // Test cr�ation anomalie avec mock de la cr�ation du Defect
//        ControlRTC mock = Mockito.mock(ControlRTC.class);
//        getField(ControlSuivi.class, "controlRTC").set(handler, mock);        
//        ano.setNumeroAnoRTC(0);
//        ano.setAction(TypeAction.CREER);
//        invokeMethod(handler, methode, ano, anoLot, sheet);
//        
//        // Test cr�ation anomalie avec simulation cr�ation anomalie
//        Mockito.when(mock.creerDefect(ano)).thenReturn(1);
//        invokeMethod(handler, methode, ano, anoLot, sheet);
//        Mockito.verify(logger, Mockito.times(1)).info("Cr�ation anomalie 1 pour le lot 123456");        
    }

    @Test
    public void testAjouterAnomaliesCloses() throws Exception
    {
//        // V�rification si une anomaie est bien rajout�e � la feuille des anomalies closes.
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
//        // Appel m�thode et controle
//        int nbreLignes = sheet.getPhysicalNumberOfRows();
//        invokeMethod(handler, "ajouterAnomaliesCloses", sheet, anoClose);
//        assertEquals(nbreLignes + 1l, sheet.getPhysicalNumberOfRows());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException1() throws IllegalAccessException
    {
        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, DefautQualite.class, IndexedColors.class);
        try
        {
            method.invoke(handler, null, ModelFactory.getModel(DefautQualite.class), IndexedColors.AQUA);

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
            method.invoke(handler, wb.getSheetAt(0).getRow(0), null, IndexedColors.AQUA);
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
            method.invoke(handler, wb.getSheetAt(0).getRow(0), ModelFactory.getModel(DefautQualite.class), null);

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
            method.invoke(handler, null, null, null);

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

        // V�rification 1 seule ligne et nombre de colonnes bon
        invokeMethod(handler, "creerLigneTitres", sheet);
        assertEquals(1, sheet.getPhysicalNumberOfRows());
        assertEquals(TypeColSuivi.values().length, sheet.getRow(0).getPhysicalNumberOfCells());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterLiensException1() throws Exception
    {
        Cell cell = null;
        String baseAdresse = "adresse";
        String variable = "var";
        invokeMethod(handler, AJOUTERLIENS, cell, baseAdresse, variable);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterLiensException2() throws Exception
    {
        Cell cell = wb.createSheet().createRow(0).createCell(0);
        String baseAdresse = null;
        String variable = "var";
        invokeMethod(handler, AJOUTERLIENS, cell, baseAdresse, variable);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterLiensException3() throws Exception
    {
        Cell cell = wb.createSheet().createRow(0).createCell(0);
        String baseAdresse = EMPTY;
        String variable = "var";
        invokeMethod(handler, AJOUTERLIENS, cell, baseAdresse, variable);
    }

    @Test
    public void testAjouterLiens() throws Exception
    {
        Cell cell = wb.createSheet().createRow(0).createCell(0);
        String baseAdresse = "adresse";
        String variable = "var";
        invokeMethod(handler, AJOUTERLIENS, cell, baseAdresse, variable);
        assertEquals(baseAdresse + variable, cell.getHyperlink().getAddress());
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

    @Test
    public void testSaveAnomaliesCloses() throws Exception
    {
        // Initialisation
        Map<String, DefautQualite> dqClos = new HashMap<>();

        // Test 1
        Sheet sheet = invokeMethod(handler, "saveAnomaliesCloses", dqClos);
        assertEquals(1, sheet.getPhysicalNumberOfRows());
        assertEquals(13, dqClos.size()); 

        // Test 2 - � partir d'une feuille vide
        removeSheet(AC);
        dqClos.clear();
        sheet = invokeMethod(handler, "saveAnomaliesCloses", dqClos);
        assertEquals(1, sheet.getPhysicalNumberOfRows());
        assertEquals(0, dqClos.size());
    }

    @Test
    @Override
    public void testInitEnum() throws IllegalAccessException
    {
        // Test - �num�ration du bon type
        assertEquals(TypeColSuivi.class, getField(ControlSuivi.class, "enumeration").get(handler));
    }

    @Test
    @Override
    public void testInitSheet() throws Exception
    {
        // Test 1 - feuille ok
        Sheet sheet = invokeMethod(handler, "initSheet");
        assertNotNull(sheet);
        assertEquals(wb.getSheet(SQ), sheet);
    }

    @Test(expected = FunctionalException.class)
    @Override
    public void testInitSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        removeSheet(SQ);
        invokeMethod(handler, "initSheet");
    }

    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected void initOther()
    {
        handler.createControlRapport(TypeRapport.SUIVIJAVA); 
    }
    
    private void removeSheet(String nomSheet)
    {
        wb.removeSheetAt(wb.getSheetIndex(wb.getSheet(nomSheet)));
    }
    /*---------- ACCESSEURS ----------*/
}