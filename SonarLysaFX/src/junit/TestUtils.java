package junit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Logger;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

public class TestUtils
{
    private TestUtils()
    {
    }

    /**
     * Cr�ation d'un  mock des logger de l'application
     * @param loggerName
     *          non du logger (ils sont d�clar�s dans la classe {@code utilities.Statics}
     * @return
     */
    public static Logger getMockLogger(String loggerName)
    {
        Logger logger = PowerMockito.mock(Logger.class);
        Whitebox.setInternalState(Statics.class, loggerName, logger);
        return logger;
    }

    /**
     * Permet d'appeler une m�thode priv�e d'une classe.<br>
     * le {@code nomMethode} est le nom de la m�thode que l'on veut appeler. {@code instance} correspond � l'instance de la classe que l'on veut utiliser.
     * {@code retour} correspond au type de retour de la m�thode (null si retour void). {@code params} est un array de tous les param�tres de la m�thode
     * 
     * @param nomMethode
     *          Nom de la m�thode � appeler
     * @param instance
     *          Instance de l'objet d�clarant la m�thode
     * @param retour
     *          Classe de l'objet de retour
     * @param params
     *          Param�tre de la m�thode
     * @return
     */
    public static <T> T callPrivate(String nomMethode, Object instance, Class<T> retour, Object... params)
    {
        if (nomMethode == null || instance == null)
            throw new FunctionalException(Severity.SEVERITY_ERROR,
                    "Les param�tres de la m�thodes ne peuvent pas �tre nuls - TestUtils.callPrivate() - " + "nomMethode = " + nomMethode + " - instance = " + instance);

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
            throw new IllegalArgumentException("Erreur � l'invocation de la m�thode", e);
        }

        return null;
    }
}
