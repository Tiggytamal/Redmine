package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message
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
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML     
	}
	
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
	
	@XmlAttribute (name = "msg")
	public String getMsg()
	{
		return msg;
	}
	
	public void setMsg(String msg)
	{
	    this.msg = msg;
	}
}
