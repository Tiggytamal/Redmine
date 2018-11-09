package dao;

import java.io.File;
import java.io.Serializable;

import javax.persistence.EntityManager;

import model.bdd.Edition;
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
    public void persistImpl(LotRTC lot)
    {
            // Persistance projet Clarity
            persistSousObjet(ProjetClarity.class, lot.getProjetClarity());
            
            // Persistance édition
            persistSousObjet(Edition.class, lot.getEdition());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
