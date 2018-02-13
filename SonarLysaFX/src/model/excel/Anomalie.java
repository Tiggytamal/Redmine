package model.excel;

/**
 * Classe de mod�le uqi correspond aux donn�es du fichier Excel des anomalies
 * 
 * @author ETP8137
 *
 */
public class Anomalie
{
	/*---------- ATTRIBUTS ----------*/

	private String direction;
	private String departement;
	private String service;
	private String responsableService;
	private String projetClarity;
	private String libelleProjet;
	private String cpiProjet;
	private String edition;
	private String lot;
	private String environnement;
	private String numeroAnomalie;
	private String etat;
	private String remarque;
	
	/*---------- CONSTRUCTEURS ----------*/
	/*---------- METHODES PUBLIQUES ----------*/
	/*---------- METHODES PRIVEES ----------*/
	/*---------- ACCESSEURS ----------*/
	
	public String getDirection()
	{
		return direction;
	}
	public void setDirection(String direction)
	{
		this.direction = direction;
	}
	public String getDepartement()
	{
		return departement;
	}
	public void setDepartement(String departement)
	{
		this.departement = departement;
	}
	public String getService()
	{
		return service;
	}
	public void setService(String service)
	{
		this.service = service;
	}
	public String getResponsableService()
	{
		return responsableService;
	}
	public void setResponsableService(String responsableService)
	{
		this.responsableService = responsableService;
	}
	public String getProjetClarity()
	{
		return projetClarity;
	}
	public void setProjetClarity(String projetClarity)
	{
		this.projetClarity = projetClarity;
	}
	public String getLibelleProjet()
	{
		return libelleProjet;
	}
	public void setLibelleProjet(String libelleProjet)
	{
		this.libelleProjet = libelleProjet;
	}
	public String getCpiProjet()
	{
		return cpiProjet;
	}
	public void setCpiProjet(String cpiProjet)
	{
		this.cpiProjet = cpiProjet;
	}
	public String getEdition()
	{
		return edition;
	}
	public void setEdition(String edition)
	{
		this.edition = edition;
	}
	public String getLot()
	{
		return lot;
	}
	public void setLot(String lot)
	{
		this.lot = lot;
	}
	public String getEnvironnement()
	{
		return environnement;
	}
	public void setEnvironnement(String environnement)
	{
		this.environnement = environnement;
	}
	public String getnumeroAnomalie()
	{
		return numeroAnomalie;
	}
	public void setnumeroAnomalie(String numeroAnomalie)
	{
		this.numeroAnomalie = numeroAnomalie;
	}
	public String getEtat()
	{
		return etat;
	}
	public void setEtat(String etat)
	{
		this.etat = etat;
	}
	public String getRemarque()
	{
		return remarque;
	}
	public void setRemarque(String remarque)
	{
		this.remarque = remarque;
	}
	
}