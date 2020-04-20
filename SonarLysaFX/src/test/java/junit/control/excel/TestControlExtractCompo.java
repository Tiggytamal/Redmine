package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import control.excel.ControlExtractCompo;
import control.task.MajLotsRTCTask;
import dao.DaoFactory;
import de.saxsys.javafx.test.JfxRunner;
import model.bdd.ComposantSonar;
import model.enums.TypeColCompo;

@RunWith(JfxRunner.class)
public class TestControlExtractCompo extends TestControlExcelWrite<TypeColCompo, ControlExtractCompo, Map<TypeColCompo, List<ComposantSonar>>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlExtractCompo()
    {
        super(TypeColCompo.class, "extractCompo.xlsx");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetRow() throws Exception
    {
        Sheet sheet = wb.createSheet();
        assertNotNull(Whitebox.invokeMethod(controlTest, "getRow", 1, sheet));
        assertNotNull(Whitebox.invokeMethod(controlTest, "getRow", 1, sheet));
    }

    @Test
    public void testAjouterExtraction() throws IllegalArgumentException, IllegalAccessException
    {
        // Données depuis la base
        List<ComposantSonar> composBase = DaoFactory.getDao(ComposantSonar.class).readAll();

        // Préparation de la map
        Map<TypeColCompo, List<ComposantSonar>> composSonar = new HashMap<>();
        for (TypeColCompo type : TypeColCompo.values())
        {
            composSonar.put(type, new ArrayList<>());
        }

        for (int i = 0; i < composBase.size(); i++)
        {
            if (i < 11)
                composSonar.get(TypeColCompo.INCONNU).add(composBase.get(i));
            else if (i < 21)
                composSonar.get(TypeColCompo.NONPROD).add(composBase.get(i));
            else if (i < 31)
                composSonar.get(TypeColCompo.PATRIMOINE).add(composBase.get(i));
            else if (i < 41)
                composSonar.get(TypeColCompo.TERMINE).add(composBase.get(i));
        }

        controlTest.ajouterExtraction(composSonar, null);

        Sheet sheet = wb.getSheet("Composants Sonar");
        assertNotNull(sheet);
        assertNotNull(sheet.getRow(11));
        int colInco = (int) Whitebox.getField(ControlExtractCompo.class, "colInco").get(controlTest);
        int colNonProd = (int) Whitebox.getField(ControlExtractCompo.class, "colNonProd").get(controlTest);
        int colPat = (int) Whitebox.getField(ControlExtractCompo.class, "colPat").get(controlTest);
        int colTermine = (int) Whitebox.getField(ControlExtractCompo.class, "colTermine").get(controlTest);

        for (int i = 0; i < 10; i++)
        {
            Row row = sheet.getRow(i + 1);
            assertEquals(composSonar.get(TypeColCompo.INCONNU).get(i).getNom(), row.getCell(colInco).getStringCellValue());
            assertEquals(composSonar.get(TypeColCompo.NONPROD).get(i).getNom(), row.getCell(colNonProd).getStringCellValue());
            assertEquals(composSonar.get(TypeColCompo.PATRIMOINE).get(i).getNom(), row.getCell(colPat).getStringCellValue());
            assertEquals(composSonar.get(TypeColCompo.TERMINE).get(i).getNom(), row.getCell(colTermine).getStringCellValue());
        }

        composSonar = new HashMap<>();
        composSonar.put(TypeColCompo.INCONNU, new ArrayList<>());
        composSonar.get(TypeColCompo.INCONNU).add(composBase.get(0));
        controlTest.ajouterExtraction(composSonar, new MajLotsRTCTask(LocalDate.now()));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
