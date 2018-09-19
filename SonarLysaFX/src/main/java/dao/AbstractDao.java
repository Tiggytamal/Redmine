package dao;


import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe abstraite de cr�ation de l'Entity Manager
 * Toutes les classes de DAO vont h�riter de cette classe
 * 
 * @author Tiggy Tamal
 *
 * @param <T>
 *          Correspond � la classe de mod�le que l'on va g�rer
 */
public abstract class AbstractDao<T>
{
    /* ---------- ATTIBUTES ---------- */
    
	private EntityManagerFactory emf;
	protected EntityManager em;

    /* ---------- CONSTUCTORS ---------- */
	
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

    /* ---------- METHODS ---------- */
	
	public abstract List<T> readAll();
	
    /* ---------- ACCESS ---------- */
	
	 /**
     * getter de l'EntityManager.
     *
     * @return l'EntityManager
     */
    public EntityManager getEm()
    {
        return em;
    }
}