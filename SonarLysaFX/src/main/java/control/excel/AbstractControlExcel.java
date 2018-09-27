package control.excel;

import java.io.File;
import java.time.LocalDate;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import model.enums.EtatLot;
import model.enums.TypeAction;
import utilities.AbstractToStringImpl;
import utilities.CellHelper;
import utilities.DateConvert;

/**
 * Classe abstraite g�n�rique de contr�le des fichiers Excel
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public abstract class AbstractControlExcel extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/
    
    /** Param�trage pour �viter les Zip Bomb */
    private static final double MININFLATERATIO = 0;
    
    /** Fichier Excel � modifier */
    protected File file;
    /** Workbook repr�sentant le fichier */
    protected Workbook wb;
    /** Gestionnaire de cellules */
    protected CellHelper helper;
    /** Indice de la derni�re colonne de la feuille */
    protected int maxIndice;
    /** Helper de gestion du worlkbook */
    protected CreationHelper createHelper;
    /** Ancre pour les commentaire */
    protected ClientAnchor ca;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    protected AbstractControlExcel(File file)
    {
        this.file = file;
        // Changement de param�trage pour �viter les zip bombs
        ZipSecureFile.setMinInflateRatio(MININFLATERATIO);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES ABSTRAITES ----------*/
    
    /**
     * Initialise le WorkBook
     */
    protected abstract void createWb();
    
    /**
     * Initialisa les indices des colonnes
     */
    protected abstract void calculIndiceColonnes();
    
    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * recalcule la largeur de chaque colonne de la feuille.
     * 
     * @param sheet
     *            Feuillle � traiter
     */
    protected void autosizeColumns(Sheet sheet)
    {
        for (int i = 0; i <= maxIndice; i++)
        {
            sheet.autoSizeColumn(i);
        }
    }
    
    /**
     * Met � jour l'indice max des colonnes
     * 
     * @param i
     */
    protected void testMax(int i)
    {
        if (maxIndice < i)
            maxIndice = i;
    }
    
    /**
     * Permet de cr�er et de valoriser une cellule. Le texte peut-�tre de type {@link String}, {@linkplain model.enum.Environnement}, {@link LocalDate}.
     * 
     * @param row
     *            Ligne dans laquelle on veut cr�er la cellule
     * @param indexCol
     *            Index de colonne pour cr�er la cellule
     * @param style
     *            Style utilis� pour la cellule
     * @param texte
     *            Texte de la cellule
     * @param commentaire
     *            Commentaire de la cellule
     * @return
     */
    protected Cell valoriserCellule(Row row, Integer indexCol, CellStyle style, Object texte)
    {
        // Contr�le
        if (row == null || indexCol == null)
            throw new IllegalArgumentException("row null pour la m�thode control.parent.ControlExcel.valoriserCellule.");

        // Cr�ation cellule
        Cell cell = row.createCell(indexCol);

        // Style
        if (style != null)
            cell.setCellStyle(style);

        // Ajout du texte non null dans le bon format
        if (texte == null)
            return cell;

        if (texte instanceof String)
            cell.setCellValue((String) texte);
        else if (texte instanceof EtatLot)
            cell.setCellValue(((EtatLot) texte).getValeur());
        else if (texte instanceof TypeAction)
            cell.setCellValue(((TypeAction) texte).getValeur());
        else if (texte instanceof LocalDate)
            cell.setCellValue(DateConvert.convertToOldDate(texte));
        else
            cell.setCellValue(texte.toString());

        return cell;
    }
    
    /**
     * Copie un commentaire dans une cellule
     * 
     * @param commentaire
     *            Commentaire � ajouter
     * @param cell
     *            Cellule � traiter
     */
    protected Comment copieComment(Comment commentaire, Cell cell)
    {
        if (cell == null || commentaire == null)
            throw new IllegalArgumentException("Arguments nuls pour m�thode control.parent.ControlExcel.copieComment : commentaire = " + commentaire + " - cell = " + cell);

        // Drawing de base pour le commentaire
        Drawing<?> drawing = cell.getSheet().getDrawingPatriarch();
        if (drawing == null)
            drawing = cell.getSheet().createDrawingPatriarch();

        // On utilise la position relative du commentaire pr�cedent pour cr�er le nouveau
        ca.setRow1(cell.getRowIndex());
        ca.setRow2(cell.getRowIndex() + commentaire.getClientAnchor().getRow2() - commentaire.getClientAnchor().getRow1());
        ca.setCol1(cell.getColumnIndex());
        ca.setCol2(cell.getColumnIndex() + commentaire.getClientAnchor().getCol2() - commentaire.getClientAnchor().getCol1());

        // Cr�ation et valorisation des donn�es du commentaire
        Comment retour = drawing.createCellComment(ca);
        retour.setAuthor(commentaire.getAuthor());
        retour.setString(commentaire.getString());
        return retour;
    }
    
    /**
     * Copie d'une cellule
     * 
     * @param newCell
     *            Nouvelle cellule
     * @param oldCell
     *            Cellule copi�e
     */
    protected void copierCellule(Cell newCell, Cell oldCell)
    {
        // On sort si la cellule est nulle
        if (oldCell == null || newCell == null)
            return;

        // Copie du style des cellules
        CellStyle newCellStyle = wb.createCellStyle();
        newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
        newCell.setCellStyle(newCellStyle);

        // Copie des valeurs
        switch (oldCell.getCellTypeEnum())
        {
            case BOOLEAN:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;

            case FORMULA:
                newCell.setCellFormula(oldCell.getCellFormula());
                break;

            case NUMERIC:
                newCell.setCellValue(oldCell.getNumericCellValue());
                break;

            case STRING:
                newCell.setCellValue(oldCell.getRichStringCellValue());
                break;

            default:
                break;
        }

        // Commentaire
        Comment commentaire = oldCell.getCellComment();
        if (commentaire != null)
            copieComment(commentaire, newCell);
    }
    
    /*---------- ACCESSEURS ----------*/


}
