package utilities.interfaces;

import java.util.List;

import javax.persistence.EntityManager;

/**
 * Classe abtraite g�n�rique de tous les Dao.
 * Contient une m�thode pour lire tous les objets de la table correspondant au model en param�tre
 * 
 * @author Gr�goire Mathon
 * @since 1.0
 *
 * @param <T>
 * 	la classe de mod�le correspondante
 */
public abstract class DaoGeneric<T>
{
    protected EntityManager em;
	/**
	 * Permet d'extraire tous les objets correspondant � la classe de modele en param�tre
	 * 
	 * @return
	 * une liste des objets
	 */
	public abstract List<T> readAll();
	
    
    public void setEm(EntityManager em)
    {
        this.em = em;
    }
}
