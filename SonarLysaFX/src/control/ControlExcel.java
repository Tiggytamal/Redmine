package control;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

public abstract class ControlExcel
{
	/*---------- ATTRIBUTS ----------*/

	protected File file;
	protected Workbook wb;
	
	/*---------- CONSTRUCTEURS ----------*/
	
	protected ControlExcel(File file) throws InvalidFormatException, IOException
	{
		this.file = file;
		createWb();
	}
	
	/*---------- METHODES PUBLIQUES ----------*/
	
	protected void createWb() throws InvalidFormatException, IOException
	{
	    // Cr�ation du workbook depuis le fichier excel
		wb = WorkbookFactory.create(file);
	}
	
	protected void close() throws IOException
	{
		wb.close();
	}
	
	/**
	 * Permet de changer la couleur de fond d'une ligne du fichier
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

	/*---------- METHODES PRIVEES ----------*/
	/*---------- ACCESSEURS ----------*/
}
