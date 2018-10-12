package dao;

import model.bdd.AbstractBDDModele;
import utilities.TechnicalException;

/**
 * Factory de cr�ation des Dao
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public class DaoFactory
{
    @SuppressWarnings("unchecked")
    public static <T extends AbstractDao<M>, M extends AbstractBDDModele> T getDao(Class<M> daoClass)
    {
        switch (daoClass.getName())
        {
            case "model.bdd.Application":
                return (T) new DaoApplication();

            case "model.bdd.ChefService":
                return (T) new DaoChefService();

            case "model.bdd.ComposantSonar":
                return (T) new DaoComposantSonar();

            case "model.bdd.Edition":
                return (T) new DaoEdition();

            case "model.bdd.LotRTC":
                return (T) new DaoLotRTC();
                
            case "model.bdd.ProjetClarity":
                return (T) new DaoProjetClarity();
                
            case "model.bdd.Anomalie":
                return (T) new DaoAnomalie();
                
            case "model.bdd.DateMaj":
                return (T) new DaoDateMaj();
                
            case "model.bdd.GroupementProjet":
                return (T) new DaoGroupementProjet();

            default:
                throw new TechnicalException("dao.DaoFactory - Cr�ation Dao impossible - classe : " + daoClass.getName());
        }
    }
}
