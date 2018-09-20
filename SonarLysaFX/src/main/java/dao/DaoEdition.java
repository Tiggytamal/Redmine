package dao;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import control.excel.ControlEdition;
import control.excel.ExcelFactory;
import model.Edition;
import model.enums.TypeColEdition;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoEdition extends AbstractDao<Edition> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<Edition> readAll()
    {
        return em.createNamedQuery("Edition.findAll", Edition.class).getResultList();
    }

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlEdition control = ExcelFactory.getReader(TypeColEdition.class, file);
        Map<String, Edition> mapExcel = control.recupDonneesDepuisExcel();

        Map<String, Edition> mapBase = readAllMap();

        for (Map.Entry<String, Edition> entry : mapExcel.entrySet())
        {            
            mapBase.put(entry.getKey(), mapBase.computeIfAbsent(entry.getKey(), key -> entry.getValue()).update(entry.getValue()));
        }

        em.getTransaction().begin();
        for (Edition appli : mapBase.values())
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
    public Map<String, Edition> readAllMap()
    {
        Map<String, Edition> retour = new HashMap<>();

        for (Edition appli : readAll())
        {
            retour.put(appli.getNumero(), appli);
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
        retour = em.createNamedQuery("Edition.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE applications AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
