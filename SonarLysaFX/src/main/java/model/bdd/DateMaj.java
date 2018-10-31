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
 * Classe de modèle qui correspond aux données du fichier Excel des anomalies.
 * 
 * @author ETP8137 - Grégoire mathon
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

    DateMaj() {}

    /*---------- METHODES PUBLIQUES ----------*/

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

    public void setTypeDonnee(TypeDonnee typeDonnee)
    {
        this.typeDonnee = typeDonnee;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getHeure()
    {
        return heure;
    }

    public void setHeure(LocalTime heure)
    {
        this.heure = heure;
    }
}
