package control.excel;

import static utilities.Statics.proprietesXML;
import static utilities.Statics.EMPTY;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import model.enums.TypeColR;
import utilities.CellHelper;
import utilities.DateConvert;
import utilities.FunctionalException;
import utilities.TechnicalException;
import utilities.enums.Severity;

/**
 * Classe m�re des contr�leurs pour les fichiers Excel en lecture
 * 
 * @author ETP137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
public abstract class AbstractControlExcelRead<T extends Enum<T> & TypeColR, R> extends AbstractControlExcel
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");
    
    /** Ligne de titres de la feuille */
    protected Row titres;
    /** Classe de l'�num�ration des classes filles */
    protected Class<T> enumeration; 

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur du controleur. Cr�e le workbook, et les gestionnaire. Puis invoque la m�thode {@codecalculIndiceColonnes} qui doit �tre impl�ment�es dans les
     * classe files<br>
     * pour calculer l'indice de chaque colonne de la feuille. Ne pas oublier d'utiliser la m�thode {@code close} lorsque les traitements sont finis.
     * 
     * @param file
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
     * Remonte les donn�es depuis le fichier Excel selon l'impl�mentation choisie.
     * 
     * @return
     */
    public abstract R recupDonneesDepuisExcel();
    
    /*---------- METHODES PUBLIQUES ----------*/
    
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
        } 
        catch (IOException e)
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
    public boolean write()
    {
        
        try(FileOutputStream stream = new FileOutputStream(file.getName()))
        {
            long time = file.lastModified();
            wb.write(stream);
            return time < file.lastModified();
        } 
        catch (IOException e)
        {
            throw new TechnicalException("Erreur au moment de sauvegarder le fichier Excel :" + file.getName(), e);
        }
    }
    
    /*---------- METHODES PROTECTED ----------*/
    
    /**
     * R�cup�ration de la feuille Excel pour le traitement. Remonte de base la premi�re feuille. Surcharge possible si besoin d'un traitement diff�rent.
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
            LOGPLANTAGE.error(e);
            throw new FunctionalException(Severity.ERROR, "Le fichier est vide");
        }
        return sheet;
    }

    /**
     * Initialise les num�ro des colonnes du fichier Excel.
     */
    @Override
    protected final void calculIndiceColonnes()
    {
        titres = initSheet().getRow(0); 
        int nbreCol = 0;

        // R�cup�ration de l'�num�ration depuis les param�tres XML, sauf que l'on inverse la map. le noms des colonnes passe en clef, et la valeur de l'�num�ration en valeur.
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
            } 
            catch (NoSuchFieldException | IllegalAccessException e)
            {
                throw new TechnicalException("Erreur � l'affectation d'une variable lors de l'initialisation d'une colonne : " + cell.getStringCellValue(), e);
            }
        }

        // Gestion des erreurs si on ne trouve pas le bon nombre de colonnes
        int enumLength = enumeration.getEnumConstants().length;
        if (nbreCol != enumLength)
            throw new FunctionalException(Severity.ERROR,
                    "Le fichier excel est mal configur�, v�rifi� les colonnes de celui-ci : Diff�rence = " + (enumLength - nbreCol));
    }

    /**
     * Permet de recr�er un wokbook ainsi que les gestionnaires si celui-ci a �t� ferm�.
     * 
     * @throws InvalidFormatException
     * @throws IOException
     */
    protected final void createWb()
    {
        // Cr�ation du workbook depuis le fichier excel
        try
        {
            wb = WorkbookFactory.create(new FileInputStream(file));
        } 
        catch (IOException | InvalidFormatException e)
        {
            throw new TechnicalException("Impossible de creer le Workbook pour " + file.getName(), e);
        }
        helper = new CellHelper(wb);
        createHelper = wb.getCreationHelper();
        ca = createHelper.createClientAnchor();
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
        return EMPTY;
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
        return EMPTY;
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
    
    /*---------- METHODES PRIVEES ----------*/

    @SuppressWarnings("unchecked")
    private void initEnum()
    {
        // Permet de r�cuperer la classe sous forme de type param�tr�
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        
        // On r�cup�re les param�tres de classe (ici T et R), on prend le premier (T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];
        
        // Instantiate the Parameter and initialize it.
        try
        {
            enumeration = (Class<T>) Class.forName(parameterClassName);
        } 
        catch (ClassNotFoundException e)
        {
            LOGPLANTAGE.error(e);
            throw new TechnicalException("Impossible d'instancier l'�num�ration - control.excel.ControlExcelRead", e);
        }
    }

    /*---------- ACCESSEURS ----------*/
}
