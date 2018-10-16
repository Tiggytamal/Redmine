package model.bdd;

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

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import model.enums.EtatDefault;
import model.enums.TypeAction;

/**
 * Classe de modèle qui correspond aux défaults des codes applications
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
@Entity
@Table(name = "defaults_application")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="DefaultAppli" + AbstractBDDModele.FINDALL, query="SELECT da FROM DefaultAppli da JOIN FETCH da.compo c LEFT JOIN FETCH da.defaultQualite dq"),
        @NamedQuery(name="DefaultAppli" + AbstractBDDModele.FINDINDEX, query="SELECT da FROM DefaultAppli da WHERE da.compo.nom = :index"),
        @NamedQuery(name="DefaultAppli" + AbstractBDDModele.RESETTABLE, query="DELETE FROM DefaultQualite")
})
//@formatter:on
public class DefaultAppli extends AbstractBDDModele
{
    /*---------- ATTRIBUTS ----------*/
    
    @BatchFetch(value = BatchFetchType.JOIN)
    @OneToOne (targetEntity = ComposantSonar.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "composant")
    private ComposantSonar compo;
    
    @Column (name = "appli_corrigee", nullable = true, length = 4)
    public String appliCorrigee;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = true)
    private TypeAction action;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "etat_default", nullable = false)
    private EtatDefault etatDefault;
    
    @BatchFetch(value = BatchFetchType.JOIN)
    @OneToOne (targetEntity = DefaultQualite.class, cascade = CascadeType.MERGE)
    @JoinColumn (name = "default_qualite")
    private DefaultQualite defaultQualite;
    
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return compo.getNom();
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
    
}
