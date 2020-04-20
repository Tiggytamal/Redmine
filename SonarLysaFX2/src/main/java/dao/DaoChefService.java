package dao;

import java.io.File;
import java.util.Map;

import control.excel.ControlChefService;
import control.excel.ExcelFactory;
import model.bdd.ChefService;
import model.enums.ColChefServ;

/**
 * CDOA pour la classe {@link model.bdd.ChefService}
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoChefService extends AbstractMySQLDao<ChefService, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "chefs_de_service";

    /*---------- CONSTRUCTEURS ----------*/

    DaoChefService()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlChefService control = ExcelFactory.getReader(ColChefServ.class, file);
        Map<String, ChefService> mapExcel = control.recupDonneesDepuisExcel();

        // Récupération des données en base
        Map<String, ChefService> mapBase = readAllMap();

        // Mise à jour des données avec celles du fichier Excel
        for (Map.Entry<String, ChefService> entry : mapExcel.entrySet())
        {
            mapBase.put(entry.getKey(), mapBase.computeIfAbsent(entry.getKey(), key -> entry.getValue()).update(entry.getValue()));
        }

        // Persistance en base de données
        return persist(mapBase.values());
    }

    @Override
    protected void persistImpl(ChefService t)
    {
        // Pas d'implementation necessaire
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
