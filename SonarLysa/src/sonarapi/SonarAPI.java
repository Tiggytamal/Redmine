package sonarapi;

import java.util.Base64;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import sonarapi.model.Validation;
import sonarapi.model.Views;

public class SonarAPI 
{
	/* Param�tres */
	
	private WebTarget webTarget;
	private String codeUser;
	private static SonarAPI sonarAPI;
	
	/* Constructeurs */
	
	/**
	 * Cr�ation d'une instance de l'API pour acc�der � SonarCube. La m�thode est priv�e.
	 * 
	 * @param url
	 * 			Url du serveur SonarQube
	 * @param user
	 * 			id de l'utilisateur
	 * @param password
	 * 			mot de passe de l'utilisateur
	 */
	private SonarAPI(String url, String user, String password)
	{
		webTarget  = ClientBuilder.newClient().target(url);
		StringBuilder builder = new StringBuilder(user);
		builder.append(":");
		builder.append(password);
		codeUser = Base64.getEncoder().encodeToString(builder.toString().getBytes());				
	}
	
	/**
	 * 
	 * @param url
	 * 			Url du serveur SonarQube
	 * @param user
	 * 			id de l'utilisateur
	 * @param password
	 * 			mot de passe de l'utilisateur
	 * @return
	 * 		Une instance singleton de l'api
	 */
	public static SonarAPI getInstance(String url, String user, String password)
	{
		if (sonarAPI == null)
		{
			return new SonarAPI(url, user, password);
		}

		return sonarAPI;
	}
	
	/* M�thodes */
	
	/**
	 * Permet de remonter toutes les vues d�j� cr��es
	 * @return
	 */
	public Views getViews()
	{
		Response response = appelWebservice("api/views/list");
		if (response.getStatus() == Status.OK.getStatusCode())
			return response.readEntity(Views.class);
		return new Views();
	}
	
	/**
	 * Permet de v�rifier si l'utilisateur a bien les acc�s � SonarQube
	 * @return
	 * 		true si l'utilisateur a les acc�s.
	 */
	public boolean verificationUtilisateur()
	{
		Response response = appelWebservice("api/authentication/validate");
		if (response.getStatus() == Status.OK.getStatusCode())
			return response.readEntity(Validation.class).isValid();
		return false;
	}
	
	/* M�thodes priv�es */
	
	private Response appelWebservice(String url)
	{
		WebTarget requete = webTarget.path(url);
		Invocation.Builder invocationBuilder = requete.request(MediaType.APPLICATION_JSON).header("Authorization", codeUser);
		return invocationBuilder.get();
	}
	
}
