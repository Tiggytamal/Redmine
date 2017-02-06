package model;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

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

@Table(name="issues")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Incident.findAll", query="SELECT i FROM Incident i "),
        @NamedQuery(name="Incident.findByProject", query="SELECT distinct(i) FROM Incident i "
                + "JOIN FETCH i.projet p "
                + "JOIN FETCH i.responsable r "
                + "JOIN FETCH i.createur c "
                + "JOIN FETCH i.statut s "
                + "JOIN FETCH i.tracker t "
                + "JOIN FETCH i.priorite prio "
                + "JOIN FETCH i.valeurs v "
                + "WHERE p.nom = :projet")
})
//@formatter:on
public class Incident implements Serializable
{
    /* Attributes */
    
	private static final long serialVersionUID = 1L;

	/** Id auto_incr�ment� */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

	/** Responsable de l'incident */
    @BatchFetch(value = BatchFetchType.IN)	
	@ManyToOne (targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn (name = "assigned_to_id")
	private User responsable;

	/** Cr�ateur de l'incident */
    @BatchFetch(value = BatchFetchType.IN)	
	@ManyToOne (targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn (name = "author_id")
	private User createur;

	/** Date de cloture de l'incident */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="closed_on")
	private Date dateCloture;

	/** Date de cr�ation de l'incident */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_on")
	private Date dateCreation;

	/** Description de l'incident */
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	/** pourcentage de l'incident effectu� */
	@Column(name="done_ratio")
	private int ratioEffectue;

	/** Date pr�vue de r�solution de l'incident */
	@Temporal(TemporalType.DATE)
	@Column(name="due_date")
	private Date dueDate;

	/** Incident parent */
	@ManyToOne (targetEntity = Incident.class, fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	private Incident incidentParent;
	
	/** Liste des incidents li�s */
	@OneToMany (mappedBy = "incidentParent", targetEntity = Incident.class, fetch = FetchType.LAZY)
    private List<Incident> incidentslies;

	/** Priorit� de l'incident */
	@BatchFetch(value = BatchFetchType.IN)
	@ManyToOne (targetEntity = Priorite.class, fetch = FetchType.LAZY)
	@JoinColumn(name="priority_id")
	private Priorite priorite;

	/** Type de l'incident */
	@BatchFetch(value = BatchFetchType.IN)
	@ManyToOne (targetEntity = Projet.class, fetch = FetchType.LAZY)
	@JoinColumn(name="project_id")
	private Projet projet;

	/** Statut de l'incident */
	@BatchFetch(value = BatchFetchType.IN)
	@ManyToOne (targetEntity = Statut.class, fetch = FetchType.LAZY)
	@JoinColumn(name="status_id")
	private Statut statut;

	/** Titre de l'incident */
	@Column (name = "subject", length = 255, nullable = false)
	private String sujet;

	/** Tracker de l'incident */
	@BatchFetch(value = BatchFetchType.IN)
	@ManyToOne (targetEntity = Tracker.class, fetch = FetchType.LAZY)
	@JoinColumn(name="tracker_id")
	private Tracker tracker;

	/** Date de mise � jour de l'incident */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_on")
	private Date dateMiseAJour;
	
	/* Liste des journaux de mise � jour de l'incident */
	@OneToMany (targetEntity = Journal.class, fetch = FetchType.LAZY)
	@JoinColumn (name = "journalized_id")
	private List<Journal> journaux;
	
	@OneToMany (mappedBy = "incident", targetEntity = Valeur.class, fetch = FetchType.LAZY)
	private List<Valeur> valeurs;
	
	@Transient
	private Map<String, String> mapValeurs;

	/* Constructors */

    public Incident() 
	{
	}
	
	/* Methods */
	
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("Incident : ").append(Statics.NL);
        if( responsable != null)
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
        builder.append("priorite = ").append(priorite.getNom()).append(Statics.NL);
        if (priorite != null)
        builder.append("projet = ").append(projet.getNom()).append(Statics.NL);
        if (statut != null)
        builder.append("statut = ").append(statut.getNom()).append(Statics.NL);
        if (sujet != null)
        builder.append("sujet = ").append(sujet).append(Statics.NL);
        if (tracker != null)
        builder.append("tracker = ").append(tracker.getNom()).append(Statics.NL);
        if (dateMiseAJour != null)
        builder.append("dateMiseAJour = ").append(dateMiseAJour.toString()).append(Statics.NL);
        if (valeurs != null)
        builder.append(listeValeurs());
        
        return  builder.toString();
    }
    
    private String listeValeurs()
    {
        StringBuilder builder = new StringBuilder("Valeurs \t : ");
        for (Valeur valeur : valeurs) 
        {
            builder.append(valeur.getChamp().getName()).append(valeur.getValue()).append(Statics.NL);
        }
        
        return builder.toString();
    }
    /* Access */

    public int getId()
    {
        return id;
    }
    
    /**
     * Permet de remonter une HashMap des vlauers de l'incident.
     * @return
     */
    public Map<String, String> getMapValeurs()
    {
        if(mapValeurs != null)
            return mapValeurs;
        else
        {
            mapValeurs = new HashMap<>();
            for (Valeur valeur : valeurs) 
            {
                mapValeurs.put(valeur.getChamp().getName(), valeur.getValue());
            }
            return mapValeurs;
        }
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

    public Priorite getPriorite()
    {
        return priorite;
    }

    public Projet getProjet()
    {
        return projet;
    }

    public Statut getStatut()
    {
        return statut;
    }

    public String getSujet()
    {
        return sujet;
    }

    public Tracker getTracker()
    {
        return tracker;
    }

    public Date getDateMiseAJour()
    {
        return dateMiseAJour;
    }

    public List<Journal> getJournaux()
    {
        return journaux;
    }

    public List<Valeur> getValeurs()
    {
        return valeurs;
    }
}