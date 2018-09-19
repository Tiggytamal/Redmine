package dao;


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