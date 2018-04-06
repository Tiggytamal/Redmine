package junit.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.TODAY;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.excel.ControlAno;
import junit.TestUtils;
import model.Anomalie;
import model.enums.Environnement;
import model.enums.Matiere;
import model.enums.TypeColSuivi;

public class ControlAnoTest
{
    private ControlAno handler;
    private File file;
    private Workbook wb;
    private static final String SNAPSHOT = "SNAPSHOT";
    private static final String RELEASE = "RELEASE";
    private static final String LOT = "Lot 10";
    
    @Before
    public void init() throws InvalidFormatException, IOException, IllegalArgumentException, IllegalAccessException
    {
        file = new File(getClass().getResource("/resources/Suivi_Quality_GateTest.xlsx").getFile());
        handler = new ControlAno(file);
        wb = (Workbook) Whitebox.getField(ControlAno.class, "wb").get(handler);
    }
    
    @Test
    public void listAnomaliesSurLotsCrees()
    {
        List<Anomalie> liste = handler.recupDonneesDepuisExcel();
        assertTrue(liste.size() == 80);
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
        
        // Test ano déja abandonnée - Retour normalement vide
        Anomalie ano = new Anomalie();
        ano.setLot("Lot 305388");
        ano.setEnvironnement(Environnement.NOUVEAU);
        anoAcreer.add(ano);
        List<Anomalie> liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
        assertTrue(liste.size() == 0);
        
        // Test nouvelle ano - Retour normalement à 1
        ano = new Anomalie();
        ano.setLot("Lot 305128");
        ano.setEnvironnement(Environnement.NOUVEAU);
        anoAcreer.add(ano);
        liste = handler.createSheetError(nomSheet, anoAcreer, anoDejacrees);
        assertTrue(liste.size() == 1);
    }
    
    @Test(expected = IOException.class)
    public void sauvegardeFichierException() throws IOException
    {
        // Envoi d'une IOException avec un nom non compatible.
        handler.sauvegardeFichier("@|(['{");      
    }
    
    @Test
    public void sauvegardeFichier() throws Exception
    {
        // On vérifie que la feuille renvoyée ne contient bien qu'une seule ligne.
        Sheet sheet = handler.sauvegardeFichier(file.getName());
        assertTrue(sheet.getPhysicalNumberOfRows() == 1);      
    }
    
    @Test
    public void calculerCouleurLigne() throws Exception
    {
        // Vérification de al couleur de sortie de la méthode
        
        // Initialisation
        Anomalie ano = new Anomalie();
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
        anoLot= LOT;
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
        
        // Test 4 = Gris
        ano.setEtat("A vérifier");
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
        Anomalie ano = new Anomalie();
        ano.setNumeroAnomalie(10);
        ano.setLot(LOT);
        ano.setEnvironnement(Environnement.NOUVEAU);
        anoClose.put(ano.getLot(), ano);
        
        // Appel méthode et controle
        int nbreLignes = sheet.getPhysicalNumberOfRows();
        Whitebox.invokeMethod(handler, "ajouterAnomaliesCloses", sheet, anoClose);
        assertEquals(nbreLignes +1, sheet.getPhysicalNumberOfRows());
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
    
    @Test (expected = IllegalArgumentException.class)
    public void ajouterLiensException() throws Exception
    {
        Cell cell = null;
        String baseAdresse = "adresse";
        String variable = "var";
        Whitebox.invokeMethod(handler, "ajouterLiens", cell, baseAdresse, variable);
    }
    
    @Test
    public void ajouterLiens() throws Exception
    {
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);       
        Cell cell = row.createCell(0);
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
        Anomalie ano = new Anomalie();
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
        Anomalie anoClose = new Anomalie();
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
        
        // Test
        Sheet sheet = Whitebox.invokeMethod(handler, "saveAnomaliesCloses", anoClose);
        assertTrue(sheet.getPhysicalNumberOfRows() == 1);
        assertTrue(anoClose.size() == 49);
    }
    
    @Test
    public void controleClarity() throws Exception
    {
        Anomalie ano = new Anomalie();
        Whitebox.invokeMethod(handler, "controleClarity", ano);
        ano.setProjetClarity("a");
    }
}