package junit.dao;

import java.lang.reflect.ParameterizedType;

import dao.AbstractDao;
import dao.DaoFactory;
import junit.JunitBase;
import model.bdd.AbstractBDDModele;
import utilities.TechnicalException;

public abstract class AbstractTestDao<T extends AbstractDao<R>, R extends AbstractBDDModele> extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    protected T handler;
    protected Class<R> modele;
    
    /*---------- CONSTRUCTEURS ----------*/

    @SuppressWarnings("unchecked")
    @Override
    public void init() throws Exception
    {
        // Permet de r�cuperer la classe sous forme de type param�tr�
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On r�cup�re les param�tres de classe (ici T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[1].toString().split("\\s")[1];

        // Instanciation du param�tre avec la bonne classe
        try
        {
            modele = (Class<R>) Class.forName(parameterClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new TechnicalException("Impossible d'instancier l'�num�ration - control.excel.ControlExcelRead", e);
        }
        
        DaoFactory.getDao(modele);
    }
    
    /*---------- METHODES ABSTRAITES ----------*/
    
    public abstract void testReadAll();
    
    public abstract void testResetTable();
    
    public abstract void testRecupDonneesDepuisExcel();

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
