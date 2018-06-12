package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Validation implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private boolean valid;

    /*---------- CONSTRUCTEURS ----------*/

    public Validation(boolean valid)
    {
        this.valid = valid;
    }

    public Validation()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "valid", required = true)
    public boolean isValid()
    {
        return valid;
    }

    public void setValid(final boolean valid)
    {
        this.valid = valid;
    }
}