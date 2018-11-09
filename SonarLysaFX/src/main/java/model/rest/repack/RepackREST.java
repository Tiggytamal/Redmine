package model.rest.repack;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;

/**
 * Classe modèle pour remonter les informations d'un repacks après appel du webservice PIC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class RepackREST extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private String idCompHpR;
    private String nom;
    private String nomComposantMarimba;
    private String idVCompHpR;
    private String version;
    private String dateCreation;
    private String idEdition;
    private String idNgm;
    private String libelle;
    private String idGc;
    private String nomGC;

    /*---------- CONSTRUCTEURS ----------*/

    public RepackREST(String idCompHpR, String nom, String nomComposantMarimba, String idVCompHpR, String version, String dateCreation, String idEdition, String idNgm, String libelle, String idGc,
            String nomGC)
    {
        super();
        this.idCompHpR = idCompHpR;
        this.nom = nom;
        this.nomComposantMarimba = nomComposantMarimba;
        this.idVCompHpR = idVCompHpR;
        this.version = version;
        this.dateCreation = dateCreation;
        this.idEdition = idEdition;
        this.idNgm = idNgm;
        this.libelle = libelle;
        this.idGc = idGc;
        this.nomGC = nomGC;
    }

    public RepackREST()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "idCompHpR")
    public String getIdCompHpR()
    {
        return idCompHpR;
    }

    public void setIdCompHpR(String idCompHpR)
    {
        this.idCompHpR = idCompHpR;
    }

    @XmlAttribute(name = "nom")
    public String getNom()
    {
        return nom;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    @XmlAttribute(name = "nomComposantMarimba")
    public String getNomComposantMarimba()
    {
        return nomComposantMarimba;
    }

    public void setNomComposantMarimba(String nomComposantMarimba)
    {
        this.nomComposantMarimba = nomComposantMarimba;
    }

    @XmlAttribute(name = "idVCompHpR")
    public String getIdVCompHpR()
    {
        return idVCompHpR;
    }

    public void setIdVCompHpR(String idVCompHpR)
    {
        this.idVCompHpR = idVCompHpR;
    }

    @XmlAttribute(name = "version")
    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    @XmlAttribute(name = "dateCreation")
    public String getDateCreation()
    {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation)
    {
        this.dateCreation = dateCreation;
    }

    @XmlAttribute(name = "idEdition")
    public String getIdEdition()
    {
        return idEdition;
    }

    public void setIdEdition(String idEdition)
    {
        this.idEdition = idEdition;
    }

    @XmlAttribute(name = "idNgm")
    public String getIdNgm()
    {
        return idNgm;
    }

    public void setIdNgm(String idNgm)
    {
        this.idNgm = idNgm;
    }

    @XmlAttribute(name = "libelle")
    public String getLibelle()
    {
        return libelle;
    }

    public void setLibelle(String libelle)
    {
        this.libelle = libelle;
    }

    @XmlAttribute(name = "idGc")
    public String getIdGc()
    {
        return idGc;
    }

    public void setIdGc(String idGc)
    {
        this.idGc = idGc;
    }

    @XmlAttribute(name = "nomGC")
    public String getNomGC()
    {
        return nomGC;
    }

    public void setNomGC(String nomGC)
    {
        this.nomGC = nomGC;
    }
}
