package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;

/**
 * Classe de mod�le des colonnes avec indice pour les fichier Excel en �criture
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class Colonne extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private String nom;
    private String indice;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "nom", required = true)
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    @XmlAttribute(name = "indice", required = true)
    public String getIndice()
    {
        return getString(indice);
    }

    public void setIndice(String indice)
    {
        this.indice = indice;
    }

}
