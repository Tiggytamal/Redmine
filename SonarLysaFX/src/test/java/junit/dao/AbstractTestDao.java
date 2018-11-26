package junit.dao;

import java.lang.reflect.ParameterizedType;

import dao.AbstractDao;
import dao.ListeDao;
import junit.JunitBase;
import model.bdd.AbstractBDDModele;
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
                daoTest = (T) ListeDao.daoAppli;

            case "model.bdd.ChefService":
                daoTest = (T) ListeDao.daoChefServ;

            case "model.bdd.ComposantSonar":
                daoTest = (T) ListeDao.daoCompo;

            case "model.bdd.Edition":
                daoTest = (T) ListeDao.daoEdition;

            case "model.bdd.LotRTC":
                daoTest = (T) ListeDao.daoLotRTC;

            case "model.bdd.ProjetClarity":
                daoTest = (T) ListeDao.daoProjetClarity;

            case "model.bdd.DefautQualite":
                daoTest = (T) ListeDao.daoDefautQualite;

            case "model.bdd.DateMaj":
                daoTest = (T) ListeDao.daoDateMaj;

            case "model.bdd.Produit":
                daoTest = (T) ListeDao.daoProduit;

            case "model.bdd.DefautAppli":
                daoTest = (T) ListeDao.daoDefautAppli;

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
