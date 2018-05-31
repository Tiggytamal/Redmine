package model.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Flow
{
    /*---------- ATTRIBUTS ----------*/

    private List<Location> locations;

    /*---------- CONSTRUCTEURS ----------*/

    public Flow(List<Location> locations)
    {
        if (locations == null || locations.isEmpty())
            throw new IllegalArgumentException("Création model.sonarapi.Flow - locations nulle ou vide.");
        
        this.locations = locations;
    }
    
    public Flow()
    {
        locations = new ArrayList<>();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "locations", required = false)
    public List<Location> getLocations()
    {
        return locations;
    }

    public void setLocations(List<Location> locations)
    {
        this.locations = locations;
    }
}