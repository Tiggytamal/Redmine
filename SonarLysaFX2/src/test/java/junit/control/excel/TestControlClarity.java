package junit.control.excel;

import java.util.Map;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.api.mockito.PowerMockito;

import control.excel.ControlClarity;
import dao.DaoChefService;
import junit.AutoDisplayName;
import model.bdd.ProjetClarity;
import model.enums.ColClarity;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlClarity extends TestAbstractControlExcelRead<ColClarity, ControlClarity, Map<String, ProjetClarity>>
{
    /*---------- ATTRIBUTS ----------*/

    /*---------- CONSTRUCTEURS ----------*/

    public TestControlClarity()
    {
        super(ColClarity.class, "Referentiel_Projets.xlsm");
    }

    @Override
    public void initOther()
    {
        DaoChefService dao = PowerMockito.mock(DaoChefService.class);
        PowerMockito.when(dao.readAll()).thenReturn(databaseXML.getChefService());
        try
        {
            setField("dao", dao);
        }
        catch (IllegalAccessException e)
        {
            throw new TechnicalException("junit.control.excel.TestControlClarity - Impossible d'affecter le mock", e);
        }
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testRecupDonneesDepuisExcel(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        testRecupDonneesDepuisExcel(1096, map -> map.size());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
