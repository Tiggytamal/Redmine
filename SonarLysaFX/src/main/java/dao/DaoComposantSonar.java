package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import model.bdd.ComposantSonar;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoComposantSonar extends AbstractDao<ComposantSonar> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/
    
    DaoComposantSonar() { }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        return 0;       
    }
    
    @Override
    public List<ComposantSonar> readAll()
    {
        return em.createNamedQuery("ComposantSonar.findAll", ComposantSonar.class).getResultList();
    }

    @Override
    public boolean persist(ComposantSonar compo)
    {
        if (compo.getIdBase() == 0)
        {
            if (compo.getAppli().getIdBase() == 0)
                em.persist(compo.getAppli());
            if (compo.getLotRTC().getIdBase() == 0)
                em.persist(compo.getLotRTC());
            em.persist(compo);
            return true;
        }
        else
            em.merge(compo);
        return false;
    }

    @Override
    public int resetTable()
    {
        int retour = 0;
        em.getTransaction().begin();
        retour = em.createNamedQuery("ComposantSonar.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE composants AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    @Override
    public ComposantSonar recupEltParCode(String key)
    {
        List<ComposantSonar> liste = em.createNamedQuery("ComposantSonar.findByCode", ComposantSonar.class).setParameter("code", key).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
