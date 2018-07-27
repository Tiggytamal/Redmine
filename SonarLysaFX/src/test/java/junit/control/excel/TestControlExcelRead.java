package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.reflect.Whitebox.getField;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.function.Function;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import control.excel.AbstractControlExcelRead;
import control.excel.ControlSuivi;
import control.excel.ExcelFactory;
import junit.JunitBase;
import model.enums.TypeColR;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe générale pour les Junits sur les controleurs Excel.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * @param <T>
 *            L'énumération correxpondante au type de colonnes des fichiers
 * @param <C>
 *            Le type du controleur
 * @param <Y>
 *            Le type de l'objet en valeur de la map
 */
public abstract class TestControlExcelRead<T extends Enum<T> & TypeColR, C extends AbstractControlExcelRead<T, Y>, Y> extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    /** L'énumération du type de colonne */
    private Class<T> typeColClass;
    /** Le chemin d'accès au fichier Excel depuis les ressources */
    private String fichier;
    /** Le fichier Excel utilisé */
    protected File file;
    /** Le workbook gérant le fichier Excel */
    protected Workbook wb;
    /** Le controleur Excel */
    protected C handler;
    
    private static final String MAXINDICE = "maxIndice";

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur de la classe de test
     * 
     * @param typeColClass
     *            L'énumération correspondante au fichier
     * @param fichier
     *            Nom du fichier dans les resources de test
     */
    public TestControlExcelRead(Class<T> typeColClass, String fichier)
    {
        super();
        this.typeColClass = typeColClass;
        this.fichier = fichier;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Méthode d'initialisation des tests
     * 
     * @throws InvalidFormatException
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Before
    public void init() throws IOException, IllegalAccessException
    {
        file = new File(getClass().getResource(Statics.ROOT + fichier).getFile());
        handler = ExcelFactory.getReader(typeColClass, file);
        wb = (Workbook) getField(handler.getClass(), "wb").get(handler);
    }
    
    @Test (expected = TechnicalException.class)
    public void testInitException() throws Exception
    {
        // Initialisation handler avec mauvais format de fichier
        handler = ExcelFactory.getReader(typeColClass, new File("12:;"));
    }
    
    @Test
    public void testCreateWb() throws Exception
    {
        // Appel méthode
        invokeMethod(handler, "createWb");
        
        // Test initialisation des données
        assertNotNull(getField(handler.getClass(), "wb").get(handler));
        assertNotNull(getField(handler.getClass(), "helper").get(handler));
        assertNotNull(getField(handler.getClass(), "createHelper").get(handler));
        assertNotNull(getField(handler.getClass(), "ca").get(handler));
    }

    @Test
    public void testInitEnum() throws IllegalAccessException
    {
        // test - énumération du bon Type
        assertEquals(typeColClass, getField(ControlSuivi.class, "enumeration").get(handler)); 
    }

    @Test
    public void testMajCouleurLigne() throws Exception
    {
        // Initilisation
        Row row = wb.getSheetAt(0).getRow(0);

        // Test 1 - changement couleur
        invokeMethod(handler, "majCouleurLigne", row, IndexedColors.AQUA);
        for (Cell cell : row)
        {
            assertEquals(IndexedColors.AQUA.index, cell.getCellStyle().getFillForegroundColor());
        }

        // Test 2 - autre couleur
        invokeMethod(handler, "majCouleurLigne", row, IndexedColors.BROWN);
        for (Cell cell : row)
        {
            assertEquals(IndexedColors.BROWN.index, cell.getCellStyle().getFillForegroundColor());
        }
    }

    @Test
    public void testInitSheet() throws Exception
    {
        // Test 1 - feuille ok
        Sheet sheet = invokeMethod(handler, "initSheet");
        assertNotNull(sheet);
        assertEquals(wb.getSheetAt(0), sheet);
    }

    @Test (expected = FunctionalException.class)
    public void testInitSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        wb.removeSheetAt(0);
        invokeMethod(handler, "initSheet");
    }

    @Test (expected = TechnicalException.class)
    public void testClose() throws Exception
    {
        // Test - appel de la méthode close.
        invokeMethod(handler, "close");
        
        // Appel méthode write  pour voir si le Workbook est bien fermé
        invokeMethod(handler, "write");
    }
    
    @Test
    public void testAutosizeColumns() throws Exception
    {
        // Initialisation - Mock Sheet
        Sheet mock = Mockito.mock(Sheet.class);
        Mockito.when(mock.getRow(Mockito.anyInt())).thenReturn(wb.getSheetAt(0).getRow(0));
        
        // Test - vérifie qu'on appele bien la méthode autant de fois qu'il y a de colonnes
        invokeMethod(handler, "autosizeColumns", mock);
        Mockito.verify(mock, Mockito.times((int) getField(handler.getClass(), MAXINDICE).get(handler) +1)).autoSizeColumn(Mockito.anyInt());
    }
    
    @Test
    public void testTestMax() throws Exception
    {
        String methode = "testMax";
        
        // Test - Appel méthode avec différentes valeur. le maxIndice doit toujours avoir la valeur maximum
        
        // Valur initiale du nombre max = taille du nombre d'éléments de l'énumération
        int max = (int) getField(handler.getClass(), MAXINDICE).get(handler);
        invokeMethod(handler, methode, max -2);
        assertEquals(max, getField(handler.getClass(), MAXINDICE).get(handler));
        invokeMethod(handler, methode, 0);
        assertEquals(max, getField(handler.getClass(), MAXINDICE).get(handler));
        invokeMethod(handler, methode, max +3);
        assertEquals(max + 3, getField(handler.getClass(), MAXINDICE).get(handler));
        invokeMethod(handler, methode, max -1);
        assertEquals(max + 3, getField(handler.getClass(), MAXINDICE).get(handler));
    }
    
    @Test
    public void testGetCellValueNotNull() throws Exception
    {        
        for (Sheet sheet : wb)
        {
            for (Row row : sheet)
            {
                for (Cell cell : row)
                {
                    assertNotNull(invokeMethod(handler, "getCellStringValue", row, cell.getColumnIndex()));
                    assertNotNull(invokeMethod(handler, "getCellFormulaValue", row, cell.getColumnIndex()));
                    assertNotNull(invokeMethod(handler, "getCellNumericValue", row, cell.getColumnIndex()));
                }               
            }
        }        
    }
        
    @Test
    public void testCopierCelluleVide() throws Exception
    {
        String methode = "copierCellule";
        // Initialisation
        Cell newCell = null;
        Cell oldCell = null;
        
        // test 1 - retour avec cellules vides.
        invokeMethod(handler, methode, newCell, oldCell);
        assertNull(newCell);
        assertNull(oldCell);
        
        newCell = wb.getSheetAt(0).getRow(0).getCell(0);
        invokeMethod(handler, methode, newCell, oldCell);
        assertNull(oldCell);  
        
        newCell = null;
        oldCell = wb.getSheetAt(0).getRow(0).getCell(0);
        invokeMethod(handler, methode, newCell, oldCell);
        assertNull(newCell);  
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * Méthode générique pour tester la méthode recupDonneesDepuisExcel. Il suffit d'injecter le contrôle de la taille
     * de la liste en paramètre
     * 
     * @param tailleListe
     */
    protected Y testRecupDonneesDepuisExcel(Function<Y, Boolean> tailleListe)
    {
        Y map = handler.recupDonneesDepuisExcel();
        assertNotNull(map);
        assertTrue(tailleListe.apply(map));
        return map;
    }
    
    /**
     * Itère sur tous les indices de colonnes, et remonte le nombre toujours initialisées à zéro. <br>
     * Controle par rapport au nombre de colonnes prévues (soit 0 ou 1).<br>
     * Les indices des colonnes commencent toujours par col.
     * 
     * @param nbre
     *          nombre de colonnes initilisées à zéro prévues
     * @throws Exception
     */
    protected void testCalculIndiceColonnes(int nbre) throws Exception
    {
        invokeMethod(handler, "calculIndiceColonnes");
        int nbrecolA0 = 0;
        for (Field field : handler.getClass().getDeclaredFields())
        {
            field.setAccessible(true);
            if (field.getName().startsWith("col") && (int) field.get(handler) == 0)
                nbrecolA0++;
        }
        assertEquals(nbre, nbrecolA0);
    }

    /*---------- ACCESSEURS ----------*/
}
