package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

/**
 * Classe de mod�le repr�sentant un message en retour du webservice Sonar.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Message extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String msg;

    /*---------- CONSTRUCTEURS ----------*/

    public Message(String msg)
    {
        this.msg = msg;
    }

    public Message()
    {
        // Constructeur vide pour initialiser des objets sans param�tre et la cr�ation depuis le XML
    }

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
        this.msg = msg;
    }
}
