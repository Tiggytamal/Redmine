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
 * classe de contr�le pour le fichier des chefs de services
 * 
 * @author ETP8137 - Gr�goire Mathon
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
     * Constructeur avec visibilit� par default pour obliger l'utilisation de la factory
     * 
     * @param file
     *            Fichier qui sera traiter par l'instance du contr�leur
     * @throws IOException
     *             Exception lors des acc�s lecture/�criture
     */
    ControlChefService(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public Map<String, ChefService> recupDonneesDepuisExcel()
    {
        // It�ration sur toutes les feuilles du fichier Excel
        Sheet sheet = wb.getSheetAt(0);
        
        // Liste de retour
        Map<String, ChefService> retour = new HashMap<>(sheet.getLastRowNum());

        // It�ration sur chaque ligne pour r�cup�rer les donn�es
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);

            // Cr�ation de l'objet
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
