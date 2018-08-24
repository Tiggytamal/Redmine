package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

/**
 * Classe de mod�le pour ajouter une clef dans les param�tres des Webservices Sonar.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Clef extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;

    /*---------- CONSTRUCTEURS ----------*/

    public Clef(String key)
    {
        this.key = key;
    }
    
    public Clef()
    {
        // Constructeur vide pour initialiser des objets sans param�tre et la cr�ation depuis le XML        
    }

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
    
}
