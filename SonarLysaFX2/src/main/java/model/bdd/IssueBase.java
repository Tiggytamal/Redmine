package model.bdd;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import dao.AbstractMySQLDao;
import model.rest.sonarapi.Issue;
import utilities.Statics;

/**
 * Classe de modele qui correspond aux anomalies créées dans SonarQube après analyse d'un composant.
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 2.0
 */
@Entity(name = "IssueBase")
@Access(AccessType.FIELD)
@Table(name = "issues")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="IssueBase" + AbstractMySQLDao.FINDALL, query="SELECT ib FROM IssueBase ib "
                + "JOIN FETCH ib.utilisateur u"),
        @NamedQuery(name="IssueBase" + AbstractMySQLDao.FINDINDEX, query="SELECT ib FROM IssueBase ib WHERE ib.key = :index"),
        @NamedQuery(name="IssueBase" + AbstractMySQLDao.RESET, query="DELETE FROM IssueBase")
})
//@formatter:on
public class IssueBase extends AbstractBDDModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "issue_key", unique = true, nullable = false, length = 64)
    private String key;

    @ManyToOne(targetEntity = Utilisateur.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "utilisateur")
    private Utilisateur utilisateur;

    @Column(name = "analyse_id", nullable = false, length = 7)
    private String analyseId;

    @ManyToOne(targetEntity = DefautQualite.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "defaut_qualite")
    private DefautQualite defautQualite;

    @Transient
    private boolean assignee;

    /*---------- CONSTRUCTEURS ----------*/

    IssueBase()
    {
        // Constructeur vide pour la persistance JPA
    }

    public static IssueBase build(Issue issue)
    {
        IssueBase retour = new IssueBase();
        retour.setKey(issue.getKey());

        return retour;
    }
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getKey();
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + Objects.hash(analyseId, assignee, defautQualite, key, utilisateur);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        IssueBase other = (IssueBase) obj;
        return Objects.equals(analyseId, other.analyseId) 
                && assignee == other.assignee 
                && Objects.equals(defautQualite, other.defautQualite) 
                && Objects.equals(key, other.key)
                && Objects.equals(utilisateur, other.utilisateur);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public Utilisateur getUtilisateur()
    {
        if (utilisateur == null)
            utilisateur = Statics.USERINCONNU;
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur)
    {
        if (utilisateur != null)
            this.utilisateur = utilisateur;
    }

    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = getString(key);
    }

    public String getAnalyseId()
    {
        return getString(analyseId);
    }

    public void setAnalyseId(String analyseId)
    {
        this.analyseId = getString(analyseId);
    }

    public DefautQualite getDefautQualite()
    {
        return defautQualite;
    }

    public void setDefautQualite(DefautQualite defautQualite)
    {
        if (defautQualite != null)
            this.defautQualite = defautQualite;
    }

    public boolean isAssignee()
    {
        return assignee;
    }

    public void setAssignee(boolean assignee)
    {
        this.assignee = assignee;
    }
}
