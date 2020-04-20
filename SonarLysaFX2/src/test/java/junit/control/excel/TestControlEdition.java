package junit.control.excel;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import control.excel.ControlEdition;
import control.excel.ExcelFactory;
import junit.AutoDisplayName;
import model.bdd.Edition;
import model.enums.ColEdition;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlEdition extends TestAbstractControlExcelRead<ColEdition, ControlEdition, Map<String, Edition>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlEdition()
    {
        super(ColEdition.class, "codification_des_editions.xlsx");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testRecupDonneesDepuisExcel(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Attention resultats dependant du fichier.

        // Test sur le nombre de lignes remontées
        Map<String, Edition> map = testRecupDonneesDepuisExcel(52, a -> a.size());

        // Test sur le format des clefs/valeurs dans la map
        for (Map.Entry<String, Edition> entry : map.entrySet())
        {
            assertThat(entry.getValue().getNumero()).matches("^([0-9]{2}\\.){3}[0-9]{2}$");
        }
    }

    @Test
    public void testCalculActif(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Attention resultats dependants du fichier.

        // Variables
        String methode = "calculActif";
        Sheet sheet = Whitebox.invokeMethod(objetTest, "initSheet");

        // Test ligne verte
        Row row = sheet.getRow(2);
        for (Cell cell : row)
        {
            assertThat((boolean) Whitebox.invokeMethod(objetTest, methode, cell)).isTrue();
        }

        // Test ligne rouge
        row = sheet.getRow(3);
        for (Cell cell : row)
        {
            assertThat((boolean) Whitebox.invokeMethod(objetTest, methode, cell)).isFalse();
        }

        // Test erreur couleurs
        file = new File(getClass().getResource(Statics.ROOT + new File("codification_des_editions_erreurs.xlsx")).getFile());
        objetTest = ExcelFactory.getReader(ColEdition.class, file);
        sheet = Whitebox.invokeMethod(objetTest, "initSheet");

        // Test erreurs vert
        row = sheet.getRow(4);
        testExceptionCouleur(methode, row.getCell(1), "BF069501-1", 100, 166, 90);
        testExceptionCouleur(methode, row.getCell(0), "16.00.48.03", 0, 250, 90);
        testExceptionCouleur(methode, row.getCell(2), "E33", 0, 166, 0);

        // Test erreurs rouge
        row = sheet.getRow(5);
        testExceptionCouleur(methode, row.getCell(0), "16.00.48.00", 0, 75, 57);
        testExceptionCouleur(methode, row.getCell(1), "CHC201943", 221, 200, 57);
        testExceptionCouleur(methode, row.getCell(2), "E33", 221, 75, 250);

    }

    @Test
    public void testPrepareDateMEP(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        String methode = "prepareDateMEP";

        assertThat((LocalDate) Whitebox.invokeMethod(objetTest, methode, 201920)).isEqualTo(LocalDate.of(2019, 5, 20));
        assertThat((LocalDate) Whitebox.invokeMethod(objetTest, methode, 201935)).isEqualTo(LocalDate.of(2019, 9, 02));
        assertThat((LocalDate) Whitebox.invokeMethod(objetTest, methode, 201948)).isEqualTo(LocalDate.of(2019, 12, 02));
        assertThat((LocalDate) Whitebox.invokeMethod(objetTest, methode, 123)).isEqualTo(Statics.DATEINCO2099);
    }

    @Test
    @Override
    public void testInitSheet_Feuille_OK(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test 1 - feuille ok
        Sheet sheet = Whitebox.invokeMethod(objetTest, "initSheet");
        assertThat(sheet).isNotNull();
        assertThat(sheet).isEqualTo(wb.getSheetAt(0));
    }

    @Test
    @Override
    public void testInitSheet_Feuille_Nulle(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test 2 - feuille nulle - resultat valide s'il n'y a pas de feuillle vide dans le classeur
        wb.removeSheetAt(0);
        assertThrows(FunctionalException.class, () -> Whitebox.invokeMethod(objetTest, "initSheet"));
    }

    /*---------- METHODES PRIVEES ----------*/

    private void testExceptionCouleur(String methode, Cell cell, String lib, int rgb0, int rgb1, int rgb2) throws Exception
    {
        // Variables
        String tiret = Statics.SPACE + Statics.TIRET;
        String texteException = "control.excel.ControlEdition.testCalculActif - Exception non remontée.";

        // Test exception
        TechnicalException e = assertThrows(TechnicalException.class, () -> Whitebox.invokeMethod(objetTest, methode, cell), texteException);

        // Test message exception
        StringBuilder builder = new StringBuilder("control.excel.ControlEdition.calculActif - la couleur de la cellule est inconnue : ");
        builder.append(lib).append(tiret).append(rgb0).append(tiret).append(rgb1).append(tiret).append(rgb2);
        assertThat(e.getMessage()).isEqualTo(builder.toString());
    }
    /*---------- ACCESSEURS ----------*/
}
