package dao;

import model.bdd.Edition;
import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
import model.bdd.Utilisateur;

/**
 * DAO pour la classe {@link model.bdd.LotRTC}.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class DaoLotRTC extends AbstractMySQLDao<LotRTC, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "projets_groupe";

    /*---------- CONSTRUCTEURS ----------*/

    DaoLotRTC()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void persistImpl(LotRTC lot)
    {
        // Persistance projet Clarity
        persistSousObjet(ProjetClarity.class, lot.getProjetClarity());

        // Persistance edition
        persistSousObjet(Edition.class, lot.getEdition());

        // Persistance cpi
        persistSousObjet(Utilisateur.class, lot.getCpiProjet());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
