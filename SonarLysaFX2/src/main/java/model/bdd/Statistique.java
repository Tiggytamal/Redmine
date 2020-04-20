package model.bdd;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import dao.AbstractMySQLDao;
import dao.DaoStatistique;
import model.enums.StatistiqueEnum;
import utilities.Statics;

/**
 * Classe de modèle représentant les statitiques du nombres d'assemblages par jour.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
@Entity(name = "Statistique")
@Access(AccessType.FIELD)
@Table(name = "statistiques")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Statistique" + AbstractMySQLDao.FINDALL, query="SELECT s FROM Statistique s"),
        @NamedQuery(name="Statistique" + AbstractMySQLDao.FINDINDEX, query="SELECT s FROM Statistique s WHERE s.date = :index"),
        @NamedQuery(name="Statistique" + AbstractMySQLDao.RESET, query="DELETE FROM Statistique"),
        @NamedQuery(name="Statistique" + DaoStatistique.READALLPARTYPE, query="SELECT s FROM Statistique s WHERE s.type = :type")
})
//@formatter:on
@XmlRootElement(name = "composant")
public class Statistique extends AbstractBDDModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "date", unique = true, nullable = false)
    private LocalDate date;

    @Column(name = "valeur", nullable = false, length = 4)
    private int valeur;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private StatistiqueEnum type;

    /*---------- CONSTRUCTEURS ----------*/

    Statistique()
    {}

    private Statistique(LocalDate date, StatistiqueEnum type)
    {
        if (date == null || type == null)
            throw new IllegalArgumentException("model.bdd.Statistique.build - date et/ou type nul(s)");
        this.date = date;
        this.type = type;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static Statistique build(LocalDate date, StatistiqueEnum type)
    {
        return new Statistique(date, type);
    }

    @Override
    public String getMapIndex()
    {
        if (date != null)
            return date.toString();
        return Statics.EMPTY;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(idBase, date, type);
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Statistique other = (Statistique) obj;
        return idBase == other.idBase 
                && Objects.equals(date, other.date) 
                && valeur == other.valeur
                && type == other.type;
        //@formatter:on
    }

    public Statistique incrementerValeur()
    {
        valeur++;
        return this;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        if (date != null)
            this.date = date;
    }

    public int getValeur()
    {
        return valeur;
    }

    public void setValeur(int nbreFichiers)
    {
        this.valeur = nbreFichiers;
    }

    public StatistiqueEnum getType()
    {
        return type;
    }

    public void setType(StatistiqueEnum type)
    {
        if (type != null)
            this.type = type;
    }
}
