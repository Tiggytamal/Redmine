package model.bdd;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import dao.AbstractMySQLDao;
import dao.DaoStatistiqueCompo;
import model.enums.StatistiqueCompoEnum;

/**
 * Classe de modèle représentant les statitiques du nombres d'assemblages par jour.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
@Entity(name = "StatistiqueCompo")
@Access(AccessType.FIELD)
@Table(name = "statistiques_par_compo")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="StatistiqueCompo" + AbstractMySQLDao.FINDALL, query="SELECT s FROM StatistiqueCompo s"),
        @NamedQuery(name="StatistiqueCompo" + AbstractMySQLDao.FINDINDEX, query="SELECT s FROM StatistiqueCompo s WHERE s.date = :date AND s.compo.nom = :nom AND s.type = :type"),
        @NamedQuery(name="StatistiqueCompo" + AbstractMySQLDao.RESET, query="DELETE FROM StatistiqueCompo"),
        @NamedQuery(name="StatistiqueCompo" + DaoStatistiqueCompo.READALLPARTYPE, query="SELECT s FROM StatistiqueCompo s WHERE s.type = :type"),
        @NamedQuery(name="StatistiqueCompo" + DaoStatistiqueCompo.RECUPCOMPOSKEY, query="SELECT DISTINCT(s.compo.key) FROM StatistiqueCompo s")
})
//@formatter:on
@XmlRootElement(name = "composant")
public class StatistiqueCompo extends AbstractBDDModele<StatistiqueCompoIndex>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "date", unique = true, nullable = false)
    private LocalDate date;

    @Column(name = "valeur", nullable = false, length = 4)
    private int valeur;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private StatistiqueCompoEnum type;

    @ManyToOne(targetEntity = ComposantBase.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "compo")
    private ComposantBase compo;

    /*---------- CONSTRUCTEURS ----------*/

    StatistiqueCompo()
    {}

    private StatistiqueCompo(LocalDate date, StatistiqueCompoEnum type, ComposantBase compo, int valeur)
    {
        if (date == null || type == null)
            throw new IllegalArgumentException("model.bdd.FichierParJour.constructor - date ou type nuls");
        this.date = date;
        this.type = type;
        this.compo = compo;
        this.valeur = valeur;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static StatistiqueCompo build(StatistiqueCompoIndex index, int valeur)
    {
        return new StatistiqueCompo(index.getDate(), index.getType(), index.getCompo(), valeur);
    }

    public StatistiqueCompo update(int valeur)
    {
        this.valeur = valeur;
        return this;
    }

    @Override
    public StatistiqueCompoIndex getMapIndex()
    {
        return new StatistiqueCompoIndex(getDate(), getCompo(), getType());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(idBase, date, type, compo);
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

        StatistiqueCompo other = (StatistiqueCompo) obj;
        return idBase == other.idBase 
                && Objects.equals(date, other.date) 
                && valeur == other.valeur
                && type == other.type
                && Objects.equals(compo, other.compo);
        //@formatter:on
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
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

    public StatistiqueCompoEnum getType()
    {
        return type;
    }

    public void setType(StatistiqueCompoEnum type)
    {
        if (type != null)
            this.type = type;
    }

    public ComposantBase getCompo()
    {
        return compo;
    }

    public void setCompo(ComposantBase compo)
    {
        this.compo = compo;
    }

}
