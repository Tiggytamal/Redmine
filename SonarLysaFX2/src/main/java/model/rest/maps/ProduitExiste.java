package model.rest.maps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import model.AbstractModele;

/**
 * Classe de modele représentant une fonctionnalité depuis le webservice MAPS.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
@JsonRootName(value = "ProduitExiste")
@JsonIgnoreProperties(
{ "P_Statut", "P_Type"})
public class ProduitExiste extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private String statut;
    private String code;
    private boolean existe;

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

    @JsonProperty(value = "code-produit")
    public String getCode()
    {
        return getString(code);
    }

    public void setCode(String code)
    {
        this.code = getString(code);
    }

    @JsonProperty(value = "produit-exist")
    public boolean existe()
    {
        return existe;
    }

    public void setExiste(boolean existe)
    {
        this.existe = existe;
    }
}
