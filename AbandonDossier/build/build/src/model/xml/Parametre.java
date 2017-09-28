package model.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe d'import/export des param�tres au format XML
 * 
 * @author Gregoire Mathon
 *
 */
@XmlRootElement
public class Parametre implements Serializable
{
    /* ---------- ATTIBUTES ---------- */
	
	private static final long serialVersionUID = 1L;
	
	/** Param�tres */
	
	private String url;
	
	private List<BanquesXML> listBanques;
	
    /* ---------- CONSTUCTORS ---------- */
	
    /**
	 * Constructeur par default
	 */
	public Parametre()
	{
		listBanques = new ArrayList<>();
	}

    /* ---------- METHODS ---------- */

    /* ---------- ACCESS ---------- */
	
	/**
     * @return the listAppsXML
     */
	@XmlElementWrapper
	@XmlElement (name = "BaqnuesXML")
    public List<BanquesXML> getListBanques()
    {
        return listBanques;
    }

    /**
     * @param listAppsXML the listAppsXML to set
     */
    public void setListBanques(List<BanquesXML> listAppsXML)
    {
        this.listBanques = listAppsXML;
    }

	
    @XmlAttribute (required = true)
	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
}