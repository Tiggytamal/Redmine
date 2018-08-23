package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.utilities.AbstractModele;
import model.utilities.ModeleSonar;

@XmlRootElement
public class TextRange extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private int startLine;
    private int endLine;
    private int startOffset;
    private int endOffset;

    /*---------- CONSTRUCTEURS ----------*/

    public TextRange(int startLine, int endLine, int startOffset, int endOffset)
    {
        this.startLine = startLine;
        this.endLine = endLine;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public TextRange()
    {
        // Constructeur vide pour initialiser des objets sans paramètre et la création depuis le XML
    }

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
