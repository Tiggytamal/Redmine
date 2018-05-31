package model;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import model.enums.EtatLot;
import utilities.adapter.LocalDateAdapter;

/**
 * Classe répresentant l'extraction d'un lot depuis RTC
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
public class LotSuiviRTC implements Modele
{
    /*---------- ATTRIBUTS ----------*/

    private String lot;
    private String libelle;
    private String projetClarity;
    private String cpiProjet;
    private String edition;
    private EtatLot etatLot;
    private String projetRTC;
    private LocalDate dateMajEtat;

    /*---------- CONSTRUCTEURS ----------*/

    LotSuiviRTC() {}

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(required = false)
    public String getLot()
    {
        return lot;
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    @XmlAttribute(required = false)
    public String getLibelle()
    {
        return libelle;
    }

    public void setLibelle(String libelle)
    {
        this.libelle = libelle;
    }

    @XmlAttribute(required = false)
    public String getProjetClarity()
    {
        return projetClarity;
    }

    public void setProjetClarity(String projetClarity)
    {
        this.projetClarity = projetClarity;
    }

    @XmlAttribute(required = false)
    public String getCpiProjet()
    {
        return cpiProjet;
    }

    public void setCpiProjet(String cpiProjet)
    {
        this.cpiProjet = cpiProjet;
    }

    @XmlAttribute(required = false)
    public String getEdition()
    {
        return edition;
    }

    public void setEdition(String edition)
    {
        this.edition = edition;
    }

    @XmlAttribute(required = false)
    public EtatLot getEtatLot()
    {
        return etatLot;
    }

    public void setEtatLot(EtatLot etatLot)
    {
        this.etatLot = etatLot;
    }

    @XmlAttribute(required = false)
    public String getProjetRTC()
    {
        return projetRTC;
    }

    public void setProjetRTC(String projetRTC)
    {
        this.projetRTC = projetRTC;
    }
    
    @XmlAttribute(required = false)
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateMajEtat()
    {
        return dateMajEtat;
    }

    public void setDateMajEtat(LocalDate dateMajEtat)
    {
        this.dateMajEtat = dateMajEtat;
    }
}