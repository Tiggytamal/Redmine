package model.sonarapi;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

/**
 * Classe de modèle représentant une vue en retour ou à l'envoie du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Vue extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String name;
    private boolean selected;
    private String selectionMode;
    private String manual_measure_key;
    private String manual_measure_value;
    private String description;
    private List<String> listeClefsComposants;

    /*---------- CONSTRUCTEURS ----------*/

    public Vue()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    public Vue(String key, String nom)
    {
        this.key = key;
        this.name = nom;
    }

    public Vue(String key, String name, boolean selected, String selectionMode, String description, List<String> listeClefsComposants)
    {
        super();
        this.key = key;
        this.name = name;
        this.selected = selected;
        this.selectionMode = selectionMode;
        this.description = description;
        this.listeClefsComposants = listeClefsComposants;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Contrôle qu'une vue n'est pas nulle, et qu'elle a bien sa clef et so nom renseignés
     * 
     * @param vue
     * @return
     */
    public static boolean controleVue(Vue vue)
    {
        return (vue != null && vue.getKey() != null && !vue.getKey().isEmpty() && vue.getName() != null && !vue.getName().isEmpty());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "key")
    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @XmlAttribute(name = "name")
    public String getName()
    {
        return getString(name);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlAttribute(name = "selected", required = false)
    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    @XmlAttribute(name = "selectionMode", required = false)
    public String getSelectionMode()
    {
        return getString(selectionMode);
    }

    public void setSelectionMode(String selectionMode)
    {
        this.selectionMode = selectionMode;
    }

    @XmlAttribute(name = "desc", required = false)
    public String getDescription()
    {
        return getString(description);
    }

    public void setDescription(String desc)
    {
        this.description = desc;
    }

    @XmlAttribute(name = "projects", required = false)
    public List<String> getListeClefsComposants()
    {
        return getList(listeClefsComposants);
    }

    public void setListeClefsComposants(List<String> listeClefsComposants)
    {
        this.listeClefsComposants = listeClefsComposants;
    }
    
    @XmlAttribute(name = "manual_measure_key", required = false)
    public String getManual_measure_key()
    {
        return manual_measure_key;
    }

    public void setManual_measure_key(String manual_measure_key)
    {
        this.manual_measure_key = manual_measure_key;
    }

    @XmlAttribute(name = "manual_measure_value", required = false)
    public String getManual_measure_value()
    {
        return manual_measure_value;
    }

    public void setManual_measure_value(String manual_measure_value)
    {
        this.manual_measure_value = manual_measure_value;
    }
}
