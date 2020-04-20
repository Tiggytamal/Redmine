package model.rest.maps;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import model.AbstractModele;

/**
 * Classe de modele représentant un produit depuis le webservice MAPS.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
@JsonRootName(value = "produit")
@JsonIgnoreProperties(
{ "P_Statut_Code", "P_Statut", "P_Version", "createdAt", "P_PP_EDS", "P_TR_EDS", "P_Tour_Applic_Id", "P_Tour_Applic_Libelle", "P_Type", "Squad", "Squad_Count" })
public class Produit extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private String statut;
    private String code;
    private String libelle;
    private String idPo;
    private String nomPo;
    private String codePP;
    private String libellePP;
    private String codeTR;
    private String libelleTR;
    private String idPU;
    private String libellePU;
    private String contactPP;
    private String description;
    private int nbreSolutions;
    private List<String> solutions;

    /*---------- CONSTRUCTEURS ----------*/
    
    public Produit()
    {
        solutions = new ArrayList<>();
    }
    
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

    @JsonProperty(value = "P_Code")
    public String getCode()
    {
        return getString(code);
    }

    public void setCode(String code)
    {
        this.code = getString(code);
    }

    @JsonProperty(value = "P_Libelle")
    public String getLibelle()
    {
        return getString(libelle);
    }

    public void setLibelle(String libelle)
    {
        this.libelle = getString(libelle);
    }

    @JsonProperty(value = "P_PO_Id")
    public String getIdPo()
    {
        return getString(idPo);
    }

    public void setIdPo(String idPo)
    {
        this.idPo = getString(idPo);
    }

    @JsonProperty(value = "P_PO_Nom")
    public String getNomPo()
    {
        return getString(nomPo);
    }

    public void setNomPo(String nomPo)
    {
        this.nomPo = getString(nomPo);
    }

    @JsonProperty(value = "P_PP_Code")
    public String getCodePP()
    {
        return getString(codePP);
    }

    public void setCodePP(String codePP)
    {
        this.codePP = getString(codePP);
    }

    @JsonProperty(value = "P_PP_Libelle")
    public String getLibellePP()
    {
        return getString(libellePP);
    }

    public void setLibellePP(String libellePP)
    {
        this.libellePP = getString(libellePP);
    }

    @JsonProperty(value = "P_TR_Code")
    public String getCodeTR()
    {
        return getString(codeTR);
    }

    public void setCodeTR(String codeTR)
    {
        this.codeTR = getString(codeTR);
    }

    @JsonProperty(value = "P_TR_Libelle")
    public String getLibelleTR()
    {
        return getString(libelleTR);
    }

    public void setLibelleTR(String libelleTR)
    {
        this.libelleTR = getString(libelleTR);
    }

    @JsonProperty(value = "P_PU_Id")
    public String getIdPU()
    {
        return getString(idPU);
    }

    public void setIdPU(String idPU)
    {
        this.idPU = getString(idPU);
    }

    @JsonProperty(value = "P_PU_Libelle")
    public String getLibellePU()
    {
        return getString(libellePU);
    }

    public void setLibellePU(String libellePU)
    {
        this.libellePU = getString(libellePU);
    }

    @JsonProperty(value = "P_PP_Contact")
    public String getContactPP()
    {
        return getString(contactPP);
    }

    public void setContactPP(String contactPP)
    {
        this.contactPP = getString(contactPP);
    }

    @JsonProperty(value = "P_Description")
    public String getDescription()
    {
        return getString(description);
    }

    public void setDescription(String description)
    {
        this.description = getString(description);
    }

    @JsonProperty(value = "Solution_Count")
    public int getNbreSolutions()
    {
        return nbreSolutions;
    }

    public void setNbreSolutions(int nbreSolutions)
    {
        this.nbreSolutions = nbreSolutions;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "Solutions", required = false)
    public List<String> getSolutions()
    {
        if (solutions == null)
            solutions = new ArrayList<>();
        return solutions;
    }

    public void setSolutions(List<String> solutions)
    {
        if (solutions != null && !solutions.isEmpty())
            this.solutions = solutions;
    }
}
