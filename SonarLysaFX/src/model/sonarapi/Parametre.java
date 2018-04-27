package model.sonarapi;

public class Parametre
{
	private String clef;
	private String valeur;

	public Parametre(String clef, String valeur)
	{
		this.clef = clef;
		this.valeur = valeur;
	}

	public Parametre() {}

	public String getClef()
	{
		return clef;
	}

	public void setClef(String clef)
	{
		this.clef = clef;
	}

	public String getValeur()
	{
		return valeur;
	}

	public void setValeur(String valeur)
	{
		this.valeur = valeur;
	}

}
