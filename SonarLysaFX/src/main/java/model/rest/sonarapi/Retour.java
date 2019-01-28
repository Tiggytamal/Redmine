package model.rest.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de wrapping des retour JSON. tous les attibuts sont optionnels,pour permettre le retour de n'importe quel type d'objet.
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
@XmlRootElement
public class Retour extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private Composant component;
    private List<Vue> listeVues;
    private StatusProjet statusProjet;
    private List<Vue> results;
    private boolean more;
    private List<Message> errors;
    private List<QualityProfile> qualityGates;
    private String defaut;

    /*---------- CONSTRUCTEURS ----------*/

    public Retour(Composant component, List<Vue> listeVues, StatusProjet statusProjet, List<Vue> results, boolean more, List<Message> errors, List<QualityProfile> qualityGates, String defaut)
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
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "component", required = false)
    public Composant getComponent()
    {
        if (component == null)
            return new Composant();
        return component;
    }

    @XmlAttribute(name = "views", required = false)
    public List<Vue> getListeVues()
    {
        if (listeVues == null)
            listeVues = new ArrayList<>();
        return listeVues;
    }

    @XmlAttribute(name = "projectStatus", required = false)
    public StatusProjet getStatusProjet()
    {
        if (statusProjet == null)
            return new StatusProjet();
        return statusProjet;
    }

    @XmlAttribute(name = "results", required = false)
    public List<Vue> getResults()
    {
        if (results == null)
            results = new ArrayList<>();
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
        if (errors == null)
            errors = new ArrayList<>();
        return errors;
    }

    @XmlAttribute(name = "qualitygates", required = false)
    public List<QualityProfile> getQualityGates()
    {
        if (qualityGates == null)
            qualityGates = new ArrayList<>();
        return qualityGates;
    }

    @XmlAttribute(name = "default", required = false)
    public String getDefaut()
    {
        return getString(defaut);
    }
}
