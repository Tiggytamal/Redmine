package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.mockito.Mockito;

import control.excel.ControlSuivi;
import model.bdd.DefautQualite;
import model.enums.TypeColSuivi;
import utilities.Statics;

public class TestControlExcelSpecial extends TestControlExcelRead<TypeColSuivi, ControlSuivi, List<DefautQualite>>
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final String COPIECOMMENT = "copieComment";
    private static final String VALORISERCELLULE = "valoriserCellule";   
    
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlExcelSpecial()
    {
        super(TypeColSuivi.class, "Tests.xlsx");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testCalculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la premi�re colonne est utilis�e
        testCalculIndiceColonnes(2);
    }

    @Test
    public void testCopierCellule() throws Exception
    {
        // Initialisation - cr�ation des deux lignes
        Row row1 = wb.getSheetAt(0).getRow(1);
        Row row2 = wb.getSheetAt(0).createRow(2);

        // It�ration sur la premi�re pour cr�er les cellules de la deuxi�me
        for (int i = 0; i < row1.getPhysicalNumberOfCells(); i++)
        {
            Cell cell1 = row1.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Cell cell2 = row2.createCell(i);
            invokeMethod(controlTest, "copierCellule", cell2, cell1);

            // Assertion sur les cellules
            assertNotNull(cell1);
            assertEquals(cell1.getCellTypeEnum(), cell2.getCellTypeEnum());

            // Test sur le texte des commentaires
            if (cell1.getCellComment() != null)
                assertEquals(cell1.getCellComment().getString().getString(), cell2.getCellComment().getString().getString());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopieCommentException1() throws Exception
    {
        // R�cup�ration premi�re cellule avec un commentaire, et envoi d'une cellule nulle en param�tre
        Cell cell = wb.getSheetAt(0).getRow(1).getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        invokeMethod(controlTest, COPIECOMMENT, cell.getCellComment(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopieCommentException2() throws Exception
    {
        // Envoi des deux param�tres nuls
        invokeMethod(controlTest, COPIECOMMENT, new Class[] { Comment.class, Cell.class }, null, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCopieCommentException3() throws Exception
    {
        // Envoi d'un commentaire vide en param�tre
        Cell cell = wb.getSheetAt(0).getRow(1).getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        invokeMethod(controlTest, COPIECOMMENT, new Class[] { Comment.class, Cell.class }, null, cell);
    }

    @Test
    public void testCopieComment1() throws Exception
    {
        // test avec drawing de base de la feuille
        // Initialisation - cr�ation de la ligne
        Row row1 = wb.getSheetAt(0).getRow(1);
        boolean testEffectue = false;

        // It�ration pour r�cup�rer les cellules avec des commentaires
        for (int i = 0; i < row1.getPhysicalNumberOfCells(); i++)
        {
            Cell cell1 = row1.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Cell cell2 = wb.getSheetAt(0).createRow(3).createCell(i);
            
            // Appel m�thode et Test sur les attributs du commentaire
            if (cell1.getCellComment() != null)
            {                
                Comment comment1 = cell1.getCellComment();
                invokeMethod(controlTest, COPIECOMMENT, comment1, cell2);
                Comment comment2 = cell2.getCellComment();
                testEffectue = true;
                assertEquals(comment1.getString().getString(), comment2.getString().getString());
                assertEquals(comment1.getAuthor(), comment2.getAuthor());
            }
        }

        if (!testEffectue)
            fail("Pas de commentaire � tester");
    }
    
    @Test
    public void testCopieComment2() throws Exception
    {
        // test avec drawing de la feuille � nul
        // Initialisation - cr�ation de la ligne
        Row row1 = wb.getSheetAt(0).getRow(1);
        boolean testEffectue = false;

        // It�ration pour r�cup�rer les cellules avec des commentaires
        for (int i = 0; i < row1.getPhysicalNumberOfCells(); i++)
        {
            // Test dans le cas d'un drawingpatriarch nul

            Cell cell1 = row1.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            
            Sheet sheet = Mockito.mock(Sheet.class);
            Mockito.when(sheet.getDrawingPatriarch()).thenReturn(null);
            Cell cell = Mockito.mock(Cell.class);
            Mockito.when(cell.getSheet()).thenReturn(sheet, cell1.getSheet());
            
            // Appel m�thode et Test sur les attributs du commentaire
            if (cell1.getCellComment() != null)
            {                
                Comment comment1 = cell1.getCellComment();       
                Comment comment2 = invokeMethod(controlTest, COPIECOMMENT, comment1, cell);
                assertEquals(comment1.getString().getString(), comment2.getString().getString());
                assertEquals(comment1.getAuthor(), comment2.getAuthor());
                testEffectue = true;
                break;
            }
        }

        if (!testEffectue)
            fail("Pas de commentaire � tester");
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testValoriserCelluleException1() throws Exception
    {
        // Appel m�thode avec ligne nulle       
        invokeMethod(controlTest, VALORISERCELLULE, new Class[] {Row.class, Integer.class, CellStyle.class, Object.class}, null, 1, null, Statics.EMPTY); 
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValoriserCelluleException2() throws Exception
    {
        // Initialisation
        Row row = wb.getSheetAt(0).getRow(1);
        
        // Appel m�thode avec conIndex null
        invokeMethod(controlTest, VALORISERCELLULE, new Class[] {Row.class, Integer.class, CellStyle.class, Object.class}, row, null, null, 32);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}