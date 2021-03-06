package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de mod�le repr�sentant un flux en retour du webservice Sonar.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Flow extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private List<Location> locations;

    /*---------- CONSTRUCTEURS ----------*/

    public Flow(List<Location> locations)
    {
        if (locations == null || locations.isEmpty())
            throw new IllegalArgumentException("Cr�ation model.sonarapi.Flow - locations nulle ou vide.");

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
        if (locations == null)
            locations = new ArrayList<>();
        return locations;
    }

    public void setLocations(List<Location> locations)
    {
        this.locations = locations;
    }
}
