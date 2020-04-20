package control.excel;

import static utilities.Statics.EMPTY;
import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.time.LocalDate;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import model.enums.ColR;
import model.enums.Severity;
import utilities.CellHelper;
import utilities.DateHelper;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe mère des contrôleurs pour les fichiers Excel en lecture
 * 
 * @author ETP137 - Grégoire Mathon
 * @since 1.0
 * 
 * @param <T>
 *        Enumeration représentant le type de colonne du fichier
 * @param <R>
 *        Liste des données à utiliser
 */
public abstract class AbstractControlExcelRead<T extends Enum<T> & ColR, R> extends AbstractControlExcel
{
    /*---------- ATTRIBUTS ----------*/

    /** Ligne de titres de la feuille */
    protected Row titres;
    /** Classe de l'énumération des classes filles */
    protected Class<T> enumeration;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur du contrôleur. Crée le workbook, et les gestionnaire. Puis invoque la methode {@code calculIndiceColonnes} qui doit être implementée
     * dans les classe files pour calculer l'indice de chaque colonne de la feuille.<br>
     * Ne pas oublier d'utiliser la methode {@code close} lorsque les traitements sont finis.
     * 
     * @param file
     *             Fichier à traiter
     */
    protected AbstractControlExcelRead(File file)
    {
        super(file);
        createWb();
        initEnum();
        calculIndiceColonnes();
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Remonte les données depuis le fichier Excel selon l'implementation choisie.
     * 
     * @return
     *         La liste des données extraites du fichier
     */
    public abstract R recupDonneesDepuisExcel();

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    /**
     * Récupération de la feuille Excel pour le traitement. Remonte de base la première feuille. Surcharge possible si besoin d'un traitement différent.
     * 
     * @return
     *         La représentation JAVA de la feuille du fichier
     */
    protected Sheet initSheet()
    {
        Sheet sheet;
        try
        {
            sheet = wb.getSheetAt(0);
        }
        catch (IllegalArgumentException e)
        {
            throw new FunctionalException(Severity.ERROR, "Le fichier est vide");
        }
        return sheet;
    }

    /**
     * Initialise les numéros des colonnes du fichier Excel.
     */
    @Override
    protected final void calculIndiceColonnes()
    {
        titres = initSheet().getRow(0);
        int nbreCol = 0;

        // Récupération de l'énumération depuis les paramètres XML, sauf que l'on inverse la map. le noms des colonnes passent en clef, et les valeurs de
        // l'énumération en valeur.
        Map<String, T> mapColonnesInvert = proprietesXML.getMapColsInvert(enumeration);

        for (Cell cell : titres)
        {
            T typeCol = mapColonnesInvert.get(cell.getStringCellValue());

            // On saute les types de cellule non STRING et les types de colonne inconnus
            if (cell.getCellType() != CellType.STRING || typeCol == null)
                continue;

            // Initialisation du champ, calcule de l'indice max des colonnes, incrémentation du nombre de colonnes et passage à l'élément suivant.
            Field field;
            try
            {
                field = getClass().getDeclaredField(typeCol.getNomCol());
                field.setAccessible(true);
                field.set(this, cell.getColumnIndex());
                testMax((int) field.get(this));
                nbreCol++;
            }
            catch (NoSuchFieldException | IllegalAccessException e)
            {
                throw new TechnicalException("Erreur à l'affectation d'une variable lors de l'initialisation d'une colonne : " + cell.getStringCellValue(), e);
            }
        }

        // Gestion des erreurs si on ne trouve pas le bon nombre de colonnes
        int enumLength = enumeration.getEnumConstants().length;
        if (nbreCol != enumLength)
            throw new FunctionalException(Severity.ERROR, "Le fichier excel est mal configuré, vérifier les colonnes de celui-ci : Difference = " + (enumLength - nbreCol));
    }

    /**
     * Permet de recréer un wokbook ainsi que les gestionnaires si celui-ci a été fermé.
     * 
     */
    protected final void createWb()
    {
        // Création du workbook depuis le fichier excel
        try
        {
            wb = WorkbookFactory.create(Files.newInputStream(file.toPath()));
        }
        catch (IOException | InvalidPathException e)
        {
            throw new TechnicalException("Impossible de créer le Workbook pour " + file.getName(), e);
        }
        helper = new CellHelper(wb);
        createHelper = wb.getCreationHelper();
    }

    /**
     * Permet de changer la couleur de fond d'une ligne du fichier.
     * 
     * @param row
     *                Ligne à traiter
     * @param couleur
     *                Couleur à utiliser pour le fond de la ligne
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
     * Retourne la valeur d'une cellule de type String.
     * 
     * @param row
     *                  Ligne à traiter
     * @param cellIndex
     *                  Index de la cellule à traiter
     * @return
     *         La valeur de la cellule.
     */
    protected String getCellStringValue(Row row, int cellIndex)
    {
        Cell cell = row.getCell(cellIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellType() == CellType.STRING)
            return cell.getStringCellValue().trim();
        return EMPTY;
    }

    /**
     * Retourne la valeur d'une cellule de type Date.
     * 
     * @param row
     *                  Ligne à traiter
     * @param cellIndex
     *                  Index de la cellule à traiter
     * @return
     *         La valeur de la cellule.
     */
    protected LocalDate getCellDateValue(Row row, int cellIndex)
    {
        Cell cell = row.getCell(cellIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellType() == CellType.NUMERIC)
            return DateHelper.localDate(cell.getDateCellValue());
        return null;
    }

    /**
     * Retourne la valeur d'une cellule de type numérique.
     * 
     * @param row
     *                  Ligne à traiter
     * @param cellIndex
     *                  Index de la cellule à traiter
     * @return
     *         La valeur de la cellule.
     */
    protected int getCellNumericValue(Row row, int cellIndex)
    {
        Cell cell = row.getCell(cellIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellType() == CellType.NUMERIC)
            return (int) cell.getNumericCellValue();
        return 0;
    }

    /**
     * Retourne la valeur d'une cellule contenant une formule.
     * 
     * @param row
     *                  Ligne à traiter
     * @param cellIndex
     *                  Index de la cellule à traiter
     * @return
     *         La valeur de la cellule.
     */
    protected String getCellFormulaValue(Row row, int cellIndex)
    {
        Cell cell = row.getCell(cellIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellType() == CellType.FORMULA)
            return createHelper.createFormulaEvaluator().evaluate(cell).formatAsString();
        return EMPTY;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Initialisation de l'énumération correspondante au fichier traité.
     */
    @SuppressWarnings("unchecked")
    private void initEnum()
    {
        // Permet de récupérer la classe sous forme de ParameterizedType
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On récupère les paramètres de classe (ici T et R), on prend le premier (T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = Statics.PATTERNSPACE.split(pt.getActualTypeArguments()[0].toString())[1];

        // Instanciation de l'énumération
        try
        {
            enumeration = (Class<T>) Class.forName(parameterClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new TechnicalException("Impossible d'instancier l'énumération - control.excel.AbstractControlExcelRead", e);
        }
    }

    /*---------- ACCESSEURS ----------*/
}
