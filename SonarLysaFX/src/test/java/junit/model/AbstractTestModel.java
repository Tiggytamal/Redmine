package junit.model;

import java.lang.reflect.ParameterizedType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.JunitBase;
import model.ModelFactory;
import model.Modele;
import utilities.TechnicalException;

public abstract class AbstractTestModel<T extends Modele> extends JunitBase
{
     /*---------- ATTRIBUTS ----------*/
    
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");
    
    protected T handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @SuppressWarnings("unchecked")
    @Override
    public void init() throws Exception
    {
        // Permet de récuperer la classe sous forme de type paramétré
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        
        // On récupère les paramètres de classe (ici T), on prend le premier (T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];
        
        // instanciation du paramètre, récupération de la classe, et création du handler depuis la factory
        try
        {
            handler = ModelFactory.getModel((Class<T>) Class.forName(parameterClassName));
        } 
        catch (ClassNotFoundException e)
        {
            LOGPLANTAGE.error(e);
            throw new TechnicalException("Impossible d'instancier l'énumération - junit.model.AbstractTestModel.init", e);
        }
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/




}
