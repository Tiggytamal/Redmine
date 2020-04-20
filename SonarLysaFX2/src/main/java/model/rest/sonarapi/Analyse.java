package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe de modele pour les analyses de composants remontées depuis SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class Analyse extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String date;
    private List<Event> events;

    /*---------- CONSTRUCTEURS ----------*/

    public Analyse()
    {
        events = new ArrayList<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "key")
    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = getString(key);
    }

    @XmlAttribute(name = "date")
    public String getDate()
    {
        return getString(date);
    }

    public void setDate(String date)
    {
        this.date = getString(date);
    }

    @XmlElementWrapper
    @XmlAttribute(name = "events")
    public List<Event> getEvents()
    {
        if (events == null)
            events = new ArrayList<>();
        return events;
    }

    public void setEvents(List<Event> events)
    {
        if (events != null && !events.isEmpty())
            this.events = events;
    }
}
