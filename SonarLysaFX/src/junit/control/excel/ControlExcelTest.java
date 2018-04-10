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

import control.excel.ControlExcel;
import control.excel.ControlSuivi;
import control.excel.ExcelFactory;
import model.enums.TypeCol;
import utilities.FunctionalException;

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
public abstract class ControlExcelTest<T extends Enum<T> & TypeCol, C extends ControlExcel<T, Y>, Y>
{
    /*---------- ATTRIBUTS ----------*/

    /** L'énumération du type de colonne */
    private Class<T> typeColClass;
    /** Le chemin d'accès au fichier Excel depuis les ressources */
    private String chemin;
    /** Le fichier Excel utilisé */
    protected File file;
    /** Le workbook gérant le fichier Excel */
    protected Workbook wb;
    /** Le controleur Excel */
    protected C handler;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur de la classe de test
     * 
     * @param typeColClass
     *            L'énumération correspondante au fichier
     * @param chemin
     *            Chemin d'accès au fichier de test dans les ressources
     */
    public ControlExcelTest(Class<T> typeColClass, String chemin)
    {
        this.typeColClass = typeColClass;
        this.chemin = chemin;
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
    public void init() throws InvalidFormatException, IOException, IllegalArgumentException, IllegalAccessException
    {
        file = new File(getClass().getResource(chemin).getFile());
        handler = ExcelFactory.getControlleur(typeColClass, file);
        wb = (Workbook) getField(handler.getClass(), "wb").get(handler);
    }
    
    @Test
    public void createWb() throws Exception
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
    public void initEnum() throws IllegalArgumentException, IllegalAccessException
    {
        // test - énumération du bon Type
        assertTrue(getField(ControlSuivi.class, "enumeration").get(handler).equals(typeColClass));
    }

    @Test
    public void majCouleurLigne() throws Exception
    {
        // Initilisation
        Row row = wb.getSheetAt(0).getRow(0);

        // Test 1 - changement couleur
        invokeMethod(handler, "majCouleurLigne", row, IndexedColors.AQUA);
        for (Cell cell : row)
        {
            assertTrue(cell.getCellStyle().getFillForegroundColor() == IndexedColors.AQUA.index);
        }

        // Test 2 - autre couleur
        invokeMethod(handler, "majCouleurLigne", row, IndexedColors.BROWN);
        for (Cell cell : row)
        {
            assertTrue(cell.getCellStyle().getFillForegroundColor() == IndexedColors.BROWN.index);
        }
    }

    @Test
    public void initSheet() throws Exception
    {
        // Test 1 - feuille ok
        Sheet sheet = invokeMethod(handler, "initSheet");
        assertTrue(sheet != null);
        assertTrue(sheet == wb.getSheetAt(0));
    }

    @Test (expected = FunctionalException.class)
    public void initSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        wb.removeSheetAt(0);
        invokeMethod(handler, "initSheet");
    }

    @Test (expected = IOException.class)
    public void close() throws Exception
    {
        // Test - appel de la méthode close.
        invokeMethod(handler, "close");
        
        // Appel méthode write  pour voir si le Workbook est bine fermé
        try
        {
            invokeMethod(handler, "write");
        }
        catch (IOException e)
        {
            assertTrue(e.getMessage().equals("Cannot write data, document seems to have been closed already"));
            throw e;
        }
    }
    
    @Test
    public void autosizeColumns() throws Exception
    {
        // Initialisation - Mock Sheet
        Sheet mock = Mockito.mock(Sheet.class);
        Mockito.when(mock.getRow(Mockito.anyInt())).thenReturn(wb.getSheetAt(0).getRow(0));
        
        // Test - vérifie qu'on appele bien la méthode autant de fois qu'il y a de colonnes
        invokeMethod(handler, "autosizeColumns", mock);
        Mockito.verify(mock, Mockito.times((int) getField(handler.getClass(), "maxIndice").get(handler) +1)).autoSizeColumn(Mockito.anyInt());
    }
    
    @Test
    public void testMax() throws Exception
    {
        // Test - Appel méthode avec différentes valeur. le maxIndice doit toujours avoir la valeur maximum
        
        // Valur initiale du nombre max = taille du nombre d'éléments de l'énumération
        int max = (int) getField(handler.getClass(), "maxIndice").get(handler);
        invokeMethod(handler, "testMax", max -2);
        assertEquals(max, getField(handler.getClass(), "maxIndice").get(handler));
        invokeMethod(handler, "testMax", 0);
        assertEquals(max, getField(handler.getClass(), "maxIndice").get(handler));
        invokeMethod(handler, "testMax", max +3);
        assertEquals(max + 3, getField(handler.getClass(), "maxIndice").get(handler));
        invokeMethod(handler, "testMax", max -1);
        assertEquals(max + 3, getField(handler.getClass(), "maxIndice").get(handler));
    }
    
    @Test
    public void getCellValueNotNull() throws Exception
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
    public void copierCelluleVide() throws Exception
    {
        // Initialisation
        Cell newCell = null;
        Cell oldCell = null;
        
        // test 1 - retour avec cellules vides.
        invokeMethod(handler, "copierCellule", newCell, oldCell);
        assertNull(newCell);
        assertNull(oldCell);
        
        newCell = wb.getSheetAt(0).getRow(0).getCell(0);
        invokeMethod(handler, "copierCellule", newCell, oldCell);
        assertNull(oldCell);  
        
        newCell = null;
        oldCell = wb.getSheetAt(0).getRow(0).getCell(0);
        invokeMethod(handler, "copierCellule", newCell, oldCell);
        assertNull(newCell);  
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * Méthode générique pour tester la méthode recupDonneesDepuisExcel. Il suffit d'injecter le contrôle de la taille
     * de la liste en paramètre
     * 
     * @param tailleListe
     */
    protected Y recupDonneesDepuisExcel(Function<Y, Boolean> tailleListe)
    {
        Y map = handler.recupDonneesDepuisExcel();
        assertTrue(map != null);
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
    protected void calculIndiceColonnes(int nbre) throws Exception
    {
        Sheet sheet = wb.getSheetAt(0);
        invokeMethod(handler, "calculIndiceColonnes", sheet);
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
