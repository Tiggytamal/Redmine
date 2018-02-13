package sonarapi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement(name = "component")
@JsonIgnoreProperties({"id", "key", "name", "description", "qualifier", "language", "path"})
public class Composant implements ModeleSonar
{
	/*---------- ATTRIBUTS ----------*/

	private String id;
	private String key;
	private String nom;
	private String descritpion;
	private String qualifier;
	private String langage;
	private String path;
	private List<Metrique> metriques;

	/*---------- ACCESSEURS ----------*/

	@XmlAttribute(name = "id")
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

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
	public String getNom()
	{
		return nom;
	}

	public void setNom(String name)
	{
		this.nom = name;
	}

	@XmlAttribute(name = "description", required = false)
	public String getDescritpion()
	{
		return descritpion;
	}

	public void setDescritpion(String descritpion)
	{
		this.descritpion = descritpion;
	}

	@XmlAttribute(name = "qualifier")
	public String getQualifier()
	{
		return qualifier;
	}

	public void setQualifier(String qualifier)
	{
		this.qualifier = qualifier;
	}

	@XmlAttribute(name = "language", required = false)
	public String getLangage()
	{
		return langage;
	}

	public void setLangage(String langage)
	{
		this.langage = langage;
	}

	@XmlAttribute(name = "path", required = false)
	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	@XmlElementWrapper
	@XmlElement(name = "measures")
	public List<Metrique> getMetriques()
	{
		if (metriques == null)
			return new ArrayList<>();
		return metriques;
	}
	
	/**
	 * Permet de retourner une map des m�triques plut�t qu'une liste.<br>
	 * clef = identifation du m�trique<br>
	 * valeur = valeur du m�trique
	 * @return
	 */
	public Map<String, String> getMapMetriques()
	{
		if (metriques == null)
			return new HashMap<>();
		
		Map<String, String> retour = new HashMap<>();
		
		for (Metrique metrique : metriques)
		{
			retour.put(metrique.getMetric(), metrique.getValue());
		}
		return retour;
	}

	public void setListeMeriques(List<Metrique> metriques)
	{
		this.metriques = metriques;
	}
}