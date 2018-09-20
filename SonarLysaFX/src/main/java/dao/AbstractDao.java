package dao;


import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe abstraite de création de l'Entity Manager
 * Toutes les classes de DAO vont hériter de cette classe
 * 
 * @author Tiggy Tamal
 *
 * @param <T>
 *          Correspond à la classe de modèle que l'on va gérer
 */
public abstract class AbstractDao<T>
{
    /*---------- ATTRIBUTS ----------*/
    
	private EntityManagerFactory emf;
	protected EntityManager em;

    /*---------- CONSTRUCTEURS ----------*/
	
	public AbstractDao()
	{
		emf = Persistence.createEntityManagerFactory("SonarLysaFX");
		em = emf.createEntityManager();
	}
	
	public AbstractDao(Map<String, String> map)
	{
	    emf = Persistence.createEntityManagerFactory("SonarLysaFX", map);
        em = emf.createEntityManager();
	}

    /*---------- METHODES ABSTRAITES ----------*/
	
	public abstract List<T> readAll();
	
	public abstract int recupDonneesDepuisExcel(File file);
	
    /*---------- METHODES PUBLIQUES ----------*/
	
	/**
	 * Méthode de sauvegarde d'un élément.<br/>
	 * Retourne vrai si l'objet a bien été persisté.
	 * 
	 * @param t
	 */
	public final boolean save(T t)
	{
	    boolean ok = false;
        em.getTransaction().begin();
        if (!em.contains(t))
        {
            em.persist(t);
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
	public final int save(Collection<T> collection)
	{
        em.getTransaction().begin();
        int i = 0;
        for (T t : collection)
        {
            if (!em.contains(t))
            {
                em.persist(t);
                i++;
            }
        }
        em.getTransaction().commit();
        return i;
	}
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
	
//	 /**
//     * getter de l'EntityManager.
//     *
//     * @return l'EntityManager
//     */
//    public EntityManager getEm()
//    {
//        return em;
//    }
}