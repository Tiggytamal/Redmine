package junit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import utilities.FunctionalException;
import utilities.enums.Severity;

public class TestUtils
{
    private TestUtils()
    {
    }

    /**
     * Permet d'appeler une méthode privée d'une classe.<br>
     * le {@code nomMethode} est le nom de la méthode que l'on veut appeler. {@code instance} correspond à l'instance de
     * la classe que l'on veut utiliser.
     * {@code retour} correspond au type de retour de la méthode (null si retour void). {@code params} est un array de
     * tous les paramètres de la méthode
     * 
     * @param nomMethode
     * @param instance
     * @param params
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> T callPrivate(String nomMethode, Object instance, Class<T> retour, Object... params)
    {
        if (nomMethode == null || instance == null)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Les paramètres de la méthodes ne peuvent pas être nuls - TestUtils.callPrivate() - " 
                    + "nomMethode = " + nomMethode + " - instance = " + instance);

        Class<?>[] classParams = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++)
        {
            classParams[i] = params[i].getClass();
        }

        Method call;
        try
        {
            call = instance.getClass().getDeclaredMethod(nomMethode, classParams);
            call.setAccessible(true);

            if (retour != null)
                return retour.cast(call.invoke(instance, params));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            throw new IllegalArgumentException("Erreur à l'invocation de la méthode", e);
        }

        return null;
    }
}
