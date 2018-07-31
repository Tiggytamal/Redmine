package control.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import model.Application;
import model.enums.Param;
import model.enums.TypeColApps;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.enums.Bordure;
import utilities.enums.Severity;

/**
 * Classe de controle des applications en �criture
 * 
 * @author ETP8137 - Gr�goier Mathon
 * @since 1.0
 *
 */
public class ControlAppsW extends AbstractControlExcelWrite<TypeColApps, Collection<Application>>
{
    /*---------- ATTRIBUTS ----------*/

    private int colCode;
    private int colActif;
    private int colLib;
    private int colOpen;
    private int colMainFrame;
    private int colCrit;
    private int colVuln;
    private int colLDCSonar;
    private int colLDCMain;
    private File fileIn;
    private static final String APPLIGEREES = "P�rim�tre Couverts SonarQbe";
    private static final String REFAPPLIS = "Ref CodeApps detaill�s";

    /*---------- CONSTRUCTEURS ----------*/

    ControlAppsW(File sortie)
    {
        super(sortie);
        
        // Cr�ation des deux feuilles ud fichier Excel
        wb.createSheet(APPLIGEREES);
        wb.createSheet(REFAPPLIS);
        calculIndiceColonnes();
        initTitres();
        fileIn = new File (Statics.proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + Statics.proprietesXML.getMapParams().get(Param.NOMFICHIERAPPLI));
        if (!fileIn.exists())
            throw new FunctionalException(Severity.ERROR, "Le fichier des applicaitons est introuvables. merci de v�rifier le param�trage.");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void creerfeuilleSonar(Set<Application> applisOpenSonar)
    {
        Sheet sheet = wb.getSheet(APPLIGEREES);
        enregistrerDonnees(applisOpenSonar, sheet);
    }

    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected final void calculIndiceColonnes()
    {
        colCode = 0;
        colActif = 1;
        colLib = 2;
        colOpen = 3;
        colMainFrame = 4;
        colCrit = 5;
        colVuln = 6;
        colLDCSonar = 7;
        colLDCMain = 8;
    }

    @Override
    protected final void initTitres()
    {
        CellStyle centre = helper.getStyle(IndexedColors.AQUA, Bordure.BAS, HorizontalAlignment.CENTER);
        centre.setWrapText(false);

        // Cr�ation de la ligne de titre
        Sheet sheet = wb.getSheet(APPLIGEREES);

        Row row = sheet.createRow(0);
        valoriserCellule(row, colCode, centre, "Code Application");
        valoriserCellule(row, colActif, centre, "Actif");
        valoriserCellule(row, colLib, centre, "Libell�");
        valoriserCellule(row, colOpen, centre, "Open");
        valoriserCellule(row, colMainFrame, centre, "MainFrame");
        valoriserCellule(row, colCrit, centre, "Valeur S�curit�");
        valoriserCellule(row, colVuln, centre, "Vuln�rabilit�s");
        valoriserCellule(row, colLDCSonar, centre, "LDC SonarQube");
        valoriserCellule(row, colLDCMain, centre, "LDC MainFrame");
        autosizeColumns(sheet);
    }

    @Override
    protected void enregistrerDonnees(Collection<Application> donnees, Sheet sheet)
    {
        List<String> codeApps = new ArrayList<>();
        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        centre.setWrapText(false);
        Row row;

        for (Application app : donnees)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            valoriserCellule(row, colCode, centre, app.getCode());
            valoriserCellule(row, colActif, centre, String.valueOf(app.isActif()));
            valoriserCellule(row, colLib, centre, app.getLibelle());
            valoriserCellule(row, colOpen, centre, String.valueOf(app.isOpen()));
            valoriserCellule(row, colMainFrame, centre, String.valueOf(app.isMainFrame()));
            valoriserCellule(row, colCrit, centre, app.getValSecurite());
            valoriserCellule(row, colVuln, centre, String.valueOf(app.getNbreVulnerabilites()));
            valoriserCellule(row, colLDCSonar, centre, String.valueOf(app.getLDCSonar()));
            valoriserCellule(row, colLDCMain, centre, String.valueOf(app.getLDCMainframe()));
            codeApps.add(app.getCode());
        }
        autosizeColumns(sheet);
        calculDeuxiemeFeuille(codeApps);
    }

    private void calculDeuxiemeFeuille(List<String> codeApps)
    {
        // R�cup�ration de la feuille venant du fichier fourni.
        Sheet sheetbase = null;
        int colCodeBis = 0;
        try (Workbook wb2 = WorkbookFactory.create(fileIn))
        {
            sheetbase = wb2.getSheet(REFAPPLIS);

        }
        catch (InvalidFormatException | IOException e)
        {
            throw new TechnicalException("Impossible de r�cup�rer la feuille excel - fichier : " + fileIn, e);
        }

        // R�cup�ration de la feuille
        Sheet sheetFinale = wb.getSheet(REFAPPLIS);
        
        // Test de la valeur de la colonne code pour retrouver les codes applications
        Row row = sheetbase.getRow(0);
        for (Cell cell : row)
        {
            if ("Code".equals(cell.getStringCellValue()))
                colCodeBis = cell.getColumnIndex();                
        }

        for (Iterator<Row> iter = sheetbase.iterator(); iter.hasNext();)
        {
            // Initialisation des deux lignes
            Row base = iter.next();
            row = sheetFinale.createRow(sheetFinale.getLastRowNum() + 1);

            // Copie de chaque ligne
            for (int i = 0; i < base.getLastCellNum(); i++)
            {
                Cell newCell = row.createCell(i);
                Cell oldCell = base.getCell(i);

                copierCellule(newCell, oldCell);
            }

            // Test si l'application est pr�sente dans les applications SonarQube. On prot�ge si le nom de l'application est une valeur num�rique
            Cell cell = base.getCell(colCodeBis);
            String value = "";
            if (cell.getCellTypeEnum() == CellType.STRING)
                value = cell.getStringCellValue();
            else if (cell.getCellTypeEnum() == CellType.NUMERIC)
                value = String.valueOf(cell.getNumericCellValue());

            if (codeApps.contains(value))
                row.getCell(0).setCellValue("X");
        }
    }

    /*---------- ACCESSEURS ----------*/
}
