package model.rest.sonarapi67;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle pour la gestion des PortFolio SonarQube 6+
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class Portfolio extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String nom;
    private String key;
    private String description;
    private String visibility;

    /*---------- CONSTRUCTEURS ----------*/

    public Portfolio(String key, String name, String description)
    {
        if (key == null || key.isEmpty() || name == null || name.isEmpty())
            throw new IllegalArgumentException("model.rest.sonarapi67.Portfolio  Constructeur : argument nulls ou vides");
        this.key = key;
        this.nom = name;
        this.description = getString(description);
        visibility = "public";
    }
    
    public Portfolio()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Contrôle qu'un portfolio n'est pas nulle, et qu'il a bien sa clef et son nom renseignés
     * 
     * @param pf
     * @return
     */
    public static boolean controle(Portfolio pf)
    {
        return (pf != null && !pf.getKey().isEmpty() && !pf.getNom().isEmpty());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "name")
    public String getNom()
    {
        return getString(nom);
    }

    public void setName(String name)
    {
        this.nom = getString(name);
    }
    
    @XmlAttribute(name = "key")
    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = getString(key);
    }

    @XmlAttribute(name = "description")
    public String getDescription()
    {
        return getString(description);
    }

    @XmlAttribute(name = "visibility")
    public void setDescription(String description)
    {
        this.description = getString(description);
    }

    public String getVisibility()
    {
        return visibility;
    }


    public void setVisibility(String visibility)
    {
        this.visibility = visibility;
    }
}
