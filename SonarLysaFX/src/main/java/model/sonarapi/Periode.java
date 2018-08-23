package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

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
        // Constructeur vide pour initialiser des objets sans param�tre et la cr�ation depuis le XML
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
