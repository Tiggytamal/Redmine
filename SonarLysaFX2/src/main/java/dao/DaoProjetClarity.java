package dao;

import java.io.File;
import java.util.Map;

import control.excel.ControlClarity;
import control.excel.ExcelFactory;
import model.bdd.ChefService;
import model.bdd.ProjetClarity;
import model.enums.ColClarity;

/**
 * DAO pour la classe {@link model.bdd.ProjetClarity}.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class DaoProjetClarity extends AbstractMySQLDao<ProjetClarity, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "projets_clarity";

    /*---------- CONSTRUCTEURS ----------*/

    DaoProjetClarity()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlClarity control = ExcelFactory.getReader(ColClarity.class, file);
        Map<String, ProjetClarity> mapExcel = control.recupDonneesDepuisExcel();

        // Récupération des données depuis la base
        Map<String, ProjetClarity> mapBase = readAllMap();

        // Mise à jour des données
        for (Map.Entry<String, ProjetClarity> entry : mapExcel.entrySet())
        {
            if (mapBase.containsKey(entry.getKey()))
                mapBase.get(entry.getKey()).update(entry.getValue());
            else
                mapBase.put(entry.getKey(), entry.getValue());
        }

        // Persistance des données
        return persist(mapBase.values());
    }

    @Override
    public void persistImpl(ProjetClarity projet)
    {
        // Persistance chef de service
        persistSousObjet(ChefService.class, projet.getChefService());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
