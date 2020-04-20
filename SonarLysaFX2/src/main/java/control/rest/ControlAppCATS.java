package control.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.Logger;

import model.enums.Param;
import model.enums.Severity;
import model.rest.sonarapi.ParamAPI;
import utilities.FunctionalException;
import utilities.Utilities;

/**
 * Classe d'appel des API AppCATS.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class ControlAppCATS extends AbstractControlRest
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");
    public static final ControlAppCATS INSTANCE = new ControlAppCATS();

    private static final String TESTAPPLI = "api/v1/applications/exist";

    /*---------- CONSTRUCTEURS ----------*/

    private ControlAppCATS()
    {
        super(Param.URLAPIAPPCATS);

        // Protection contre la création d'une nouvelle instance par reflexion
        if (INSTANCE != null)
            throw new AssertionError("control.rest.ControlAppCATS - Singleton, instanciation interdite!");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Test si le code applicaiton existe dans le référentiel des applications.
     * 
     * @param codeAppli
     *                  Le code applicaiton à tester.
     * @return
     *         Vrai si le code applicaiton existe.
     */
    public boolean testApplicationExiste(String codeAppli)
    {
        // Retour à faux des codes vides, pour éviter les plantages
        if (codeAppli.isEmpty())
            return false;

        Response response = appelWebserviceGET(TESTAPPLI, new ParamAPI[]
        { new ParamAPI("code", codeAppli) });

        // Le retour de l'api est du HTML, on verifie juste que l'objet JSON à l'interieur est bien à vrai.
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            String retour = response.readEntity(String.class);
            return retour.contains("true");
        }
        else
        {
            String erreur = "Impossible de remonter les informations du code application " + codeAppli + " - API : " + TESTAPPLI;
            LOGPLANTAGE.error(erreur);
            throw new FunctionalException(Severity.ERROR, erreur);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
