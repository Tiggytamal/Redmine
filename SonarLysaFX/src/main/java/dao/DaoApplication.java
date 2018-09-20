package dao;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import control.excel.ControlApps;
import control.excel.ExcelFactory;
import model.Application;
import model.enums.TypeColApps;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoApplication extends AbstractDao<Application> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<Application> readAll()
    {
        return em.createNamedQuery("Application.findAll", Application.class).getResultList();
    }

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlApps control = ExcelFactory.getReader(TypeColApps.class, file);
        Map<String, Application> mapExcel = control.recupDonneesDepuisExcel();

        Map<String, Application> mapBase = readAllMap();

        for (Map.Entry<String, Application> entry : mapExcel.entrySet())
        {            
            mapBase.put(entry.getKey(), mapBase.computeIfAbsent(entry.getKey(), key -> entry.getValue()).update(entry.getValue()));
        }

        em.getTransaction().begin();
        for (Application appli : mapBase.values())
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
    public Map<String, Application> readAllMap()
    {
        Map<String, Application> retour = new HashMap<>();

        for (Application appli : readAll())
        {
            retour.put(appli.getCode(), appli);
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
        retour = em.createNamedQuery("Application.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE applications AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
