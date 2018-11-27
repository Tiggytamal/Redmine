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
 * Classe g�n�rale pour les Junits sur les controleurs Excel.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * @param <T>
 *            L'�num�ration correxpondante au type de colonnes des fichiers
 * @param <C>
 *            Le type du controleur
 * @param <Y>
 *            Le type de l'objet en valeur de la map
 */
public abstract class TestControlExcelRead<T extends Enum<T> & TypeColR, C extends AbstractControlExcelRead<T, Y>, Y> extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    /** L'�num�ration du type de colonne */
    private Class<T> typeColClass;
    /** Le chemin d'acc�s au fichier Excel depuis les ressources */
    private String fichier;
    /** Le fichier Excel utilis� */
    protected File file;
    /** Le workbook g�rant le fichier Excel */
    protected Workbook wb;
    /** Le controleur Excel */
    protected C controlTest;
    
    private static final String MAXINDICE = "maxIndice";

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur de la classe de test
     * 
     * @param typeColClass
     *            L'�num�ration correspondante au fichier
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
     * M�thode d'initialisation des tests
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
        controlTest = ExcelFactory.getReader(typeColClass, file);
        wb = (Workbook) getField(controlTest.getClass(), "wb").get(controlTest);
        initOther();
    }
    
    @Test (expected = TechnicalException.class)
    public void testInitException() throws Exception
    {
        // Initialisation handler avec mauvais format de fichier
        controlTest = ExcelFactory.getReader(typeColClass, new File("12:;"));
    }
    
    @Test
    public void testCreateWb() throws Exception
    {
        // Appel m�thode
        invokeMethod(controlTest, "createWb");
        
        // Test initialisation des donn�es
        assertNotNull(getField(controlTest.getClass(), "wb").get(controlTest));
        assertNotNull(getField(controlTest.getClass(), "helper").get(controlTest));
        assertNotNull(getField(controlTest.getClass(), "createHelper").get(controlTest));
        assertNotNull(getField(controlTest.getClass(), "ca").get(controlTest));
    }

    @Test
    public void testInitEnum() throws IllegalAccessException
    {
        // test - �num�ration du bon Type
        assertEquals(typeColClass, getField(ControlSuivi.class, "enumeration").get(controlTest)); 
    }

    @Test
    public void testMajCouleurLigne() throws Exception
    {
        // Initilisation
        Row row = wb.getSheetAt(0)
                .getRow(0);

        // Test 1 - changement couleur
        invokeMethod(controlTest, "majCouleurLigne", row, IndexedColors.AQUA);
        for (Cell cell : row)
        {
            assertEquals(IndexedColors.AQUA.index, cell.getCellStyle().getFillForegroundColor());
        }

        // Test 2 - autre couleur
        invokeMethod(controlTest, "majCouleurLigne", row, IndexedColors.BROWN);
        for (Cell cell : row)
        {
            assertEquals(IndexedColors.BROWN.index, cell.getCellStyle().getFillForegroundColor());
        }
    }

    @Test
    public void testInitSheet() throws Exception
    {
        // Test 1 - feuille ok
        Sheet sheet = invokeMethod(controlTest, "initSheet");
        assertNotNull(sheet);
        assertEquals(wb.getSheetAt(0), sheet);
    }

    // Peut-�tre surcharg� en cas de modification de l'initialisation des feuilles excel.
    @Test (expected = FunctionalException.class)
    public void testInitSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        wb.removeSheetAt(0);
        invokeMethod(controlTest, "initSheet");
    }
    
    @Test
    public void testAutosizeColumns() throws Exception
    {
        // Initialisation - Mock Sheet
        Sheet mock = Mockito.mock(Sheet.class);
        Mockito.when(mock.getRow(Mockito.anyInt())).thenReturn(wb.getSheetAt(0).getRow(0));
        
        // Test - v�rifie qu'on appele bien la m�thode autant de fois qu'il y a de colonnes
        invokeMethod(controlTest, "autosizeColumns", mock);
        Mockito.verify(mock, Mockito.times((int) getField(controlTest.getClass(), MAXINDICE).get(controlTest) +1)).autoSizeColumn(Mockito.anyInt());
    }
    
    @Test
    public void testTestMax() throws Exception
    {
        String methode = "testMax";
        
        // Test - Appel m�thode avec diff�rentes valeur. le maxIndice doit toujours avoir la valeur maximum
        
        // Valur initiale du nombre max = taille du nombre d'�l�ments de l'�num�ration
        int max = (int) getField(controlTest.getClass(), MAXINDICE).get(controlTest);
        invokeMethod(controlTest, methode, max -2);
        assertEquals(max, getField(controlTest.getClass(), MAXINDICE).get(controlTest));
        invokeMethod(controlTest, methode, 0);
        assertEquals(max, getField(controlTest.getClass(), MAXINDICE).get(controlTest));
        invokeMethod(controlTest, methode, max +3);
        assertEquals(max + 3, getField(controlTest.getClass(), MAXINDICE).get(controlTest));
        invokeMethod(controlTest, methode, max -1);
        assertEquals(max + 3, getField(controlTest.getClass(), MAXINDICE).get(controlTest));
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
                    assertNotNull(invokeMethod(controlTest, "getCellStringValue", row, cell.getColumnIndex()));
                    assertNotNull(invokeMethod(controlTest, "getCellFormulaValue", row, cell.getColumnIndex()));
                    assertNotNull(invokeMethod(controlTest, "getCellNumericValue", row, cell.getColumnIndex()));
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
        invokeMethod(controlTest, methode, newCell, oldCell);
        assertNull(newCell);
        assertNull(oldCell);
        
        newCell = wb.getSheetAt(0).getRow(0).getCell(0);
        invokeMethod(controlTest, methode, newCell, oldCell);
        assertNull(oldCell);  
        
        newCell = null;
        oldCell = wb.getSheetAt(0).getRow(0).getCell(0);
        invokeMethod(controlTest, methode, newCell, oldCell);
        assertNull(newCell);  
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * Permet d'initialisaer d'autres objets
     */
    protected void initOther()
    {
        // Pas d'initialisation par d�faut
    }
    
    /**
     * M�thode g�n�rique pour tester la m�thode recupDonneesDepuisExcel. Il suffit d'injecter le contr�le de la taille
     * de la liste en param�tre
     * 
     * @param tailleListe
     */
    protected Y testRecupDonneesDepuisExcel(Function<Y, Boolean> tailleListe)
    {
        Y map = controlTest.recupDonneesDepuisExcel();
        assertNotNull(map);
        assertTrue(tailleListe.apply(map));
        return map;
    }
    
    /**
     * It�re sur tous les indices de colonnes, et remonte le nombre toujours initialis�es � z�ro. <br>
     * Controle par rapport au nombre de colonnes pr�vues (soit 0 ou 1).<br>
     * Les indices des colonnes commencent toujours par col.
     * 
     * @param nbre
     *          nombre de colonnes initilis�es � z�ro pr�vues
     * @throws Exception
     */
    protected void testCalculIndiceColonnes(int nbre) throws Exception
    {
        invokeMethod(controlTest, "calculIndiceColonnes");
        int nbrecolA0 = 0;
        for (Field field : controlTest.getClass().getDeclaredFields())
        {
            field.setAccessible(true);
            if (field.getName().startsWith("col") && (int) field.get(controlTest) == 0)
                nbrecolA0++;
        }
        assertEquals(nbre, nbrecolA0);
    }

    /*---------- ACCESSEURS ----------*/
}
