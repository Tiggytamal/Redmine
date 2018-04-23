package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.TODAY;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.powermock.reflect.Whitebox;

import control.excel.ControlSuivi;
import control.rtc.ControlRTC;
import junit.TestUtils;
import model.Anomalie;
import model.ModelFactory;
import model.enums.Environnement;
import model.enums.Matiere;
import model.enums.TypeAction;
import model.enums.TypeColSuivi;
import utilities.FunctionalException;

public class TestControlSuivi extends TestControlExcel<TypeColSuivi, ControlSuivi, List<Anomalie>>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String SNAPSHOT = "SNAPSHOT";
    private static final String RELEASE = "RELEASE";
    private static final String LOT = "Lot 10";
    private static final String SQ = "SUIVI Qualité";
    private static final String AC = "Anomalies closes";

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlSuivi()
    {
        super(TypeColSuivi.class, "/resources/Suivi_Quality_GateTest.xlsx");
        ControlRTC.INSTANCE.connexion();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void recupDonneesDepuisExcel()
    {
        recupDonneesDepuisExcel((map) -> map.size() == 79);
    }

    @Test
    public void controleKey() throws Exception
    {
        assertTrue(TestUtils.callPrivate("controleKey", handler, Boolean.class, "a", "a"));
        assertTrue(TestUtils.callPrivate("controleKey", handler, Boolean.class, "A", "a"));
        assertFalse(TestUtils.callPrivate("controleKey", handler, Boolean.class, "A", "b"));
        assertTrue(TestUtils.callPrivate("controleKey", handler, Boolean.class, "BEF000", "BEF0009"));
        assertTrue(TestUtils.callPrivate("controleKey", handler, Boolean.class, "T7004360", "T7004360E"));
    }

    @Test
    public void createSheetError()
    {
        // Intialisation
        List<Anomalie> anoAcreer = new ArrayList<>();
        List<Anomalie> anoDejacrees = new ArrayList<>();
        String nomSheet = "E30";

        // Test 1 - ano déja abandonnée - Retour normalement vide
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 305388");
        ano.setEnvironnement(Environnement.NOUVEAU);
        anoAcreer.add(ano);
        List<Anomalie> liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
        assertTrue(liste.size() == 0);

        // Test 2 - nouvelle ano - Retour normalement à 1
        ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 305128");
        ano.setEnvironnement(Environnement.NOUVEAU);
        anoAcreer.add(ano);
        liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
        assertTrue(liste.size() == 1);

        // Test 3 - feuille nulle - Retour normalement à 2
        removeSheet(nomSheet);
        liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
        assertTrue(liste.size() == 2);

        // Test 4 - feuille nulle - anomalie déjà créée
        removeSheet(nomSheet);
        ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 305129");
        ano.setEdition("E31");
        anoDejacrees.add(ano);
        liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
        assertTrue(wb.getSheet(nomSheet).getPhysicalNumberOfRows() == 4);

    }

    @Test(expected = IOException.class)
    public void sauvegardeFichierException1() throws IOException
    {
        // Envoi d'une IOException avec un nom non compatible.
        handler.sauvegardeFichier("@|(['{");
    }

    @Test
    public void sauvegardeFichier() throws Exception
    {
        // Test 1 - On vérifie que la feuille renvoyée ne contient bien qu'une seule ligne.
        Sheet sheet = handler.sauvegardeFichier(file.getName());
        assertTrue(sheet.getPhysicalNumberOfRows() == 1);

        // Test 2 - test avec une feuille nulle
        removeSheet(SQ);
        sheet = handler.sauvegardeFichier(file.getName());
        assertTrue(sheet.getPhysicalNumberOfRows() == 1);
    }
    
    @Test
    public void majFeuillePrincipale() throws Exception
    {
        // Initialisation
        handler = PowerMockito.spy(handler);
        PowerMockito.doNothing().when(handler, "write");
        List<Anomalie> lotsEnAno = new ArrayList<>();
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 290318");
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
        Set<String> lotsRelease = new HashSet<>(); 
        Sheet sheet = wb.createSheet();
        Matiere matiere = Matiere.JAVA;
        
        // Appel méthode sans écriture du fichier
        handler.majFeuillePrincipale(lotsEnAno, anoAajouter, lotsEnErreurSonar, lotsSecurite, lotsRelease, sheet, matiere);
    }

    @Test(expected = FunctionalException.class)
    public void majMultiMatiereException() throws IOException
    {
        removeSheet(SQ);
        handler.majMultiMatiere(new ArrayList<>());
    }

    @Test
    public void majMultiMatiere() throws IOException
    {
        // intialisation
        List<String> anoMultiple = new ArrayList<>();
        anoMultiple.add("258058");
        anoMultiple.add("10");
        anoMultiple.add("123456");

        // test
        handler.majMultiMatiere(anoMultiple);
    }

    @Test
    public void calculerCouleurLigne() throws Exception
    {
        // Vérification de al couleur de sortie de la méthode

        // Initialisation
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        Set<String> lotsEnErreurSonar = new HashSet<>();
        String anoLot = "";
        Set<String> lotsRelease = new HashSet<>();

        // Test 1 = Vert
        ano.setEtat("");
        ano.setNumeroAnomalie(1);
        ano.calculTraitee();
        IndexedColors couleur = Whitebox.invokeMethod(handler, "calculCouleurLigne", ano, lotsEnErreurSonar, anoLot, lotsRelease);
        assertEquals(IndexedColors.LIGHT_GREEN, couleur);

        // Test 2 = Jaune
        anoLot = LOT;
        lotsRelease.add(anoLot);
        lotsEnErreurSonar.add(anoLot);
        couleur = Whitebox.invokeMethod(handler, "calculCouleurLigne", ano, lotsEnErreurSonar, anoLot, lotsRelease);
        assertEquals(IndexedColors.LIGHT_YELLOW, couleur);
        assertEquals(RELEASE, ano.getVersion());

        // Test 3 = Blanc
        lotsRelease.clear();
        couleur = Whitebox.invokeMethod(handler, "calculCouleurLigne", ano, lotsEnErreurSonar, anoLot, lotsRelease);
        assertEquals(IndexedColors.WHITE, couleur);
        assertEquals(SNAPSHOT, ano.getVersion());
        
        // Test 3 = Torquoise
        lotsRelease.clear();
        ano.setAction(TypeAction.ASSEMBLER);
        couleur = Whitebox.invokeMethod(handler, "calculCouleurLigne", ano, lotsEnErreurSonar, anoLot, lotsRelease);
        assertEquals(IndexedColors.LIGHT_TURQUOISE, couleur);
        assertEquals(SNAPSHOT, ano.getVersion());

        // Test 4 = Gris
        ano.setAction(TypeAction.VERIFIER);
        couleur = Whitebox.invokeMethod(handler, "calculCouleurLigne", ano, lotsEnErreurSonar, anoLot, lotsRelease);
        assertEquals(IndexedColors.GREY_25_PERCENT, couleur);

        // Test 5 = Orange
        ano.setNumeroAnomalie(0);
        ano.calculTraitee();
        couleur = Whitebox.invokeMethod(handler, "calculCouleurLigne", ano, lotsEnErreurSonar, anoLot, lotsRelease);
        assertEquals(IndexedColors.LIGHT_ORANGE, couleur);
    }

    @Test
    public void ajouterAnomaliesCloses() throws Exception
    {
        // Vérification si une anomaie est bien rajoutée à la feuille des anomalies closes.

        // Initialisation
        Sheet sheet = wb.getSheet("Anomalies closes");
        Map<String, Anomalie> anoClose = new HashMap<>();
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setNumeroAnomalie(10);
        ano.setLot(LOT);
        ano.setEnvironnement(Environnement.NOUVEAU);
        anoClose.put(ano.getLot(), ano);
        ano = ModelFactory.getModel(Anomalie.class);
        ano.setNumeroAnomalie(0);
        ano.setLot("Lot 20");
        anoClose.put(ano.getLot(), ano);

        // Appel méthode et controle
        int nbreLignes = sheet.getPhysicalNumberOfRows();
        Whitebox.invokeMethod(handler, "ajouterAnomaliesCloses", sheet, anoClose);
        assertEquals(nbreLignes + 1, sheet.getPhysicalNumberOfRows());
    }

    @Test(expected = IllegalArgumentException.class)
    public void creerLigneSQException1() throws Throwable
    {
        Method method = Whitebox.getMethod(ControlSuivi.class, "creerLigneSQ", Row.class, Anomalie.class, IndexedColors.class);
        try
        {
            method.invoke(handler, wb.getSheetAt(0).getRow(0), null, IndexedColors.AQUA);

        } catch (InvocationTargetException e)
        {
            throw e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void creerLigneSQException2() throws Throwable
    {
        Method method = Whitebox.getMethod(ControlSuivi.class, "creerLigneSQ", Row.class, Anomalie.class, IndexedColors.class);
        try
        {
            method.invoke(handler, wb.getSheetAt(0).getRow(0), null, IndexedColors.AQUA);
        } catch (InvocationTargetException e)
        {
            throw e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void creerLigneSQException3() throws Throwable
    {
        Method method = Whitebox.getMethod(ControlSuivi.class, "creerLigneSQ", Row.class, Anomalie.class, IndexedColors.class);
        try
        {
            method.invoke(handler, wb.getSheetAt(0).getRow(0), ModelFactory.getModel(Anomalie.class), null);

        } catch (InvocationTargetException e)
        {
            throw e.getCause();
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void creerLigneSQException4() throws Throwable
    {
        Method method = Whitebox.getMethod(ControlSuivi.class, "creerLigneSQ", Row.class, Anomalie.class, IndexedColors.class);
        try
        {
            method.invoke(handler, null, null, null);

        } catch (InvocationTargetException e)
        {
            throw e.getCause();
        }
    }

    @Test
    public void creerLigneTitres() throws Exception
    {
        Sheet sheet = wb.createSheet();

        // Vérification 1 seule ligne et nombre de colonnes bon
        Whitebox.invokeMethod(handler, "creerLigneTitres", sheet);
        assertTrue(sheet.getPhysicalNumberOfRows() == 1);
        assertTrue(sheet.getRow(0).getPhysicalNumberOfCells() == TypeColSuivi.values().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ajouterLiensException1() throws Exception
    {
        Cell cell = null;
        String baseAdresse = "adresse";
        String variable = "var";
        Whitebox.invokeMethod(handler, "ajouterLiens", cell, baseAdresse, variable);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void ajouterLiensException2() throws Exception
    {
        Cell cell = wb.createSheet().createRow(0).createCell(0);
        String baseAdresse = null;
        String variable = "var";
        Whitebox.invokeMethod(handler, "ajouterLiens", cell, baseAdresse, variable);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void ajouterLiensException3() throws Exception
    {
        Cell cell = wb.createSheet().createRow(0).createCell(0);
        String baseAdresse = "";
        String variable = "var";
        Whitebox.invokeMethod(handler, "ajouterLiens", cell, baseAdresse, variable);
    }

    @Test
    public void ajouterLiens() throws Exception
    {
        Cell cell = wb.createSheet().createRow(0).createCell(0);
        String baseAdresse = "adresse";
        String variable = "var";
        Whitebox.invokeMethod(handler, "ajouterLiens", cell, baseAdresse, variable);
        assertEquals(baseAdresse + variable, cell.getHyperlink().getAddress());
    }

    @Test
    public void ajouterNouvellesAnos() throws Exception
    {
        // Initialisation
        Sheet sheet = wb.createSheet();
        List<Anomalie> anoAajouter = new ArrayList<>();
        Map<String, Anomalie> mapAnoCloses = new HashMap<>();
        Set<String> lotsSecurite = new HashSet<>();
        Set<String> lotsRelease = new HashSet<>();
        Matiere matiere = Matiere.JAVA;

        // Test 1. liste vide
        Whitebox.invokeMethod(handler, "ajouterNouvellesAnos", sheet, anoAajouter, mapAnoCloses, lotsSecurite, lotsRelease, matiere);
        assertTrue(sheet.getPhysicalNumberOfRows() == 0);

        // Test 2. ni securite / ni release / ni close
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setNumeroAnomalie(10);
        ano.setEnvironnement(Environnement.NOUVEAU);
        ano.setLot(LOT);
        ano.setMatieresString("JAVA");
        anoAajouter.add(ano);
        Whitebox.invokeMethod(handler, "ajouterNouvellesAnos", sheet, anoAajouter, mapAnoCloses, lotsSecurite, lotsRelease, matiere);
        assertTrue(sheet.getPhysicalNumberOfRows() == 1);
        assertEquals(TODAY, ano.getDateDetection());
        assertEquals("", ano.getSecurite());
        assertEquals(SNAPSHOT, ano.getVersion());

        // Test 3. securite / release
        lotsSecurite.add("10");
        lotsRelease.add("10");
        Whitebox.invokeMethod(handler, "ajouterNouvellesAnos", sheet, anoAajouter, mapAnoCloses, lotsSecurite, lotsRelease, matiere);
        assertTrue(sheet.getPhysicalNumberOfRows() == 2);
        assertEquals(TODAY, ano.getDateDetection());
        assertEquals("X", ano.getSecurite());
        assertEquals(RELEASE, ano.getVersion());

        // Test 4. close
        Anomalie anoClose = ModelFactory.getModel(Anomalie.class);
        anoClose.setNumeroAnomalie(20);
        anoClose.setLot(LOT);
        anoClose.setDateCreation(TODAY);
        anoClose.setDateRelance(TODAY);
        anoClose.setRemarque("remarque");
        mapAnoCloses.put(LOT, anoClose);
        Whitebox.invokeMethod(handler, "ajouterNouvellesAnos", sheet, anoAajouter, mapAnoCloses, lotsSecurite, lotsRelease, matiere);
        assertTrue(sheet.getPhysicalNumberOfRows() == 3);
        assertEquals(anoClose.getNumeroAnomalie(), ano.getNumeroAnomalie());
        assertEquals(anoClose.getLot(), ano.getLot());
        assertEquals(anoClose.getNumeroAnomalie(), ano.getNumeroAnomalie());
        assertEquals(anoClose.getDateCreation(), ano.getDateCreation());
        assertEquals(anoClose.getDateRelance(), ano.getDateRelance());
    }

    @Test
    public void saveAnomaliesCloses() throws Exception
    {
        // Initialisation
        Map<String, Anomalie> anoClose = new HashMap<>();

        // Test 1
        Sheet sheet = Whitebox.invokeMethod(handler, "saveAnomaliesCloses", anoClose);
        assertTrue(sheet.getPhysicalNumberOfRows() == 1);
        System.out.println(anoClose.size());
        assertTrue(anoClose.size() == 49);

        // Test 2 - à partir d'une feuille vide
        removeSheet(AC);
        anoClose.clear();
        sheet = Whitebox.invokeMethod(handler, "saveAnomaliesCloses", anoClose);
        assertTrue(sheet.getPhysicalNumberOfRows() == 1);
        assertTrue(anoClose.size() == 0);
    }

    @Test
    public void controleClarity() throws Exception
    {
        // Intialisation
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setProjetClarity("a");

        // Test 1 - aucune correspondance
        Whitebox.invokeMethod(handler, "controleClarity", ano);
        assertEquals("", ano.getDepartement());
        assertEquals("", ano.getService());
        assertEquals("", ano.getDirection());

        // Test 2 - correspondance parfaite - données tirées du fichier excel
        ano.setProjetClarity("BT097902");
        Whitebox.invokeMethod(handler, "controleClarity", ano);
        assertEquals("GESTION DES SERVICES DEVOPS", ano.getDepartement());
        assertEquals("Gestion projets techniques Ouest", ano.getService());
        assertEquals("DEVOPS", ano.getDirection());

        // test 3 - correspondance trouvé avec algo de recherche du projet T le plus récent
        // On doit trouver les informations du projet T3004736E
        ano.setProjetClarity("T300473");
        Whitebox.invokeMethod(handler, "controleClarity", ano);
        assertEquals("Distribution Ouest et Marches specialises", ano.getDepartement());
        assertEquals("Distribution Ouest", ano.getService());
        assertEquals("DOMAINE DISTRIBUTION ET OUTILS SOCLES", ano.getDirection());

        // test 4 - Correspondance avec les deux derniers caratères manquants
        ano.setProjetClarity("P00839");
        Whitebox.invokeMethod(handler, "controleClarity", ano);
        assertEquals("Risques Financier RH et CIS", ano.getDepartement());
        assertEquals("Financier", ano.getService());
        assertEquals("DOMAINES REGALIENS", ano.getDirection());
    }

    @Test
    public void controleChefDeService() throws Exception
    {
        // Initialisation
        Anomalie ano = ModelFactory.getModel(Anomalie.class);

        // Test 1 nulle
        Whitebox.invokeMethod(handler, "controleChefDeService", ano);
        assertTrue(ano.getSecurite() == "");

        // Test 2 empty
        ano.setService("");
        assertTrue(ano.getSecurite() == "");

        // Test 3 ok
        ano.setService("Projets Credits");
        Whitebox.invokeMethod(handler, "controleChefDeService", ano);
        assertEquals("METROP-TAINTURIER, NATHALIE", ano.getResponsableService());

        // Test 4 loggin
        ano.setService("abc");
        ano.setResponsableService("abc");
        Whitebox.invokeMethod(handler, "controleChefDeService", ano);
        assertEquals("abc", ano.getResponsableService());

    }

    @Test
    public void initEnum() throws IllegalArgumentException, IllegalAccessException
    {
        // Test - énumération du bon type
        assertTrue(Whitebox.getField(ControlSuivi.class, "enumeration").get(handler).equals(TypeColSuivi.class));
    }

    @Test
    public void initSheet() throws Exception
    {
        // Test 1 - feuille ok
        Sheet sheet = Whitebox.invokeMethod(handler, "initSheet");
        assertTrue(sheet != null);
        assertTrue(sheet == wb.getSheet(SQ));
    }

    @Test(expected = FunctionalException.class)
    public void initSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        removeSheet(SQ);
        Whitebox.invokeMethod(handler, "initSheet");
    }

    /*---------- METHODES PRIVEES ----------*/

    private void removeSheet(String nomSheet)
    {
        wb.removeSheetAt(wb.getSheetIndex(wb.getSheet(nomSheet)));
    }
    /*---------- ACCESSEURS ----------*/
}