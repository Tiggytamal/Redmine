package junit.utilities;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import junit.AutoDisplayName;
import junit.JunitBase;
import model.enums.Bordure;
import utilities.CellHelper;
import utilities.Statics;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestCellHelper extends JunitBase<CellHelper>
{
    /*---------- ATTRIBUTS ----------*/

    private Workbook wb;
    private Cell cell;

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        wb = WorkbookFactory.create(new FileInputStream(new File(getClass().getResource(Statics.ROOT + "Tests.xlsx").getFile())));
        objetTest = new CellHelper(wb);
        cell = wb.getSheetAt(0).getRow(0).getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testCreateAllStyles(TestInfo testInfo)
    {
        // Appel méthode et création des listes
        Map<Boolean, List<CellStyle>> map = objetTest.createAllStyles(IndexedColors.AQUA);
        List<CellStyle> normaux = map.get(false);
        List<CellStyle> centres = map.get(true);

        // Contrôles nullité
        assertThat(map).hasSize(2);
        assertThat(normaux).isNotNull();
        assertThat(centres).isNotNull();

        // Contrôles liste non centrée
        assertThat(normaux).hasSize(Bordure.values().length);
        for (CellStyle style : normaux)
        {
            assertThat(style.getAlignment()).isEqualTo(HorizontalAlignment.GENERAL);
            assertThat(style.getFillPattern()).isEqualTo(FillPatternType.SOLID_FOREGROUND);
        }

        // Contrôles liste centrée
        assertThat(centres).hasSize(Bordure.values().length);
        for (CellStyle style : centres)
        {
            assertThat(style.getAlignment()).isEqualTo(HorizontalAlignment.CENTER);
            assertThat(style.getFillPattern()).isEqualTo(FillPatternType.SOLID_FOREGROUND);
        }

        assertThrows(IllegalArgumentException.class, () -> objetTest.createAllStyles(null));
    }

    @Test
    public void testSetFontColor(TestInfo testInfo)
    {
        // Récupération cell et appel méthode
        objetTest.setFontColor(cell, IndexedColors.BLUE_GREY);

        // Contrôle - besoin cast pour avoir accès à la police
        Font font = ((XSSFCell) cell).getCellStyle().getFont();
        assertThat(font.getFontName()).isEqualTo("Comic Sans MS");
        assertThat(font.getFontHeightInPoints()).isEqualTo(12);
        assertThat(font.getColor()).isEqualTo(IndexedColors.BLUE_GREY.index);
    }

    @Test
    public void testRecentrage(TestInfo testInfo)
    {
        // Appel méthode
        objetTest.recentrage(cell);

        // Contrôle alignement
        assertThat(cell.getCellStyle().getAlignment()).isEqualTo(HorizontalAlignment.CENTER);
        assertThat(cell.getCellStyle().getVerticalAlignment()).isEqualTo(VerticalAlignment.CENTER);
    }

    @Test
    public void testGetStyle(TestInfo testInfo)
    {
        // Appel et contrôle méthode avec 4 paramètres
        CellStyle style = objetTest.getStyle(IndexedColors.AQUA, Bordure.BASDROITE, FillPatternType.LEAST_DOTS, HorizontalAlignment.CENTER);
        assertThat(style.getFillBackgroundColor()).isEqualTo(IndexedColors.AQUA.index);
        assertThat(style.getFillForegroundColor()).isEqualTo(IndexedColors.BLACK.index);
        assertThat(style.getAlignment()).isEqualTo(HorizontalAlignment.CENTER);
        assertThat(style.getBorderRight()).isEqualTo(BorderStyle.THICK);
        assertThat(style.getBorderBottom()).isEqualTo(BorderStyle.THICK);
        assertThat(style.getBorderLeft()).isEqualTo(BorderStyle.THIN);
        assertThat(style.getBorderTop()).isEqualTo(BorderStyle.NONE);
        assertThat(style.getFillPattern()).isEqualTo(FillPatternType.LEAST_DOTS);

        // Appel et contrôle avec 3 paramètres
        style = objetTest.getStyle(IndexedColors.BLUE, Bordure.HAUT, HorizontalAlignment.CENTER);
        assertThat(style.getFillBackgroundColor()).isEqualTo(IndexedColors.AUTOMATIC.index);
        assertThat(style.getFillForegroundColor()).isEqualTo(IndexedColors.BLUE.index);
        assertThat(style.getAlignment()).isEqualTo(HorizontalAlignment.CENTER);
        assertThat(style.getBorderRight()).isEqualTo(BorderStyle.THIN);
        assertThat(style.getBorderBottom()).isEqualTo(BorderStyle.NONE);
        assertThat(style.getBorderLeft()).isEqualTo(BorderStyle.THIN);
        assertThat(style.getBorderTop()).isEqualTo(BorderStyle.THICK);
        assertThat(style.getFillPattern()).isEqualTo(FillPatternType.SOLID_FOREGROUND);

        // Appel contrôle avec 2 paramètres
        style = objetTest.getStyle(IndexedColors.RED, FillPatternType.THICK_FORWARD_DIAG);
        assertThat(style.getFillBackgroundColor()).isEqualTo(IndexedColors.WHITE.index);
        assertThat(style.getFillForegroundColor()).isEqualTo(IndexedColors.RED.index);
        assertThat(style.getAlignment()).isEqualTo(HorizontalAlignment.GENERAL);
        assertThat(style.getBorderRight()).isEqualTo(BorderStyle.THIN);
        assertThat(style.getBorderBottom()).isEqualTo(BorderStyle.NONE);
        assertThat(style.getBorderLeft()).isEqualTo(BorderStyle.THIN);
        assertThat(style.getBorderTop()).isEqualTo(BorderStyle.NONE);
        assertThat(style.getFillPattern()).isEqualTo(FillPatternType.THICK_FORWARD_DIAG);

        // Appel contrôle avec 1 paramètre
        style = objetTest.getStyle(IndexedColors.BROWN);
        assertThat(style.getFillBackgroundColor()).isEqualTo(IndexedColors.AUTOMATIC.index);
        assertThat(style.getFillForegroundColor()).isEqualTo(IndexedColors.BROWN.index);
        assertThat(style.getAlignment()).isEqualTo(HorizontalAlignment.GENERAL);
        assertThat(style.getBorderRight()).isEqualTo(BorderStyle.THIN);
        assertThat(style.getBorderBottom()).isEqualTo(BorderStyle.NONE);
        assertThat(style.getBorderLeft()).isEqualTo(BorderStyle.THIN);
        assertThat(style.getBorderTop()).isEqualTo(BorderStyle.NONE);
        assertThat(style.getFillPattern()).isEqualTo(FillPatternType.SOLID_FOREGROUND);

        // Test exception
        assertThrows(IllegalArgumentException.class, () -> objetTest.getStyle(null));
        assertThrows(IllegalArgumentException.class, () -> objetTest.getStyle(IndexedColors.AQUA, null));
        assertThrows(IllegalArgumentException.class, () -> objetTest.getStyle(IndexedColors.AQUA, null, FillPatternType.SOLID_FOREGROUND));
        assertThrows(IllegalArgumentException.class, () -> objetTest.getStyle(IndexedColors.AQUA, Bordure.BASDROITE, FillPatternType.LEAST_DOTS, null));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Whitebox.invokeMethod(objetTest, "getStyle", new Class[]
        { IndexedColors.class, Bordure.class, HorizontalAlignment.class }, IndexedColors.BLACK, Bordure.VIDE, null));
        assertThat(e.getMessage()).isEqualTo("La couleur ou la bordure ne peuvent être nulles");
    }

    @Test
    public void testCreateHyperLink(TestInfo testInfo)
    {
        // Appel et contrôle
        objetTest.createHyperLink("liens", cell);

        Font font = ((XSSFCell) cell).getCellStyle().getFont();
        assertThat(font.getUnderline()).isEqualTo(Font.U_SINGLE);
        assertThat(font.getColor()).isEqualTo(IndexedColors.BLUE.index);
        assertThat(cell.getHyperlink()).isNotNull();
        assertThat(cell.getHyperlink().getAddress()).isEqualTo("liens");
    }

    @Test
    public void testPrepareStyle(TestInfo testInfo) throws Throwable
    {
        TechnicalException e = assertThrows(TechnicalException.class,
                () -> Whitebox.invokeMethod(objetTest, "prepareStyle", cell.getCellStyle(), IndexedColors.LIME, Bordure.DROITE, FillPatternType.BRICKS));
        assertThat(e.getMessage()).isEqualTo("utilities.CellHelper.prepareStyle - FillpatternType non prévu : BRICKS");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
