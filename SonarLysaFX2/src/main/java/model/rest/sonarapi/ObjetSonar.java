package model.rest.sonarapi;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import model.AbstractModele;
import model.KeyDateMEP;
import model.enums.TypeObjetSonar;
import utilities.Statics;

/**
 * Classe de modele pour la gestion des objets SonarQube 6+.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
@XmlRootElement
public class ObjetSonar extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String nom;
    private String key;
    private String description;
    private String visibility;
    private TypeObjetSonar type;

    /*---------- CONSTRUCTEURS ----------*/

    public ObjetSonar(String key, String name, String description, TypeObjetSonar type)
    {
        if (key == null || key.isEmpty() || name == null || name.isEmpty() || type == null)
            throw new IllegalArgumentException("model.rest.sonarapi.Portfolio  Constructeur : argument nulls ou vides");
        this.key = key;
        this.nom = name;
        this.description = getString(description);
        visibility = "public";
        this.type = type;
    }

    ObjetSonar()
    {
        // Constructeur vide pour la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Contrôle qu'un objet Sonar n'est pas nul, et qu'il a bien sa clef et son nom renseignés.
     * 
     * @param objet
     *              L'objet à controler.
     * @return
     *         Vrai si l'objet est bien instancié. (nom et clef non ulle ou vide).
     */
    public static boolean controle(ObjetSonar objet)
    {
        return objet != null && !objet.getKey().isEmpty() && !objet.getNom().isEmpty();
    }

    /**
     * Création d'un ObjetSonar depuis un composant.
     * 
     * @param compo
     *              Composant à traiter.
     * @return
     *         L'ObjetSonar.
     */
    public static ObjetSonar from(ComposantSonar compo)
    {
        return new ObjetSonar(compo.getKey(), compo.getNom(), compo.getDescription(), compo.getType());
    }

    /**
     * Création d'un ObjetSonar depuis une KeyDateMEP). Pour la gestion des vues trimestrielles.
     * 
     * @param kdMep
     *              Objet à traiter.
     * @return
     *         L'ObjetSonar.
     */
    public static ObjetSonar from(KeyDateMEP kdMep)
    {
        return new ObjetSonar(kdMep.getCle(), kdMep.getNom(), Statics.EMPTY, TypeObjetSonar.PROJECT);
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + Objects.hash(nom, key, visibility, type);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        //@formatter:off
        if (!super.equals(obj))
            return false;
        ObjetSonar other = (ObjetSonar) obj;
        return Objects.equals(nom, other.nom) 
                && Objects.equals(key, other.key)
                && Objects.equals(description, other.description) 
                && Objects.equals(visibility, other.visibility) 
                && type == other.type;
      //@formatter:on
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "name")
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String name)
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

    public void setDescription(String description)
    {
        this.description = getString(description);
    }

    @XmlAttribute(name = "visibility")
    public String getVisibility()
    {
        return getString(visibility);
    }

    public void setVisibility(String visibility)
    {
        this.visibility = getString(visibility);
    }

    @XmlTransient
    public TypeObjetSonar getType()
    {
        if (type == null)
            type = TypeObjetSonar.PROJECT;
        return type;
    }
}
