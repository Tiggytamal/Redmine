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
 * Classe de contr�le du fichier Clarity
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public class ControlClarity extends AbstractControlExcelRead<TypeColClarity, Map<String, InfoClarity>>
{
    /*---------- ATTRIBUTS ----------*/

    // Liste des indices des colonnes
    // Attention les noms des variables doivent �tre identiques aux donn�es dans l'�num�ration TypeColClarity
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
     * Constructeur avec visibilit� par default pour obliger l'utilisation de la factory
     * 
     * @param file
     *            Fichier qui sera traiter par l'instance du contr�leur
     * @throws IOException
     *             Exception lors des acc�s lecture/�criture
     */
    ControlClarity(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public Map<String, InfoClarity> recupDonneesDepuisExcel()
    {
        // R�cup�ration de la premi�re feuille
        Sheet sheet = wb.getSheetAt(0);

        //Map de retour
        Map<String, InfoClarity> retour = new HashMap<>();
        
        // Liste des chef de service depuis la base de donn�es - clef = service
        Map<String, ChefService> mapChefService = new DaoChefService().readAllMap();

        // It�ration sur la feuille hormis la ligne des titres, et r�cup�ration des lignes qui ont un code Clarity
        for (int i = 1; i < sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
            
            // On contr�le que la cellule avec le code Clarity existe bien et est bien valoris�e.
            if (!getCellStringValue(row, colClarity).isEmpty())
            {
                InfoClarity info = creerInfoClarityDepuisExcel(row);
                
                // R�cup�ration du chef de service depuis la map et cr�ation d'un chef inconnu si on n'en a pas trouv�
                ChefService chefService = mapChefService.computeIfAbsent(info.getService(), key -> ModelFactory.getModelWithParams(ChefService.class, info.getService()));
                info.setChefService(chefService);
                retour.put(info.getCodeClarity(), info);
            }
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Cr�ation d'un objet InfoClarity depuis une ligne du fichier Excel
     * 
     * @param row
     *            Ligne du fichier Excel utilis�e pour cr�er l'Objet JAVA
     * @return {@link model.InfoClarity} - Mod�le de donn�es du fichier excel
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
