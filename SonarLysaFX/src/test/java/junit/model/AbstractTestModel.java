package junit.model;

import java.lang.reflect.ParameterizedType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.JunitBase;
import model.ModelFactory;
import model.interfaces.AbstractModele;
import utilities.TechnicalException;

public abstract class AbstractTestModel<T extends AbstractModele> extends JunitBase
{
     /*---------- ATTRIBUTS ----------*/
    
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");
    
    protected T objetTest;
    
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
            objetTest = ModelFactory.build((Class<T>) Class.forName(parameterClassName));
        } 
        catch (ClassNotFoundException e)
        {
            LOGPLANTAGE.error(e);
            throw new TechnicalException("Impossible d'instancier l'énumération - junit.model.AbstractTestModel.init", e);
        }
        
        initImpl();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/    
    
    /**
     * Implémentation de l'initialisation si besoin pour les classes filles
     */
    protected void initImpl()
    {
        
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/




}
