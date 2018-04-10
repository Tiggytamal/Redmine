package junit.control.excel;

import java.util.Map;

import org.junit.Test;

import control.excel.ControlClarity;
import model.InfoClarity;
import model.enums.TypeColClarity;

public class ControlClarityTest extends ControlExcelTest<TypeColClarity, ControlClarity, Map<String, InfoClarity>>
{
    /*---------- ATTRIBUTS ----------*/
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public ControlClarityTest()
    {
        super(TypeColClarity.class, "/resources/Referentiel_Projets.xlsm");
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void calculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne n'est pas utilisée
        calculIndiceColonnes(0);
    }
    
    @Test
    public void recupDonneesDepuisExcel()
    {
        recupDonneesDepuisExcel((map) -> map.size() == 8277);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}