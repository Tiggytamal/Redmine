package model.sonarapi;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Vue implements ModeleSonar
{
	/*---------- ATTRIBUTS ----------*/

	private String key;
	private String name;
	private boolean selected;
	private String selectionMode;
	private String description;
	private List<String> listeClefsComposants; 

    /*---------- CONSTRUCTEURS ----------*/
	
	public Vue()
	{
	    
	}
	
	public Vue (String key, String nom)
	{
	    this.key = key;
	    this.name = nom;
	}
	
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

	@XmlAttribute (name = "key")
	public String getKey() 
	{
		return key;
	}

	public void setKey(String key) 
	{
		this.key = key;
	}

	@XmlAttribute (name = "name")
	public String getName() 
	{
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "View [key=" + key + ", name=" + name + "]";
	}

	@XmlAttribute (name = "selected", required = false)
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@XmlAttribute (name = "selectionMode", required = false)
	public String getSelectionMode() {
		return selectionMode;
	}

	public void setSelectionMode(String selectionMode) {
		this.selectionMode = selectionMode;
	}

	@XmlAttribute (name = "description", required = false)
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public List<String> getListeClefsComposants()
	{
		return listeClefsComposants;
	}

	public void setListeClefsComposants(List<String> listeClefsComposants)
	{
		this.listeClefsComposants = listeClefsComposants;
	}
}