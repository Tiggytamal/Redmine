package model.rest.repack;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe modele pour remonter les informations d'un composants après appel du webservice repack.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class ComposantRepack extends AbstractModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    private String id;
    private String idRtc;
    private String nom;
    private String version;
    private String idBaseLineRtc;
    private String baseline;
    private String versionPic;
    private String dateCreation;

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantRepack()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "id")
    public String getId()
    {
        return getString(id);
    }

    public void setId(String id)
    {
        this.id = getString(id);
    }

    @XmlAttribute(name = "idRtc")
    public String getIdRtc()
    {
        return getString(idRtc);
    }

    public void setIdRtc(String idRtc)
    {
        this.idRtc = getString(idRtc);
    }

    @XmlAttribute(name = "nom")
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = getString(nom);
    }

    @XmlAttribute(name = "version")
    public String getVersion()
    {
        return getString(version);
    }

    public void setVersion(String version)
    {
        this.version = getString(version);
    }

    @XmlAttribute(name = "idBaselineRtc")
    public String getIdBaseLineRtc()
    {
        return getString(idBaseLineRtc);
    }

    public void setIdBaseLineRtc(String idBaselineRtc)
    {
        this.idBaseLineRtc = getString(idBaselineRtc);
    }

    @XmlAttribute(name = "baseline")
    public String getBaseline()
    {
        return getString(baseline);
    }

    public void setBaseline(String baseline)
    {
        this.baseline = getString(baseline);
    }

    @XmlAttribute(name = "versionPic")
    public String getVersionPic()
    {
        return getString(versionPic);
    }

    public void setVersionPic(String versionPic)
    {
        this.versionPic = getString(versionPic);
    }

    @XmlAttribute(name = "dateCreation")
    public String getDateCreation()
    {
        return getString(dateCreation);
    }

    public void setDateCreation(String dateCreation)
    {
        this.dateCreation = getString(dateCreation);
    }
}
