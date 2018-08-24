package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

/**
 * Classe de modèle pour ajouter une vue depuis le Webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class AjouterVueLocale extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String refKey;

    /*---------- CONSTRUCTEURS ----------*/

    public AjouterVueLocale(String key, String refKey)
    {
        this.key = key;
        this.refKey = refKey;
    }
    
    public AjouterVueLocale()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "key", required = true)
    public String getKey()
    {
        return getString(key);
    }
    
    public void setKey(String key)
    {
        this.key = key;
    }

    @XmlAttribute(name = "ref_key", required = true)
    public String getRefKey()
    {
        return getString(refKey);
    }
    
    public void setRefKey(String refKey)
    {
        this.refKey = refKey;
    }
}
