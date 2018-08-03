package model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import utilities.TechnicalException;

public interface ModelFactory
{   
    /**
     * Retourne une instance de la classe de modèle avec le constructeur de base
     * 
     * @param modelClass
     * @return
     */
    public static <T extends Modele> T getModel(Class<T> modelClass)
    {
        try
        {
            return modelClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new TechnicalException("Impossible d'instancier l'objet - classe : " + modelClass.getName(), e);
        }
    }
    
    /**
     * Retourne une instance de la classe de modèle avec un constructeur utilisant des paramètres
     * Attention il n'est asp possible de retrouver un constructeur avec un varaible nulle.
     * Si c'est le cas, un test sera fait avec un {@code String}.
     * 
     * @param modelClass
     * @param params
     * @return
     */
    public static <T extends Modele> T getModelWithParams(Class<T> modelClass, Object... params)
    {
        try
        {
            Class<?>[] classParams = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++)
            {
                if (params[i] != null)
                    classParams[i] = params[i].getClass();
                else
                    classParams[i] = String.class;
            }
            Constructor<T> constructor = modelClass.getDeclaredConstructor(classParams);
            constructor.setAccessible(true);
            return constructor.newInstance(params);
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
        {
            throw new TechnicalException("Impossible d'instancier l'objet - classe : " + modelClass.getName(), e);
        }
    }
}
