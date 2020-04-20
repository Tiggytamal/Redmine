package junit.control.excel;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState;

import control.excel.AbstractControlExcelWrite;
import control.excel.ExcelFactory;
import junit.AutoDisplayName;
import model.Colonne;
import model.enums.ColW;
import model.enums.EtatLot;
import utilities.CellHelper;
import utilities.DateHelper;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public abstract class TestAbstractControlExcelWrite<T extends Enum<T> & ColW, C extends AbstractControlExcelWrite<T, Y>, Y> extends TestAbstractControlExcel<T, C>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String MAXINDICE = "maxIndice";
    private static final String VALRISERFORMULECELLULE = "valoriserFormuleCellule";
    private static final String FORMULE = "SUM(A2:A3)";

    /** Liste des colonnes parametrees pour le fichier donne */
    protected Map<T, Colonne> colonnes;

    /*---------- CONSTRUCTEURS ----------*/

    public TestAbstractControlExcelWrite(Class<T> colClass, String fichier)
    {
        super(colClass, fichier);
        colonnes = Statics.proprietesXML.getEnumMapColW(colClass);
    }

    @BeforeEach
    @Override
    public void init() throws IOException, IllegalAccessException
    {
        file = new File(Statics.ROOT + fichier);
        objetTest = ExcelFactory.getWriter(colClass, file);
        wb = (Workbook) getField("wb");
        initOther();
    }

    /*---------- METHODES ABSTRAITES ----------*/

    public abstract void testInitTitres(TestInfo testInfo) throws Exception;

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testValoriserFormuleCellule_Exception(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        Row row = prepareRow();

        // Test exceptions avec arguments nuls.
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> invokeMethod(objetTest, VALRISERFORMULECELLULE, new Class<?>[]
        { Row.class, Integer.class, CellStyle.class, String.class }, null, 0, null, EMPTY));
        assertThat(e1.getMessage()).isEqualTo("La ligne ou l'indice de la cellule ne peuvent être nulles.");

        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> invokeMethod(objetTest, VALRISERFORMULECELLULE, new Class<?>[]
        { Row.class, Integer.class, CellStyle.class, String.class }, row, null, null, EMPTY));
        assertThat(e2.getMessage()).isEqualTo("La ligne ou l'indice de la cellule ne peuvent être nulles.");
    }
    
    @Test
    public void testValoriserFormuleCellule_Formule_vide(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        Row row = prepareRow();

        // Test avec formule vide
        invokeMethod(objetTest, VALRISERFORMULECELLULE, new Class<?>[]
        { Row.class, Integer.class, CellStyle.class, String.class }, row, 500, null, EMPTY);
        assertThat(row.getCell(500)).isNotNull();
        assertThat(row.getCell(500).getStringCellValue()).isEmpty();
    }
    
    @Test
    public void testValoriserFormuleCellule_Formule_nulle(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        Row row = prepareRow();

        // Test avec formule nulle
        invokeMethod(objetTest, VALRISERFORMULECELLULE, new Class<?>[]
        { Row.class, Integer.class, CellStyle.class, String.class }, row, 500, null, null);
        assertThat(row.getCell(500)).isNotNull();
        assertThat(row.getCell(500).getStringCellValue()).isEmpty();
    }
    
    @Test
    public void testValoriserFormuleCellule_Formule_style(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        Row row = prepareRow();

        // Test avec formule avec style
        CellStyle style = new CellHelper(wb).getStyle(IndexedColors.AQUA);
        invokeMethod(objetTest, VALRISERFORMULECELLULE, new Class<?>[]
        { Row.class, Integer.class, CellStyle.class, String.class }, row, 500, style, FORMULE);
        assertThat(row.getCell(500)).isNotNull();
        assertThat(row.getCell(500).getCellFormula()).isEqualTo(FORMULE);
        assertThat(row.getCell(500).getCellStyle()).isEqualTo(style);
    }
    
    @Test
    public void testValoriserFormuleCellule_Formule_sans_style(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        Row row = prepareRow();

        // Test avec formule sans style
        CellStyle style = new CellHelper(wb).getStyle(IndexedColors.AQUA);
        invokeMethod(objetTest, VALRISERFORMULECELLULE, new Class<?>[]
        { Row.class, Integer.class, CellStyle.class, String.class }, row, 500, null, FORMULE);
        assertThat(row.getCell(500)).isNotNull();
        assertThat(row.getCell(500).getCellFormula()).isEqualTo(FORMULE);
        assertThat(row.getCell(500).getCellStyle()).isNotEqualTo(style);
    }

    @Test
    public void testAutosizeColumns(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation - Mock Sheet
        Sheet mock = Mockito.mock(Sheet.class);
        Mockito.when(mock.getRow(Mockito.anyInt())).thenReturn(wb.getSheetAt(0).getRow(0));

        // Test - verifie qu'on appele bien la methode autant de fois qu'il y a de colonnes
        invokeMethod(objetTest, "autosizeColumns", mock);
        Mockito.verify(mock, Mockito.times((int) getField(MAXINDICE) + 1)).autoSizeColumn(Mockito.anyInt());
    }

    @Test
    public void testCreateWb(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(wb).isNotNull();
        assertThat(getField("helper")).isNotNull();
        assertThat(getField("createHelper")).isNotNull();
    }

    @Test
    public void testInitEnum(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(getField("enumeration")).isNotNull();
    }

    @Test
    public void testCalculIndicesColonnes(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test que pour chaque indice de colonne du contrôleur on a la même valeur que dans le fichier de parametrage
        for (Map.Entry<T, Colonne> entry : colonnes.entrySet())
        {
            assertThat(Integer.parseInt(entry.getValue().getIndice())).isEqualTo(getField(entry.getKey().getNomCol()));
        }
    }

    @Test
    public void testWrite(TestInfo testInfo) throws IllegalAccessException, IOException
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest.write();
        for (Sheet sheet : wb)
        {
            // Cast de l'objet pour obtenir les methodes hors de l'API
            XSSFSheet xssf = (XSSFSheet) sheet;

            // Verification que l'autofilter est bien mis
            assertThat(xssf.getCTWorksheet().isSetAutoFilter()).isTrue();

            // Verifcation que l'on a bien la première ligne bloquee
            assertThat(xssf.getCTWorksheet().getSheetViews().getSheetViewList().get(0).getPane().getState()).isEqualTo(STPaneState.FROZEN);
        }
    }

    @Test
    public void testValoriserCellule(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Variables
        String methode = "valoriserCellule";
        Row row = prepareRow();

        // Test exceptions avec arguments nuls.
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> invokeMethod(objetTest, methode, new Class<?>[]
        { Row.class, Integer.class, CellStyle.class, Object.class }, null, 0, null, EMPTY));
        assertThat(e1.getMessage()).isEqualTo("La ligne ou l'indice de la cellule ne peuvent être nulles.");

        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> invokeMethod(objetTest, methode, new Class<?>[]
        { Row.class, Integer.class, CellStyle.class, String.class }, row, null, null, EMPTY));
        assertThat(e2.getMessage()).isEqualTo("La ligne ou l'indice de la cellule ne peuvent être nulles.");

        // Test avec EtatLot
        invokeMethod(objetTest, methode, new Class<?>[]
        { Row.class, Integer.class, CellStyle.class, String.class }, row, 0, null, EtatLot.ABANDONNE);
        assertThat(row.getCell(0)).isNotNull();
        assertThat(row.getCell(0).getCellType()).isEqualTo(CellType.STRING);
        assertThat(row.getCell(0).getStringCellValue()).isEqualTo(EtatLot.ABANDONNE.getValeur());

        // Test avec LocalDate
        invokeMethod(objetTest, methode, new Class<?>[]
        { Row.class, Integer.class, CellStyle.class, String.class }, row, 0, null, today);
        assertThat(row.getCell(0)).isNotNull();
        assertThat(row.getCell(0).getCellType()).isEqualTo(CellType.NUMERIC);
        assertThat(DateHelper.localDate(row.getCell(0).getDateCellValue())).isEqualTo(today);

        // Test avec LocalDate
        invokeMethod(objetTest, methode, new Class<?>[]
        { Row.class, Integer.class, CellStyle.class, String.class }, row, 0, null, todayTime);
        assertThat(row.getCell(0)).isNotNull();
        assertThat(row.getCell(0).getCellType()).isEqualTo(CellType.NUMERIC);
        assertThat(DateHelper.localDateTime(row.getCell(0).getDateCellValue())).isEqualTo(todayTime);

        // Test texte null
        invokeMethod(objetTest, methode, new Class<?>[]
        { Row.class, Integer.class, CellStyle.class, String.class }, row, 0, null, null);
        assertThat(row.getCell(0)).isNotNull();
        assertThat(row.getCell(0).getCellType()).isEqualTo(CellType.BLANK);
        assertThat(row.getCell(0).getStringCellValue()).isEmpty();
    }

    /* ---------- METHODES PROTEDTEC ---------- */

    /**
     * Permet d'initialisaer d'autres objets.
     */
    protected void initOther()
    {
        // Pas d'initialisation par defaut
    }

    /*---------- METHODES PRIVEES ----------*/
    
    private Row prepareRow()
    {
        return wb.getSheetAt(0).getRow(0);
    }
    
    /*---------- ACCESSEURS ----------*/
}
