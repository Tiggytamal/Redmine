package model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import utilities.TechnicalException;

/**
 * Factory pour l'instanciation des classes de modèle sans paramètre.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public interface ModelFactory
{
    /**
     * Retourne une instance de la classe de modèle avec le constructeur de base
     * 
     * @param modelClass
     *                   Classe du modèle désiré.
     * @param            <T>
     *                   Classe du modèle.
     * @return
     *         Une instance de la classe de modèle désiré.
     */
    static <T extends AbstractModele> T build(Class<T> modelClass)
    {
        try
        {
            Constructor<T> constructor = modelClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            throw new TechnicalException("Impossible d'instancier l'objet - classe : " + modelClass.getName(), e);
        }
    }
}
