package control.excel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import dao.DaoFactory;
import model.ModelFactory;
import model.bdd.ChefService;
import model.bdd.ProjetClarity;
import model.enums.TypeColClarity;

/**
 * Classe de contrôle du fichier Clarity
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class ControlClarity extends AbstractControlExcelRead<TypeColClarity, Map<String, ProjetClarity>>
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

    public Map<String, ProjetClarity> recupDonneesDepuisExcel()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheetAt(0);

        // Map de retour
        Map<String, ProjetClarity> retour = new HashMap<>();

        // Liste des chef de service depuis la base de données - clef = service
        Map<String, ChefService> mapChefService = DaoFactory.getDao(ChefService.class).readAllMap();

        // Itération sur la feuille hormis la ligne des titres, et récupération des lignes qui ont un code Clarity
        for (int i = 1; i < sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);

            // On contrôle que la cellule avec le code Clarity existe bien et est bien valorisée, sinon on saute l'élément
            if (getCellStringValue(row, colClarity).isEmpty())
                continue;
            
            // Initialisation projet depuis les données Excel
            ProjetClarity projet = creerInfoClarityDepuisExcel(row);
            String service = projet.getService();

            // Récupération du chef de service depuis la map issu de la base de données et création d'un chef inconnu si on n'en a pas trouvé
            if (mapChefService.containsKey(service))
                projet.setChefService(mapChefService.get(service));
            else
            {
                ChefService chefService = ChefService.getChefServiceInconnu(service);
                projet.setChefService(chefService);
                mapChefService.put(service, chefService);
            }

            retour.put(projet.getCode(), projet);
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Création d'un objet InfoClarity depuis une ligne du fichier Excel
     * 
     * @param row
     *            Ligne du fichier Excel utilisée pour créer l'Objet JAVA
     * @return {@link model.bdd.ProjetClarity} - Modèle de données du fichier excel
     */
    private ProjetClarity creerInfoClarityDepuisExcel(Row row)
    {
        ProjetClarity retour = ModelFactory.getModel(ProjetClarity.class);
        retour.setChefProjet(getCellStringValue(row, colCpi));
        retour.setCode(getCellStringValue(row, colClarity));
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
