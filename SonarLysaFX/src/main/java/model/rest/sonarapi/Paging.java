package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;

/**
 * Classe de modèle représentant la pagination des retours des webservices Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class Paging extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private int pageIndex;
    private int pageSize;
    private int total;

    /*---------- CONSTRUCTEURS ----------*/

    public Paging(int pageIndex, int pageSize, int total)
    {
        super();
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.total = total;
    }

    public Paging()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "pageIndex")
    public int getPageIndex()
    {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex)
    {
        this.pageIndex = pageIndex;
    }

    @XmlAttribute(name = "pageSize")
    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    @XmlAttribute(name = "total")
    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }
}
