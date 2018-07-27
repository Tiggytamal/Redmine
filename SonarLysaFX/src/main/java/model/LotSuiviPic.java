package model;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import utilities.adapter.LocalDateAdapter;

/**
 * Classe représentant les informations du fichier Excel d'extraction de la Pic
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
public class LotSuiviPic implements Modele
{
    /*---------- ATTRIBUTS ----------*/

    private String lot;
    private String libelle;
    private String projetClarity;
    private String cpiProjet;
    private String edition;
    private int nbreComposants;
    private int nbrePaquets;
    private LocalDate build;
    private LocalDate devtu;
    private LocalDate tfon;
    private LocalDate vmoe;
    private LocalDate vmoa;
    private LocalDate livraison;

    /*---------- CONSTRUCTEURS ----------*/

    LotSuiviPic()
    {
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(required = false)
    public String getLot()
    {
        return getString(lot);
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    @XmlAttribute(required = false)
    public String getLibelle()
    {
        return getString(libelle);
    }

    public void setLibelle(String libelle)
    {
        this.libelle = libelle;
    }

    @XmlAttribute(required = false)
    public String getProjetClarity()
    {
        return getString(projetClarity);
    }

    public void setProjetClarity(String projetClarity)
    {
        this.projetClarity = projetClarity;
    }

    @XmlAttribute(required = false)
    public String getCpiProjet()
    {
        return getString(cpiProjet);
    }

    public void setCpiProjet(String cpiProjet)
    {
        this.cpiProjet = cpiProjet;
    }

    @XmlAttribute(required = false)
    public String getEdition()
    {
        return getString(edition);
    }

    public void setEdition(String edition)
    {
        this.edition = edition;
    }

    @XmlAttribute(required = false)
    public int getNbreComposants()
    {
        return nbreComposants;
    }

    public void setNbreComposants(int nbreComposants)
    {
        this.nbreComposants = nbreComposants;
    }

    @XmlAttribute(required = false)
    public int getNbrePaquets()
    {
        return nbrePaquets;
    }

    public void setNbrePaquets(int nbrePaquets)
    {
        this.nbrePaquets = nbrePaquets;
    }

    @XmlAttribute(required = false)
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getBuild()
    {
        return build;
    }

    public void setBuild(LocalDate build)
    {
        this.build = build;
    }

    @XmlAttribute(required = false)
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDevtu()
    {
        return devtu;
    }

    public void setDevtu(LocalDate devtu)
    {
        this.devtu = devtu;
    }

    @XmlAttribute(required = false)
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getTfon()
    {
        return tfon;
    }

    public void setTfon(LocalDate tfon)
    {
        this.tfon = tfon;
    }

    @XmlAttribute(required = false)
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getVmoe()
    {
        return vmoe;
    }

    public void setVmoe(LocalDate vmoe)
    {
        this.vmoe = vmoe;
    }

    @XmlAttribute(required = false)
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getVmoa()
    {
        return vmoa;
    }

    public void setVmoa(LocalDate vmoa)
    {
        this.vmoa = vmoa;
    }

    @XmlAttribute(required = false)
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getLivraison()
    {
        return livraison;
    }

    public void setLivraison(LocalDate livraison)
    {
        this.livraison = livraison;
    }
}
