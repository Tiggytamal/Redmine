package junit.control.excel;

import java.util.Map;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.api.mockito.PowerMockito;

import control.excel.ControlAppliDir;
import dao.DaoApplication;
import junit.AutoDisplayName;
import model.bdd.Application;
import model.enums.ColAppliDir;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlAppliDir extends TestAbstractControlExcelRead<ColAppliDir, ControlAppliDir, Map<String, Application>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlAppliDir()
    {
        super(ColAppliDir.class, "appli-dir.xlsx");
    }

    @Override
    public void initOther()
    {
        DaoApplication dao = PowerMockito.mock(DaoApplication.class);
        PowerMockito.when(dao.readAll()).thenReturn(databaseXML.getApps());
        try
        {
            setField("dao", dao);
        }
        catch (IllegalAccessException e)
        {
            throw new TechnicalException("junit.control.excel.TestControlAppliDir- Impossible d'affecter le mock", e);
        }
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testRecupDonneesDepuisExcel(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Vérfication liste = 341
        testRecupDonneesDepuisExcel(341, liste -> liste.size());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
