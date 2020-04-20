package junit.control.excel;

import static com.google.common.truth.Truth.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import control.excel.ControlExtractRegles;
import control.rest.SonarAPI;
import junit.AutoDisplayName;
import model.enums.ColRegle;
import model.rest.sonarapi.ParamAPI;
import model.rest.sonarapi.Regle;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlExtractRegles extends TestAbstractControlExcelWrite<ColRegle, ControlExtractRegles, Collection<Regle>>
{
    /*---------- ATTRIBUTS ----------*/

    private Sheet sheet;

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlExtractRegles()
    {
        super(ColRegle.class, "extractRegles.xslx");
    }

    @Override
    protected void initOther()
    {
        sheet = wb.getSheet("Règles");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testInitTitres(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());
        // TODO
    }

    @Test
    public void testCreerFichierExtraction(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation de la map de test
        List<ParamAPI> liste = new ArrayList<>();
        liste.add(new ParamAPI("tags", "sql"));
        liste.add(new ParamAPI("languages", "java"));
        List<Regle> regles = SonarAPI.build().getReglesGenerique(liste);

        // Appel de la methode
        objetTest.creationExtraction(regles, null);

        // Test du nombre de lignes
        assertThat(sheet.getLastRowNum()).isEqualTo(5);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
