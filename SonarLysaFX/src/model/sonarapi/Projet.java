package model.sonarapi;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Projet implements ModeleSonar, Serializable
{
	/*---------- ATTRIBUTS ----------*/

	private static final long serialVersionUID = 1L;
	private String id;
	private String key;
	private String nom;
	private String sc;
	private String qu;
	private String lot;
	
	/*---------- CONSTRUCTEURS ----------*/
	/*---------- METHODES PUBLIQUES ----------*/
	/*---------- METHODES PRIVEES ----------*/
	/*---------- ACCESSEURS ----------*/

	@XmlAttribute (name = "k", required = true)
	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}
	
	@XmlAttribute (name = "id")
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@XmlAttribute (name = "nm")
	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	@XmlAttribute (name = "sc")
	public String getSc()
	{
		return sc;
	}

	public void setSc(String sc)
	{
		this.sc = sc;
	}

	@XmlAttribute (name = "qu")
	public String getQu()
	{
		return qu;
	}

	public void setQu(String qu)
	{
		this.qu = qu;
	}

	public String getLot()
	{
		return lot;
	}

	public void setLot(String lot)
	{
		this.lot = lot;
	}
}