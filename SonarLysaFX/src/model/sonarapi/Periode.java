package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Periode
{
	private int index;
	private String valeur;

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
