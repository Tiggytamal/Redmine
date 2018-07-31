package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Location
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
        return textRange;
    }

    public void setTextRange(TextRange textRange)
    {
        this.textRange = textRange;
    }

    @XmlAttribute(name = "msg")
    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
