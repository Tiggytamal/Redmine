package junit;

import org.apache.logging.log4j.Logger;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import utilities.Statics;

public class TestUtils
{
    /*---------- ATTRIBUTS ----------*/
    
    public static final String NOTNULL = "notNull";
    public static final String NEWVAL = "newVal";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    private TestUtils() {}

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Création d'un  mock des logger de l'application
     * @param loggerName
     *          non du logger (ils sont déclarès dans la classe {@code utilities.Statics}
     * @return
     */
    public static Logger getMockLogger(String loggerName)
    {
        Logger logger = PowerMockito.mock(Logger.class);
        Whitebox.setInternalState(Statics.class, loggerName, logger);
        return logger;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
}