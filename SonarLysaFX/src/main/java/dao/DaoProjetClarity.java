package dao;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import javax.persistence.EntityManager;

import control.excel.ControlClarity;
import control.excel.ExcelFactory;
import model.bdd.ChefService;
import model.bdd.ProjetClarity;
import model.enums.TypeColClarity;
import model.enums.TypeDonnee;

/**
 * Classe de DAO pour la sauvegarde des infoClarity en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class DaoProjetClarity extends AbstractDao<ProjetClarity> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "projets_clarity";

    /*---------- CONSTRUCTEURS ----------*/

    DaoProjetClarity()
    {
        super(TABLE);
        typeDonnee = TypeDonnee.CLARITY;
    }

    DaoProjetClarity(EntityManager em)
    {
        super(TABLE, em);
        typeDonnee = TypeDonnee.CLARITY;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlClarity control = ExcelFactory.getReader(TypeColClarity.class, file);
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
        int retour = persist(mapBase.values());
        majDateDonnee();
        return retour;
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
