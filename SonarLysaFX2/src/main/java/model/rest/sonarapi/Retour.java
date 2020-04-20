package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.AbstractModele;

/**
 * Classe de wrapping des retour JSON. Tous les attibuts sont optionnels pour permettre l'utilisation avec n'importe quel webservice.
 * Il n'y a pas de setter du fait que la classe est toujours generee par l'API du webservice.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
@JsonIgnoreProperties(
{ "ancestors" })
public class Retour extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private ComposantSonar composant;
    private List<Message> erreurs;
    private List<Branche> branches;
    private StatutQGProjet statutProjet;
    private boolean exist;

    /*---------- CONSTRUCTEURS ----------*/

    public Retour()
    {
        erreurs = new ArrayList<>();
        branches = new ArrayList<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlElement(name = "component", required = false)
    public ComposantSonar getComposant()
    {
        return composant;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "errors", required = false)
    public List<Message> getErreurs()
    {
        if (erreurs == null)
            erreurs = new ArrayList<>();
        return erreurs;
    }

    @XmlElementWrapper
    @XmlAttribute(name = "branches", required = false)
    public List<Branche> getBranches()
    {
        if (branches == null)
            branches = new ArrayList<>();
        return branches;
    }

    @XmlElement(name = "projectStatus", required = false)
    public StatutQGProjet getStatutProjet()
    {
        return statutProjet;
    }

    @XmlAttribute(name = "exist", required = false)
    public boolean isExist()
    {
        return exist;
    }
}
