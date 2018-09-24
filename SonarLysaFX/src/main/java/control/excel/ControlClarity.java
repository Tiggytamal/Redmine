package control.excel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import dao.DaoChefService;
import model.ChefService;
import model.InfoClarity;
import model.ModelFactory;
import model.enums.TypeColClarity;

/**
 * Classe de contrôle du fichier Clarity
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ControlClarity extends AbstractControlExcelRead<TypeColClarity, Map<String, InfoClarity>>
{
    /*---------- ATTRIBUTS ----------*/

    // Liste des indices des colonnes
    // Attention les noms des variables doivent être identiques aux données dans l'énumération TypeColClarity
    private int colActif;
    private int colClarity;
    private int colLib;
    private int colCpi;
    private int colEdition;
    private int colDir;
    private int colDepart;
    private int colService;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur avec visibilité par default pour obliger l'utilisation de la factory
     * 
     * @param file
     *            Fichier qui sera traiter par l'instance du contrôleur
     * @throws IOException
     *             Exception lors des accès lecture/écriture
     */
    ControlClarity(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public Map<String, InfoClarity> recupDonneesDepuisExcel()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheetAt(0);

        //Map de retour
        Map<String, InfoClarity> retour = new HashMap<>();
        
        // Liste des chef de service depuis la base de données - clef = service
        Map<String, ChefService> mapChefService = new DaoChefService().readAllMap();

        // Itération sur la feuille hormis la ligne des titres, et récupération des lignes qui ont un code Clarity
        for (int i = 1; i < sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
            
            // On contrôle que la cellule avec le code Clarity existe bien et est bien valorisée.
            if (!getCellStringValue(row, colClarity).isEmpty())
            {
                InfoClarity info = creerInfoClarityDepuisExcel(row);
                
                // Récupération du chef de service depuis la map et création d'un chef inconnu si on n'en a pas trouvé
                ChefService chefService = mapChefService.computeIfAbsent(info.getService(), key -> ModelFactory.getModelWithParams(ChefService.class, info.getService()));
                info.setChefService(chefService);
                retour.put(info.getCodeClarity(), info);
            }
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Création d'un objet InfoClarity depuis une ligne du fichier Excel
     * 
     * @param row
     *            Ligne du fichier Excel utilisée pour créer l'Objet JAVA
     * @return {@link model.InfoClarity} - Modèle de données du fichier excel
     */
    private InfoClarity creerInfoClarityDepuisExcel(Row row)
    {
        InfoClarity retour = ModelFactory.getModel(InfoClarity.class);
        retour.setChefProjet(getCellStringValue(row, colCpi));
        retour.setCodeClarity(getCellStringValue(row, colClarity));
        retour.setDepartement(getCellStringValue(row, colDepart));
        retour.setDirection(getCellStringValue(row, colDir));
        retour.setEdition(getCellStringValue(row, colEdition));
        retour.setLibelleProjet(getCellStringValue(row, colLib));
        retour.setService(getCellStringValue(row, colService));
        String actif = getCellStringValue(row, colActif);
        if ("Oui".equals(actif))
            retour.setActif(true);
        else
            retour.setActif(false);

        return retour;
    }

    /*---------- ACCESSEURS ----------*/

}
