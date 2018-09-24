package dao;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.LotSuiviRTC;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoLotSuiviRTC extends AbstractDao<LotSuiviRTC> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<LotSuiviRTC> readAll()
    {
        return em.createNamedQuery("LotSuiviRTC.findAll", LotSuiviRTC.class).getResultList();
    }

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        return 0;
    }

    /**
     * Retourne tous les éléments sous forme d'une map
     * 
     * @return
     */
    public Map<String, LotSuiviRTC> readAllMap()
    {
        Map<String, LotSuiviRTC> retour = new HashMap<>();

        for (LotSuiviRTC lotRTC : readAll())
        {
            retour.put(lotRTC.getLot(), lotRTC);
        }
        return retour;
    }
    
    /**
     * Mise à jour d'une itération de {@code LotSuiviRTC}
     * 
     * @param collection
     */
    public boolean update(Iterable<LotSuiviRTC> collection)
    {
        DaoInfoClarity daoClarity = new DaoInfoClarity();
        int total = 0;
        em.getTransaction().begin();
        for (LotSuiviRTC lotRTC : collection)
        {
            if (lotRTC.getIdBase() != 0)
                em.merge(lotRTC);
            else
            {
                if (daoClarity.getInfoClarityByCode(lotRTC.getProjetClarity().getCodeClarity()) == null)
                    daoClarity.save(lotRTC.getProjetClarity());
                em.persist(lotRTC);    
                em.flush();
            }
            total++;
        }
        em.getTransaction().commit();
        return total > 0;
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
        retour = em.createNamedQuery("LotSuiviRTC.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE applications AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
