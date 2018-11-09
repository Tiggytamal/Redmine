package model.bdd;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

/**
 * Classe de modèle réprésentant un composant SonarQube du fichier XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@Entity
@Table(name = "repacks")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Repack.findAll", query="SELECT r FROM Repack r"),
        @NamedQuery(name="Repack.findByIndex", query="SELECT r FROM Repack r WHERE r.nom = :index"),
        @NamedQuery(name="Repack.resetTable", query="DELETE FROM Repack")
})
//@formatter:on
public class Repack extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final long serialVersionUID = 1L;
    
    @Column(name = "nom", nullable = false, length = 32)
    private String nom;
    
    @Column(name = "idVCompHpR", nullable = false, length = 4)
    private int idVCompHpR;
    
    @Column(name = "version", nullable = false, length = 32)
    private String version;
    
    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;
    
    @BatchFetch(value = BatchFetchType.JOIN)
    @ManyToOne(targetEntity = Edition.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "edition")
    private Edition edition;
    
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Override
    public String getMapIndex()
    {
        return getNom() + getVersion();
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public int getIdVCompHpR()
    {
        return idVCompHpR;
    }

    public void setIdVCompHpR(int idVCompHpR)
    {
        this.idVCompHpR = idVCompHpR;
    }

    public String getVersion()
    {
        return getString(version);
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public Edition getEdition()
    {
        return edition;
    }

    public void setEdition(Edition edition)
    {
        this.edition = edition;
    }

}
