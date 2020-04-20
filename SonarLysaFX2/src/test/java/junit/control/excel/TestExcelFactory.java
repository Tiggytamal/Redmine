package junit.control.excel;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import control.excel.ControlChefService;
import control.excel.ControlClarity;
import control.excel.ControlEdition;
import control.excel.ControlExtractCompo;
import control.excel.ControlExtractVul;
import control.excel.ExcelFactory;
import junit.AutoDisplayName;
import junit.JunitBase;
import model.enums.ColChefServ;
import model.enums.ColClarity;
import model.enums.ColCompo;
import model.enums.ColEdition;
import model.enums.ColR;
import model.enums.ColVul;
import model.enums.ColW;
import utilities.Statics;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestExcelFactory extends JunitBase<ExcelFactory>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public void init() throws Exception
    {
        // Pas d'initialisation necessaire
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetReader(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel de toutes les enumerations, et verification de la bonne instanciation des contrôleurs
        assertThat(ExcelFactory.getReader(ColClarity.class, createFile("Referentiel_Projets.xlsm")).getClass()).isEqualTo(ControlClarity.class);
        assertThat(ExcelFactory.getReader(ColChefServ.class, createFile("Reorg_managers.xlsx")).getClass()).isEqualTo(ControlChefService.class);
        assertThat(ExcelFactory.getReader(ColEdition.class, createFile("Codification_des_Editions.xlsx")).getClass()).isEqualTo(ControlEdition.class);

        // Utilisation enumeration privée pour faire resortir l'exception
        assertThrows(TechnicalException.class, () -> ExcelFactory.getReader(TestTypeColRW.class, createFile("Plantage.xlsx")).getClass());
    }

    @Test
    public void testGetWriter(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel de toutes les enumerations, et verification de la bonne instanciation des contrôleurs
        assertThat(ExcelFactory.getWriter(ColVul.class, new File("testExtract.xlsx")).getClass()).isEqualTo(ControlExtractVul.class);
        assertThat(ExcelFactory.getWriter(ColCompo.class, new File("testExtractCompo.xlsx")).getClass()).isEqualTo(ControlExtractCompo.class);

        // Utilisation enumeration privéee pour faire resortir l'exception
        assertThrows(TechnicalException.class, () -> ExcelFactory.getWriter(TestTypeColRW.class, createFile("Plantage.xlsx")).getClass());
    }

    /*---------- METHODES PRIVEES ----------*/

    private File createFile(String fichier)
    {
        return new File(getClass().getResource(Statics.ROOT + fichier).getFile());
    }

    /*---------- ACCESSEURS ----------*/

    private enum TestTypeColRW implements ColR, ColW
    {
        TEST;

        @Override
        public String getValeur()
        {
            return null;
        }

        @Override
        public String getNomCol()
        {
            return null;
        }
    }
}
