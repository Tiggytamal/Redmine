package model.rest.sonarapi;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe de modele représentant la periode d'une anomalie en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Periode extends AbstractModele implements ModeleSonar, Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    
    private int index;
    private String valeur;
    private boolean bestValue;

    /*---------- CONSTRUCTEURS ----------*/

    public Periode(int index, String valeur)
    {
        this.index = index;
        this.valeur = valeur;
        bestValue = true;
    }

    Periode()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "index")
    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    @XmlAttribute(name = "value")
    public String getValeur()
    {
        return getString(valeur);
    }

    public void setValeur(String value)
    {
        this.valeur = getString(value);
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
