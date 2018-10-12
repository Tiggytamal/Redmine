package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle représentant la validation d'un utilisateur en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Validation extends AbstractModele implements ModeleSonar
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
