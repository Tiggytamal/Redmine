package model.rest.sonarapi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.AbstractModele;
import model.enums.TypeDefautSonar;
import model.enums.TypeResolution;
import utilities.adapter.LocalDateTimeSonarAdapter;
import utilities.adapter.TypeResolutionAdapter;

/**
 * Classe de modele pour les erreurs remontées dans SonarQube les proprietes flows et components sont ignorees pour diminuer la taille des flux XML
 * traites.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
@JsonIgnoreProperties(
{ "flows", "organization", "hash", "transitions", "actions" })
public class Issue extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String regle;
    private String severite;
    private String composant;
    private String projet;
    private String sousProjet;
    private int ligne;
    private TextRange textRange;
    private TypeResolution resolution;
    private String statut;
    private String message;
    private String effort;
    private String debt;
    private String auteur;
    private String assigne;
    private List<String> tags;
    private List<Commentaire> commentaires;
    private LocalDateTime dateCreation;
    private LocalDateTime dateMaj;
    private LocalDateTime dateCloture;
    private TypeDefautSonar type;
    private boolean fromHotspot;

    /*---------- CONSTRUCTEURS ----------*/
    public Issue()
    {
        tags = new ArrayList<>();
        commentaires = new ArrayList<>();
        textRange = new TextRange();
    }
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "key")
    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = getString(key);
    }

    @XmlAttribute(name = "rule", required = false)
    public String getRegle()
    {
        return getString(regle);
    }

    public void setRegle(String regle)
    {
        this.regle = getString(regle);
    }

    @XmlAttribute(name = "severity", required = false)
    public String getSeverite()
    {
        return getString(severite);
    }

    public void setSeverite(String severity)
    {
        this.severite = getString(severity);
    }

    @XmlAttribute(name = "component", required = false)
    public String getComposant()
    {
        return getString(composant);
    }

    public void setComposant(String composant)
    {
        this.composant = getString(composant);
    }

    @XmlAttribute(name = "project", required = false)
    public String getProjet()
    {
        return getString(projet);
    }

    public void setProjet(String projet)
    {
        this.projet = getString(projet);
    }
    
    @XmlAttribute(name = "subProject", required = false)
    public String getSousProjet()
    {
        return getString(sousProjet);
    }

    public void setSousProjet(String sousProjet)
    {
        this.sousProjet = getString(sousProjet);
    }

    @XmlAttribute(name = "status", required = false)
    public String getStatut()
    {
        return getString(statut);
    }

    public void setStatut(String statut)
    {
        this.statut = getString(statut);
    }

    @XmlAttribute(name = "message", required = false)
    public String getMessage()
    {
        return getString(message);
    }

    public void setMessage(String message)
    {
        this.message = getString(message);
    }

    @XmlAttribute(name = "author", required = false)
    public String getAuteur()
    {
        return getString(auteur);
    }

    public void setAuteur(String auteur)
    {
        this.auteur = getString(auteur);
    }

    @XmlAttribute(name = "assignee", required = false)
    public String getAssigne()
    {
        return getString(assigne);
    }

    public void setAssigne(String assigne)
    {
        this.assigne = getString(assigne);
    }

    @XmlAttribute(name = "tags", required = false)
    public List<String> getTags()
    {
        if (tags == null)
            tags = new ArrayList<>();
        return tags;
    }

    public void setTags(List<String> tags)
    {
        if (tags != null && !tags.isEmpty())
            this.tags = tags;
    }

    @XmlAttribute(name = "creationDate", required = false)
    @XmlJavaTypeAdapter(value = LocalDateTimeSonarAdapter.class)
    public LocalDateTime getDateCreation()
    {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation)
    {
        if (dateCreation != null)
            this.dateCreation = dateCreation;
    }

    @XmlAttribute(name = "updateDate", required = false)
    @XmlJavaTypeAdapter(value = LocalDateTimeSonarAdapter.class)
    public LocalDateTime getDateMaj()
    {
        return dateMaj;
    }

    public void setDateMaj(LocalDateTime dateMaj)
    {
        if (dateMaj != null)
            this.dateMaj = dateMaj;
    }

    @XmlAttribute(name = "closeDate", required = false)
    @XmlJavaTypeAdapter(value = LocalDateTimeSonarAdapter.class)
    public LocalDateTime getDateCloture()
    {
        return dateCloture;
    }

    public void setDateCloture(LocalDateTime dateCloture)
    {
        if (dateCloture != null)
            this.dateCloture = dateCloture;
    }

    @XmlAttribute(name = "type", required = false)
    public TypeDefautSonar getType()
    {
        return type;
    }

    public void setType(TypeDefautSonar type)
    {
        if (type != null)
            this.type = type;
    }

    @XmlAttribute(name = "resolution", required = false)
    @XmlJavaTypeAdapter(value = TypeResolutionAdapter.class)
    public TypeResolution getResolution()
    {
        return resolution;
    }

    public void setResolution(TypeResolution resolution)
    {
        this.resolution = resolution;
    }

    @XmlAttribute(name = "line", required = false)
    public int getLigne()
    {
        return ligne;
    }

    public void setLigne(int ligne)
    {
        this.ligne = ligne;
    }

    @XmlElement(name = "textRange", required = false)
    public TextRange getTextRange()
    {
        if (textRange == null)
            return new TextRange();
        return textRange;
    }

    public void setTextRange(TextRange textRange)
    {
        if (textRange != null)
            this.textRange = textRange;
    }

    @XmlAttribute(name = "effort", required = false)
    public String getEffort()
    {
        return getString(effort);
    }

    public void setEffort(String effort)
    {
        this.effort = getString(effort);
    }

    @XmlAttribute(name = "comments", required = false)
    public List<Commentaire> getCommentaires()
    {
        if (commentaires == null)
            commentaires = new ArrayList<>();
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires)
    {
        if (commentaires != null && !commentaires.isEmpty())
            this.commentaires = commentaires;
    }

    @XmlAttribute(name = "debt", required = false)
    public String getDebt()
    {
        return getString(debt);
    }

    public void setDebt(String debt)
    {
        this.debt = getString(debt);
    }

    @XmlAttribute(name = "fromHotspot", required = false)
    public boolean isFromHotspot()
    {
        return fromHotspot;
    }

    public void setFromHotspot(boolean fromHotSpot)
    {
        this.fromHotspot = fromHotSpot;
    }
}
