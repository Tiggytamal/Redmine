package junit.control.excel;


import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import control.excel.ControlApps;
import control.excel.ControlAppsW;
import control.excel.ControlChefService;
import control.excel.ControlClarity;
import control.excel.ControlEdition;
import control.excel.ControlExtractVul;
import control.excel.ControlPic;
import control.excel.ControlSuivi;
import control.excel.ExcelFactory;
import junit.JunitBase;
import model.enums.TypeColApps;
import model.enums.TypeColChefServ;
import model.enums.TypeColClarity;
import model.enums.TypeColEdition;
import model.enums.TypeColPic;
import model.enums.TypeColR;
import model.enums.TypeColSuivi;
import model.enums.TypeColVul;
import model.enums.TypeColW;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe de test de la Factory Excel
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestExcelFactory extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        // Pas d'initialisation nécessaire       
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetReader()
    {
        // Appel de toutes les énumérations, et vérification de la bonne instanciation des controleurs
        assertEquals(ControlSuivi.class, ExcelFactory.getReader(TypeColSuivi.class, createFile("Suivi_Quality_GateTest.xlsx")).getClass());
        assertEquals(ControlClarity.class, ExcelFactory.getReader(TypeColClarity.class, createFile("Referentiel_Projets.xlsm")).getClass());
        assertEquals(ControlChefService.class, ExcelFactory.getReader(TypeColChefServ.class, createFile("Reorg_managers.xlsx")).getClass());
        assertEquals(ControlPic.class, ExcelFactory.getReader(TypeColPic.class, createFile("MEP_mars_2018.xlsx")).getClass());
        assertEquals(ControlEdition.class, ExcelFactory.getReader(TypeColEdition.class, createFile("Codification_des_Editions.xlsx")).getClass());
        assertEquals(ControlApps.class, ExcelFactory.getReader(TypeColApps.class, createFile("liste_applis.xlsx")).getClass());
    }
    
    @Test (expected = TechnicalException.class)
    public void testGetReaderException()
    {
        // Utilisation enumeration privée pour faire resortir l'exception
        ExcelFactory.getReader(TestTypeColRW.class, createFile("Suivi_Quality_GateTest.xlsx")).getClass();
    }
    
    @Test 
    public void testGetWriter()
    {
        // Appel de toutes les énumérations, et vérification de la bonne instanciation des controleurs
        assertEquals(ControlExtractVul.class, ExcelFactory.getWriter(TypeColVul.class, new File("testExtract.xlsx")).getClass());
        assertEquals(ControlAppsW.class, ExcelFactory.getWriter(TypeColApps.class, new File("testAppsW.xlsx")).getClass());
    }
    
    @Test (expected = TechnicalException.class)
    public void testGetWriterException()
    {
        // Utilisation enumeration privée pour faire resortir l'exception
        ExcelFactory.getWriter(TestTypeColRW.class, createFile("Suivi_Quality_GateTest.xlsx")).getClass();
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    private File createFile(String fichier)
    {
        return new File(getClass().getResource(Statics.ROOT + fichier).getFile());
    }

    /*---------- ACCESSEURS ----------*/


    
    private enum TestTypeColRW implements TypeColR, TypeColW
    {
        TEST;

        @Override
        public String getValeur()
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getNomCol()
        {
            // TODO Auto-generated method stub
            return null;
        }        
    }
}