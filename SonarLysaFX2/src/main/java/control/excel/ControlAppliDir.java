package control.excel;

import java.io.File;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import dao.Dao;
import dao.DaoFactory;
import model.bdd.Application;
import model.enums.ColAppliDir;

/**
 * Classe de contrôle pour le fichier de liens entre les code applications et les directions.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class ControlAppliDir extends AbstractControlExcelRead<ColAppliDir, Map<String, Application>>
{
    /*---------- ATTRIBUTS ----------*/

    private int colAppli;
    private int colDir;

    private Dao<Application, String> dao;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur avec visibilité par défaut pour obliger l'utilisation de la factory
     * 
     * @param file
     *             Fichier qui sera traité par l'instance du contrôleur
     */
    ControlAppliDir(File file)
    {
        super(file);
        dao = DaoFactory.getMySQLDao(Application.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public Map<String, Application> recupDonneesDepuisExcel()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheetAt(0);

        // Liste des applications depuis la base de données
        Map<String, Application> mapAppli = dao.readAllMap();

        // Itération sur la feuille hormis la ligne des titres
        for (int i = 1; i < sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);

            String appliString = getCellStringValue(row, colAppli);
            String dirString = getCellStringValue(row, colDir);

            // On saute les lignes mal renseignées
            if (appliString.isEmpty() || dirString.isEmpty())
                continue;

            Application appli = mapAppli.get(appliString);

            if (appli != null)
                appli.setDirection(dirString);
        }

        return mapAppli;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
