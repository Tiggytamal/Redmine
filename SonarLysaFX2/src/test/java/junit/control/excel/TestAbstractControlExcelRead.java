package junit.control.excel;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.xml.bind.JAXBException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import control.excel.AbstractControlExcelRead;
import control.excel.ExcelFactory;
import junit.AutoDisplayName;
import model.enums.ColR;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public abstract class TestAbstractControlExcelRead<T extends Enum<T> & ColR, C extends AbstractControlExcelRead<T, Y>, Y> extends TestAbstractControlExcel<T, C>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String MAXINDICE = "maxIndice";
    private static final String GETCELLFORMULAVALUE = "getCellFormulaValue";

    /*---------- CONSTRUCTEURS ----------*/

    public TestAbstractControlExcelRead(Class<T> colClass, String fichier)
    {
        super(colClass, fichier);
        this.colClass = colClass;
        this.fichier = fichier;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @BeforeEach
    public void init() throws IOException, IllegalAccessException, JAXBException
    {
        initDataBase();
        file = new File(getClass().getResource(Statics.ROOT + fichier).getFile());
        objetTest = ExcelFactory.getReader(colClass, file);
        wb = (Workbook) getField("wb");
        initOther();
    }

    @Test
    public void testInitException(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation handler avec mauvais format de fichier
        assertThrows(TechnicalException.class, () -> ExcelFactory.getReader(colClass, new File("12:;")));
    }

    @Test
    public void testCreateWb(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel methode
        invokeMethod(objetTest, "createWb");

        // Test initialisation des donnees
        assertThat(getField("wb")).isNotNull();
        assertThat(getField("helper")).isNotNull();
        assertThat(getField("createHelper")).isNotNull();
    }

    @Test
    public void testInitEnum(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test - enumeration du bon Type
        assertThat(getField("enumeration")).isEqualTo(colClass);
    }

    @ParameterizedTest
    @EnumSource(value = IndexedColors.class, names = {"INDIGO", "PLUM", "ROSE", "ROYAL_BLUE", "YELLOW", "WHITE", "GREEN", "ORCHID"})
    public void testMajCouleurLigne(IndexedColors couleur, TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initilisation
        Row row = wb.getSheetAt(0).getRow(0);

        // Test
        invokeMethod(objetTest, "majCouleurLigne", row, couleur);
        for (Cell cell : row)
        {
            assertThat(cell.getCellStyle().getFillForegroundColor()).isEqualTo(couleur.index);
        }
    }

    @Test
    public void testInitSheet_Feuille_OK(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test 1 - feuille ok
        Sheet sheet = invokeMethod(objetTest, "initSheet");
        assertThat(sheet).isNotNull();
        assertThat(sheet).isEqualTo(wb.getSheetAt(0));

        // Test 2 - feuille nulle
        wb.removeSheetAt(0);
        assertThrows(FunctionalException.class, () -> invokeMethod(objetTest, "initSheet"));
    }
    
    @Test
    public void testInitSheet_Feuille_Nulle(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test 2 - feuille nulle
        wb.removeSheetAt(0);
        assertThrows(FunctionalException.class, () -> invokeMethod(objetTest, "initSheet"));
    }

    @Test
    public void testTestMax(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        String methode = "testMax";

        // Test - Appel methode avec différentes valeur. le maxIndice doit toujours avoir la valeur maximum

        // Valur initiale du nombre max = taille du nombre d'éléments de l'énumération
        int max = (int) getField(MAXINDICE);
        invokeMethod(objetTest, methode, max - 2);
        assertThat(getField(MAXINDICE)).isEqualTo(max);

        invokeMethod(objetTest, methode, 0);
        assertThat(getField(MAXINDICE)).isEqualTo(max);

        invokeMethod(objetTest, methode, max + 3);
        assertThat(getField(MAXINDICE)).isEqualTo(max + 3);

        invokeMethod(objetTest, methode, max - 1);
        assertThat(getField(MAXINDICE)).isEqualTo(max + 3);
    }

    @Test
    public void testGetCellValueNotNull(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        for (Sheet sheet : wb)
        {
            for (Row row : sheet)
            {
                for (Cell cell : row)
                {
                    assertThat((Object) invokeMethod(objetTest, "getCellStringValue", row, cell.getColumnIndex())).isNotNull();
                    assertThat((Object) invokeMethod(objetTest, "getCellFormulaValue", row, cell.getColumnIndex())).isNotNull();
                    assertThat((Object) invokeMethod(objetTest, "getCellNumericValue", row, cell.getColumnIndex())).isNotNull();
                }
            }
        }
    }

    @Test
    public void testGetCellFormulaValue_OK(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // initialisation variables et cellule et ajout formule
        Row row = prepareGetCellFormulaValue();

        // Test retour formule
        String valeur = invokeMethod(objetTest, GETCELLFORMULAVALUE, row, 0);
        assertThat(valeur).isEqualTo("0.0");
    }
    
    @Test
    public void testGetCellFormulaValue_Cellule_non_formule(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // initialisation variables et cellule et ajout formule
        Row row = prepareGetCellFormulaValue();

        // Test contrôle cellule non formule
        String valeur = invokeMethod(objetTest, GETCELLFORMULAVALUE, row, 1);
        assertThat(valeur).isEmpty();
    }
    
    @Test
    public void testGetCellFormulaValue_Cellule_nulle(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // initialisation variables et cellule et ajout formule
        Row row = prepareGetCellFormulaValue();

        // Test contrôle cellule nulle
        String valeur = invokeMethod(objetTest, GETCELLFORMULAVALUE, row, 254);
        assertThat(valeur).isEmpty();
    }

    @Test
    public void testCalculIndiceColonnes(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel méthode
        invokeMethod(objetTest, "calculIndiceColonnes");
        
        // Itère sur tous les indices de colonnes, et verifie que chaque colonne a un indice différent.  
        Map<String, Integer> map = new HashMap<>();
        for (Field field : objetTest.getClass().getDeclaredFields())
        {
            field.setAccessible(true);
            String name = field.getName();
            
            if (!name.startsWith("col"))
                continue;
            
            int valeur = (int) field.get(objetTest);
            
            if (!map.containsValue(valeur))
                map.put(field.getName(), valeur);
            else
                fail("Deux colonnes ont le même indice sur le contrôleur " + objetTest.getClass().getSimpleName());
        }
    }

    /* ---------- METHODES PROTECTED ---------- */

    /**
     * Permet d'initialiaser d'autres objets.
     */
    protected void initOther()
    {
        // Pas d'initialisation par defaut
    }

    /**
     * Methode générique pour tester la methode recupDonneesDepuisExcel. Il suffit d'injecter le contrôle de la taille
     * de la liste en paramètre.
     * 
     * @param desire
     *               Fonction de test de la taille pour la colleciton ou map.
     * @param size
     *               Taille désirée à tester.
     * @return
     *         La map ou la collection testée.
     */
    protected Y testRecupDonneesDepuisExcel(int desire, Function<Y, Integer> size)
    {
        Y map = objetTest.recupDonneesDepuisExcel();
        assertThat(map).isNotNull();
        assertThat((int) size.apply(map)).isEqualTo(desire);
        return map;
    }

    /*---------- METHODES PRIVEES ----------*/
    
    private Row prepareGetCellFormulaValue()
    {
        Row retour = wb.getSheetAt(0).getRow(0);
        Cell cell = retour.getCell(0);
        cell.setCellFormula("SUM(A400:A500)");
        return retour;
    }
    /*---------- ACCESSEURS ----------*/
}
