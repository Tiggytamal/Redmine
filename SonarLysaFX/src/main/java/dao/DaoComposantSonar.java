package dao;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ComposantSonar;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de donn�es
 * 
 * @author ETP8137 - Gr�goire Mathon
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
     * M�thode de sauvegarde d'un �l�ment.<br/>
     * Retourne vrai si l'objet a bien �t� persist�.
     * 
     * @param compo
     */
    @Override
    public boolean save(ComposantSonar compo)
    {
        if (!em.contains(compo))
        {
            if (compo.getAppli().getIdBase() == 0)
                em.persist(compo.getAppli());
            em.persist(compo);
            return true;
        }
        return false;
    }
    
    /**
     * M�thode de sauvegarde d'une collection d'�l�ments.<br/>
     * Retourne le nombre d'�l�ments enregistr�s.
     * 
     * @param collection
     * @return
     */
    @Override
    public int save(Collection<ComposantSonar> collection)
    {
        em.getTransaction().begin();
        int i = 0;
        for (ComposantSonar t : collection)
        {
            if (!em.contains(t))
            {
                if (t.getAppli().getIdBase() == 0)
                    em.persist(t.getAppli());
                em.persist(t);
                i++;
            }
        }
        em.getTransaction().commit();
        return i;
    }
    
    /**
     * Retourne tous les �l�ments sous forme d'une map
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
     * Supprime tous les enregistrements de la base de la table des composants Sonar. Retourne le nombre d'enregistrements effac�s. Reset l'incr�mentation.
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
