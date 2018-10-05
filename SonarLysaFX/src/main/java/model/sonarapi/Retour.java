package model.sonarapi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

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
        return listeVues == null ? listeVues = new ArrayList<>() : listeVues;
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
        return results == null ? results = new ArrayList<>() : results;
    }

    @XmlAttribute(name = "more", required = false)
    public boolean isMore()
    {
        return more;
    }

    @XmlAttribute(name = "errors", required = false)
    public List<Message> getErrors()
    {
        return errors == null ? errors = new ArrayList<>() : errors;
    }

    @XmlAttribute(name = "qualitygates", required = false)
    public List<QualityGate> getQualityGates()
    {
        return qualityGates == null ? qualityGates = new ArrayList<>() : qualityGates;
    }

    @XmlAttribute(name = "default", required = false)
    public String getDefaut()
    {
        return getString(defaut);
    }
}
