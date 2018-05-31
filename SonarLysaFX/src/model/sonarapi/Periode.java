package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Periode
{
    /*---------- ATTRIBUTS ----------*/

	private int index;
	private String valeur;

    /*---------- CONSTRUCTEURS ----------*/
	
	public Periode(int index, String valeur)
	{
	    this.index = index;
	    this.valeur = valeur;
	}
	
	public Periode()
	{
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML        
	}
	
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
	
	@XmlAttribute(name = "index")
	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	@XmlAttribute(name = "value")
	public String getValeur()
	{
		return valeur;
	}

	public void setValeur(String value)
	{
		this.valeur = value;
	}
}
