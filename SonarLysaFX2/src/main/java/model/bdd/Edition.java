package model.bdd;

import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dao.AbstractMySQLDao;
import model.enums.TypeEdition;
import utilities.Statics;
import utilities.adapter.LocalDateAdapter;

/**
 * Classe de modele des Edition de la PIC.
 * 
 * @author ETP8137- Grégoire Mathon
 * @since 1.0
 *
 */
@Entity(name = "Edition")
@Access(AccessType.FIELD)
@Table(name = "editions")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Edition" + AbstractMySQLDao.FINDALL, query="SELECT e FROM Edition e"),
        @NamedQuery(name="Edition" + AbstractMySQLDao.FINDINDEX, query="SELECT e FROM Edition e WHERE e.nom = :index"),
        @NamedQuery(name="Edition" + AbstractMySQLDao.RESET, query="DELETE FROM Edition"),
        @NamedQuery(name="Edition.recupDateDepuisRepack", query="SELECT e.dateMEP from Edition e "
                + "WHERE e.numero LIKE :numero AND e.nom LIKE :semaine AND e.nom LIKE 'CHC%'"),
        @NamedQuery(name="Edition.findByNumero", query="SELECT e from Edition e WHERE e.numero = :numero")
})
//@formatter:on
public class Edition extends AbstractBDDModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String COMEDINCO = "Inconnue dans la codification des Editions";
    private static final Pattern PATTERNEDITION = Pattern.compile("(\\d\\d\\.){3}\\d\\d");

    @Column(name = "nom", unique = true, nullable = false, length = 64)
    private String nom;

    @Column(name = "numero", unique = true, nullable = false, length = 32)
    private String numero;

    @Column(name = "edition_majeure", nullable = false, length = 4)
    private String editionMajeure;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_edition", nullable = false)
    private TypeEdition typeEdition;

    @Column(name = "actif", nullable = false)
    private boolean actif;

    @Column(name = "date_mep", nullable = false)
    private LocalDate dateMEP;

    @Column(name = "commentaire", nullable = false, columnDefinition = "TEXT")
    private String commentaire;

    /*---------- CONSTRUCTEURS ----------*/

    Edition()
    {}

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
        retour.setTypeEdition(TypeEdition.INCONNUE);
        retour.setDateMEP(Statics.DATEINCO2099);
        retour.setCommentaire(COMEDINCO);
        return retour;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + Objects.hash(actif, commentaire, dateMEP, editionMajeure, nom, numero, typeEdition);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        Edition other = (Edition) obj;
        return Objects.equals(commentaire, other.commentaire) 
                && Objects.equals(dateMEP, other.dateMEP) 
                && Objects.equals(editionMajeure, other.editionMajeure)
                && Objects.equals(nom, other.nom) 
                && Objects.equals(numero, other.numero) 
                && typeEdition == other.typeEdition;
        //@formatter:on
    }
    
    @Override
    public String getMapIndex()
    {
        return getNom();
    }

    public Edition update(Edition update)
    {
        setCommentaire(update.commentaire);
        setTypeEdition(update.typeEdition);
        setEditionMajeure(update.editionMajeure);
        setNumero(update.numero);
        setDateMEP(update.dateMEP);
        return this;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = getString(nom);
    }

    public String getNumero()
    {
        return getString(numero);
    }

    public void setNumero(String numero)
    {
        if (numero != null && !numero.isEmpty() && PATTERNEDITION.matcher(numero).matches())
            this.numero = numero;
    }

    public String getCommentaire()
    {
        return getString(commentaire);
    }

    public void setCommentaire(String commentaire)
    {
        this.commentaire = getString(commentaire);
    }

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

    public TypeEdition getTypeEdition()
    {
        if (typeEdition == null)
            typeEdition = TypeEdition.INCONNUE;
        return typeEdition;
    }

    public void setTypeEdition(TypeEdition typeEdition)
    {
        if (typeEdition != null)
            this.typeEdition = typeEdition;
    }

    public String getEditionMajeure()
    {
        return getString(editionMajeure);
    }

    public void setEditionMajeure(String editionMajeure)
    {
        this.editionMajeure = getString(editionMajeure);
    }

    public boolean isActif()
    {
        return actif;
    }

    public void setActif(boolean actif)
    {
        this.actif = actif;
    }
}
