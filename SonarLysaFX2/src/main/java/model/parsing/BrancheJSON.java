package model.parsing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import model.AbstractModele;
import model.enums.TypeBranche;
import utilities.Statics;

/**
 * * Classe de modele d'une branche crée à partir du fichier JSOM d'extraction de SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
@JsonRootName(value = "branche")
public class BrancheJSON extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private String nom;
    private TypeBranche type;
    private boolean principal;

    /*---------- CONSTRUCTEURS ----------*/

    public BrancheJSON()
    {
        nom = Statics.EMPTY;
        type = TypeBranche.LONG;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @JsonProperty(value = "typeB")
    public TypeBranche getType()
    {
        if (type == null)
            type = TypeBranche.LONG;
        return type;
    }

    public void setType(TypeBranche type)
    {
        if (type != null)
            this.type = type;
    }

    @JsonProperty(value = "nom")
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

    @JsonProperty(value = "main")
    public boolean isPrincipal()
    {
        return principal;
    }

    public void setPrincipal(boolean principal)
    {
        this.principal = principal;
    }
}
