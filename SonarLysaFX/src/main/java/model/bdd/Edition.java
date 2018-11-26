package model.bdd;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import model.enums.TypeEdition;
import utilities.Statics;
import utilities.adapter.LocalDateAdapter;

/**
 * Classe de modèle des Edition de la PIC
 * 
 * @author ETP8137- Grégoire Mathon
 * @since 1.0
 *
 */
@Entity
@Table(name = "editions")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Edition.findAll", query="SELECT e FROM Edition e"),
        @NamedQuery(name="Edition.recupDateDepuisRepack", query="SELECT e.dateMEP from Edition e "
                + "WHERE e.numero LIKE :numero AND e.nom LIKE :semaine AND e.nom LIKE 'CHC%'"),
        @NamedQuery(name="Edition.findByIndex", query="SELECT e FROM Edition e WHERE e.nom = :index"),
        @NamedQuery(name="Edition.resetTable", query="DELETE FROM Edition")
})
//@formatter:on
@XmlRootElement
public class Edition extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String COMEDINCO = "Inconnue dans la codification des Editions";

    @Column(name = "nom", unique = true, nullable = false, length = 64)
    private String nom;

    @Column(name = "numero", unique = true, nullable = false, length = 32)
    private String numero;

    @Column(name = "commentaire", nullable = false, columnDefinition = "TEXT")
    private String commentaire;

    @Column(name = "date_mep", nullable = false)
    private LocalDate dateMEP;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_edition", nullable = false)
    private TypeEdition typeEdition;

    /*---------- CONSTRUCTEURS ----------*/

    Edition()
    {
    }

    private Edition(String nom, String numero)
    {
        this.nom = nom;
        this.numero = numero;
        commentaire = Statics.EMPTY;
    }

    public static Edition build(String nom, String numero)
    {
        return new Edition(nom, numero);
    }

    public static Edition getEditionInconnue(String edition)
    {
        Edition retour;
        if (edition == null || edition.isEmpty())
            retour = new Edition(Statics.EDINCONNUE, Statics.EDINCONNUE);
        else
            retour = new Edition(edition, Statics.EDINCONNUE);
        retour.setTypeEdition(TypeEdition.INCONNU);
        retour.setDateMEP(Statics.DATEINCO2099);
        retour.setCommentaire(COMEDINCO);
        return retour;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getNom();
    }

    public Edition update(Edition update)
    {
        nom = update.nom;
        commentaire = update.commentaire;
        typeEdition = update.typeEdition;
        dateMEP = update.dateMEP;
        return this;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "nom")
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    @XmlAttribute(name = "numero")
    public String getNumero()
    {
        return getString(numero);
    }

    public void setNumero(String numero)
    {
        if (numero != null && !numero.isEmpty() && numero.matches("(\\d\\d\\.){3}\\d\\d"))
            this.numero = numero;
    }

    @XmlAttribute(name = "commentaire")
    public String getCommentaire()
    {
        return getString(commentaire);
    }

    public void setCommentaire(String commentaire)
    {
        this.commentaire = getString(commentaire);
    }

    @XmlAttribute(name = "dateMEP")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateMEP()
    {
        if (dateMEP == null)
            dateMEP = Statics.DATEINCO2099;
        return dateMEP;
    }

    public void setDateMEP(LocalDate dateMEP)
    {
        if (dateMEP != null)
            this.dateMEP = dateMEP;
    }

    @XmlAttribute(name = "typeEdition")
    public TypeEdition getTypeEdition()
    {
        if (typeEdition == null)
            typeEdition = TypeEdition.INCONNU;
        return typeEdition;
    }

    public void setTypeEdition(TypeEdition typeEdition)
    {
        if (typeEdition != null)
            this.typeEdition = typeEdition;
    }
}
