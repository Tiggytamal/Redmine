package dao;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import control.excel.ControlClarity;
import control.excel.ExcelFactory;
import model.InfoClarity;
import model.enums.TypeColClarity;

/**
 * Classe de DAO pour la sauvegarde des infoClarity en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class DaoInfoClarity extends AbstractDao<InfoClarity> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<InfoClarity> readAll()
    {
        return em.createNamedQuery("InfoClarity.findAll", InfoClarity.class).getResultList();
    }

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlClarity control = ExcelFactory.getReader(TypeColClarity.class, file);
        Map<String, InfoClarity> mapExcel = control.recupDonneesDepuisExcel();

        Map<String, InfoClarity> mapBase = readAllMap();

        for (Map.Entry<String, InfoClarity> entry : mapExcel.entrySet())
        {
            mapBase.put(entry.getKey(), mapBase.computeIfAbsent(entry.getKey(), key -> entry.getValue()).update(entry.getValue()));
        }

        save(mapBase.values());
        return mapBase.values().size();
    }

    /**
     * Méthode de sauvegarde d'un élément.<br/>
     * Retourne vrai si l'objet a bien été persisté.
     * 
     * @param info
     */
    @Override
    public boolean save(InfoClarity info)
    {
        boolean ok = false;
        em.getTransaction().begin();
        if (!em.contains(info))
        {
            if (info.getChefService() != null && info.getChefService().getIdBase() == 0)
                em.persist(info.getChefService());
            em.persist(info);
            ok = true;
        }
        em.getTransaction().commit();
        return ok;
    }

    /**
     * Méthode de sauvegarde d'une collection d'éléments.<br/>
     * Retourne le nombre d'éléments enregistrés.
     * 
     * @param collection
     * @return
     */
    @Override
    public int save(Collection<InfoClarity> collection)
    {
        em.getTransaction().begin();
        int i = 0;
        for (InfoClarity info : collection)
        {
            if (!em.contains(info))
            {
                if (info.getChefService().getIdBase() == 0)
                    em.persist(info.getChefService());
                em.persist(info);
                i++;
            }
        }
        em.getTransaction().commit();
        return i;
    }

    /**
     * Retourne tous les éléments sous forme d'une map
     * 
     * @return
     */
    public Map<String, InfoClarity> readAllMap()
    {
        Map<String, InfoClarity> retour = new HashMap<>();

        for (InfoClarity info : readAll())
        {
            retour.put(info.getCodeClarity(), info);
        }
        return retour;
    }

    /**
     * Remonte un {@code InfoClarity} depuis la base de données avec le code du projet
     * 
     * @param codeClarity
     * @return
     */
    public InfoClarity getInfoClarityByCode(String codeClarity)
    {
        List<InfoClarity> liste = em.createNamedQuery("InfoClarity.findByCode", InfoClarity.class).setParameter("code", codeClarity).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
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
        retour = em.createNamedQuery("InfoClarity.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE applications AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
