package model.rest.repack;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;

/**
 * Classe modèle pour remonter les informations d'un composants après appel du webservice repack.
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

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantRepack(String id, String idRtc, String nom, String version, String idBaseLineRtc, String baseline, String versionPic)
    {
        super();
        this.id = id;
        this.idRtc = idRtc;
        this.nom = nom;
        this.version = version;
        this.idBaseLineRtc = idBaseLineRtc;
        this.baseline = baseline;
        this.versionPic = versionPic;
    }

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
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @XmlAttribute(name = "idRtc")
    public String getIdRtc()
    {
        return idRtc;
    }

    public void setIdRtc(String idRtc)
    {
        this.idRtc = idRtc;
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

    @XmlAttribute(name = "version")
    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    @XmlAttribute(name = "idBaseLineRtc")
    public String getIdBaseLineRtc()
    {
        return idBaseLineRtc;
    }

    public void setIdBaseLineRtc(String idBaseLineRtc)
    {
        this.idBaseLineRtc = idBaseLineRtc;
    }

    @XmlAttribute(name = "baseline")
    public String getBaseline()
    {
        return baseline;
    }

    public void setBaseline(String baseline)
    {
        this.baseline = baseline;
    }

    @XmlAttribute(name = "versionPic")
    public String getVersionPic()
    {
        return versionPic;
    }

    public void setVersionPic(String versionPic)
    {
        this.versionPic = versionPic;
    }
}
