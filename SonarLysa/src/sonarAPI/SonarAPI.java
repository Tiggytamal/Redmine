package sonarAPI;

import java.util.Base64;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class SonarAPI 
{
	/* Param�tres */
	
	private WebTarget webTarget;
	private String codeUser;
	private static SonarAPI sonarAPI;
	
	/**
	 * Cr�ation d'une instance de l'API pour acc�der � SonarCube. La m�thode est priv�e.
	 * @param url
	 * @param user
	 * @param password
	 */
	private SonarAPI(String url, String user, String password)
	{
		webTarget  = ClientBuilder.newClient().target("http://ttp10-snar.ca-technologies.fr");
		StringBuilder builder = new StringBuilder(user);
		builder.append(":");
		builder.append(password);
		String codeUser = Base64.getEncoder().encodeToString(builder.toString().getBytes());				
	}
	
	public static SonarAPI getInstance(String url, String user, String password)
	{
		if (sonarAPI == null)
		{
			return new SonarAPI(url, user, password);
		}

		return sonarAPI;
	}
	
	
	
}
