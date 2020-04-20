package control.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import model.enums.Param;
import model.rest.maps.Fonctionnalite;
import model.rest.maps.Produit;
import model.rest.maps.ProduitExiste;
import model.rest.maps.Solution;

/**
 * Classe de contrôle des appels webservice vers l'application MAPS
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class ControlMAPS extends AbstractControlRest
{
    /*---------- ATTRIBUTS ----------*/

    private static final String INFOSOLUTION = "api/v1/SOLUTIONINFOS/";
    private static final String INFOPRODUIT = "api/v1/PRODUITINFOS/";
    private static final String INFOFONCTIONNALITE = "api/v1/FONCTIONNALITEINFOS/";
    private static final String PRODUITEXISTE = "api/v1/PRODUITEXIST/";

    public static final ControlMAPS INSTANCE = new ControlMAPS();

    /*---------- CONSTRUCTEURS ----------*/

    private ControlMAPS()
    {
        super(Param.URLMAPS);

        // Protection contre la création d'une nouvelle instance par réflexion
        if (INSTANCE != null)
            throw new AssertionError("control.rest.ControlMAPS - Singleton, instanciation interdite!");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Retourne les informations d'une solution depuis MAPS
     * 
     * @param codeSolution
     *                     Code de la solution dans MAPS
     * @return
     *         La solution avec les informations provenant de MAPS.
     */
    public Solution getSolution(String codeSolution)
    {
        Response response = appelWebserviceGET(INFOSOLUTION + codeSolution);

        // Contrôle de la reponse
        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(Solution.class);
        else
            return null;
    }

    /**
     * Retourne les informations d'un produit depuis MAPS
     * 
     * @param codeProduit
     *                    Code du produit dans MAPS.
     * @return
     *         Le Produit avec les informations provenant de MAPS.
     */
    public Produit getProduit(String codeProduit)
    {
        Response response = appelWebserviceGET(INFOPRODUIT + codeProduit);

        // Contrôle de la reponse
        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(Produit.class);
        else
            return null;
    }

    /**
     * Retourne les informations d'une fonctionnalité depuis MAPS
     * 
     * @param codeFonctionnalite
     *                           Code de la fonctionnalité dans MAPS.
     * @return
     *         La Fonctionnalite avec les informations provenant de MAPS.
     */
    public Fonctionnalite getFonctionnalite(String codeFonctionnalite)
    {
        Response response = appelWebserviceGET(INFOFONCTIONNALITE + codeFonctionnalite);

        // Contrôle de la reponse
        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(Fonctionnalite.class);
        else
            return null;
    }

    /**
     * Retourne si un produit existe dans MAPS
     * 
     * @param codeProduit
     *                    Code du produit à tester.
     * @return
     *         Vrai sile produit existe dans MAAOS.
     */
    public boolean produitExiste(String codeProduit)
    {
        Response response = appelWebserviceGET(PRODUITEXISTE + codeProduit);

        // Contrôle de la reponse
        if (response.getStatus() == Status.OK.getStatusCode())
            return response.readEntity(ProduitExiste.class).existe();
        else
            return false;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
