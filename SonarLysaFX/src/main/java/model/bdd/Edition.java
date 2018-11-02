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

import model.enums.TypeEdition;

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
        @NamedQuery(name="Edition.findByIndex", query="SELECT e FROM Edition e WHERE e.numero = :index"),
        @NamedQuery(name="Edition.resetTable", query="DELETE FROM Edition")
})
//@formatter:on
public class Edition extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String EDITION0 = "00.00.00.00";
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

    Edition() {}

    Edition(String nom, String numero)
    {
        this.nom = nom;
        this.numero = numero;
    }

    public static Edition getEditionInconnue(String edition)
    {
        Edition retour;
        if (edition == null)
            retour = new Edition("inconnue", EDITION0);
        else
            retour = new Edition("inconnue", edition);
        retour.setTypeEdition(TypeEdition.INCONNU);
        retour.setDateMEP(LocalDate.of(2099, 1, 1));
        retour.setCommentaire(COMEDINCO);
        return retour;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getNumero();
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

    public String getNom()
    {
        return nom;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public String getNumero()
    {
        return numero;
    }

    public void setNumero(String numero)
    {
        this.numero = numero;
    }

    public String getCommentaire()
    {
        return commentaire;
    }

    public void setCommentaire(String commentaire)
    {
        this.commentaire = commentaire;
    }

    public LocalDate getDateMEP()
    {
        return dateMEP;
    }

    public void setDateMEP(LocalDate dateMEP)
    {
        this.dateMEP = dateMEP;
    }

    public TypeEdition getTypeEdition()
    {
        return typeEdition;
    }

    public void setTypeEdition(TypeEdition typeEdition)
    {
        this.typeEdition = typeEdition;
    }
}
