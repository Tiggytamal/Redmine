package model.sonarapi;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe de wrapping des retour JSON. tous les attibuts sont optionnels,pour permettre le retour de n'importe quel type d'objet.
 * 
 * @author ETP8137 - Gr�goire Mathon
 *
 */
@XmlRootElement
public class Retour implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private Composant component;
    private List<Vue> listeVues;
    private StatusProjet statusProjet;
    private List<Vue> results;
    private boolean more;
    private List<Message> errors;
    private List<QualityGate> qualityGates;
    private String defaut;

    /*---------- CONSTRUCTEURS ----------*/

    public Retour(Composant component, List<Vue> listeVues, StatusProjet statusProjet, List<Vue> results, boolean more, List<Message> errors, List<QualityGate> qualityGates, String defaut)
    {
        super();
        this.component = component;
        this.listeVues = listeVues;
        this.statusProjet = statusProjet;
        this.results = results;
        this.more = more;
        this.errors = errors;
        this.qualityGates = qualityGates;
        this.defaut = defaut;
    }

    public Retour()
    {
        // Constructeur vide pour initialiser des objets sans param�tre et la cr�ation depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "component", required = false)
    public Composant getComponent()
    {
        return component;
    }

    @XmlAttribute(name = "views", required = false)
    public List<Vue> getListeVues()
    {
        return listeVues;
    }

    @XmlAttribute(name = "projectStatus", required = false)
    public StatusProjet getStatusProjet()
    {
        return statusProjet;
    }

    @XmlAttribute(name = "results", required = false)
    public List<Vue> getResults()
    {
        return results;
    }

    @XmlAttribute(name = "more", required = false)
    public boolean isMore()
    {
        return more;
    }

    @XmlAttribute(name = "errors", required = false)
    public List<Message> getErrors()
    {
        return errors;
    }

    @XmlAttribute(name = "qualitygates", required = false)
    public List<QualityGate> getQualityGates()
    {
        return qualityGates;
    }

    @XmlAttribute(name = "default", required = false)
    public String getDefaut()
    {
        return defaut;
    }
}
