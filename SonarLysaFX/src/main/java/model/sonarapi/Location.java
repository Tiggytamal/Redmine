package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

/**
 * Classe de modèle représentant l'emplacement d'une anomalie dans le code en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Location extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private TextRange textRange;
    private String msg;

    /*---------- CONSTRUCTEURS ----------*/

    public Location(TextRange textRange, String msg)
    {
        super();
        this.textRange = textRange;
        this.msg = msg;
    }

    public Location()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "textRange")
    public TextRange getTextRange()
    {
        if (textRange == null)
            return new TextRange();
        return textRange;
    }

    public void setTextRange(TextRange textRange)
    {
        this.textRange = textRange;
    }

    @XmlAttribute(name = "msg")
    public String getMsg()
    {
        return getString(msg);
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
