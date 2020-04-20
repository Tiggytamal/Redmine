package dao;

import model.bdd.AbstractBDDModele;
import utilities.TechnicalException;

/**
 * Factory de création des Dao pour appel de la base de données MySQL.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class DaoFactory
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    DaoFactory()
    {}

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet de créer une instance d'un DAO.
     * 
     * @param daoClass
     *                 Classe de modèle gérée par le DAO.
     * @param          <T>
     *                 Classe du DAO.
     * @param          <M>
     *                 Classe du modèle.
     * @return
     *         Le DOA.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Dao<M, U>, M extends AbstractBDDModele<U>, U> T getMySQLDao(Class<M> daoClass)
    {
        switch (daoClass.getName())
        {
            case "model.bdd.Application":
                return (T) new DaoApplication();

            case "model.bdd.ChefService":
                return (T) new DaoChefService();

            case "model.bdd.ComposantBase":
                return (T) new DaoComposantBase();

            case "model.bdd.Edition":
                return (T) new DaoEdition();

            case "model.bdd.LotRTC":
                return (T) new DaoLotRTC();

            case "model.bdd.ProjetClarity":
                return (T) new DaoProjetClarity();

            case "model.bdd.DefautQualite":
                return (T) new DaoDefautQualite();

            case "model.bdd.Produit":
                return (T) new DaoProduit();

            case "model.bdd.ComposantErreur":
                return (T) new DaoComposantErreur();

            case "model.bdd.Solution":
                return (T) new DaoSolution();

            case "model.bdd.IssueBase":
                return (T) new DaoIssueBase();

            case "model.bdd.Utilisateur":
                return (T) new DaoUtilisateur();

            case "model.bdd.ProjetMobileCenter":
                return (T) new DaoProjetMobileCenter();

            case "model.bdd.AnomalieRTC":
                return (T) new DaoAnomalieRTC();

            case "model.bdd.Statistique":
                return (T) new DaoStatistique();
                
            case "model.bdd.StatistiqueCompo":
                return (T) new DaoStatistiqueCompo();

            default:
                throw new TechnicalException("dao.DaoFactory - Création Dao impossible - classe : " + daoClass.getName());
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
