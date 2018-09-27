package model;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import model.enums.Statut;
import model.enums.Tracker;
import model.enums.Champ;
import utilities.Statics;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The persistent class for the issues database table.
 * 
 */
@Entity

@Table(name = "issues")
@SecondaryTables({ @SecondaryTable(name = "issue_statuses", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "status_id")),
        @SecondaryTable(name = "trackers", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "tracker_id")),
        @SecondaryTable(name = "projects", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "project_id")),
        @SecondaryTable(name = "enumerations", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "priority_id")) })
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Incident.findAll", query="SELECT i FROM Incident i "),
        @NamedQuery(name="Incident.findByProject", query="SELECT distinct(i) FROM Incident i "
                + "JOIN FETCH i.responsable r "
                + "JOIN FETCH i.createur c "
                + "JOIN FETCH i.valeurs v "
                + "WHERE i.projet = :projet "
                + "AND i.dateCreation > :oneYear")
})
//@formatter:on
public final class Incident implements Serializable
{
    /* Attributes */

    private static final long serialVersionUID = 1L;

    /** Id auto_incrémenté */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Responsable de l'incident */
    @BatchFetch(value = BatchFetchType.JOIN)
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private User responsable;

    /** Créateur de l'incident */
    @BatchFetch(value = BatchFetchType.JOIN)
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User createur;

    /** Date de cloture de l'incident */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "closed_on")
    private Date dateCloture;

    /** Date de création de l'incident */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    private Date dateCreation;

    /** Description de l'incident */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /** pourcentage de l'incident effectué */
    @Column(name = "done_ratio")
    private int ratioEffectue;

    /** Date prévue de résolution de l'incident */
    @Temporal(TemporalType.DATE)
    @Column(name = "due_date")
    private Date dueDate;

    /** Incident parent */
    @ManyToOne(targetEntity = Incident.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Incident incidentParent;

    /** Liste des incidents liés */
    @OneToMany(mappedBy = "incidentParent", targetEntity = Incident.class, fetch = FetchType.LAZY)
    private List<Incident> incidentslies;

    /** Priorité de l'incident */
    @Column(table = "enumerations", name = "name", length = 30, nullable = false)
    private String priorite;

    /** Type de l'incident */
    @Column(table = "projects", name = "name", length = 255, nullable = false)
    private String projet;

    /** Statut de l'incident */
    @Column(table = "issue_statuses", name = "name", length = 30, nullable = false)
    private String statutString;

    /** Titre de l'incident */
    @Column(name = "subject", length = 255, nullable = false)
    private String sujet;

    /** Tracker de l'incident */
    @Column(table = "trackers", name = "name", length = 30, nullable = false)
    private String trackerString;

    /** Date de mise à jour de l'incident */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on")
    private Date dateMiseAJour;

    /* Liste des journaux de mise à jour de l'incident */
    @OneToMany(targetEntity = Journal.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "journalized_id")
    private List<Journal> journaux;

    @OneToMany(mappedBy = "incident", targetEntity = Valeur.class, fetch = FetchType.LAZY)
    private List<Valeur> valeurs;

    @Transient
    private Map<Champ, String> mapValeurs;

    @Transient
    private Statut statut;

    @Transient
    private Tracker tracker;

    @Transient
    private String commentaire;

    /* Constructors */

    public Incident()
    {
    }

    /* Methods */

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("Incident : ").append(Statics.NL);
        if (responsable != null)
            builder.append("responsable = ").append(responsable.getNom()).append(Statics.NL);
        if (createur != null)
            builder.append("createur = ").append(createur.getNom()).append(Statics.NL);
        if (dateCloture != null)
            builder.append("dateCloture = ").append(dateCloture.toString()).append(Statics.NL);
        if (dateCreation != null)
            builder.append("dateCreation =").append(dateCreation.toString()).append(Statics.NL);
        if (description != null)
            builder.append("description = ").append(description).append(Statics.NL);
        builder.append("ratioEffectue = ").append(ratioEffectue).append(Statics.NL);
        if (dueDate != null)
            builder.append("dueDate = ").append(dueDate.toString()).append(Statics.NL);
        if (priorite != null)
            builder.append("priorite = ").append(priorite).append(Statics.NL);
        if (priorite != null)
            builder.append("projet = ").append(projet).append(Statics.NL);
        if (statutString != null)
            builder.append("statut = ").append(statutString).append(Statics.NL);
        if (sujet != null)
            builder.append("sujet = ").append(sujet).append(Statics.NL);
        if (tracker != null)
            builder.append("tracker = ").append(tracker.toString()).append(Statics.NL);
        if (dateMiseAJour != null)
            builder.append("dateMiseAJour = ").append(dateMiseAJour.toString()).append(Statics.NL);
        if (valeurs != null)
        {
            builder.append("Valeurs : ");
            for (Valeur valeur : valeurs)
            {
                builder.append("\t").append(valeur.getChamp().toString()).append(" = ").append(valeur.getValue()).append(Statics.NL);
            }

        }

        return builder.toString();
    }

    /* Access */

    public int getId()
    {
        return id;
    }

    /**
     * Permet de remonter une HashMap des valeurs de l'incident.
     * 
     * @return
     */
    public Map<Champ, String> getMapValeurs()
    {
        if (mapValeurs != null)
            return mapValeurs;

        mapValeurs = new HashMap<>();
        if (valeurs == null || valeurs.isEmpty())
            return mapValeurs;

        for (Valeur valeur : valeurs)
        {
            if (valeur.getChamp() != null)
                mapValeurs.put(valeur.getChamp(), valeur.getValue());
        }
        return mapValeurs;

    }

    /**
     * Retourne le status de l'incident sous la forme d'une énumération
     * 
     * @return
     */
    public Statut getStatut()
    {
        if (statut != null)
            return statut;

        statut = Statut.getStatus(statutString);
        return statut;
    }

    /**
     * retourne le tracker de l'incident sous forme d'une énumération
     * 
     * @return
     */
    public Tracker getTracker()
    {
        if (tracker != null)
            return tracker;

        tracker = Tracker.getTracker(trackerString);
        return tracker;
    }

    public User getResponsable()
    {
        return responsable;
    }

    public User getCreateur()
    {
        return createur;
    }

    public Date getDateCloture()
    {
        return dateCloture;
    }

    public Date getDateCreation()
    {
        return dateCreation;
    }

    public String getDescription()
    {
        return description;
    }

    public int getRatioEffectue()
    {
        return ratioEffectue;
    }

    public Date getDueDate()
    {
        return dueDate;
    }

    public Incident getIncidentParent()
    {
        return incidentParent;
    }

    public List<Incident> getIncidentslies()
    {
        return incidentslies;
    }

    public String getPriorite()
    {
        return priorite;
    }

    public String getProjet()
    {
        return projet;
    }

    public String getSujet()
    {
        return sujet;
    }

    public Date getDateMiseAJour()
    {
        return dateMiseAJour;
    }

    public List<Journal> getJournaux()
    {
        return journaux;
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
