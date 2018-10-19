package dao;

import java.io.File;
import java.io.Serializable;

import javax.persistence.EntityManager;

import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
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
    private static final String TABLE = "projets_groupe";

    /*---------- CONSTRUCTEURS ----------*/

    DaoLotRTC()
    {
        super(TABLE);
        typeDonnee = TypeDonnee.LOTSRTC;
    }

    DaoLotRTC(EntityManager em)
    {
        super(TABLE, em);
        typeDonnee = TypeDonnee.LOTSRTC;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        return 0;
    }

    @Override
    public boolean persistImpl(LotRTC lot)
    {
        if (lot.getIdBase() == 0)
        {
            lot.initTimeStamp();

            if (lot.getProjetClarity() != null)
            {
                // Persistance lots RTC liés
                if (lot.getProjetClarity().getIdBase() == 0)
                    DaoFactory.getDao(ProjetClarity.class, em).persist(lot.getProjetClarity());
                else
                    em.merge(lot.getProjetClarity());
            }

            em.persist(lot);
            return true;
        }
        else
            em.merge(lot);
        return false;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
