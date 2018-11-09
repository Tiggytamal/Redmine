package model.rest.sonarapi;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle représentant un projet en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Projet extends AbstractModele implements ModeleSonar, Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private String id;
    private String key;
    private String nom;
    private String sc;
    private String qu;
    private String lot;

    /*---------- CONSTRUCTEURS ----------*/

    public Projet(String id, String key, String nom, String sc, String qu, String lot)
    {
        super();
        this.id = id;
        this.key = key;
        this.nom = nom;
        this.sc = sc;
        this.qu = qu;
        this.lot = lot;
    }

    public Projet()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String toString()
    {
        return "Projet [nom=" + nom + "]";
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "k", required = true)
    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @XmlAttribute(name = "id")
    public String getId()
    {
        return getString(id);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @XmlAttribute(name = "nm")
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    @XmlAttribute(name = "sc")
    public String getSc()
    {
        return getString(sc);
    }

    public void setSc(String sc)
    {
        this.sc = sc;
    }

    @XmlAttribute(name = "qu")
    public String getQu()
    {
        return getString(qu);
    }

    public void setQu(String qu)
    {
        this.qu = qu;
    }

    public String getLot()
    {
        return getString(lot);
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }
}
