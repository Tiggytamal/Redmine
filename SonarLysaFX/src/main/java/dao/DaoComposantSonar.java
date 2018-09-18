package dao;

import java.io.Serializable;
import java.util.List;

import model.sql.ComposantSonar;

public class DaoComposantSonar extends DaoSonarLysa<ComposantSonar> implements Serializable
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
    
    public void save(ComposantSonar compo)
    {
        em.persist(compo);
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}