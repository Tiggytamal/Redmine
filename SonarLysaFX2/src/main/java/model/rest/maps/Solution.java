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
 * Classe de modele représentant une solution depuis le webservice MAPS.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
@JsonRootName(value = "solution")
@JsonIgnoreProperties(
{ "P_Libelle", "P_Type", "P_Etat", "P_Statut", "P_PO_Nom", "P_PP_Libelle", "P_PP_Code", "P_PP_EDS", "P_TR_Libelle", "P_TR_Code", "P_TR_EDS", "Squad", "Squad_Count", "P_PU_Libelle", "P_PP_Contact",
        "P_Tour_Applic_Libelle", "P_Tour_Applic_Id", "P_Version", "S_Etat", "S_Type", "S_Description", "S_Version_Prod", "S_MEP_Dernier", "S_Hebergee_CAGIP", "S_Com_Hebergee_Non", "SE_Nom",
        "SE_Groupe_CA", "SE_Editeur", "SE_Prog_Perso_CA", "SE_Date_Fin_Support", "SE_Degre_Customisation", "SE_Version_Commercialisee", "SE_Version_Last_Maintenue", "S_Lang_Prog_Main",
        "S_Matiere_Mainframe", "S_ARM", "S_ARM_Date", "S_DICP_DI", "S_DICP_DI_Libelle", "S_DICP_IN", "S_DICP_IN_Libelle", "S_DICP_CO", "S_DICP_CO_Libelle", "S_DICP_PR", "S_DICP_PR_Libelle", "S_DIMA",
        "S_DIMA_Libelle", "S_PDMA", "S_PDMA_Libelle", "S_Internet", "S_Criticite", "S_MEP_Premier", "S_NB_MEP", "S_NB_Incident", "S_Documentation", "S_Documentation_Libelle", "S_Qualite_Code",
        "S_Qualite_Code_Libelle", "Instances_Deploiement_Count" })
public class Solution extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private String statut;
    private String code;
    private String codeProduit;
    private String libelle;
    private String statutS;
    private List<String> instancesDeploiment;

    /*---------- CONSTRUCTEURS ----------*/

    public Solution()
    {
        instancesDeploiment = new ArrayList<>();
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

    @JsonProperty(value = "S_Code")
    public String getCode()
    {
        return getString(code);
    }

    public void setCode(String code)
    {
        this.code = getString(code);
    }

    @JsonProperty(value = "P_Code")
    public String getCodeProduit()
    {
        return getString(codeProduit);
    }

    public void setCodeProduit(String codeProduit)
    {
        this.codeProduit = getString(codeProduit);
    }

    @JsonProperty(value = "S_Libelle")
    public String getLibelle()
    {
        return getString(libelle);
    }

    public void setLibelle(String libelle)
    {
        this.libelle = getString(libelle);
    }

    @JsonProperty(value = "S_Statut")
    public String getStatutS()
    {
        return getString(statutS);
    }

    public void setStatutS(String statutS)
    {
        this.statutS = getString(statutS);
    }

    @XmlElementWrapper
    @XmlAttribute(name = "Instances_Deploiement", required = false)
    public List<String> getInstancesDeploiment()
    {
        if (instancesDeploiment == null)
            instancesDeploiment = new ArrayList<>();
        return instancesDeploiment;
    }

    public void setInstancesDeploiment(List<String> instancesDeploiment)
    {
        if (instancesDeploiment != null && !instancesDeploiment.isEmpty())
            this.instancesDeploiment = instancesDeploiment;
    }
}
