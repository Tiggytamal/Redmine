package dao;

import java.util.List;

import javax.ejb.Stateless;

import model.Incident;

@Stateless
public class DaoIncident extends DaoModel<Incident>
{

    @Override
    public List<Incident> readAll()
    {
        return em.createNamedQuery("incident.findAll", Incident.class).getResultList();
    }
    
    /**
     * Permet de r�cup�rer une formation avec son intitul�
     * 
     * @param sujet
     *            l'intitul� de la formation recherch�e
     * @return la formation correspondante � cette identifiant ou null si aucune a �t� trouv�e
     */
    public List<Incident> findOne(String sujet)
    {
        return em.createNamedQuery("incident.findOne", Incident.class).setParameter("sujet", sujet).getResultList();
    }

}
