package control.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.mchange.util.AssertException;

import model.bdd.ComposantSonar;
import model.enums.Param;
import model.rest.repack.ComposantRepack;
import model.rest.repack.RepackREST;
import utilities.AbstractToStringImpl;
import utilities.Statics;

public class ControlRepack extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    public static final ControlRepack INSTANCE = new ControlRepack();

    private static final String VERSIONCOMPO = "versionComposants/getListByNameBaseline/";
    private static final String ACCESREPACKS = "versionComposantsHorsPicRepack/getListContainingIdVComp/";
    private static final String FINACCESREPACKS = "/DERNIERE_VERSION_UNIQUEMENT";

    private final WebTarget webTarget;

    /*---------- CONSTRUCTEURS ----------*/

    private ControlRepack()
    {
        // Protection contre la création d'une nouvelle instance par réflexion
        if (INSTANCE != null)
            throw new AssertException();

        webTarget = ClientBuilder.newClient().target(Statics.proprietesXML.getMapParams().get(Param.URLREPACK));
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public List<RepackREST> getRepacksComposant(ComposantSonar compo)
    {
        // Création de la première resource pour le webservice pour récupérer l'id du composant
        List<RepackREST> retour = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        String nomWebService = compo.getNom().substring(0, compo.getNom().length() - 3).replace("Composant ", Statics.EMPTY);
        String version = compo.getVersion();
        builder.append(VERSIONCOMPO).append(nomWebService).append("/").append("V").append(version);
        Response response = appelWebserviceGET(builder.toString());
        ComposantRepack compoRepack = null;

        // Contrôle de la réponse
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            List<ComposantRepack> liste = response.readEntity(new GenericType<List<ComposantRepack>>() { });
            if (!liste.isEmpty())
                compoRepack = liste.get(0);
            else
                return retour;
        }
        else
            return retour;

        // Appel du deusième webservice pour récupérer le repack du composant
        builder = new StringBuilder(ACCESREPACKS);
        builder.append(compoRepack.getId()).append(FINACCESREPACKS);
        response = appelWebserviceGET(builder.toString());

        // Contrôle de la réponse
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            return response.readEntity(new GenericType<List<RepackREST>>() { });
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Appel des webservices en GET sans paramètres supplémentaires
     * 
     * @param url
     *            Url du webservices
     * @return
     */
    private Response appelWebserviceGET(String url)
    {
        WebTarget requete = webTarget.path(url);

        return requete.request(MediaType.APPLICATION_JSON).get();
    }

    /*---------- ACCESSEURS ----------*/

}
