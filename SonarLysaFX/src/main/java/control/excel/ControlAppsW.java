package control.excel;

import static utilities.Statics.EMPTY;

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
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import model.Application;
import model.enums.Param;
import model.enums.TypeColAppsW;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.enums.Bordure;

/**
 * Classe de controle des applications en écriture
 * 
 * @author ETP8137 - Grégoier Mathon
 * @since 1.0
 *
 */
public class ControlAppsW extends AbstractControlExcelWrite<TypeColAppsW, Collection<Application>>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String APPLIGEREES = "Périmètre Couverts SonarQbe";
    private static final String REFAPPLIS = "Ref CodeApps detaillés";

    private int colCode;
    private int colActif;
    private int colLib;
    private int colOpen;
    private int colMainFrame;
    private int colCrit;
    private int colVuln;
    private int colLDCSonar;
    private int colLDCMain;

    /*---------- CONSTRUCTEURS ----------*/

    ControlAppsW(File sortie)
    {
        super(sortie);

        // Création des deux feuilles du fichier Excel
        wb.createSheet(APPLIGEREES);
        wb.createSheet(REFAPPLIS);
        calculIndiceColonnes();
        initTitres();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void creerfeuilleSonar(Set<Application> applisOpenSonar)
    {
        Sheet sheet = wb.getSheet(APPLIGEREES);
        enregistrerDonnees(applisOpenSonar, sheet);
    }

    /*---------- METHODES PRIVEES ----------*/
    
    @Override
    protected final void initTitres()
    {
        CellStyle centre = helper.getStyle(IndexedColors.AQUA, Bordure.BAS, HorizontalAlignment.CENTER);
        centre.setWrapText(false);

        // Création de la ligne de titre
        Sheet sheet = wb.getSheet(APPLIGEREES);

        Row row = sheet.createRow(0);
        
        for (TypeColAppsW typeColAppsW : TypeColAppsW.values())
        {
            try
            {
                valoriserCellule(row, (Integer) getClass().getDeclaredField(typeColAppsW.getNomCol()).get(this), 
                        centre, Statics.proprietesXML.getEnumMapColW(TypeColAppsW.class).get(typeColAppsW).getNom(), null);
            }
            catch (IllegalAccessException | NoSuchFieldException | SecurityException e)
            {
                throw new TechnicalException("", e);
            }
        }
        
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
        // Récupération de la feuille venant du fichier fourni.
        Sheet sheetbase = null;
        File file = new File(Statics.proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + Statics.proprietesXML.getMapParams().get(Param.NOMFICHIERAPPLI));
        try (Workbook wb2 = WorkbookFactory.create(file))
        {
            sheetbase = wb2.getSheet(REFAPPLIS);

        }
        catch (InvalidFormatException | IOException e)
        {
            throw new TechnicalException("Impossible de récupérer la feuille excel - fichier : " + file, e);
        }

        Sheet sheetFinale = wb.getSheet(REFAPPLIS);

        // Récupération index de la colonne des code appli du fichier de base
        Row row = sheetbase.getRow(0);
        int colCodeBase = 0;

        // Parcours de la première ligne, calcul de l'index et arrêt de la boucle.
        for (int i = 0; i < row.getLastCellNum(); i++)
        {
            Cell cell = row.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if ("Code".equals(cell.getStringCellValue()))
            {
                colCodeBase = cell.getColumnIndex();
                break;
            }
        }

        // Copie des cellules du fichier initial vers le fichier final
        for (Iterator<Row> iter = sheetbase.iterator(); iter.hasNext();)
        {
            // Initialisation des deux lignes
            Row base = iter.next();
            row = sheetFinale.createRow(sheetFinale.getLastRowNum() + 1);

            // On itère sur la ligne de titres
            for (int i = 0; i < base.getLastCellNum(); i++)
            {
                Cell newCell = row.createCell(i);
                Cell oldCell = base.getCell(i);

                copierCellule(newCell, oldCell);
            }

            // Test si l'application est présente dans les applications SonarQube. On protège si le nom de l'application est une valeur numérique
            Cell cell = base.getCell(colCodeBase, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String value = EMPTY;
            if (cell.getCellTypeEnum() == CellType.STRING)
                value = cell.getStringCellValue();
            else if (cell.getCellTypeEnum() == CellType.NUMERIC)
                value = String.valueOf(cell.getNumericCellValue());

            if (codeApps.contains(value))
                row.getCell(0).setCellValue(Statics.X);
        }
    }

    /*---------- ACCESSEURS ----------*/
}
