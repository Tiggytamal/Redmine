package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.mchange.util.AssertException;

import control.rtc.ControlRTC;
import control.task.CreerVueParAppsTask;
import model.enums.CreerVueParAppsTaskOption;
import model.sonarapi.Projet;
import utilities.Statics;
import utilities.TechnicalException;

public class TestCreerVueParAppsTask extends AbstractTestTask<CreerVueParAppsTask>
{
    /*---------- ATTRIBUTS ----------*/
   
    private File file;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init() throws IllegalAccessException
    {
        file = new File(Statics.RESSTEST + "testExtract.xlsx");
        ControlRTC.INSTANCE.connexion();  
        handler = new CreerVueParAppsTask(CreerVueParAppsTaskOption.FICHIERS, file);  
        initAPI(CreerVueParAppsTask.class, true);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test (expected = TechnicalException.class)
    public void testConstructeurException()
    {
        // Exception car demande cr�ation de fichier et pas de fichier en entr�e
        handler = new CreerVueParAppsTask(CreerVueParAppsTaskOption.ALL, null);
    }
    
    @Test (expected = TechnicalException.class)
    public void testConstructeurException2()
    {
        // Exception car demande cr�ation de fichier et pas de fichier en entr�e
        handler = new CreerVueParAppsTask(CreerVueParAppsTaskOption.FICHIERS, null);
    }
    
    @Test
    public void testCreerVueParApplicationExcelSeul() throws Exception
    {
        handler = new CreerVueParAppsTask(CreerVueParAppsTaskOption.FICHIERS, file); 
        assertFalse(Whitebox.invokeMethod(handler, "call"));
        
        Workbook wb = WorkbookFactory.create(file);
        Sheet sheet = wb.getSheet("P�rim�tre Couverts SonarQbe");
        if (sheet == null)
            throw new AssertException("La feuille 'P�rim�tre Couverts SonarQbe' n'a pas �t� cr��e");
        
        //Initialisation des indices des colonnes � tester
        int colLib = 0;
        int colCode = 0;
        int colSecu = 0;
        int colLdc = 0;
        
        for (Iterator<Cell> iter =  sheet.getRow(0).cellIterator(); iter.hasNext();)
        {
            Cell cell = iter.next();
            
            if ("LDC SonarQube".equals(cell.getStringCellValue()))
                colLdc = cell.getColumnIndex();
            else if ("Valeur S�curit�".equals(cell.getStringCellValue()))
                colSecu = cell.getColumnIndex();
            else if ("Libell�".equals(cell.getStringCellValue()))
                colLib = cell.getColumnIndex();
            else if ("Code Application".equals(cell.getStringCellValue()))
                colCode = cell.getColumnIndex();
        }
        
        // Cr�ation it�rateur des lignes en supprimant les titres        
        Iterator<Row> iter =  sheet.rowIterator();
        if (iter.hasNext())
        {
            iter.next();
            iter.remove();
        }
        
        // It�ration sur les lignes pour v�rifier les donn�es
        while (iter.hasNext())
        {
            Row row = iter.next();
            
            // Test le lib�ll� est pr�sent
            assertFalse(row.getCell(colLib, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().isEmpty());
            
            // Test le code applicaiton est pr�sent
            assertFalse(row.getCell(colCode, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().isEmpty());   

            // Test le nombre de lignes est sup�rieur � z�ro
            assertTrue(Integer.parseInt(row.getCell(colLdc, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue()) > 0);
            
            // Test la s�curit� est pr�sente
            assertEquals(1, row.getCell(colSecu, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().length()); 
        }
        
    }
    
    @Test
    public void testCreerVueParApplicationVueSeul() throws Exception
    {    
        // Intialisation handler et mock SonarAPI pour ne pas cr�eer de vue ni les supprimer.
        handler = new CreerVueParAppsTask(CreerVueParAppsTaskOption.VUE, file);  
        initAPI(CreerVueParAppsTask.class, true);
        List<Projet> retour = new ArrayList<>();
        retour.add(new Projet("id", "key", "nom", null, null, null));
        Mockito.doReturn(retour).when(api).getVuesParNom("APPLI MASTER ");
        
        assertTrue(Whitebox.invokeMethod(handler, "call"));
    }
    
    @Test (expected = TechnicalException.class)
    public void testCreerFichierExtractionException() throws Exception
    {
        // Appel de la m�thode avec une option de cr�ation de vue uniquement
        handler = new CreerVueParAppsTask(CreerVueParAppsTaskOption.VUE, null);
        
        Whitebox.invokeMethod(handler, "creerFichiersExtraction");
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
