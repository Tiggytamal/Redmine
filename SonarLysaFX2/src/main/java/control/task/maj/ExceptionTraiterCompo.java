package control.task.maj;

import org.apache.logging.log4j.Logger;

import utilities.Utilities;

/**
 * Exception sépcifique au traitement des fichiers JSON provenant de l'assemblage des composants par la PIC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class ExceptionTraiterCompo extends Exception
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    /*---------- CONSTRUCTEURS ----------*/

    public ExceptionTraiterCompo(String message)
    {
        super(message);
        LOGPLANTAGE.error(message);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
