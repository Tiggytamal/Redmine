package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle représentant la période d'une anomalie en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Periode extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private int index;
    private String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    public Periode(int index, String valeur)
    {
        this.index = index;
        this.valeur = valeur;
    }

    public Periode()
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
        this.valeur = value;
    }
}
