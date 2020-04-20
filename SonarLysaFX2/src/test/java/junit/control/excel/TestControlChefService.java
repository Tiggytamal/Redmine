package junit.control.excel;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import control.excel.ControlChefService;
import junit.AutoDisplayName;
import model.bdd.ChefService;
import model.enums.ColChefServ;
import utilities.FunctionalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlChefService extends TestAbstractControlExcelRead<ColChefServ, ControlChefService, Map<String, ChefService>>
{
    /*---------- ATTRIBUTS ----------*/

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlChefService()
    {
        super(ColChefServ.class, "Reorg_managers.xlsx");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testRecupDonneesDepuisExcel(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Contrôle taille de la map = 28
        testRecupDonneesDepuisExcel(28, map -> map.size());
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

        // Test 2 - feuille nulle
        wb.removeSheetAt(0);
        assertThrows(FunctionalException.class, () -> Whitebox.invokeMethod(objetTest, "initSheet"));
    }

    @Test
    public void testInitSheet_Feuille_Nulle(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test 2 - feuille nulle
        wb.removeSheetAt(0);
        assertThrows(FunctionalException.class, () -> Whitebox.invokeMethod(objetTest, "initSheet"));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
