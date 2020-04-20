package model.rest.sonarapi;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.AbstractModele;
import utilities.adapter.LocalDateTimeSonarAdapter;

/**
 * Classe de modele d'un profile renvoye par SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
@JsonIgnoreProperties({ "actions", "organization" })
public class QualityProfile extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String nom;
    private String langage;
    private String nomLangage;
    private String parentKey;
    private boolean isInherited;
    private boolean isBuiltIn;
    private int compteRegleActive;
    private int compteRegleDepreciee;
    private int compteProjet;
    private boolean isDefaut;
    private LocalDateTime reglesUpdatedAt;
    private LocalDateTime userUpdatedAt;
    private LocalDateTime lastUsed;

    /*---------- CONSTRUCTEURS ----------*/
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

    @XmlAttribute(name = "name")
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String name)
    {
        this.nom = getString(name);
    }

    @XmlAttribute(name = "language")
    public String getLangage()
    {
        return getString(langage);
    }

    public void setLangage(String langage)
    {
        this.langage = getString(langage);
    }

    @XmlAttribute(name = "languageName")
    public String getNomLangage()
    {
        return getString(nomLangage);
    }

    public void setNomLangage(String nomLangage)
    {
        this.nomLangage = getString(nomLangage);
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
        return getString(parentKey);
    }

    public void setParentKey(String parentKey)
    {
        this.parentKey = getString(parentKey);
    }

    @XmlAttribute(name = "activeRuleCount", required = false)
    public int getCompteRegleActive()
    {
        return compteRegleActive;
    }

    public void setCompteRegleActive(int compteRegleActive)
    {
        this.compteRegleActive = compteRegleActive;
    }

    @XmlAttribute(name = "activeDeprecatedRuleCount", required = false)
    public int getCompteRegleDepreciee()
    {
        return compteRegleDepreciee;
    }

    public void setCompteRegleDepreciee(int compteRegleDepreciee)
    {
        this.compteRegleDepreciee = compteRegleDepreciee;
    }

    @XmlAttribute(name = "projectCount", required = false)
    public int getCompteProjet()
    {
        return compteProjet;
    }

    public void setCompteProjet(int compteProjet)
    {
        this.compteProjet = compteProjet;
    }

    @XmlAttribute(name = "isDefault", required = false)
    public boolean isDefaut()
    {
        return isDefaut;
    }

    public void setDefaut(boolean isDefault)
    {
        this.isDefaut = isDefault;
    }

    @XmlAttribute(name = "rulesUpdatedAt", required = false)
    @XmlJavaTypeAdapter(value = LocalDateTimeSonarAdapter.class)
    public LocalDateTime getReglesUpdatedAt()
    {
        return reglesUpdatedAt;
    }

    public void setReglesUpdatedAt(LocalDateTime reglesUpdatedAt)
    {
        if (reglesUpdatedAt != null)
            this.reglesUpdatedAt = reglesUpdatedAt;
    }

    @XmlAttribute(name = "userUpdatedAt", required = false)
    @XmlJavaTypeAdapter(value = LocalDateTimeSonarAdapter.class)
    public LocalDateTime getUserUpdatedAt()
    {
        return userUpdatedAt;
    }

    public void setUserUpdatedAt(LocalDateTime userUpdatedAt)
    {
        if (userUpdatedAt != null)
            this.userUpdatedAt = userUpdatedAt;
    }

    @XmlAttribute(name = "lastUsed", required = false)
    @XmlJavaTypeAdapter(value = LocalDateTimeSonarAdapter.class)
    public LocalDateTime getLastUsed()
    {
        return lastUsed;
    }

    public void setLastUsed(LocalDateTime lastUsed)
    {
        if (lastUsed != null)
            this.lastUsed = lastUsed;
    }
}
