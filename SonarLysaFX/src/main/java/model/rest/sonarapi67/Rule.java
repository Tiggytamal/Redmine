package model.rest.sonarapi67;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle représentant une règle SonarQube depuis le Webservice
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class Rule extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String repo;
    private String name;
    private String createdAt;
    private String htmlDesc;
    private String mdDesc;
    private String severity;
    private String status;
    private String internalKey;
    private boolean isTemplate;
    private List<String> tags;
    private List<String> sysTags;
    private String lang;
    private String langName;
    private List<Param> params;
    private String defaultDebtRemFnType;
    private String defaultDebtRemFnCoeff;
    private String defaultDebtRemFnOffset;
    private String effortToFixDescription;
    private boolean debtOverloaded;
    private String debtRemFnType;
    private String debtRemFnCoeff;
    private String debtRemFnOffset;
    private String defaultRemFnType;
    private String defaultRemFnGapMultiplier;
    private String defaultRemFnBaseEffort;
    private String remFnType;
    private String remFnGapMultiplier;
    private String remFnBaseEffort;
    private boolean remFnOverloaded;
    private String gapDescription;
    private String scope;
    private boolean isExternal;
    private String type;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "key", required = false)
    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = getString(key);
    }

    @XmlAttribute(name = "repo", required = false)
    public String getRepo()
    {
        return getString(repo);
    }

    public void setRepo(String repo)
    {
        this.repo = getString(repo);
    }

    @XmlAttribute(name = "name", required = false)
    public String getName()
    {
        return getString(name);
    }

    public void setName(String name)
    {
        this.name = getString(name);
    }

    @XmlAttribute(name = "createdAt", required = false)
    public String getCreatedAt()
    {
        return getString(createdAt);
    }

    public void setCreatedAt(String createdAt)
    {
        this.createdAt = getString(createdAt);
    }

    @XmlAttribute(name = "htmlDesc", required = false)
    public String getHtmlDesc()
    {
        return getString(htmlDesc);
    }

    public void setHtmlDesc(String htmlDesc)
    {
        this.htmlDesc = getString(htmlDesc);
    }

    @XmlAttribute(name = "mdDesc", required = false)
    public String getMdDesc()
    {
        return getString(mdDesc);
    }

    public void setMdDesc(String mdDesc)
    {
        this.mdDesc = getString(mdDesc);
    }

    @XmlAttribute(name = "severity", required = false)
    public String getSeverity()
    {
        return getString(severity);
    }

    public void setSeverity(String severity)
    {
        this.severity = getString(severity);
    }

    @XmlAttribute(name = "status", required = false)
    public String getStatus()
    {
        return getString(status);
    }

    public void setStatus(String status)
    {
        this.status = getString(status);
    }

    @XmlAttribute(name = "internalKey", required = false)
    public String getInternalKey()
    {
        return getString(internalKey);
    }

    public void setInternalKey(String internalKey)
    {
        this.internalKey = getString(internalKey);
    }

    @XmlAttribute(name = "isTemplate", required = false)
    public boolean isTemplate()
    {
        return isTemplate;
    }

    public void setTemplate(boolean isTemplate)
    {
        this.isTemplate = isTemplate;
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
        if (tags != null)
            this.tags = tags;
    }

    @XmlAttribute(name = "sysTags", required = false)
    public List<String> getSysTags()
    {
        if (sysTags == null)
            sysTags = new ArrayList<>();
        return sysTags;
    }

    public void setSysTags(List<String> sysTags)
    {
        if (sysTags != null)
            this.sysTags = sysTags;
    }

    @XmlAttribute(name = "lang", required = false)
    public String getLang()
    {
        return getString(lang);
    }

    public void setLang(String lang)
    {
        this.lang = getString(lang);
    }

    @XmlAttribute(name = "langName", required = false)
    public String getLangName()
    {
        return getString(langName);
    }

    public void setLangName(String langName)
    {
        this.langName = getString(langName);
    }

    @XmlAttribute(name = "params", required = false)
    @XmlElementWrapper
    public List<Param> getParams()
    {
        if (params == null)
            params = new ArrayList<>();
        return params;
    }

    public void setParams(List<Param> params)
    {
        if (params != null)
            this.params = params;
    }

    @XmlAttribute(name = "defaultDebtRemFnType", required = false)
    public String getDefaultDebtRemFnType()
    {
        return getString(defaultDebtRemFnType);
    }

    public void setDefaultDebtRemFnType(String defaultDebtRemFnType)
    {
        this.defaultDebtRemFnType = getString(defaultDebtRemFnType);
    }

    @XmlAttribute(name = "defaultDebtRemFnCoeff", required = false)
    public String getDefaultDebtRemFnCoeff()
    {
        return getString(defaultDebtRemFnCoeff);
    }

    public void setDefaultDebtRemFnCoeff(String defaultDebtRemFnCoeff)
    {
        this.defaultDebtRemFnCoeff = getString(defaultDebtRemFnCoeff);
    }

    @XmlAttribute(name = "defaultDebtRemFnOffset", required = false)
    public String getDefaultDebtRemFnOffset()
    {
        return getString(defaultDebtRemFnOffset);
    }

    public void setDefaultDebtRemFnOffset(String defaultDebtRemFnOffset)
    {
        this.defaultDebtRemFnOffset = getString(defaultDebtRemFnOffset);
    }

    @XmlAttribute(name = "effortToFixDescription", required = false)
    public String getEffortToFixDescription()
    {
        return getString(effortToFixDescription);
    }

    public void setEffortToFixDescription(String effortToFixDescription)
    {
        this.effortToFixDescription = getString(effortToFixDescription);
    }

    @XmlAttribute(name = "debtOverloaded", required = false)
    public boolean isDebtOverloaded()
    {
        return debtOverloaded;
    }

    public void setDebtOverloaded(boolean debtOverloaded)
    {
        this.debtOverloaded = debtOverloaded;
    }

    @XmlAttribute(name = "debtRemFnType", required = false)
    public String getDebtRemFnType()
    {
        return getString(debtRemFnType);
    }

    public void setDebtRemFnType(String debtRemFnType)
    {
        this.debtRemFnType = getString(debtRemFnType);
    }

    @XmlAttribute(name = "debtRemFnCoeff", required = false)
    public String getDebtRemFnCoeff()
    {
        return getString(debtRemFnCoeff);
    }

    public void setDebtRemFnCoeff(String debtRemFnCoeff)
    {
        this.debtRemFnCoeff = getString(debtRemFnCoeff);
    }

    @XmlAttribute(name = "debtRemFnOffset", required = false)
    public String getDebtRemFnOffset()
    {
        return getString(debtRemFnOffset);
    }

    public void setDebtRemFnOffset(String debtRemFnOffset)
    {
        this.debtRemFnOffset = getString(debtRemFnOffset);
    }

    @XmlAttribute(name = "defaultRemFnType", required = false)
    public String getDefaultRemFnType()
    {
        return getString(defaultRemFnType);
    }

    public void setDefaultRemFnType(String defaultRemFnType)
    {
        this.defaultRemFnType = getString(defaultRemFnType);
    }

    @XmlAttribute(name = "defaultRemFnGapMultiplier", required = false)
    public String getDefaultRemFnGapMultiplier()
    {
        return getString(defaultRemFnGapMultiplier);
    }

    public void setDefaultRemFnGapMultiplier(String defaultRemFnGapMultiplier)
    {
        this.defaultRemFnGapMultiplier = getString(defaultRemFnGapMultiplier);
    }

    @XmlAttribute(name = "defaultRemFnBaseEffort", required = false)
    public String getDefaultRemFnBaseEffort()
    {
        return getString(defaultRemFnBaseEffort);
    }

    public void setDefaultRemFnBaseEffort(String defaultRemFnBaseEffort)
    {
        this.defaultRemFnBaseEffort = getString(defaultRemFnBaseEffort);
    }

    @XmlAttribute(name = "remFnType", required = false)
    public String getRemFnType()
    {
        return getString(remFnType);
    }

    public void setRemFnType(String remFnType)
    {
        this.remFnType = getString(remFnType);
    }

    @XmlAttribute(name = "remFnGapMultiplier", required = false)
    public String getRemFnGapMultiplier()
    {
        return getString(remFnGapMultiplier);
    }

    public void setRemFnGapMultiplier(String remFnGapMultiplier)
    {
        this.remFnGapMultiplier = getString(remFnGapMultiplier);
    }

    @XmlAttribute(name = "remFnBaseEffort", required = false)
    public String getRemFnBaseEffort()
    {
        return remFnBaseEffort;
    }

    public void setRemFnBaseEffort(String remFnBaseEffort)
    {
        this.remFnBaseEffort = getString(remFnBaseEffort);
    }

    @XmlAttribute(name = "remFnOverloaded", required = false)
    public boolean isRemFnOverloaded()
    {
        return remFnOverloaded;
    }

    public void setRemFnOverloaded(boolean remFnOverloaded)
    {
        this.remFnOverloaded = remFnOverloaded;
    }

    @XmlAttribute(name = "gapDescription", required = false)
    public String getGapDescription()
    {
        return getString(gapDescription);
    }

    public void setGapDescription(String gapDescription)
    {
        this.gapDescription = getString(gapDescription);
    }

    @XmlAttribute(name = "scope", required = false)
    public String getScope()
    {
        return getString(scope);
    }

    public void setScope(String scope)
    {
        this.scope = getString(scope);
    }

    @XmlAttribute(name = "isExternal", required = false)
    public boolean isExternal()
    {
        return isExternal;
    }

    public void setExternal(boolean isExternal)
    {
        this.isExternal = isExternal;
    }

    @XmlAttribute(name = "type", required = false)
    public String getType()
    {
        return getString(type);
    }

    public void setType(String type)
    {
        this.type = getString(type);
    }

}
