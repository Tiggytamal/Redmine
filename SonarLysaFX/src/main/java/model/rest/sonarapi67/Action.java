package model.rest.sonarapi67;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle d'une action d'un profile SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
public class Action extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private boolean edit;
    private boolean setAsDefault;
    private boolean copy;
    private boolean delete;
    private boolean associateProjects;
    private boolean create;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "create", required = false)
    public boolean isCreate()
    {
        return create;
    }

    public void setCreate(boolean create)
    {
        this.create = create;
    }

    @XmlAttribute(name = "edit", required = false)
    public boolean isEdit()
    {
        return edit;
    }

    public void setEdit(boolean edit)
    {
        this.edit = edit;
    }

    @XmlAttribute(name = "setAsDefault", required = false)
    public boolean isSetAsDefault()
    {
        return setAsDefault;
    }

    public void setSetAsDefault(boolean setAsDefault)
    {
        this.setAsDefault = setAsDefault;
    }

    @XmlAttribute(name = "copy", required = false)
    public boolean isCopy()
    {
        return copy;
    }

    public void setCopy(boolean copy)
    {
        this.copy = copy;
    }

    @XmlAttribute(name = "delete", required = false)
    public boolean isDelete()
    {
        return delete;
    }

    public void setDelete(boolean delete)
    {
        this.delete = delete;
    }

    @XmlAttribute(name = "associateProjects", required = false)
    public boolean isAssociateProjects()
    {
        return associateProjects;
    }

    public void setAssociateProjects(boolean associateProjects)
    {
        this.associateProjects = associateProjects;
    }
}
