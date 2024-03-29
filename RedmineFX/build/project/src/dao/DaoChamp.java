package dao;

import java.io.Serializable;
import java.util.List;

import model.Champ;

public class DaoChamp extends DaoRedmine<Champ> implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public List<Champ> readAll()
    {
        return em.createNamedQuery("Champ.findAll", Champ.class).getResultList();
    }
    
    public Integer findId(String nom)
    {
        List<Integer> list = em.createNamedQuery("Champ.findId", Integer.class).setParameter("nom", nom).getResultList();
        if(list.isEmpty())
            return null;
        return list.get(0);
    }
}