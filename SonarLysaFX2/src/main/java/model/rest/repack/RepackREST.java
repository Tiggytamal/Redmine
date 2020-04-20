package model.rest.repack;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;
import utilities.Statics;

/**
 * Classe modele pour remonter les informations d'un repacks après appel du webservice PIC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class RepackREST extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    // Regex
    private static final Pattern PATTERNDATE = Pattern.compile("^\\d{13}$");
    
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
    private String nomGc;

    /*---------- CONSTRUCTEURS ----------*/

    public RepackREST()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    /**
     * Calcul de la date de Repack depuis la chaîne de caratères enregistrée.
     * 
     * @param dateCreation
     *                     Date à tranformer.
     * @return
     *         Date mise en forme.
     */
    public LocalDate calculDateRepack()
    {
        if (dateCreation == null || dateCreation.isEmpty() || !PATTERNDATE.matcher(dateCreation).matches())
            return Statics.DATEINCONNUE;
        return Instant.ofEpochMilli(Long.parseLong(dateCreation)).atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "idCompHpR")
    public String getIdCompHpR()
    {
        return getString(idCompHpR);
    }

    public void setIdCompHpR(String idCompHpR)
    {
        this.idCompHpR = getString(idCompHpR);
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

    @XmlAttribute(name = "nomComposantMarimba")
    public String getNomComposantMarimba()
    {
        return getString(nomComposantMarimba);
    }

    public void setNomComposantMarimba(String nomComposantMarimba)
    {
        this.nomComposantMarimba = getString(nomComposantMarimba);
    }

    @XmlAttribute(name = "idVCompHpR")
    public String getIdVCompHpR()
    {
        return getString(idVCompHpR);
    }

    public void setIdVCompHpR(String idVCompHpR)
    {
        this.idVCompHpR = getString(idVCompHpR);
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

    @XmlAttribute(name = "dateCreation")
    public String getDateCreation()
    {
        return getString(dateCreation);
    }

    public void setDateCreation(String dateCreation)
    {
        this.dateCreation = getString(dateCreation);
    }

    @XmlAttribute(name = "idEdition")
    public String getIdEdition()
    {
        return getString(idEdition);
    }

    public void setIdEdition(String idEdition)
    {
        this.idEdition = getString(idEdition);
    }

    @XmlAttribute(name = "idNgm")
    public String getIdNgm()
    {
        return getString(idNgm);
    }

    public void setIdNgm(String idNgm)
    {
        this.idNgm = getString(idNgm);
    }

    @XmlAttribute(name = "libelle")
    public String getLibelle()
    {
        return getString(libelle);
    }

    public void setLibelle(String libelle)
    {
        this.libelle = getString(libelle);
    }

    @XmlAttribute(name = "idGc")
    public String getIdGc()
    {
        return getString(idGc);
    }

    public void setIdGc(String idGc)
    {
        this.idGc = getString(idGc);
    }

    @XmlAttribute(name = "nomGc")
    public String getNomGc()
    {
        return getString(nomGc);
    }

    public void setNomGc(String nomGC)
    {
        this.nomGc = getString(nomGC);
    }
}
