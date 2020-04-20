package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe de modele représentant un message en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Message extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String msg;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "msg")
    public String getMsg()
    {
        return getString(msg);
    }

    public void setMsg(String msg)
    {
        this.msg = getString(msg);
    }
}
