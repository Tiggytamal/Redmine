package junit.dao;

import java.lang.reflect.ParameterizedType;

import dao.AbstractDao;
import dao.DaoFactory;
import junit.JunitBase;
import model.bdd.AbstractBDDModele;
import model.bdd.Application;
import model.bdd.ChefService;
import model.bdd.ComposantSonar;
import model.bdd.DateMaj;
import model.bdd.DefautAppli;
import model.bdd.DefautQualite;
import model.bdd.Edition;
import model.bdd.LotRTC;
import model.bdd.Produit;
import model.bdd.ProjetClarity;
import utilities.TechnicalException;

public abstract class AbstractTestDao<T extends AbstractDao<R>, R extends AbstractBDDModele> extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    protected T daoTest;
    protected Class<R> modele;

    /*---------- CONSTRUCTEURS ----------*/

    @SuppressWarnings("unchecked")
    @Override
    public void init() throws Exception
    {
        // Permet de récuperer la classe sous forme de type paramétré
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

        // Switch pour déterminer le dao à créer
        switch (modele.getName())
        {
            case "model.bdd.Application":
                daoTest = (T) DaoFactory.getDao(Application.class);

            case "model.bdd.ChefService":
                daoTest = (T) DaoFactory.getDao(ChefService.class);

            case "model.bdd.ComposantSonar":
                daoTest = (T) DaoFactory.getDao(ComposantSonar.class);

            case "model.bdd.Edition":
                daoTest = (T) DaoFactory.getDao(Edition.class);

            case "model.bdd.LotRTC":
                daoTest = (T) DaoFactory.getDao(LotRTC.class);

            case "model.bdd.ProjetClarity":
                daoTest = (T) DaoFactory.getDao(ProjetClarity.class);

            case "model.bdd.DefautQualite":
                daoTest = (T) DaoFactory.getDao(DefautQualite.class);

            case "model.bdd.DateMaj":
                daoTest = (T) DaoFactory.getDao(DateMaj.class);

            case "model.bdd.Produit":
                daoTest = (T) DaoFactory.getDao(Produit.class);

            case "model.bdd.DefautAppli":
                daoTest = (T) DaoFactory.getDao(DefautAppli.class);

            default:
                throw new TechnicalException("junit.dao.AbstractTestDao - Création Dao impossible - classe : " + modele.getName());
        }
    }

    /*---------- METHODES ABSTRAITES ----------*/

    public abstract void testReadAll();

    public abstract void testResetTable();

    public abstract void testRecupDonneesDepuisExcel();

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
