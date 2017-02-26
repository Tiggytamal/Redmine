package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the enumerations database table.
 * 
 */
@Entity
@Table(name="enumerations")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Priorite.findAll", query="SELECT e FROM Priorite e")
})
//@formatter:on

public final class Priorite implements Serializable
{
    /* Attributes */

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

	@Column (name = "name", length = 30, nullable = false)
	private String nom;
	
    /* Constructors */
	
	public Priorite() 
	{
	}

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }
    
    
    /* Methods */
    
    /* Access */

    /**
     * @return the nom
     */
    public String getNom()
    {
        return nom;
    }
}