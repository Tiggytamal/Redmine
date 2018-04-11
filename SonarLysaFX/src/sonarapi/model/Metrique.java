package sonarapi.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

import model.enums.TypeMetrique;

@XmlRootElement
public class Metrique
{
	private TypeMetrique type;
	private String valeur;
	private List<Periode> listePeriodes;

	@XmlAttribute(name = "metric")
	public TypeMetrique getMetric()
	{
		return type;
	}

	public void setMetric(TypeMetrique type)
	{
		this.type = type;
	}

	@XmlAttribute(name = "value")
	public String getValue()
	{
		return valeur;
	}

	public void setValue(String value)
	{
		this.valeur = value;
	}

	@XmlElementWrapper
	@XmlElement(name = "periods")
	@JsonIgnore
	public List<Periode> getListePeriodes()
	{
		if (listePeriodes == null)
			return new ArrayList<>();
		return listePeriodes;
	}

	public void setListePeriodes(List<Periode> periods)
	{
		this.listePeriodes = periods;
	}
}