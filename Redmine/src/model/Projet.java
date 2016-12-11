package model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the projects database table.
 * 
 */
@Entity
@Table(name="projects")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Projet.findAll", query="SELECT p FROM Projet p")
})
//@formatter:on
public class Projet implements Serializable
{
    /* Attributes */
    
	private static final long serialVersionUID = 1L;

	/** Id auto_incr�ment� */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

	@Column (name = "identifier")
	private String identifier;

	@Column (name = "name")
	private String nom;

	@OneToMany (mappedBy = "projetParent", targetEntity = Projet.class, fetch = FetchType.LAZY)
	private List<Projet> projetsderives;

    @ManyToOne (targetEntity = Projet.class, optional = true, fetch = FetchType.LAZY)
    @JoinColumn (name = "parent_id")
	private Projet projetParent;
	
	@ManyToMany (targetEntity = Champ.class, fetch = FetchType.LAZY)
	@JoinTable (name = "custom_fields_projects", joinColumns =@JoinColumn(name = "project_id"), inverseJoinColumns=@JoinColumn(name ="custom_field_id"))
	private List<Champ> champs;
	
	@OneToMany (mappedBy = "projet", targetEntity = Incident.class, fetch = FetchType.LAZY)
	private List<Incident> incidents;

	/* Constructors */
	
	public Projet() {
	}
	
	/*Methods */
	
    @Override
    public String toString()
    {
        return "Projet [id=" + id + ", identifier=" + identifier + ", nom=" + nom + ", derives=" + projetsderives + ", parent=" + projetParent + ", champs=" + champs + "]";
    }
    
    /* Access */

    public int getId() 
    {
		return id;
	}

    public String getIdentifier() 
	{
		return identifier;
	}

	public String getNom() 
	{
		return nom;
	}
	
	public List<Champ> getChamps()
	{
	    return champs;
	}
	
	   
    public List<Projet> getDerives()
    {
        return projetsderives;
    }

    public Projet getParent()
    {
        return projetParent;
    }
}