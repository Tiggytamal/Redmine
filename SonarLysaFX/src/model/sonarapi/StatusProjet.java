package model.sonarapi;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StatusProjet implements ModeleSonar
{	
	/*---------- ATTRIBUTS ----------*/

	private String status;
	private List<Condition> conditions;
	private List<StatusPeriode> periodes;
	
	/*---------- ACCESSEURS ----------*/

	@XmlAttribute (name = "status")
	public String getStatus()
	{
		return status;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	@XmlAttribute (name = "conditions")
	public List<Condition> getConditions()
	{
		return conditions;
	}
	
	public void setConditions(List<Condition> conditions)
	{
		this.conditions = conditions;
	}	
	
	@XmlAttribute (name = "periods")
	public List<StatusPeriode> getPeriodes()
	{
		return periodes;
	}

	public void setPeriodes(List<StatusPeriode> periodes)
	{
		this.periodes = periodes;
	}
}