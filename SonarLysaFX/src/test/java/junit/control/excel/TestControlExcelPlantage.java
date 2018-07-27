package junit.control.excel;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import control.excel.ExcelFactory;
import model.enums.TypeColSuivi;
import utilities.FunctionalException;
import utilities.Statics;

/**
 * Classe de test pour vérfier la remontée de l'exception si un fichier n'a pas le bon nombre de colonnes
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
public class TestControlExcelPlantage
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testExceptionMauvaisFichier()
    {
        try
        {
            ExcelFactory.getReader(TypeColSuivi.class, new File(getClass().getResource(Statics.ROOT + "Plantage.xlsx").getFile()));

        } catch (FunctionalException e)
        {
            assertEquals("Le fichier excel est mal configuré, vérifié les colonnes de celui-ci : Différence = 3", e.getMessage());
        }
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}