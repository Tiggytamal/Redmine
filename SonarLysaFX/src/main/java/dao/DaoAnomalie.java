package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import model.bdd.AbstractBDDModele;
import model.bdd.Anomalie;
import model.enums.TypeDonnee;

public class DaoAnomalie extends AbstractDao<Anomalie> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final long serialVersionUID = 1L;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    DaoAnomalie() 
    { 
        typeDonnee = TypeDonnee.ANOMALIE;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public Anomalie recupEltParCode(String lot)
    {
        List<Anomalie> liste = em.createNamedQuery("Anomalie" + AbstractBDDModele.FINDINDEX, Anomalie.class).setParameter("code", lot).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
    }
    
    @Override
    public int resetTable()
    {
        int retour = 0;
        em.getTransaction().begin();
        retour = em.createNamedQuery("Anomalie.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE anomalies AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
