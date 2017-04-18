package dao;


import java.util.HashMap;
import java.util.List;

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
public abstract class DaoRedmine<T>
{
    /* ---------- ATTIBUTES ---------- */
    
	private EntityManagerFactory emf;
	protected EntityManager em;

    /* ---------- CONSTUCTORS ---------- */
	
	public DaoRedmine()
	{
		emf = Persistence.createEntityManagerFactory("RedmineFX");
		em = emf.createEntityManager();
	}
	
	public DaoRedmine(HashMap<String, String> map)
	{
	    emf = Persistence.createEntityManagerFactory("RedmineFX", map);
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