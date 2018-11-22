package model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import model.interfaces.AbstractModele;
import utilities.TechnicalException;

public interface ModelFactory
{   
    /**
     * Retourne une instance de la classe de modèle avec le constructeur de base
     * 
     * @param modelClass
     * @return
     */
    public static <T extends AbstractModele> T build(Class<T> modelClass)
    {
        try
        {
            Constructor<T> constructor = modelClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e)
        {
            throw new TechnicalException("Impossible d'instancier l'objet - classe : " + modelClass.getName(), e);
        }
    }
}
