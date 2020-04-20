package model.rest.sonarapi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import model.AbstractModele;
import model.enums.Metrique;
import utilities.Statics;
import utilities.adapter.TypeMetriqueAdapter;

/**
 * Represente les types de metrique utilisés dans Sonar
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
public class Mesure extends AbstractModele implements ModeleSonar, Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    
    private Metrique type;
    private String valeur;
    private List<Periode> listePeriodes;
    private boolean bestValue;

    /*---------- CONSTRUCTEURS ----------*/

    public Mesure(Metrique type)
    {
        this(type, Statics.EMPTY);
    }

    public Mesure(Metrique type, String valeur)
    {
        if (type == null)
            throw new IllegalArgumentException("Création model.sonarapi.Metrique : TypeMetrique = null");

        this.type = type;
        this.valeur = valeur;
        listePeriodes = new ArrayList<>();
        bestValue = true;
    }

    Mesure()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et pour le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "metric")
    @XmlJavaTypeAdapter(value = TypeMetriqueAdapter.class)
    public Metrique getType()
    {
        return type;
    }

    public void setType(Metrique type)
    {
        if (type != null)
            this.type = type;
    }

    @XmlAttribute(name = "value")
    public String getValeur()
    {
        return getString(valeur);
    }

    public void setValeur(String valeur)
    {
        this.valeur = getString(valeur);
    }

    @XmlElementWrapper
    @XmlElement(name = "periods", required = false)
    public List<Periode> getListePeriodes()
    {
        if (listePeriodes == null)
            listePeriodes = new ArrayList<>();
        return listePeriodes;
    }

    public void setListePeriodes(List<Periode> listePeriodes)
    {
        if (listePeriodes != null && !listePeriodes.isEmpty())
            this.listePeriodes = listePeriodes;
    }

    @XmlAttribute(name = "bestValue")
    public boolean isBestValue()
    {
        return bestValue;
    }

    public void setBestValue(boolean bestValue)
    {
        this.bestValue = bestValue;
    }
}
