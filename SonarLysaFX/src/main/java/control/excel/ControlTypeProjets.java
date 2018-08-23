package control.excel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.enums.TypeColNPC;

/**
 * Classe de contrôle du fichier des applications
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ControlTypeProjets extends AbstractControlExcelRead<TypeColNPC, Map<String, String>>
{
    /*---------- ATTRIBUTS ----------*/
    
    private int colNom;
    
    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur avec visibilité par default pour obliger l'utilisation de la factory
     * 
     * @param file
     *            Fichier qui sera traiter par l'instance du contrôleur
     * @throws IOException
     *             Exception lors des accès lecture/écriture
     */
    ControlTypeProjets(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public Map<String, String> recupDonneesDepuisExcel()
    {
        Sheet sheet = wb.getSheetAt(0);
        Map<String, String> retour = new HashMap<>();

        // Iterateur depuis la ligne 1 - sans les titres
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);            
            retour.put(getCellStringValue(row, colNom), getCellStringValue(row, colNom));
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
