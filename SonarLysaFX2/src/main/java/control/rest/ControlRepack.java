package control.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import model.bdd.ComposantBase;
import model.enums.Param;
import model.rest.repack.ComposantRepack;
import model.rest.repack.RepackREST;
import utilities.Statics;

/**
 * Classe de contrôle des Webservices vers la PIC pour les repacks des composants.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class ControlRepack extends AbstractControlRest
{
    /*---------- ATTRIBUTS ----------*/

    private static final String VERSIONCOMPO = "versionComposants/getListByNameBaseline/";
    private static final String ACCESREPACKS = "versionComposantsHorsPicRepack/getListContainingIdVComp/";
    private static final String FINACCESREPACKS = "/DERNIERE_VERSION_UNIQUEMENT";

    /*---------- CONSTRUCTEURS ----------*/

    public ControlRepack()
    {
        super(Param.URLREPACK);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Recupère tous les repack du composant donné depuis les webservices de la PIC.
     * 
     * @param compo
     *              Composant à traiter.
     * @return
     *         Liste des repacks du composant.
     */
    public List<RepackREST> getRepacksComposant(ComposantBase compo)
    {
        // Création de la première resource pour le webservice pour récupérer l'id du composant
        List<RepackREST> retour = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        String nomWebService = compo.getNom().substring(0, compo.getNom().length() - 3).replace("Composant ", Statics.EMPTY);
        String version = compo.getVersion();
        builder.append(VERSIONCOMPO).append(nomWebService).append('/').append('V').append(version);
        Response response = appelWebserviceGETWithoutHeader(builder.toString());
        ComposantRepack compoRepack = null;

        // Contrôle de la reponse
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            List<ComposantRepack> liste = response.readEntity(new GenericType<List<ComposantRepack>>() {
            });
            if (!liste.isEmpty())
                compoRepack = liste.get(0);
            else
                return retour;
        }
        else
            return retour;

        // Appel du deuxième webservice pour récupérer le repack du composant
        builder = new StringBuilder(ACCESREPACKS);
        builder.append(compoRepack.getId()).append(FINACCESREPACKS);
        response = appelWebserviceGETWithoutHeader(builder.toString());

        // Contrôle de la reponse
        if (response.getStatus() == Status.OK.getStatusCode())
        {
            return response.readEntity(new GenericType<List<RepackREST>>() {
            });
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
