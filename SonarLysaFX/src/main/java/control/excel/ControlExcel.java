package control.excel;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
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

import model.enums.EtatLot;
import model.enums.TypeAction;
import model.enums.TypeCol;
import utilities.CellHelper;
import utilities.DateConvert;
import utilities.FunctionalException;
import utilities.TechnicalException;
import utilities.enums.Severity;

/**
 * Classe m�re des contr�leurs pour les fichiers Excel
 * 
 * @author ETP137 - Gr�goire Mathon
 *
 */
public abstract class ControlExcel<T extends Enum<T> & TypeCol, R>
{
    /*---------- ATTRIBUTS ----------*/

    /** Fichier Excel � modifier */
    protected File file;
    /** Workbook repr�sentant le fichier */
    protected Workbook wb;
    /** Gestionnaire de cellules */
    protected CellHelper helper;
    /** Indice de la derni�re colonne de la feuille */
    protected int maxIndice;
    /** Ligne de titres de la feuille */
    protected Row titres;
    /** Helper de gestion du worlkbook */
    protected CreationHelper createHelper;
    /** Ancre pour les commentaire */
    protected ClientAnchor ca;
    /** Classe de l'�num�ration des classes filles */
    protected Class<T> enumeration;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur du controleur. Cr�e le workbook, et les gestionnaire. Puis invoque la m�thode {@codecalculIndiceColonnes} qui doit �tre impl�ment�es dans les
     * classe files<br>
     * pour calculer l'indice de chaque colonne de la feuille. Ne pas oublier d'utiliser la m�thode {@code close} lorsque les traitements sont finis.
     * 
     * @param file
     * @throws InvalidFormatException
     * @throws IOException
     */
    protected ControlExcel(File file) 
    {
        this.file = file;
        createWb();
        initEnum();
        calculIndiceColonnes(initSheet());

    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Remonte les donn�es depuisle fichier Excel selon l'impl�mentation choisie.
     * 
     * @return
     */
    public abstract R recupDonneesDepuisExcel();

    /**
     * Initialise la classe de l'�num�ration
     */
    protected abstract void initEnum();

    /*---------- METHODES PRIVEES ----------*/

    /**
     * R�cup�ration de la feuille Excel pour le traitement. Remonte de base la premi�re feuille. Surcharge possible si besoin d'un traitement diff�rent.
     */
    protected Sheet initSheet()
    {
        Sheet sheet;
        try
        {
            sheet = wb.getSheetAt(0);
        } catch (IllegalArgumentException e)
        {
            throw new FunctionalException(Severity.ERROR, "Le fichier est vide");
        }
        return sheet;
    }

    /**
     * Initialise les num�ro des colonnes du fichier Excel venant de la PIC.
     */
    protected void calculIndiceColonnes(Sheet sheet)
    {
        titres = sheet.getRow(0);
        int nbreCol = 0;

        // R�cup�ration de l'�num�ration depuis les param�tres XML
        Map<String, T> mapColonnesInvert = proprietesXML.getMapColsInvert(enumeration);

        for (Cell cell : titres)
        {
            T typeCol = mapColonnesInvert.get(cell.getStringCellValue());

            if (cell.getCellTypeEnum() != CellType.STRING || typeCol == null)
                continue;

            // Initialisation du champ, calcul de l'indice max des colonnes, incr�mentation du nombre de colonnes et passage � l'�l�ment suivant.
            Field field;
            try
            {
                field = getClass().getDeclaredField(typeCol.getNomCol());
                field.setAccessible(true);
                field.set(this, cell.getColumnIndex());
                testMax((int) field.get(this));
                nbreCol++;
            } catch (NoSuchFieldException | IllegalAccessException e)
            {
                throw new TechnicalException("Erreur � l'affectation d'une variable lors de l'initialisation d'une colonne : " + cell.getStringCellValue(), e);
            }
        }

        // Gestion des erreurs si on ne trouve pas le bon nombre de colonnes
        if (nbreCol != enumeration.getEnumConstants().length)
            throw new FunctionalException(Severity.ERROR,
                    "Le fichier excel est mal configur�, v�rifi� les colonnes de celui-ci : Diff�rence = " + (enumeration.getEnumConstants().length - nbreCol));
    }

    /**
     * Permet de recr�er un wokbook ainsi que les gestionnaires si celui-ci a �t� ferm�.
     * 
     * @throws InvalidFormatException
     * @throws IOException
     */
    protected void createWb()
    {
        // Cr�ation du workbook depuis le fichier excel
        try
        {
            wb = WorkbookFactory.create(file);
        } catch (IOException | InvalidFormatException e)
        {
            throw new TechnicalException("Impossible de creer le Workbook pour " + file.getName(), e);
        }
        helper = new CellHelper(wb);
        createHelper = wb.getCreationHelper();
        ca = createHelper.createClientAnchor();
    }

    /**
     * Ferme un workbook.
     * 
     * @throws IOException
     *             Exception lors d'un probl�me I/O
     */
    public void close()
    {
        try
        {
            wb.close();
        } catch (IOException e)
        {
            throw new TechnicalException("Impossible de cl�turer le workbook du fichier : " + file.getName(), e);
        }
    }

    /**
     * Ecris le workbook dans le fichier cible.
     * 
     * @throws IOException
     *             Exception lors d'un probl�me I/O
     */
    protected void write()
    {
        try(FileOutputStream stream = new FileOutputStream(file.getName()))
        {
            wb.write(stream);
        } catch (IOException e)
        {
            throw new TechnicalException("Erreur au moment de sauvegarder le fichier Excel :" + file.getName(), e);
        }
    }

    /**
     * Permet de changer la couleur de fond d'une ligne du fichier
     * 
     * @param row
     *            Ligne � trait�e
     * @param couleur
     *            Couleur � utiliser pour le fond de la ligne
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
     * Retourne la valeur d'une cellule de type String
     * 
     * @param row
     *            Ligne � traiter
     * @param cellIndex
     *            Index de la cellule � traiter
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
     *            Ligne � traiter
     * @param cellIndex
     *            Index de la cellule � traiter
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
     *            Ligne � traiter
     * @param cellIndex
     *            Index de la cellule � traiter
     * @return
     */
    protected int getCellNumericValue(Row row, int cellIndex)
    {
        Cell cell = row.getCell(cellIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellTypeEnum() == CellType.NUMERIC)
            return (int) cell.getNumericCellValue();
        return 0;
    }

    /**
     * Retourne la valeur d'une cellule contenant une formule
     * 
     * @param row
     *            Ligne � traiter
     * @param cellIndex
     *            Index de la cellule � traiter
     * @return
     */
    protected String getCellFormulaValue(Row row, int cellIndex)
    {
        Cell cell = row.getCell(cellIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellTypeEnum() == CellType.FORMULA)
        {
            CellValue value = createHelper.createFormulaEvaluator().evaluate(cell);
            if (value.getStringValue() == null)
                return value.toString();
            return value.getStringValue();
        }
        return "";
    }

    /**
     * Retourne le commentaire d'une cellule
     * 
     * @param row
     *            Ligne � traiter
     * @param cellIndex
     *            Index de la cellule � traiter
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
     * Permet de cr�er et de valoriser une cellule. Seul le style, le texte et le commentaire peuvent �tre nuls. Le texte peut-�tre de type {@link String},
     * {@linkplain model.enum.Environnement}, {@link LocalDate}.
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
    protected Cell valoriserCellule(Row row, Integer indexCol, CellStyle style, Object texte, Comment commentaire)
    {
        // Contr�le
        if (row == null || indexCol == null)
            throw new IllegalArgumentException("row null pour la m�thode control.parent.ControlExcel.valoriserCellule.");

        // Cr�ation cellule
        Cell cell = row.createCell(indexCol);

        // Commentaire
        if (commentaire != null)
            copieComment(commentaire, cell);

        // Style
        if (style != null)
            cell.setCellStyle(style);

        // Ajout du texte non null dans le bon format
        if (texte == null)
            return cell;

        if (texte instanceof String)
            cell.setCellValue((String) texte);
        else if (texte instanceof EtatLot)
            cell.setCellValue(((EtatLot) texte).toString());
        else if (texte instanceof TypeAction)
            cell.setCellValue(((TypeAction) texte).toString());
        else if (texte instanceof LocalDate)
            cell.setCellValue(DateConvert.convertToOldDate(texte));
        else
            throw new IllegalArgumentException("Le texte n'est pas d'un type support� par la m�thode control.parent.ControlExcel.valoriserCellule.");

        return cell;
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