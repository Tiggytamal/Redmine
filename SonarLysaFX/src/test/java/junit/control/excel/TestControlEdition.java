package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.excel.ControlEdition;
import model.bdd.Edition;
import model.enums.TypeColEdition;
import model.enums.TypeEdition;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;

public class TestControlEdition extends TestControlExcelRead<TypeColEdition, ControlEdition, Map<String, Edition>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlEdition()
    {
        super(TypeColEdition.class, "Codification_des_Editions.xls");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne ne sert pas.
        testCalculIndiceColonnes(0);
    }

    @Test
    public void testRecupDonneesDepuisExcel()
    {
        // Premiers test génériques
        Map<String, Edition> map = testRecupDonneesDepuisExcel(a -> a.size() == 164);

        // Test sur le format des clefs/valeurs dans la map
        for (Map.Entry<String, Edition> entry : map.entrySet())
        {
            assertTrue(entry.getKey(), entry.getValue().getNumero().matches("^([0-9]{2}\\.){3}[0-9]{2}$"));
            assertTrue(entry.getValue().getNom(), entry.getKey().matches("^CHC(_CDM){0,1}20[12][0-9]\\-S[0-5][0-9]$") || entry.getKey().matches("^E\\d\\d.*$"));
        }
    }

    @Test
    public void testPrepareNomEdition() throws Exception
    {
        String methode = "prepareNomEdition";
        
        // Tests des différents cas de la préparation du libellé
        assertEquals("CHC_CDM2018-S32", Whitebox.invokeMethod(controlTest, methode, "CDM2018-S32"));
        assertEquals("CHC_CDM2021-S01", Whitebox.invokeMethod(controlTest, methode, "CDM2021-S01 / CHC2017-S22"));
        assertEquals("CHC_CDM2019-S16", Whitebox.invokeMethod(controlTest, methode, "CHC2013-S51 / CDM2019-S16"));
        assertEquals("CHC2017-S15", Whitebox.invokeMethod(controlTest, methode, "CHC2017-S15"));
    }

    @Test (expected = TechnicalException.class)
    public void testPrepareDateMEP() throws Exception
    {
        String methode = "prepareDateMEP";
        
        assertEquals(LocalDate.of(2099, 1, 1), Whitebox.invokeMethod(controlTest, methode, "N/A"));
        assertEquals(LocalDate.of(2099, 1, 1), Whitebox.invokeMethod(controlTest, methode, "n/a"));
        assertEquals(LocalDate.of(2099, 1, 1), Whitebox.invokeMethod(controlTest, methode, Statics.EMPTY));
        assertEquals(LocalDate.of(2018, 1, 8), Whitebox.invokeMethod(controlTest, methode, "S01 - 2018"));
        Whitebox.invokeMethod(controlTest, methode, "erreur");
    }

    @Test
    public void testPrepareTypeEdition() throws Exception
    {
        String methode = "prepareTypeEdition";
        assertEquals(TypeEdition.CDM, Whitebox.invokeMethod(controlTest, methode, "CHC_CDM2018-S39"));
        assertEquals(TypeEdition.CDM, Whitebox.invokeMethod(controlTest, methode, "CHC_CDM2017-S18"));
        assertEquals(TypeEdition.CHC, Whitebox.invokeMethod(controlTest, methode, "CHC2018-S50"));
        assertEquals(TypeEdition.CHC, Whitebox.invokeMethod(controlTest, methode, "CHC2020-S10"));
        assertEquals(TypeEdition.FDL, Whitebox.invokeMethod(controlTest, methode, "E30.Fil_De_Leau"));
        assertEquals(TypeEdition.FDL, Whitebox.invokeMethod(controlTest, methode, "E27_Fil_De_Leau"));
    }

    @Test
    @Override
    public void testInitSheet() throws Exception
    {
        // Test 1 - feuille ok
        Sheet sheet = Whitebox.invokeMethod(controlTest, "initSheet");
        assertNotNull(sheet);
        assertEquals(wb.getSheetAt(0), sheet);
    }

    @Test(expected = FunctionalException.class)
    @Override
    public void testInitSheetException() throws Exception
    {
        // Test 2 - feuille nulle
        wb.removeSheetAt(0);
        Whitebox.invokeMethod(controlTest, "initSheet");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
