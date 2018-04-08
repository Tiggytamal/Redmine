package model;

import java.lang.reflect.InvocationTargetException;

import utilities.TechnicalException;

public class ModelFactory
{
    private ModelFactory() {}
    
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
                classParams[i] = params[i].getClass();
            }
            return modelClass.getConstructor(classParams).newInstance(params);
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
        {
            throw new TechnicalException("Impossible d'instancier l'objet - classe : " + modelClass.getName(), e);
        }
    }
}