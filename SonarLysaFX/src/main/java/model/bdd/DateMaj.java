package model.bdd;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import model.enums.TypeDonnee;

/**
 * Classe de mod�le qui correspond aux donn�es du fichier Excel des anomalies.
 * 
 * @author ETP8137 - Gr�goire mathon
 * @since 1.0
 */
@Entity
@Table(name = "dates_maj")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="DateMaj.findAll", query="SELECT d FROM DateMaj d "),
        @NamedQuery(name="DateMaj.findByIndex", query="SELECT d FROM DateMaj d WHERE d.typeDonnee = :index"),
        @NamedQuery(name="DateMaj.resetTable", query="DELETE FROM DateMaj")
})
//@formatter:on
public class DateMaj extends AbstractBDDModele
{
    /*---------- ATTRIBUTS ----------*/

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeDonnee typeDonnee;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "heure", nullable = false)
    private LocalTime heure;

    /*---------- CONSTRUCTEURS ----------*/

    DateMaj()
    {
        // Constructeur pour le JPA
    }

    private DateMaj(TypeDonnee typeDonnee, LocalDate date, LocalTime heure)
    {
        if (typeDonnee == null || date == null || heure == null)
            throw new IllegalArgumentException("model.bdd.DateMaj.constructeur : arguments nuls ou non valides.");
        this.typeDonnee = typeDonnee;
        this.date = date;
        this.heure = heure;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static DateMaj build(TypeDonnee typeDonnee, LocalDate date, LocalTime heure)
    {
        return new DateMaj(typeDonnee, date, heure);
    }

    @Override
    public String getMapIndex()
    {
        return typeDonnee.toString();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public TypeDonnee getTypeDonnee()
    {
        return typeDonnee;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        if (date != null)
            this.date = date;
    }

    public LocalTime getHeure()
    {
        return heure;
    }

    public void setHeure(LocalTime heure)
    {
        if (heure != null)
            this.heure = heure;
    }
}
