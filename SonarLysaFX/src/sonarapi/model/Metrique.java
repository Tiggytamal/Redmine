package sonarapi.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import model.enums.TypeMetrique;

/**
 * Représente les types de métrique utilisés dans Sonar
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
public class Metrique
{
    /*---------- ATTRIBUTS ----------*/

	private TypeMetrique type;
	private String valeur;
	private List<Periode> listePeriodes;

    /*---------- CONSTRUCTEURS ----------*/
	
	public Metrique()
	{
	    listePeriodes = new ArrayList<>();
	}
	
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
	
	@XmlAttribute(name = "metric")
	@XmlJavaTypeAdapter(value = TypeMetriqueAdapter.class)
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
	
    private static class TypeMetriqueAdapter extends XmlAdapter<String, TypeMetrique>
    {
        public TypeMetrique unmarshal(String v) throws Exception
        {
            if (v != null)
                return TypeMetrique.from(v);
            return null;
        }

        public String marshal(TypeMetrique v) throws Exception
        {
            if (v != null)
                return v.toString();
            return null;
        }
    }
}