package model.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import model.enums.TypeMetrique;
import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;
import utilities.adapter.TypeMetriqueAdapter;

/**
 * Représente les types de métrique utilisés dans Sonar
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
public class Metrique extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private TypeMetrique type;
    private String valeur;
    private List<Periode> listePeriodes;

    /*---------- CONSTRUCTEURS ----------*/

    public Metrique(TypeMetrique type)
    {
        this(type, null);
    }

    public Metrique(TypeMetrique type, String valeur)
    {
        if (type == null)
            throw new IllegalArgumentException("Création model.sonarapi.Metrique : TypeMetrique = null");

        this.type = type;
        this.valeur = valeur;
        listePeriodes = new ArrayList<>();
    }

    public Metrique()
    {
        // Constructeur vide pour initialiser des objets sans paramètre
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

    public void setMetric(TypeMetrique metric)
    {
        type = metric;
    }

    @XmlAttribute(name = "value")
    public String getValue()
    {
        return getString(valeur);
    }

    public void setValue(String value)
    {
        valeur = value;
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
        this.listePeriodes = listePeriodes;
    }
}
