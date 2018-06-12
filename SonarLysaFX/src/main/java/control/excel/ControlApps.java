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

import model.enums.TypeColApps;

/**
 * Classe de contrôle du fichier des applications
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ControlApps extends ControlExcel<TypeColApps, Map<String, Boolean>>
{
    /*---------- ATTRIBUTS ----------*/

    private int colApps;
    private int colActif;
    private static final String ACTIF = "Actif";

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur avec visibilité par default pour obliger l'utilisation de la factory
     * 
     * @param file
     *            Fichier qui sera traiter par l'instance du contrôleur
     * @throws IOException
     *             Exception lors des accès lecture/écriture
     */
    ControlApps(File file) throws IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public Map<String, Boolean> recupDonneesDepuisExcel()
    {
        Sheet sheet = wb.getSheetAt(0);
        Map<String, Boolean> retour = new HashMap<>();

        // Iterateur depuis la ligne 1 - sans les titres
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);
            Boolean bool;

            // taritement application active ou non
            String actif = getCellStringValue(row, colActif);
            if (ACTIF.equals(actif))
                bool = Boolean.TRUE;
            else
                bool = Boolean.FALSE;

            // Traitement nom d'application
            Cell cell = row.getCell(colApps, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell.getCellTypeEnum() == CellType.STRING)
                retour.put(getCellStringValue(row, colApps), bool);
            else if (cell.getCellTypeEnum() == CellType.NUMERIC)
                retour.put(String.format("%04d", getCellNumericValue(row, colApps)), bool);
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Override
    protected void initEnum()
    {
        enumeration = TypeColApps.class;
    }
}