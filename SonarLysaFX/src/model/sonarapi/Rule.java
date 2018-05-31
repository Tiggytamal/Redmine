package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Rule
{
	/*---------- ATTRIBUTS ----------*/

	private String key;
	private String name;
	private String status;
	private String lang;
	private String langName;

	/*---------- CONSTRUCTEURS ----------*/
	/*---------- METHODES PUBLIQUES ----------*/
	/*---------- METHODES PRIVEES ----------*/
	/*---------- ACCESSEURS ----------*/

	@XmlAttribute(name = "key")
	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	@XmlAttribute(name = "name")
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@XmlAttribute(name = "status")
	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	@XmlAttribute(name = "lang")
	public String getLang()
	{
		return lang;
	}

	public void setLang(String lang)
	{
		this.lang = lang;
	}

	@XmlAttribute(name = "langName")
	public String getLangName()
	{
		return langName;
	}

	public void setLangName(String langName)
	{
		this.langName = langName;
	}

}
