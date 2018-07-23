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
import model.Anomalie;
import model.enums.TypeColSuivi;

public class TestControlExcelSpecial extends TestControlExcelRead<TypeColSuivi, ControlSuivi, List<Anomalie>>
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
        // Test initialisation colonnes. Pour ce fichier, la première colonne est utilisée
        testCalculIndiceColonnes(2);
    }

    @Test
    public void testCopierCellule() throws Exception
    {
        // Initialisation - création des deux lignes
        Row row1 = wb.getSheetAt(0).getRow(1);
        Row row2 = wb.getSheetAt(0).createRow(2);

        // Itération sur la première pour créer les cellules de la deuxième
        for (int i = 0; i < row1.getPhysicalNumberOfCells(); i++)
        {
            Cell cell1 = row1.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Cell cell2 = row2.createCell(i);
            invokeMethod(handler, "copierCellule", cell2, cell1);

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
        // Récupération première cellule avec un commentaire, et envoi d'une cellule nulle en paramètre
        Cell cell = wb.getSheetAt(0).getRow(1).getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        invokeMethod(handler, COPIECOMMENT, cell.getCellComment(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopieCommentException2() throws Exception
    {
        // Envoi des deux paramètres nuls
        invokeMethod(handler, COPIECOMMENT, new Class[] { Comment.class, Cell.class }, null, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCopieCommentException3() throws Exception
    {
        // Envoi d'un commentaire vide en paramètre
        Cell cell = wb.getSheetAt(0).getRow(1).getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        invokeMethod(handler, COPIECOMMENT, new Class[] { Comment.class, Cell.class }, null, cell);
    }

    @Test
    public void testCopieComment1() throws Exception
    {
        // test avec drawing de base de la feuille
        // Initialisation - création de la ligne
        Row row1 = wb.getSheetAt(0).getRow(1);
        boolean testEffectue = false;

        // Itération pour récupérer les cellules avec des commentaires
        for (int i = 0; i < row1.getPhysicalNumberOfCells(); i++)
        {
            Cell cell1 = row1.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Cell cell2 = wb.getSheetAt(0).createRow(3).createCell(i);
            
            // Appel méthode et Test sur les attributs du commentaire
            if (cell1.getCellComment() != null)
            {                
                Comment comment1 = cell1.getCellComment();
                invokeMethod(handler, COPIECOMMENT, comment1, cell2);
                Comment comment2 = cell2.getCellComment();
                testEffectue = true;
                assertEquals(comment1.getString().getString(), comment2.getString().getString());
                assertEquals(comment1.getAuthor(), comment2.getAuthor());
            }
        }

        if (!testEffectue)
            fail("Pas de commentaire à tester");
    }
    
    @Test
    public void testCopieComment2() throws Exception
    {
        // test avec drawing de la feuille à nul
        // Initialisation - création de la ligne
        Row row1 = wb.getSheetAt(0).getRow(1);
        boolean testEffectue = false;

        // Itération pour récupérer les cellules avec des commentaires
        for (int i = 0; i < row1.getPhysicalNumberOfCells(); i++)
        {
            // Test dans le cas d'un drawingpatriarch nul

            Cell cell1 = row1.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            
            Sheet sheet = Mockito.mock(Sheet.class);
            Mockito.when(sheet.getDrawingPatriarch()).thenReturn(null);
            Cell cell = Mockito.mock(Cell.class);
            Mockito.when(cell.getSheet()).thenReturn(sheet, cell1.getSheet());
            
            // Appel méthode et Test sur les attributs du commentaire
            if (cell1.getCellComment() != null)
            {                
                Comment comment1 = cell1.getCellComment();       
                Comment comment2 = invokeMethod(handler, COPIECOMMENT, comment1, cell);
                assertEquals(comment1.getString().getString(), comment2.getString().getString());
                assertEquals(comment1.getAuthor(), comment2.getAuthor());
                testEffectue = true;
                break;
            }
        }

        if (!testEffectue)
            fail("Pas de commentaire à tester");
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testValoriserCelluleException1() throws Exception
    {
        // Appel méthode avec ligne nulle       
        invokeMethod(handler, VALORISERCELLULE, new Class[] {Row.class, Integer.class, CellStyle.class, Object.class, Comment.class}, null, 1, null, "", null);        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValoriserCelluleException2() throws Exception
    {
        // Initialisation
        Row row = wb.getSheetAt(0).getRow(1);
        
        // Appel méthode avec object texte non pris en compte
        invokeMethod(handler, VALORISERCELLULE, new Class[] {Row.class, Integer.class, CellStyle.class, Object.class, Comment.class}, row, 1, null, 32, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValoriserCelluleException3() throws Exception
    {
        // Initialisation
        Row row = wb.getSheetAt(0).getRow(1);
        
        // Appel méthode avec conIndex null
        invokeMethod(handler, VALORISERCELLULE, new Class[] {Row.class, Integer.class, CellStyle.class, Object.class, Comment.class}, row, null, null, 32, null);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}