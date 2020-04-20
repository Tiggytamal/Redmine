package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe de modele représentant la taille d'une anomalie en ligne de texte en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class TextRange extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private int startLine;
    private int endLine;
    private int startOffset;
    private int endOffset;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "startLine")
    public int getStartLine()
    {
        return startLine;
    }

    public void setStartLine(int startLine)
    {
        this.startLine = startLine;
    }

    @XmlAttribute(name = "endLine")
    public int getEndLine()
    {
        return endLine;
    }

    public void setEndLine(int endLine)
    {
        this.endLine = endLine;
    }

    @XmlAttribute(name = "startOffset")
    public int getStartOffset()
    {
        return startOffset;
    }

    public void setStartOffset(int startOffset)
    {
        this.startOffset = startOffset;
    }

    @XmlAttribute(name = "endOffset")
    public int getEndOffset()
    {
        return endOffset;
    }

    public void setEndOffset(int endOffset)
    {
        this.endOffset = endOffset;
    }
}
