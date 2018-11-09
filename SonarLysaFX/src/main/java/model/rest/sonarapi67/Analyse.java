package model.rest.sonarapi67;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle pour les analyses de composants remontées depuis SonarQube
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
    
    public Analyse(String key, String date, List<Event> events)
    {
        super();
        this.key = key;
        this.date = date;
        this.events = events;
    }
    
    public Analyse()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
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
        this.key = key;
    }

    @XmlAttribute(name = "date")
    public String getDate()
    {
        return getString(date);
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "events")
    public List<Event> getEvent()
    {
        if (events == null)
            events = new ArrayList<>();
        return events;
    }

    public void setEvent(List<Event> events)
    {
        this.events = events;
    }
}
