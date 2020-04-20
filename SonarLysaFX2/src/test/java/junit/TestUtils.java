package junit;

import org.apache.logging.log4j.Logger;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

public class TestUtils
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    private TestUtils()
    {
        throw new AssertionError();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Création d'un mock des logger de l'application.
     * 
     * @param clazz
     *                   Nom du logger (ils sont declarés dans la classe {@code utilities.Statics}).
     * @param loggerName
     *                   Nom du logger dans la classe.
     * @param            <T>
     *                   Classe où l'on veut remplacer le logger.
     * @return
     *         Le mock du logger.
     */
    public static <T> Logger getMockLogger(Class<T> clazz, String loggerName)
    {
        Logger logger = PowerMockito.mock(Logger.class);
        Whitebox.setInternalState(clazz, loggerName, logger);
        return logger;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
