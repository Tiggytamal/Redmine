package control.parent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import model.enums.Environnement;
import utilities.CellHelper;
import utilities.DateConvert;

/**
 * Classe mère des contrôleurs pour les fichiers Excel
 * 
 * @author ETP137 - Grégoire Mathon
 *
 */
public abstract class ControlExcel
{
    /*---------- ATTRIBUTS ----------*/

    /** Fichier Excel à modifier */
    protected File file;
    /** Workbook représentant le fichier */
    protected Workbook wb;
    /** Gestionnaire de cellules */
    protected CellHelper helper;
    /** Indice de la dernière colonne de la feuille */
    protected int maxIndice;
    /** Ligne de titres de la feuille */
    protected Row titres;
    /** Helper de gestion du worlkbook */
    protected CreationHelper createHelper;
    /** Ancre pour les commentaire */
    protected ClientAnchor ca;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur du controleur. Crée le workbook, et les gestionnaire. Puis invoque la méthode {@codecalculIndiceColonnes} qui doit être implémentées dans les classe files<br>
     * pour calculer l'indice de chaque colonne de la feuille. Ne pas oublier d'utiliser la méthode {@code close} lorsque les traitements sont finis.
     * 
     * @param file
     * @throws InvalidFormatException
     * @throws IOException
     */
    protected ControlExcel(File file) throws InvalidFormatException, IOException
    {
        this.file = file;
        createWb();
        initColonnes();
        calculIndiceColonnes();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet de recréer un wokbook ainsi que les gestionnaires si celui-ci a été fermé.
     * 
     * @throws InvalidFormatException
     * @throws IOException
     */
    protected void createWb() throws InvalidFormatException, IOException
    {
        // Création du workbook depuis le fichier excel
        wb = WorkbookFactory.create(file);
        helper = new CellHelper(wb);
        createHelper = wb.getCreationHelper();
        ca = createHelper.createClientAnchor();
    }

    /**
     * Ferme un workbook.
     * 
     * @throws IOException
     */
    public void close() throws IOException
    {
        wb.close();
    }

    /**
     * Ecris le workbook dans le fichier cible.
     * 
     * @throws IOException
     */
    protected void write() throws IOException
    {
        wb.write(new FileOutputStream(file.getName()));
    }

    /**
     * Permet de changer la couleur de fond d'une ligne du fichier
     * 
     * @param row
     * @param couleur
     */
    protected void majCouleurLigne(Row row, IndexedColors couleur)
    {
        for (int j = 0; j < row.getLastCellNum(); j++)
        {
            Cell cell = row.getCell(j, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            CellStyle style = wb.createCellStyle();
            style.cloneStyleFrom(cell.getCellStyle());
            style.setFillForegroundColor(couleur.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(style);
        }
    }

    /**
     * recalcule la largeur de chaque colonne de la feuille.
     * 
     * @param sheet
     */
    protected void autosizeColumns(Sheet sheet)
    {
        for (int i = 0; i <= maxIndice; i++)
        {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Initialise les numéro des colonnes du fichier Excel venant de la PIC.
     */
    protected abstract void calculIndiceColonnes();

    /**
     * Initialise les noms des colonnes
     */
    protected abstract void initColonnes();

    /**
     * Met à jour l'indice max des colonnes
     * 
     * @param i
     */
    protected void testMax(int i)
    {
        if (maxIndice < i)
            maxIndice = i;
    }

    /**
     * Retourne la valeur d'une cellule de type String
     * 
     * @param row
     * @param cellIndex
     * @return
     */
    protected String getCellStringValue(Row row, int cellIndex)
    {
        Cell cell = row.getCell(cellIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellTypeEnum() == CellType.STRING)
            return cell.getStringCellValue();
        return "";
    }

    /**
     * Retourne la valeur d'une cellule de type Date
     * 
     * @param row
     * @param cellIndex
     * @return
     */
    protected LocalDate getCellDateValue(Row row, int cellIndex)
    {
        Cell cell = row.getCell(cellIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellTypeEnum() == CellType.NUMERIC)
            return DateConvert.localDate(cell.getDateCellValue());
        return null;
    }

    /**
     * Retourne la valeur d'une cellule de type numerique
     * 
     * @param row
     * @param cellIndex
     * @return
     */
    protected int getCellNumericValue(Row row, int cellIndex)
    {
        Cell cell = row.getCell(cellIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellTypeEnum() == CellType.NUMERIC)
            return (int) cell.getNumericCellValue();
        return 0;
    }
    
    protected String getCellFormulaValue(Row row, int cellIndex)
    {
        Cell cell = row.getCell(cellIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellTypeEnum() == CellType.FORMULA)
            return createHelper.createFormulaEvaluator().evaluate(cell).getStringValue();
        return "";
    }

    /**
     * Retourne le commentaire d'une cellule
     * 
     * @param row
     * @param cellIndex
     * @return
     */
    protected Comment getCellComment(Row row, int cellIndex)
    {
        Cell cell = row.getCell(cellIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return cell.getCellComment();
    }

    /**
     * Copie un commentaire dans une cellule
     * 
     * @param commentaire
     * @param cell
     */
    protected Comment copieComment(Comment commentaire, Cell cell)
    {
        if (cell == null || commentaire == null)
            throw new IllegalArgumentException("Arguments nuls pour méthode control.parent.ControlExcel.copieComment : commentaire = " + commentaire + " - cell = " + cell);
        
        // Drawing de base pour le commentaire
        Drawing<?> drawing = cell.getSheet().getDrawingPatriarch();
        if (drawing == null)
        drawing = cell.getSheet().createDrawingPatriarch();

        // On utilise la position relative du commentaire précedent pour créer le nouveau
        ca.setRow1(cell.getRowIndex());
        ca.setRow2(cell.getRowIndex() + commentaire.getClientAnchor().getRow2() - commentaire.getClientAnchor().getRow1());
        ca.setCol1(cell.getColumnIndex());
        ca.setCol2(cell.getColumnIndex() + commentaire.getClientAnchor().getCol2() - commentaire.getClientAnchor().getCol1());

        // Création et valorisation des données du commentaire
        Comment retour = drawing.createCellComment(ca);
        retour.setAuthor(commentaire.getAuthor());
        retour.setString(commentaire.getString());
        return retour;
    }

    /**
     * Crée un commentaire dans une cellule depuis un texte donné
     * 
     * @param commentaire
     * @param cell
     */
    protected Comment creerComment(String commentaire, String autheur, Cell cell, int largeur, int hauteur)
    {
        if (cell == null || commentaire == null || commentaire.isEmpty() || largeur == 0 || hauteur == 0)
            throw new IllegalArgumentException("Mauvais arguments pour méthode control.parent.ControlExcel.creerComment : cell = " + cell + " - commentaire = " + commentaire
                    + " - largeur = " + largeur + " - hauteur = " + hauteur);

        // Drawing de base pour le commentaire
        Drawing<?> drawing = cell.getSheet().getDrawingPatriarch();
        if (drawing == null)
        drawing = cell.getSheet().createDrawingPatriarch();
        
        // On utilise la position relative du commentaire précedent pour créer le nouveau
        ca.setRow1(cell.getRowIndex());
        ca.setRow2(cell.getRowIndex() + hauteur);
        ca.setCol1(cell.getColumnIndex());
        ca.setCol2(cell.getColumnIndex() + largeur);

        // Création et valorisation des données du commentaire
        Comment retour = drawing.createCellComment(ca);
        retour.setString(createHelper.createRichTextString(commentaire));
        if (autheur != null)
            retour.setAuthor(autheur);

        return retour;
    }

    /**
     * Permet de créer et de valoriser une cellule. Seul le style, le texte et le commentaire peuvent être nuls. Le texte peut-être de type {@link String},
     * {@linkplain model.enum.Environnement}, {@link LocalDate}.
     * 
     * @param row
     *            Ligne dans laquelle on veut créer la cellule
     * @param indexCol
     *            Index de colonne pour créer la cellule
     * @param style
     *            Style utilisé pour la cellule
     * @param texte
     *            Texte de la cellule
     * @param commentaire
     *            Commentaire de la cellule
     * @return
     */
    protected Cell valoriserCellule(Row row, int indexCol, CellStyle style, Object texte, Comment commentaire)
    {
        // Contrôle
        if (row == null)
            throw new IllegalArgumentException("Row row nul pour la méthode control.parent.ControlExcel.valoriserCellule.");

        // Création cellule
        Cell cell = row.createCell(indexCol);

        // Conversion du texte dans le bon format
        if (texte instanceof String)
            cell.setCellValue((String) texte);
        else if (texte instanceof Environnement)
            cell.setCellValue(((Environnement) texte).toString());
        else if (texte instanceof LocalDate)
            cell.setCellValue(DateConvert.convertToOldDate(texte));

        // Commentaire
        if (commentaire != null)
            copieComment(commentaire, cell);

        // Style
        if (style != null)
            cell.setCellStyle(style);
        return cell;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}