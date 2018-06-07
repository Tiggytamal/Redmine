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
 * Classe mère des contrôleurs pour les fichiers Excel
 * 
 * @author ETP137 - Grégoire Mathon
 *
 */
public abstract class ControlExcel<T extends Enum<T> & TypeCol, R>
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
    /** Classe de l'énumération des classes filles */
    protected Class<T> enumeration;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur du controleur. Crée le workbook, et les gestionnaire. Puis invoque la méthode {@codecalculIndiceColonnes} qui doit être implémentées dans les
     * classe files<br>
     * pour calculer l'indice de chaque colonne de la feuille. Ne pas oublier d'utiliser la méthode {@code close} lorsque les traitements sont finis.
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
     * Remonte les données depuisle fichier Excel selon l'implémentation choisie.
     * 
     * @return
     */
    public abstract R recupDonneesDepuisExcel();

    /**
     * Initialise la classe de l'énumération
     */
    protected abstract void initEnum();

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Récupération de la feuille Excel pour le traitement. Remonte de base la première feuille. Surcharge possible si besoin d'un traitement différent.
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
     * Initialise les numéro des colonnes du fichier Excel venant de la PIC.
     */
    protected void calculIndiceColonnes(Sheet sheet)
    {
        titres = sheet.getRow(0);
        int nbreCol = 0;

        // Récupération de l'énumération depuis les paramètres XML
        Map<String, T> mapColonnesInvert = proprietesXML.getMapColsInvert(enumeration);

        for (Cell cell : titres)
        {
            T typeCol = mapColonnesInvert.get(cell.getStringCellValue());

            if (cell.getCellTypeEnum() != CellType.STRING || typeCol == null)
                continue;

            // Initialisation du champ, calcul de l'indice max des colonnes, incrémentation du nombre de colonnes et passage à l'élément suivant.
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
                throw new TechnicalException("Erreur à l'affectation d'une variable lors de l'initialisation d'une colonne : " + cell.getStringCellValue(), e);
            }
        }

        // Gestion des erreurs si on ne trouve pas le bon nombre de colonnes
        if (nbreCol != enumeration.getEnumConstants().length)
            throw new FunctionalException(Severity.ERROR,
                    "Le fichier excel est mal configuré, vérifié les colonnes de celui-ci : Différence = " + (enumeration.getEnumConstants().length - nbreCol));
    }

    /**
     * Permet de recréer un wokbook ainsi que les gestionnaires si celui-ci a été fermé.
     * 
     * @throws InvalidFormatException
     * @throws IOException
     */
    protected void createWb()
    {
        // Création du workbook depuis le fichier excel
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
     *             Exception lors d'un problème I/O
     */
    public void close()
    {
        try
        {
            wb.close();
        } catch (IOException e)
        {
            throw new TechnicalException("Impossible de clôturer le workbook du fichier : " + file.getName(), e);
        }
    }

    /**
     * Ecris le workbook dans le fichier cible.
     * 
     * @throws IOException
     *             Exception lors d'un problème I/O
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
     *            Ligne à traitée
     * @param couleur
     *            Couleur à utiliser pour le fond de la ligne
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
     *            Feuillle à traiter
     */
    protected void autosizeColumns(Sheet sheet)
    {
        for (int i = 0; i <= maxIndice; i++)
        {
            sheet.autoSizeColumn(i);
        }
    }

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
     *            Ligne à traiter
     * @param cellIndex
     *            Index de la cellule à traiter
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
     *            Ligne à traiter
     * @param cellIndex
     *            Index de la cellule à traiter
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
     *            Ligne à traiter
     * @param cellIndex
     *            Index de la cellule à traiter
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
     *            Ligne à traiter
     * @param cellIndex
     *            Index de la cellule à traiter
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
     *            Ligne à traiter
     * @param cellIndex
     *            Index de la cellule à traiter
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
     *            Commentaire à ajouter
     * @param cell
     *            Cellule à traiter
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
    protected Cell valoriserCellule(Row row, Integer indexCol, CellStyle style, Object texte, Comment commentaire)
    {
        // Contrôle
        if (row == null || indexCol == null)
            throw new IllegalArgumentException("row null pour la méthode control.parent.ControlExcel.valoriserCellule.");

        // Création cellule
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
            throw new IllegalArgumentException("Le texte n'est pas d'un type supporté par la méthode control.parent.ControlExcel.valoriserCellule.");

        return cell;
    }

    /**
     * Copie d'une cellule
     * 
     * @param newCell
     *            Nouvelle cellule
     * @param oldCell
     *            Cellule copiée
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