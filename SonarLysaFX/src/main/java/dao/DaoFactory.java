package dao;

import javax.persistence.EntityManager;

import model.bdd.AbstractBDDModele;
import utilities.TechnicalException;

/**
 * Factory de création des Dao
 * 
 * @author ETP8137 - Grégoire Mathon
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
                
            case "model.bdd.DefaultQualite":
                return (T) new DaoDefaultQualite();
                
            case "model.bdd.DateMaj":
                return (T) new DaoDateMaj();
                
            case "model.bdd.GroupementProjet":
                return (T) new DaoGroupementProjet();
                
            case "model.bdd.DefaultAppli":
                return (T) new DaoDefaultAppli();

            default:
                throw new TechnicalException("dao.DaoFactory - Création Dao impossible - classe : " + daoClass.getName());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends AbstractDao<M>, M extends AbstractBDDModele> T getDao(Class<M> daoClass, EntityManager em)
    {
        switch (daoClass.getName())
        {
            case "model.bdd.Application":
                return (T) new DaoApplication(em);

            case "model.bdd.ChefService":
                return (T) new DaoChefService(em);

            case "model.bdd.ComposantSonar":
                return (T) new DaoComposantSonar(em);

            case "model.bdd.Edition":
                return (T) new DaoEdition(em);

            case "model.bdd.LotRTC":
                return (T) new DaoLotRTC(em);
                
            case "model.bdd.ProjetClarity":
                return (T) new DaoProjetClarity(em);
                
            case "model.bdd.DefaultQualite":
                return (T) new DaoDefaultQualite(em);
                
            case "model.bdd.DateMaj":
                return (T) new DaoDateMaj(em);
                
            case "model.bdd.GroupementProjet":
                return (T) new DaoGroupementProjet(em);
                
            case "model.bdd.DefaultAppli":
                return (T) new DaoDefaultAppli(em);

            default:
                throw new TechnicalException("dao.DaoFactory - Création Dao impossible - classe : " + daoClass.getName());
        }
    }
}
