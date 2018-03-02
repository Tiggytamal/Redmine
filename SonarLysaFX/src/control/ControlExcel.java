package control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import utilities.CellHelper;

/**
 * Classe m�re des contr�leurs pour les fichiers Excel
 * 
 * @author ETP137 - Gr�goire Mathon
 *
 */
public abstract class ControlExcel
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
    // * Helper de gestion du worlkbook */
    protected CreationHelper createHelper;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur du controleur. Cr�e le workbook, et les gestionnaire. Puis invoque la m�thode {@codecalculIndiceColonnes} qui doit �tre impl�ment�es dans
     * les classe files<br>
     * pour calculer l'indice de chaque colonne de la feuille. Ne pas oublier d'utiliser la m�thode {@code close} lorsque les traitements sont finis.
     * 
     * @param file
     * @throws InvalidFormatException
     * @throws IOException
     */
    protected ControlExcel(File file) throws InvalidFormatException, IOException
    {
        this.file = file;
        createWb();
        calculIndiceColonnes();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet de recr�er un wokbook ainsi que les gestionnaires si celui-ci a �t� ferm�.
     * 
     * @throws InvalidFormatException
     * @throws IOException
     */
    protected void createWb() throws InvalidFormatException, IOException
    {
        // Cr�ation du workbook depuis le fichier excel
        wb = WorkbookFactory.create(file);
        helper = new CellHelper(wb);
        createHelper = wb.getCreationHelper();
    }

    /**
     * Ferme un workbook.
     * 
     * @throws IOException
     */
    protected void close() throws IOException
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
     * Initialise les num�ro des colonnes du fichier Excel venant de la PIC.
     */
    protected abstract void calculIndiceColonnes();

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

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
