package dao;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ComposantSonar;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoComposantSonar extends AbstractDao<ComposantSonar> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public List<ComposantSonar> readAll()
    {
        return em.createNamedQuery("ComposantSonar.findAll", ComposantSonar.class).getResultList();
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
    public Map<String, ComposantSonar> readAllMap()
    {
        Map<String, ComposantSonar> retour = new HashMap<>();

        for (ComposantSonar compo : readAll())
        {
            retour.put(compo.getKey(), compo);
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
        retour = em.createNamedQuery("ComposantSonar.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE composants AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
