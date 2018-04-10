package junit.control.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.junit.Test;

import control.excel.ControlExcel;
import model.Anomalie;
import model.enums.TypeColSuivi;

public class ControlExcelSpecialTest extends ControlExcelTest<TypeColSuivi, ControlExcel<TypeColSuivi, List<Anomalie>>, List<Anomalie>>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public ControlExcelSpecialTest()
    {
        super(TypeColSuivi.class, "/resources/Tests.xlsx");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void calculIndiceColonnes() throws Exception
    {
        // Test initialisation colonnes. Pour ce fichier, la première colonne est utilisée
        calculIndiceColonnes(1);
    }

    @Test
    public void copierCellule() throws Exception
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
    public void copieCommentException1() throws Exception
    {
        // Récupération première cellule avec un commentaire, et envoi d'une cellule nulle en paramètre
        Cell cell1 = wb.getSheetAt(0).getRow(1).getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        invokeMethod(handler, "copieComment", cell1.getCellComment(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void copieCommentException2() throws Exception
    {
        // Envoi d'un commentaire vide en paramètre
        invokeMethod(handler, "copieComment", new Class[] { Comment.class, Cell.class }, null, null);
    }

    @Test
    public void copieComment() throws Exception
    {
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
                invokeMethod(handler, "copieComment", comment1, cell2);
                Comment comment2 = cell2.getCellComment();
                testEffectue = true;
                assertEquals(comment1.getString().getString(), comment2.getString().getString());
                assertEquals(comment1.getAuthor(), comment2.getAuthor());
            }
        }

        if (!testEffectue)
            fail("Pas de commentaire à tester");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
