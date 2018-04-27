package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User
{
	/*---------- ATTRIBUTS ----------*/

	private String login;
	private String name;
	private String active;
	private String email;

	/*---------- CONSTRUCTEURS ----------*/
	/*---------- METHODES PUBLIQUES ----------*/
	/*---------- METHODES PRIVEES ----------*/
	/*---------- ACCESSEURS ----------*/

	@XmlAttribute (name = "login")
	public String getLogin()
	{
		return login;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	@XmlAttribute (name = "name")
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@XmlAttribute (name = "active")
	public String getActive()
	{
		return active;
	}

	public void setActive(String active)
	{
		this.active = active;
	}

	@XmlAttribute (name = "email")
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
}