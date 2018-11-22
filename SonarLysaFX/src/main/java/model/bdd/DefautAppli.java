package model.bdd;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import dao.AbstractDao;
import model.enums.EtatDefaut;
import model.enums.TypeAction;
import utilities.Statics;

/**
 * Classe de mod�le qui correspond aux d�faults des codes applications
 * 
 * @author ETP8137 - Gr�goire mathon
 * @since 1.0
 */
@Entity
@Table(name = "defauts_application")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="DefautAppli" + AbstractDao.FINDALL, query="SELECT da FROM DefautAppli da "
                + "JOIN FETCH da.compo c "
                + "LEFT JOIN FETCH da.defautQualite dq"),
        @NamedQuery(name="DefautAppli" + AbstractDao.FINDINDEX, query="SELECT da FROM DefautAppli da WHERE da.compo.nom = :index"),
        @NamedQuery(name="DefautAppli" + AbstractDao.RESET, query="DELETE FROM DefautQualite")
})
//@formatter:on
public class DefautAppli extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @BatchFetch(value = BatchFetchType.JOIN)
    @OneToOne(targetEntity = ComposantSonar.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "composant")
    private ComposantSonar compo;

    @Column(name = "appli_corrigee", nullable = true, length = 4)
    public String appliCorrigee;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = true)
    private TypeAction action;

    @Column(name = "date_detection", nullable = false)
    private LocalDate dateDetection;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_defaut", nullable = false)
    private EtatDefaut etatDefaut;

    @BatchFetch(value = BatchFetchType.JOIN)
    @OneToOne(targetEntity = DefautQualite.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "defaut_qualite")
    private DefautQualite defautQualite;

    @Transient
    private String nomComposant;

    /*---------- CONSTRUCTEURS ----------*/

    DefautAppli()
    {
        etatDefaut = EtatDefaut.NOUVEAU;
        appliCorrigee = Statics.EMPTY;
        action = TypeAction.VIDE;
        dateDetection = LocalDate.now();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        if (compo != null)
            return compo.getNom();

        return getNomComposant();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public ComposantSonar getCompo()
    {
        return compo;
    }

    public void setCompo(ComposantSonar compo)
    {
        if (compo != null)
            this.compo = compo;
    }

    public String getAppliCorrigee()
    {
        return getString(appliCorrigee);
    }

    public void setAppliCorrigee(String appliCorrigee)
    {
        this.appliCorrigee = getString(appliCorrigee);
    }

    public TypeAction getAction()
    {
        if (action == null)
            action = TypeAction.VIDE;
        return action;
    }

    public void setAction(TypeAction action)
    {
        if (action != null)
            this.action = action;
    }

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

    public DefautQualite getDefautQualite()
    {
        return defautQualite;
    }

    public void setDefautQualite(DefautQualite defautQualite)
    {
        this.defautQualite = defautQualite;
    }

    public LocalDate getDateDetection()
    {
        return dateDetection;
    }

    public String getNomComposant()
    {
        return getString(nomComposant);
    }

    public void setNomComposant(String nomComposant)
    {
        this.nomComposant = getString(nomComposant);
    }
}
