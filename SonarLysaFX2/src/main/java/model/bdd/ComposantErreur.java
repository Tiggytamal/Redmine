package model.bdd;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dao.AbstractMySQLDao;
import model.enums.Param;
import utilities.Statics;
import utilities.Utilities;
import utilities.adapter.LocalDateAdapter;

/**
 * Classe de modele pour les composants en erreur dans SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@Entity(name = "ComposantErreur")
@Access(AccessType.FIELD)
@Table(name = "composants_plantes")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="ComposantErreur" + AbstractMySQLDao.FINDALL, query="SELECT distinct(c) FROM ComposantErreur c "),
        @NamedQuery(name="ComposantErreur" + AbstractMySQLDao.FINDINDEX, query="SELECT c FROM ComposantErreur c WHERE c.key = :index"),
        @NamedQuery(name="ComposantErreur" + AbstractMySQLDao.RESET, query="DELETE FROM ComposantErreur")
})
//@formatter:on
public class ComposantErreur extends AbstractBDDModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "composant_plante_key", nullable = false)
    private String key;

    @Column(name = "nom", nullable = false, length = 128)
    private String nom;

    @Column(name = "date_detection", nullable = false)
    private LocalDate dateDetection;

    @Column(name = "a_purger", nullable = false)
    private boolean aPurger;

    @Column(name = "existe", nullable = false)
    private boolean existe;

    @Column(name = "nbre_purge", length = 3)
    private int nbrePurge;

    /*---------- CONSTRUCTEURS ----------*/

    ComposantErreur()
    {}

    private ComposantErreur(String key, String nom)
    {
        if (key == null || key.isEmpty() || nom == null || nom.isEmpty())
            throw new IllegalArgumentException("model.bdd.ComposantErreur.constructor - key ou nom nuls");

        this.key = key;
        this.nom = nom;
        aPurger = true;
        existe = true;
        dateDetection = Statics.TODAY;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getKey();
    }

    public static ComposantErreur build(String key, String nom)
    {
        return new ComposantErreur(key, nom);
    }

    public void ajouterPurge()
    {
        nbrePurge++;
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + Objects.hash(dateDetection, key, nom);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        ComposantErreur other = (ComposantErreur) obj;
        return aPurger == other.aPurger 
                && Objects.equals(dateDetection, other.dateDetection) 
                && existe == other.existe 
                && Objects.equals(key, other.key) 
                && nbrePurge == other.nbrePurge
                && Objects.equals(nom, other.nom);
        //@formatter:on
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        if (nom != null && !nom.isEmpty())
            this.nom = nom;
    }

    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        if (key != null && !key.isEmpty())
            this.key = key;
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateDetection()
    {
        return dateDetection;
    }

    public void setDateDetection(LocalDate dateDetection)
    {
        if (dateDetection != null)
            this.dateDetection = dateDetection;
    }

    public String getLiens()
    {
        String liensBrut = Statics.proprietesXML.getMapParams().get(Param.URLSONAR) + Statics.proprietesXML.getMapParams().get(Param.LIENSCOMPOS) + getKey();
        return Utilities.stringToUrl(liensBrut);
    }

    public boolean isaPurger()
    {
        return aPurger;
    }

    public void setaPurger(boolean aPurger)
    {
        this.aPurger = aPurger;
    }

    public int getNbrePurge()
    {
        return nbrePurge;
    }

    public void setNbrePurge(int nbrePurge)
    {
        this.nbrePurge = nbrePurge;
    }

    public boolean existe()
    {
        return existe;
    }

    public void setExiste(boolean existe)
    {
        this.existe = existe;
    }
}
