package model.rest.maps;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import model.AbstractModele;

/**
 * Classe de modele représentant une fonctionnalité depuis le webservice MAPS.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
@JsonRootName(value = "Fonctionnalite")
public class Fonctionnalite extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private String statut;
    private String code;
    private String libelle;
    private String description;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @JsonProperty(value = "status")
    public String getStatut()
    {
        return getString(statut);
    }

    public void setStatut(String statut)
    {
        this.statut = getString(statut);
    }

    @JsonProperty(value = "F_Code")
    public String getCode()
    {
        return getString(code);
    }

    public void setCode(String code)
    {
        this.code = getString(code);
    }

    @JsonProperty(value = "F_Libelle")
    public String getLibelle()
    {
        return getString(libelle);
    }

    public void setLibelle(String libelle)
    {
        this.libelle = getString(libelle);
    }

    @JsonProperty(value = "F_Description")
    public String getDescription()
    {
        return getString(description);
    }

    public void setDescription(String description)
    {
        this.description = getString(description);
    }
}
