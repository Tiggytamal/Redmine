package control.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mchange.util.AssertException;

import model.bdd.Repack;
import model.enums.Param;
import utilities.AbstractToStringImpl;
import utilities.Statics;

public class ControlRepack extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/
    
    /** logger général */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");
    
    private static final ControlRepack INSTANCE = new ControlRepack();
    
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
    
    public List<Repack> getRepacksComposant(String nomCompo)
    {
        List<Repack> retour = new ArrayList<>();
        
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
