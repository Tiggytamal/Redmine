package junit.control.excel;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import control.excel.ExcelFactory;
import junit.AutoDisplayName;
import junit.JunitBase;
import model.enums.ColEdition;
import utilities.FunctionalException;
import utilities.Statics;

/**
 * Classe de test pour verfier la remontée de l'exception si un fichier n'a pas le bon nombre de colonnes
 * 
 * @author ETP8137 - Grégoire Mathon
 *         since 1.0
 */
@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlExcelPlantage extends JunitBase<ExcelFactory>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws Exception
    {
        // TODO Auto-generated method stub

    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testException_MauvaisFichier(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        FunctionalException e = assertThrows(FunctionalException.class, () -> ExcelFactory.getReader(ColEdition.class, new File(getClass().getResource(Statics.ROOT + "Plantage.xlsx").getFile())));
        assertThat(e.getMessage()).isEqualTo("Le fichier excel est mal configuré, vérifier les colonnes de celui-ci : Difference = 6");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
