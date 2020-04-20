package junit.control.task.extraction;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.io.File;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import control.task.extraction.ExtractionComposantsSonarTask;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;
import model.OptionInitAPI;
import model.enums.ColCompo;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestCreerExtractComposantsSonarTask extends TestAbstractTask<ExtractionComposantsSonarTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void initImpl() throws Exception
    {
        objetTest = new ExtractionComposantsSonarTask(new File(Statics.RESSTEST + "testExtractCompo.xlsx"));
        initAPI(ExtractionComposantsSonarTask.class, OptionInitAPI.MOCK);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testAnnuler(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test cancel de la methode
        objetTest.annuler();
        assertThat(objetTest.isCancelled()).isTrue();
    }

    @Test
    public void testCall(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel methode, creerVuePatrimoine depuis la methode call et test du retour à true
        assertThat((boolean) Whitebox.invokeMethod(objetTest, "call")).isTrue();

        // Test de la bonne création du fichier
        File fichier = new File(Statics.RESSTEST + "testExtractCompo.xlsx");
        assertThat(fichier.exists()).isTrue();
        assertThat(fichier.isFile()).isTrue();

        // Test que les lignes sont bien créées
        Workbook wb = WorkbookFactory.create(fichier);

        int indice = Integer.parseInt(Statics.proprietesXML.getEnumMapColW(ColCompo.class).get(ColCompo.NOM).getIndice());

        Sheet sheet = wb.getSheet("Composants Sonar");

        for (int i = 1; i < sheet.getLastRowNum(); i++)
        {
            String cell1 = sheet.getRow(i).getCell(indice).getStringCellValue();
            assertWithMessage(cell1 + " ne contiens pas ; Composant ").that(cell1).contains("Composant ");
        }

        // Nettoyage
        fichier.delete();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
