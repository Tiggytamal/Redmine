package model.bdd;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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

    @Column(name="nom", unique = true, nullable = false, length = 64)
    private String nom;
    
    @Column(name="numero", unique = true, nullable = false, length = 32)
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
    
    @Override
    public String getMapIndex()
    {
        return getNumero();
    }
    
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
