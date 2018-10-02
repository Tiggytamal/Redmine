package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import model.bdd.LotRTC;
import model.enums.TypeDonnee;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoLotRTC extends AbstractDao<LotRTC> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/
    
    DaoLotRTC() 
    {
        typeDonnee = TypeDonnee.LOTSRTC;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        return 0;
    }

    @Override
    public boolean persist(LotRTC lot)
    {
        if (lot.getIdBase() == 0)
        {
            if (lot.getProjetClarity().getIdBase() == 0)
                em.persist(lot.getProjetClarity());
            em.persist(lot);
            return true;
        }
        else
            em.merge(lot);
        return false;
    }
    
    @Override
    public LotRTC recupEltParCode(String lot)
    {
        List<LotRTC> liste = em.createNamedQuery("LotRTC.findByIndex", LotRTC.class).setParameter("index", lot).getResultList();
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
        retour = em.createNamedQuery("LotRTC.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE applications AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
