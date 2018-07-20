package control.excel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;

import model.Application;
import model.enums.TypeColApps;

/**
 * Classe de contrôle du fichier des applications
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ControlApps extends ControlExcelRead<TypeColApps, Map<String, Application>>
{
    /*---------- ATTRIBUTS ----------*/

    private int colCode;
    private int colActif;
    private int colLib;
    private int colOpen;
    private int colMainFrame;
    
    private static final String ACTIF = "Actif";
    private static final String OUI = "Oui";

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur avec visibilité par default pour obliger l'utilisation de la factory
     * 
     * @param file
     *            Fichier qui sera traiter par l'instance du contrôleur
     * @throws IOException
     *             Exception lors des accès lecture/écriture
     */
    ControlApps(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public Map<String, Application> recupDonneesDepuisExcel()
    {
        Sheet sheet = wb.getSheetAt(0);
        Map<String, Application> retour = new HashMap<>();

        // Iterateur depuis la ligne 1 - sans les titres
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);
            Application app = new Application();

            // Application active ou non
            if (ACTIF.equals(getCellStringValue(row, colActif)))
                app.setActif(true);

            // Code application
            Cell cell = row.getCell(colCode, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell.getCellTypeEnum() == CellType.STRING)
                app.setCode(getCellStringValue(row, colCode));
            else if (cell.getCellTypeEnum() == CellType.NUMERIC)
                app.setCode(String.format("%04d", getCellNumericValue(row, colCode)));
            
            // Libelle application
            app.setLibelle(getCellStringValue(row, colLib));
            
            // Indicateur application open
            if (OUI.equals(getCellStringValue(row, colOpen)))
                app.setOpen(true);
            
            // Indicateur application mainframe
            if (OUI.equals(getCellStringValue(row, colMainFrame)))
                app.setMainFrame(true);
            
            retour.put(app.getCode(), app);
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}