package model.bdd;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dao.AbstractDao;
import model.enums.EtatDefaut;
import utilities.Statics;

/**
 * Classe de modèle qui correspond aux défaults des codes applications
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
@Entity
@Table(name = "defauts_application")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="DefautAppli" + AbstractDao.FINDALL, query="SELECT da FROM DefautAppli da "
                + "JOIN FETCH da.compo c "),
        @NamedQuery(name="DefautAppli" + AbstractDao.FINDINDEX, query="SELECT da FROM DefautAppli da WHERE da.compo.nom = :index"),
        @NamedQuery(name="DefautAppli" + AbstractDao.RESET, query="DELETE FROM DefautQualite")
})
//@formatter:on
@XmlRootElement
public class DefautAppli extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @OneToOne(targetEntity = ComposantSonar.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "composant")
    private ComposantSonar compo;

    @Column(name = "appli_corrigee", nullable = true, length = 4)
    private String appliCorrigee;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_defaut", nullable = false)
    private EtatDefaut etatDefaut;

    @Transient
    private String nomComposant;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = DefautQualite.class)
    @JoinColumn(name = "defaut_qualite")
    private DefautQualite defautQualite;

    /*---------- CONSTRUCTEURS ----------*/

    DefautAppli()
    {
        etatDefaut = EtatDefaut.NOUVEAU;
        appliCorrigee = Statics.EMPTY;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    @XmlTransient
    public String getMapIndex()
    {
        if (compo != null)
            return compo.getNom();

        return getNomComposant();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlTransient
    public ComposantSonar getCompo()
    {
        return compo;
    }

    public void setCompo(ComposantSonar compo)
    {
        if (compo != null)
            this.compo = compo;
    }

    @XmlAttribute(name = "appliCorrigee")
    public String getAppliCorrigee()
    {
        return getString(appliCorrigee);
    }

    public void setAppliCorrigee(String appliCorrigee)
    {
        this.appliCorrigee = getString(appliCorrigee);
    }

    @XmlAttribute(name = "etatDefaut")
    public EtatDefaut getEtatDefaut()
    {
        if (etatDefaut == null)
            etatDefaut = EtatDefaut.NOUVEAU;
        return etatDefaut;
    }

    public void setEtatDefaut(EtatDefaut etatDefaut)
    {
        if (etatDefaut != null)
            this.etatDefaut = etatDefaut;
    }

    @XmlAttribute(name = "nomComposant")
    public String getNomComposant()
    {
        return getString(nomComposant);
    }

    public void setNomComposant(String nomComposant)
    {
        this.nomComposant = getString(nomComposant);
    }

    @XmlTransient
    public DefautQualite getDefautQualite()
    {
        return defautQualite;
    }

    public void setDefautQualite(DefautQualite defautQualite)
    {
        this.defautQualite = defautQualite;
    }
}
