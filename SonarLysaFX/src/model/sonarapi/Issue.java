package model.sonarapi;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Classe de modèle pour les erreurs remontées dans SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
@XmlRootElement
@JsonIgnoreProperties({"flows","components"})
public class Issue implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String resolution;
    private int line;
    private TextRange textRange;
    private String effort;
    private String debt;
    private List<Commentaire> commentaires;
    private String attr;
    private List<String> transitions;
    private List<String> actions;
    private String rule;
    private String severity;
    private String composant;
    private String composantId;
    private String projet;
    private String subProject;
    private String status;
    private String message;
    private String auteur;
    private List<String> tags;
    private List<Flow> flows;
    private String creationDate;
    private String updateDate;
    private String closeDate;
    private String type;
    private List<Composant> composants;
    private List<Rules> rules;
    private List<User> users;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "key")
    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @XmlAttribute(name = "rule", required = false)
    public String getRule()
    {
        return rule;
    }

    public void setRule(String rule)
    {
        this.rule = rule;
    }

    @XmlAttribute(name = "severity", required = false)
    public String getSeverity()
    {
        return severity;
    }

    public void setSeverity(String severity)
    {
        this.severity = severity;
    }

    @XmlAttribute(name = "component", required = false)
    public String getComposant()
    {
        return composant;
    }

    public void setComposant(String composant)
    {
        this.composant = composant;
    }

    @XmlAttribute(name = "componentId", required = false)
    public String getComposantId()
    {
        return composantId;
    }

    public void setComposantId(String composantId)
    {
        this.composantId = composantId;
    }

    @XmlAttribute(name = "project", required = false)
    public String getProjet()
    {
        return projet;
    }

    public void setProjet(String projet)
    {
        this.projet = projet;
    }

    @XmlAttribute(name = "status", required = false)
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @XmlAttribute(name = "message", required = false)
    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @XmlAttribute(name = "author", required = false)
    public String getAuteur()
    {
        return auteur;
    }

    public void setAuteur(String auteur)
    {
        this.auteur = auteur;
    }

    @XmlAttribute(name = "tags", required = false)
    public List<String> getTags()
    {
        return tags;
    }

    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }

    @XmlAttribute(name = "creationDate", required = false)
    public String getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(String creationDate)
    {
        this.creationDate = creationDate;
    }

    @XmlAttribute(name = "updateDate", required = false)
    public String getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate(String updateDate)
    {
        this.updateDate = updateDate;
    }

    @XmlAttribute(name = "type", required = false)
    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @XmlAttribute(name = "resolution", required = false)
    public String getResolution()
    {
        return resolution;
    }

    public void setResolution(String resolution)
    {
        this.resolution = resolution;
    }

    @XmlAttribute(name = "line", required = false)
    public int getLine()
    {
        return line;
    }

    public void setLine(int line)
    {
        this.line = line;
    }

    @XmlAttribute(name = "textRange", required = false)
    public TextRange getTextRange()
    {
        return textRange;
    }

    public void setTextRange(TextRange textRange)
    {
        this.textRange = textRange;
    }

    @XmlAttribute(name = "effort", required = false)
    public String getEffort()
    {
        return effort;
    }

    public void setEffort(String effort)
    {
        this.effort = effort;
    }

    @XmlAttribute(name = "comments", required = false)
    public List<Commentaire> getCommentaires()
    {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires)
    {
        this.commentaires = commentaires;
    }

    @XmlAttribute(name = "attr", required = false)
    public String getAttr()
    {
        return attr;
    }

    public void setAttr(String attr)
    {
        this.attr = attr;
    }

    @XmlAttribute(name = "transitions", required = false)
    public List<String> getTransitions()
    {
        return transitions;
    }

    public void setTransitions(List<String> transitions)
    {
        this.transitions = transitions;
    }

    @XmlAttribute(name = "actions", required = false)
    public List<String> getActions()
    {
        return actions;
    }

    public void setActions(List<String> actions)
    {
        this.actions = actions;
    }

    @XmlAttribute(name = "components", required = false)
    public List<Composant> getComposants()
    {
        return composants;
    }

    public void setComposants(List<Composant> composants)
    {
        this.composants = composants;
    }

    @XmlAttribute(name = "rules", required = false)
    public List<Rules> getRules()
    {
        return rules;
    }

    public void setRules(List<Rules> rules)
    {
        this.rules = rules;
    }

    @XmlAttribute(name = "users", required = false)
    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }

    @XmlAttribute(name = "subProject", required = false)
    public String getSubProject()
    {
        return subProject;
    }

    public void setSubProject(String subProject)
    {
        this.subProject = subProject;
    }

    @XmlAttribute(name = "debt", required = false)
    public String getDebt()
    {
        return debt;
    }

    public void setDebt(String debt)
    {
        this.debt = debt;
    }

    @XmlAttribute(name = "flows", required = false)
    public List<Flow> getFlows()
    {
        return flows;
    }

    public void setFlows(List<Flow> flows)
    {
        this.flows = flows;
    }

    @XmlAttribute(name = "closeDate", required = false)
    public String getCloseDate()
    {
        return closeDate;
    }

    public void setCloseDate(String closeDate)
    {
        this.closeDate = closeDate;
    }

}