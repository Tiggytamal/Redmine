package control.rest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.enums.Param;
import model.rest.sonarapi.ParamAPI;
import utilities.AbstractToStringImpl;
import utilities.Statics;

/**
 * Classe mère des classes de contrôle pour les appels aux WebServices.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public abstract class AbstractControlRest extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    private static final String AUTHORIZATION = "Authorization";

    protected final WebTarget webTarget;
    protected final String codeUser;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractControlRest(Param url)
    {
        webTarget = ClientBuilder.newClient().target(Statics.proprietesXML.getMapParams().get(url));
        StringBuilder builder = new StringBuilder(Statics.info.getPseudo());
        builder.append(':');
        builder.append(Statics.info.getMotDePasse());
        codeUser = "Basic " + Base64.getEncoder().encodeToString(builder.toString().getBytes(StandardCharsets.UTF_8));
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    /**
     * Appel des webservices en POST
     * 
     * @param webTarget
     *                  WebTargert contenant le Webservice cible ainsi que les paramètres.
     * @return
     *         La réponse du WebService.
     */
    public Response appelWebservicePOST(WebTarget webTarget)
    {
        return webTarget.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser).method("POST");
    }

    /**
     * Appel des webservices en GET sans paramètres supplémentaires
     * 
     * @param url
     *            Url du webservices
     * @return
     *         La réponse du WebService.
     */
    public Response appelWebserviceGET(String url)
    {
        WebTarget requete = webTarget.path(url);

        return requete.request().header(AUTHORIZATION, codeUser).get();
    }

    /**
     * Appel des webservices en GET sans paramètres supplementaires et sans Header.
     * 
     * @param url
     *            Url du webservices
     * @return
     *         La réponse du WebService.
     */
    public Response appelWebserviceGETWithoutHeader(String url)
    {
        WebTarget requete = webTarget.path(url);

        return requete.request().get();
    }

    /**
     * Appel des webservices en GET avec un seul paramètre pour éviter de devoir créer le tableau de paramètres.
     * 
     * @param url
     *              Url du webservices
     * @param param
     *              Paramètre optionnel de la requête
     * @return
     *         La réponse du WebService.
     */
    public Response appelWebserviceGET(String url, ParamAPI param)
    {
        return appelWebserviceGET(url, new ParamAPI[]
        { param });
    }

    /**
     * Appel des webservices en GET avec paramètres.
     * 
     * @param url
     *               Url du webservices
     * @param params
     *               Paramètres optionnels de la requête
     * @return
     *         La réponse du WebService.
     */
    public Response appelWebserviceGET(String url, ParamAPI[] params)
    {
        if (params == null)
            return appelWebserviceGET(url);

        WebTarget requete = webTarget.path(url);

        for (ParamAPI parametre : params)
        {
            requete = requete.queryParam(parametre.getClef(), parametre.getValeur());
        }

        return requete.request().header(AUTHORIZATION, codeUser).get();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
