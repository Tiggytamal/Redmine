package junit.dao;

import java.lang.reflect.ParameterizedType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import dao.AbstractMySQLDao;
import dao.DaoFactory;
import junit.JunitBase;
import model.bdd.AbstractBDDModele;
import model.bdd.Application;
import model.bdd.ChefService;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.bdd.Edition;
import model.bdd.LotRTC;
import model.bdd.Produit;
import model.bdd.ProjetClarity;
import model.bdd.StatistiqueCompo;
import utilities.TechnicalException;

public abstract class TestAbstractDao<T extends AbstractMySQLDao<R, U>, R extends AbstractBDDModele<U>, U> extends JunitBase<T>
{
    /*---------- ATTRIBUTS ----------*/

    protected Class<R> modele;

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    @SuppressWarnings("unchecked")
    public void init() throws Exception
    {
        // Permet de récupérer la classe sous forme de type parametre
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On récupère les paramètres de classe (ici T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[1].toString().split("\\s")[1];

        // Instanciation du paramètre avec la bonne classe
        try
        {
            modele = (Class<R>) Class.forName(parameterClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new TechnicalException("Impossible d'instancier l'énumération - control.excel.ControlExcelRead", e);
        }

        // Switch pour determiner le dao à creer
        switch (modele.getName())
        {
            case "model.bdd.Application":
                objetTest = (T) DaoFactory.getMySQLDao(Application.class);
                break;

            case "model.bdd.ChefService":
                objetTest = (T) DaoFactory.getMySQLDao(ChefService.class);
                break;

            case "model.bdd.ComposantBase":
                objetTest = (T) DaoFactory.getMySQLDao(ComposantBase.class);
                break;

            case "model.bdd.Edition":
                objetTest = (T) DaoFactory.getMySQLDao(Edition.class);
                break;

            case "model.bdd.LotRTC":
                objetTest = (T) DaoFactory.getMySQLDao(LotRTC.class);
                break;

            case "model.bdd.ProjetClarity":
                objetTest = (T) DaoFactory.getMySQLDao(ProjetClarity.class);
                break;

            case "model.bdd.DefautQualite":
                objetTest = (T) DaoFactory.getMySQLDao(DefautQualite.class);
                break;

            case "model.bdd.Produit":
                objetTest = (T) DaoFactory.getMySQLDao(Produit.class);
                break;
                
            case "model.bdd.StatistiqueCompo":
                objetTest = (T) DaoFactory.getMySQLDao(StatistiqueCompo.class);
                break;

            default:
                throw new TechnicalException("junit.dao.TestAbstractDao - Création Dao impossible - classe : " + modele.getName());
        }
    }

    /*---------- METHODES ABSTRAITES ----------*/

    public abstract void testReadAll(TestInfo testInfo);

    public abstract void testResetTable(TestInfo testInfo);

    public abstract void testRecupDonneesDepuisExcel(TestInfo testInfo);

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
