package model.rest.sonarapi67;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle d'un profile renvoyé par SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
@JsonIgnoreProperties({ "actions" })
public class QualityProfile extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String name;
    private String language;
    private String languageName;
    private String parentKey;
    private boolean isInherited;
    private boolean isBuiltIn;
    private int activeRuleCount;
    private int activeDeprecatedRuleCount;
    private int projectCount;
    private boolean isDefault;
    private String rulesUpdatedAt;
    private String userUpdatedAt;
    private String lastUsed;
    private List<Action> actions;
    private String organization;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "key", required = false)
    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @XmlAttribute(name = "name", required = false)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlAttribute(name = "language", required = false)
    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    @XmlAttribute(name = "languageName", required = false)
    public String getLanguageName()
    {
        return languageName;
    }

    public void setLanguageName(String languageName)
    {
        this.languageName = languageName;
    }

    @XmlAttribute(name = "isInherited", required = false)
    public boolean isInherited()
    {
        return isInherited;
    }

    public void setInherited(boolean isInherited)
    {
        this.isInherited = isInherited;
    }

    @XmlAttribute(name = "isBuiltIn", required = false)
    public boolean isBuiltIn()
    {
        return isBuiltIn;
    }

    public void setBuiltIn(boolean isBuiltIn)
    {
        this.isBuiltIn = isBuiltIn;
    }

    @XmlAttribute(name = "parentKey", required = false)
    public String getParentKey()
    {
        return parentKey;
    }

    public void setParentKey(String parentKey)
    {
        this.parentKey = parentKey;
    }

    @XmlAttribute(name = "activeRuleCount", required = false)
    public int getActiveRuleCount()
    {
        return activeRuleCount;
    }

    public void setActiveRuleCount(int activeRuleCount)
    {
        this.activeRuleCount = activeRuleCount;
    }

    @XmlAttribute(name = "activeDeprecatedRuleCount", required = false)
    public int getActiveDeprecatedRuleCount()
    {
        return activeDeprecatedRuleCount;
    }

    public void setActiveDeprecatedRuleCount(int activeDeprecatedRuleCount)
    {
        this.activeDeprecatedRuleCount = activeDeprecatedRuleCount;
    }

    @XmlAttribute(name = "projectCount", required = false)
    public int getProjectCount()
    {
        return projectCount;
    }

    public void setProjectCount(int projectCount)
    {
        this.projectCount = projectCount;
    }

    @XmlAttribute(name = "isDefault", required = false)
    public boolean isDefault()
    {
        return isDefault;
    }

    public void setDefault(boolean isDefault)
    {
        this.isDefault = isDefault;
    }

    @XmlAttribute(name = "rulesUpdatedAt", required = false)
    public String getRulesUpdatedAt()
    {
        return rulesUpdatedAt;
    }

    public void setRulesUpdatedAt(String rulesUpdatedAt)
    {
        this.rulesUpdatedAt = rulesUpdatedAt;
    }
    
    @XmlAttribute(name = "userUpdatedAt", required = false)
    public String getUserUpdatedAt()
    {
        return userUpdatedAt;
    }

    public void setUserUpdatedAt(String userUpdatedAt)
    {
        this.userUpdatedAt = userUpdatedAt;
    }

    @XmlAttribute(name = "lastUsed", required = false)
    public String getLastUsed()
    {
        return lastUsed;
    }

    public void setLastUsed(String lastUsed)
    {
        this.lastUsed = lastUsed;
    }

    @XmlAttribute(name = "actions", required = false)
    @XmlElementWrapper
    public List<Action> getActions()
    {
        return actions;
    }

    public void setActions(List<Action> actions)
    {
        this.actions = actions;
    }

    @XmlAttribute(name = "organization", required = false)
    public String getOrganization()
    {
        return organization;
    }

    public void setOrganization(String organization)
    {
        this.organization = organization;
    }

}
