package model.bdd;

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
 * Classe de modèle qui correspond aux défaults des codes applications
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
@Entity
@Table(name = "defauts_application")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="DefautAppli" + AbstractDao.FINDALL, query="SELECT da FROM DefautAppli da JOIN FETCH da.compo c LEFT JOIN FETCH da.defautQualite dq"),
        @NamedQuery(name="DefautAppli" + AbstractDao.FINDINDEX, query="SELECT da FROM DefautAppli da WHERE da.compo.nom = :index"),
        @NamedQuery(name="DefautAppli" + AbstractDao.RESET, query="DELETE FROM DefautQualite")
})
//@formatter:on
public class DefautAppli extends AbstractBDDModele
{
    /*---------- ATTRIBUTS ----------*/

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
        etatDefaut = EtatDefaut.NOUVELLE;
        appliCorrigee = Statics.EMPTY;
        action = TypeAction.VIDE;
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
        this.compo = compo;
    }

    public String getAppliCorrigee()
    {
        return appliCorrigee;
    }

    public void setAppliCorrigee(String appliCorrigee)
    {
        this.appliCorrigee = appliCorrigee;
    }

    public TypeAction getAction()
    {
        return action;
    }

    public void setAction(TypeAction action)
    {
        this.action = action;
    }

    public EtatDefaut getEtatDefaut()
    {
        return etatDefaut;
    }

    public void setEtatDefaut(EtatDefaut etatDefaut)
    {
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

    public void setDateDetection(LocalDate dateDetection)
    {
        this.dateDetection = dateDetection;
    }
    public String getNomComposant()
    {
        return getString(nomComposant);
    }

    public void setNomComposant(String nomComposant)
    {
        this.nomComposant = nomComposant;
    }
}
