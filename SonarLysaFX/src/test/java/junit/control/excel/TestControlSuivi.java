package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.reflect.Whitebox.invokeMethod;
import static org.powermock.reflect.Whitebox.getField;
import static org.powermock.reflect.Whitebox.getMethod;

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
import model.Anomalie;
import model.ModelFactory;
import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.TypeAction;
import model.enums.TypeColSuivi;
import utilities.FunctionalException;
import utilities.Statics;

public class TestControlSuivi extends TestControlExcelRead<TypeColSuivi, ControlSuivi, List<Anomalie>>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String SNAPSHOT = "SNAPSHOT";
    private static final String LOT = "Lot 315572";
    private static final String SQ = "SUIVI Qualité";
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
    public void testControleKey() throws Exception
    {
        String methode = "controleKey";
        assertTrue(invokeMethod(handler, methode, "a", "a"));
        assertTrue(invokeMethod(handler, methode, "A", "a"));
        assertFalse(invokeMethod(handler, methode, "A", "b"));
        assertTrue(invokeMethod(handler, methode, "BEF000", "BEF0009"));
        assertTrue(invokeMethod(handler, methode, "T7004360", "T7004360E"));
        assertTrue(invokeMethod(handler, methode, "BF046502", "BF046500"));
    }

    @Test
    public void testCreateSheetError()
    {
        // Intialisation
        List<Anomalie> anoAcreer = new ArrayList<>();
        List<Anomalie> anoDejacrees = new ArrayList<>();
        String nomSheet = "E30";

        // Test 1 - ano déja abandonnée - Retour normalement vide
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 305388");
        ano.setEtatLot(EtatLot.NOUVEAU);
        anoAcreer.add(ano);
        List<Anomalie> liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
        assertEquals(0, liste.size());

        // Test 2 - nouvelle ano - Retour normalement à 1
        ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 305128");
        ano.setEtatLot(EtatLot.NOUVEAU);
        ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 307128");
        ano.setEtatLot(EtatLot.NOUVEAU);

        anoAcreer.add(ano);
        liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
        assertEquals(1, liste.size());

        // Test 3 - feuille nulle - Retour normalement à 2
        removeSheet(nomSheet);
        liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
        assertEquals(2, liste.size());

        // Test 4 - feuille nulle - anomalie déjà créée
        removeSheet(nomSheet);
        ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 305129");
        ano.setEdition("E31");
        ano.setAction(TypeAction.ABANDONNER);
        anoDejacrees.add(ano);
        ano.setAction(TypeAction.RELANCER);
        anoDejacrees.add(ano);        
        liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
        assertEquals(2, liste.size());
        assertEquals(5, wb.getSheet(nomSheet).getPhysicalNumberOfRows());

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
        // Test 1 - On vérifie que la feuille renvoyée ne contient bien qu'une seule ligne.
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

        // Initialisation
        handler = PowerMockito.spy(handler);
        PowerMockito.doNothing().when(handler, "write");
        List<Anomalie> lotsEnAno = handler.recupDonneesDepuisExcel();
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 225727");
        lotsEnAno.add(ano);
        ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 156452");
        ano.setEtat("Close");
        lotsEnAno.add(ano);
        ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 239654");
        ano.setEtat("Abandonnée");
        lotsEnAno.add(ano);
        List<Anomalie> anoAajouter = new ArrayList<>();
        Set<String> lotsEnErreurSonar = new HashSet<>();
        Set<String> lotsSecurite = new HashSet<>();
        lotsSecurite.add("290318");
        lotsSecurite.add("225727");
        Set<String> lotsRelease = new HashSet<>();
        lotsRelease.add("225727");
        Sheet sheet = wb.createSheet();
        Matiere matiere = Matiere.JAVA;

        // Appel méthode sans écriture du fichier
        handler.majFeuillePrincipale(lotsEnAno, anoAajouter, lotsEnErreurSonar, lotsSecurite, lotsRelease, sheet, matiere);
    }

    @Test(expected = FunctionalException.class)
    public void testMajMultiMatiereException()
    {
        removeSheet(SQ);
        handler.majMultiMatiere(new ArrayList<>());
    }

    @Test
    public void testMajMultiMatiere()
    {
        // intialisation
        List<String> anoMultiple = new ArrayList<>();
        anoMultiple.add("258058");
        anoMultiple.add("10");
        anoMultiple.add("298491");

        // test
        handler.majMultiMatiere(anoMultiple);
    }

    @Test
    public void testCalculerCouleurLigne() throws Exception
    {
        // Vérification de al couleur de sortie de la méthode

        // Initialisation
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        String methode = "calculCouleurLigne";
        Set<String> lotsEnErreurSonar = new HashSet<>();
        String anoLot = "123456";

        // Test 1 = Vert
        ano.setEtat("");
        ano.setNumeroAnomalie(1);
        ano.calculTraitee();
        IndexedColors couleur = invokeMethod(handler, methode, ano, lotsEnErreurSonar, anoLot);
        assertEquals(IndexedColors.LIGHT_GREEN, couleur);

        // Test 3 = Blanc
        lotsEnErreurSonar.add(anoLot);
        couleur = invokeMethod(handler, methode, ano, lotsEnErreurSonar, anoLot);
        assertEquals(IndexedColors.WHITE, couleur);

        // Test 3 = Turquoise
        ano.setAction(TypeAction.ASSEMBLER);
        couleur = invokeMethod(handler, methode, ano, lotsEnErreurSonar, anoLot);
        assertEquals(IndexedColors.LIGHT_TURQUOISE, couleur);

        // Test 4 = Gris
        ano.setAction(TypeAction.VERIFIER);
        couleur = invokeMethod(handler, methode, ano, lotsEnErreurSonar, anoLot);
        assertEquals(IndexedColors.GREY_25_PERCENT, couleur);

        // Test 5 = Orange
        ano.setNumeroAnomalie(0);
        ano.calculTraitee();
        couleur = invokeMethod(handler, methode, ano, lotsEnErreurSonar, anoLot);
        assertEquals(IndexedColors.LIGHT_ORANGE, couleur);
    }
    
    @Test
    public void testGestionAction() throws Exception
    {
        // Initialisation
        String methode = "gestionAction";
        Logger logger = TestUtils.getMockLogger(ControlSuivi.class, "LOGGER");
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 123456");
        ano.setNumeroAnomalie(329000);
        ano.setAction(TypeAction.ABANDONNER);
        Sheet sheet = wb.getSheet(AC);
        String anoLot = "123456";
        
        // Test abandon avec anomalie en cours
        invokeMethod(handler, methode, ano, anoLot, sheet);
        Mockito.verify(logger, Mockito.times(1)).warn("L'anomalie 329000 n'a pas été clôturée. Impossible de la supprimer du fichier de suivi.");
        
        // Test clôture avec anomalie en cours
        ano.setAction(TypeAction.CLOTURER);
        invokeMethod(handler, methode, ano, anoLot, sheet);
        Mockito.verify(logger, Mockito.times(2)).warn("L'anomalie 329000 n'a pas été clôturée. Impossible de la supprimer du fichier de suivi.");

        // Test création anomalie avec mock de la création du Defect
        ControlRTC mock = Mockito.mock(ControlRTC.class);
        getField(ControlSuivi.class, "controlRTC").set(handler, mock);        
        ano.setNumeroAnomalie(0);
        ano.setAction(TypeAction.CREER);
        invokeMethod(handler, methode, ano, anoLot, sheet);
        
        // Test création anomalie avec simulation création anomalie
        Mockito.when(mock.creerDefect(ano)).thenReturn(1);
        invokeMethod(handler, methode, ano, anoLot, sheet);
        Mockito.verify(logger, Mockito.times(1)).info("Création anomalie 1 pour le lot 123456");        
    }

    @Test
    public void testAjouterAnomaliesCloses() throws Exception
    {
        // Vérification si une anomaie est bien rajoutée à la feuille des anomalies closes.

        // Initialisation
        Sheet sheet = wb.getSheet(AC);
        Map<String, Anomalie> anoClose = new HashMap<>();
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setNumeroAnomalie(307402);
        ano.setLot(LOT);
        ano.setEtatLot(EtatLot.NOUVEAU);
        anoClose.put(ano.getLot(), ano);
        ano = ModelFactory.getModel(Anomalie.class);
        ano.setNumeroAnomalie(0);
        ano.setLot("Lot 316089");
        anoClose.put(ano.getLot(), ano);

        // Appel méthode et controle
        int nbreLignes = sheet.getPhysicalNumberOfRows();
        invokeMethod(handler, "ajouterAnomaliesCloses", sheet, anoClose);
        assertEquals(nbreLignes + 1l, sheet.getPhysicalNumberOfRows());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException1() throws IllegalAccessException
    {
        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, Anomalie.class, IndexedColors.class);
        try
        {
            method.invoke(handler, null, ModelFactory.getModel(Anomalie.class), IndexedColors.AQUA);

        } catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException2() throws IllegalAccessException
    {
        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, Anomalie.class, IndexedColors.class);
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
        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, Anomalie.class, IndexedColors.class);
        try
        {
            method.invoke(handler, wb.getSheetAt(0).getRow(0), ModelFactory.getModel(Anomalie.class), null);

        } catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreerLigneSQException4() throws IllegalAccessException
    {
        Method method = getMethod(ControlSuivi.class, CREERLIGNESQ, Row.class, Anomalie.class, IndexedColors.class);
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

        // Vérification 1 seule ligne et nombre de colonnes bon
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
        String baseAdresse = "";
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
        // Initialisation
        Sheet sheet = wb.createSheet();
        String methode = "ajouterNouvellesAnos";
        List<Anomalie> anoAajouter = new ArrayList<>();
        Map<String, Anomalie> mapAnoCloses = new HashMap<>();
        Set<String> lotsSecurite = new HashSet<>();
        Set<String> lotsRelease = new HashSet<>();
        Matiere matiere = Matiere.JAVA;

        // Test 1. liste vide
        invokeMethod(handler, methode, sheet, anoAajouter, mapAnoCloses, lotsSecurite, lotsRelease, matiere);
        assertEquals(0, sheet.getPhysicalNumberOfRows());

        // Test 2. ni securite / ni release / ni close
        Anomalie ano1 = ModelFactory.getModel(Anomalie.class);
        ano1.setNumeroAnomalie(307402);
        ano1.setEtatLot(EtatLot.NOUVEAU);
        ano1.setLot(LOT);
        ano1.setMatieresString("JAVA");
        anoAajouter.add(ano1);
        invokeMethod(handler, methode, sheet, anoAajouter, mapAnoCloses, lotsSecurite, lotsRelease, matiere);
        assertEquals(1, sheet.getPhysicalNumberOfRows());
        assertEquals(today, ano1.getDateDetection());
        assertEquals("", ano1.getSecurite());
        assertEquals(SNAPSHOT, ano1.getVersion());

        // Test 3. securite / release
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 295711");
        anoAajouter.add(ano);
        lotsSecurite.add("295711");
        lotsRelease.add("295711");
        invokeMethod(handler, methode, sheet, anoAajouter, mapAnoCloses, lotsSecurite, lotsRelease, matiere);
        assertEquals(3, sheet.getPhysicalNumberOfRows());
        assertEquals(today, ano.getDateDetection());

        // Test 4. close
        Anomalie anoClose = ModelFactory.getModel(Anomalie.class);
        anoClose.setNumeroAnomalie(307402);
        anoClose.setLot(LOT);
        anoClose.setDateCreation(today);
        anoClose.setDateRelance(today);
        anoClose.setRemarque("remarque");
        mapAnoCloses.put(LOT, anoClose);
        invokeMethod(handler, methode, sheet, anoAajouter, mapAnoCloses, lotsSecurite, lotsRelease, matiere);
        assertEquals(5, sheet.getPhysicalNumberOfRows());
        assertEquals(anoClose.getNumeroAnomalie(), ano1.getNumeroAnomalie());
        assertEquals(anoClose.getLot(), ano1.getLot());
        assertEquals(anoClose.getNumeroAnomalie(), ano1.getNumeroAnomalie());
    }

    @Test
    public void testSaveAnomaliesCloses() throws Exception
    {
        // Initialisation
        Map<String, Anomalie> anoClose = new HashMap<>();

        // Test 1
        Sheet sheet = invokeMethod(handler, "saveAnomaliesCloses", anoClose);
        assertEquals(1, sheet.getPhysicalNumberOfRows());
        assertEquals(13, anoClose.size()); 

        // Test 2 - à partir d'une feuille vide
        removeSheet(AC);
        anoClose.clear();
        sheet = invokeMethod(handler, "saveAnomaliesCloses", anoClose);
        assertEquals(1, sheet.getPhysicalNumberOfRows());
        assertEquals(0, anoClose.size());
    }

    @Test
    public void testControleClarity() throws Exception
    {
        // Intialisation
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setProjetClarity("a");
        String methode = "controleClarity";

        // Test 1 - aucune correspondance
        invokeMethod(handler, methode, ano);
        assertEquals(Statics.INCONNU, ano.getDepartement());
        assertEquals(Statics.INCONNU, ano.getService());
        assertEquals(Statics.INCONNUE, ano.getDirection());

        // Test 2 - correspondance parfaite - données tirées du fichier excel
        ano.setProjetClarity("BDGREF047");
        invokeMethod(handler, methode, ano);
        assertEquals("Controle de gestion et pilotage", ano.getDepartement());
        assertEquals("Controle de gestion des filieres", ano.getService());
        assertEquals("FINANCE ACHATS LOGISTIQUE", ano.getDirection());

        // test 3 - correspondance trouvé avec algo de recherche du projet T le plus récent
        // On doit trouver les informations du projet T3004730E
        ano.setProjetClarity("T3004730");
        invokeMethod(handler, methode, ano);
        assertEquals("Risques Financier RH et CIS", ano.getDepartement());
        assertEquals("Service", ano.getService());
        assertEquals("DOMAINES REGALIENS", ano.getDirection());

        // test 4 - Correspondance avec les deux derniers caratères manquants
        ano.setProjetClarity("P00839");
        invokeMethod(handler, methode, ano);
        assertEquals("Risques Financier RH et CIS", ano.getDepartement());
        assertEquals("Financier", ano.getService());
        assertEquals("DOMAINES REGALIENS", ano.getDirection());
        
        // Test 5 - projet T mais trop long
        ano.setProjetClarity("T300473000");
        invokeMethod(handler, methode, ano);
        assertEquals(Statics.INCONNU, ano.getDepartement());
        assertEquals(Statics.INCONNU, ano.getService());
        assertEquals(Statics.INCONNUE, ano.getDirection());
        
        
    }

    @Test
    public void testControleChefDeService() throws Exception
    {
        // Initialisation
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        String methode = "controleChefDeService";

        // Test 1 nulle
        invokeMethod(handler, methode, ano);
        assertEquals("", ano.getSecurite());

        // Test 2 empty
        ano.setService("");
        assertEquals("", ano.getSecurite());

        // Test 3 ok
        ano.setService("Projets Credits");
        invokeMethod(handler, methode, ano);
        assertEquals("METROP-TAINTURIER, NATHALIE", ano.getResponsableService());

        // Test 4 loggin
        ano.setService("abc");
        ano.setResponsableService("abc");
        invokeMethod(handler, methode, ano);
        assertEquals("abc", ano.getResponsableService());

    }

    @Test
    @Override
    public void testInitEnum() throws IllegalAccessException
    {
        // Test - énumération du bon type
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

    private void removeSheet(String nomSheet)
    {
        wb.removeSheetAt(wb.getSheetIndex(wb.getSheet(nomSheet)));
    }
    /*---------- ACCESSEURS ----------*/
}