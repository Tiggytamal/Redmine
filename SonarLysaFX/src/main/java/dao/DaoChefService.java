package dao;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import control.excel.ControlChefService;
import control.excel.ExcelFactory;
import model.ChefService;
import model.enums.TypeColChefServ;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoChefService extends AbstractDao<ChefService> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<ChefService> readAll()
    {
        return em.createNamedQuery("ChefService.findAll", ChefService.class).getResultList();
    }

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlChefService control = ExcelFactory.getReader(TypeColChefServ.class, file);
        Map<String, ChefService> mapExcel = control.recupDonneesDepuisExcel();

        Map<String, ChefService> mapBase = readAllMap();

        for (Map.Entry<String, ChefService> entry : mapExcel.entrySet())
        {            
            mapBase.put(entry.getKey(), mapBase.computeIfAbsent(entry.getKey(), key -> entry.getValue()).update(entry.getValue()));
        }

        em.getTransaction().begin();
        for (ChefService appli : mapBase.values())
        {
            if (em.contains(appli))
                em.merge(appli);
            else
                em.persist(appli);
        }
        em.getTransaction().commit();
        return mapBase.values().size();
    }

    /**
     * Retourne tous les éléments sous forme d'une map
     * 
     * @return
     */
    public Map<String, ChefService> readAllMap()
    {
        Map<String, ChefService> retour = new HashMap<>();

        for (ChefService appli : readAll())
        {
            retour.put(appli.getNom(), appli);
        }
        return retour;
    }

    /**
     * Supprime tous les enregistrements de la base de la table des composants Sonar. Retourne le nombre d'enregistrements effacés. Reset l'incrémentation.
     * 
     * @return
     */
    public int resetTable()
    {
        int retour = 0;
        em.getTransaction().begin();
        retour = em.createNamedQuery("ChefService.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE applications AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
