package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import model.utilities.AbstractModele;

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
        @NamedQuery(name="Edition.resetTable", query="DELETE FROM Edition")
})
//@formatter:on
public class Edition extends AbstractModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBase;

    @Column(name="nom", unique = true, nullable = false, length = 64)
    private String nom;
    
    @Column(name="numéro", unique = true, nullable = false, length = 32)
    private String numero;
    
    @Column(name="commentaire", nullable = false)
    private String commentaire;
    
    /*---------- CONSTRUCTEURS ----------*/

    Edition() { }
    
    Edition(String nom, String numero)
    {
        this.nom = nom;
        this.numero = numero;
    }
    /*---------- METHODES PUBLIQUES ----------*/
    
    public Edition update(Edition update)
    {
        nom = update.nom;
        commentaire = update.commentaire;
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
}
