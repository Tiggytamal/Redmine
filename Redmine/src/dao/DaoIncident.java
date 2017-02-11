package dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.internal.jpa.EJBQueryImpl;

import model.Incident;

@Stateless
public class DaoIncident extends DaoModel<Incident>
{

    @Override
    public List<Incident> readAll()
    {
        return em.createNamedQuery("Incident.findAll", Incident.class).getResultList();
    }
    
    /**
     * Permet de r�cup�rer une formation avec son intitul�
     * 
     * @param projet
     *            l'intitul� de la formation recherch�e
     * @return la formation correspondante � cette identifiant ou null si aucune a �t� trouv�e
     */
    public List<Incident> findByProject(String projet)
    {
        TypedQuery<Incident> query = em.createNamedQuery("Incident.findByProject", Incident.class).setParameter("projet", projet);
        System.out.println(query.unwrap(EJBQueryImpl.class).getDatabaseQuery().getSQLString());
        return query.getResultList();
    }

}
