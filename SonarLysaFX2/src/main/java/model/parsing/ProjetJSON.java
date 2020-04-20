package model.parsing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import model.AbstractModele;
import utilities.Statics;

/**
 * Classe de modele d'un projet crée à partir du fichier JSOM d'extraction de SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
@JsonRootName(value = "projet")
public class ProjetJSON extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String nom;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @JsonProperty(value = "key")
    public String getKey()
    {
        if (key == null)
            key = Statics.EMPTY;
        return key;
    }

    public void setKey(String key)
    {
        if (key != null && !key.isEmpty())
            this.key = key;
    }

    @JsonProperty(value = "name")
    public String getNom()
    {
        if (nom == null)
            nom = Statics.EMPTY;
        return nom;
    }

    public void setNom(String nom)
    {
        if (nom != null && !nom.isEmpty())
            this.nom = nom;
    }
}
