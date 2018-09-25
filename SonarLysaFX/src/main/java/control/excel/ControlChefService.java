package control.excel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.ModelFactory;
import model.bdd.ChefService;
import model.enums.TypeColChefServ;

/**
 * classe de contrôle pour le fichier des chefs de services
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ControlChefService extends AbstractControlExcelRead<TypeColChefServ, Map<String, ChefService>>
{
    /*---------- ATTRIBUTS ----------*/

    // Indices des colonnes
    private int colFil;
    private int colDir;
    private int colDepart;
    private int colService;
    private int colManager;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    /**
     * Constructeur avec visibilité par default pour obliger l'utilisation de la factory
     * 
     * @param file
     *            Fichier qui sera traiter par l'instance du contrôleur
     * @throws IOException
     *             Exception lors des accès lecture/écriture
     */
    ControlChefService(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public Map<String, ChefService> recupDonneesDepuisExcel()
    {
        // Itération sur toutes les feuilles du fichier Excel
        Sheet sheet = wb.getSheetAt(0);
        
        // Liste de retour
        Map<String, ChefService> retour = new HashMap<>(sheet.getLastRowNum());

        // Itération sur chaque ligne pour récupérer les données
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);

            // Création de l'objet
            ChefService respServ = ModelFactory.getModel(ChefService.class);
            respServ.setDepartement(getCellStringValue(row, colDepart));
            respServ.setDirection(getCellStringValue(row, colDir));
            respServ.setService(getCellStringValue(row, colService));
            respServ.setFiliere(getCellStringValue(row, colFil));
            respServ.setNom(getCellStringValue(row, colManager));
            retour.put(respServ.getService(), respServ);
        }

        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
