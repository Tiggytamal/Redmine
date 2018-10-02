package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import model.bdd.Application;
import model.bdd.ComposantSonar;
import model.bdd.LotRTC;
import model.enums.TypeDonnee;

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
    
    DaoComposantSonar() 
    { 
        typeDonnee = TypeDonnee.COMPOSANT;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        return 0;       
    }

    @Override
    public boolean persist(ComposantSonar compo)
    {
        if (compo.getIdBase() == 0)
        {
            Application appli = compo.getAppli();
            if (appli != null && appli.getIdBase() == 0)
                em.persist(appli);
            
            LotRTC lotRTC = compo.getLotRTC();
            if (lotRTC != null && lotRTC.getIdBase() == 0)
                em.persist(lotRTC);
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
        List<ComposantSonar> liste = em.createNamedQuery("ComposantSonar.findByIndex", ComposantSonar.class).setParameter("index", key).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
