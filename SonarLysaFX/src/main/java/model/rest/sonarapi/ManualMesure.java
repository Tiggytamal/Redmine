package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.interfaces.AbstractModele;
import model.interfaces.ModeleSonar;
import utilities.Statics;

@XmlRootElement
public class ManualMesure extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String key;
    private String selectionMode;
    private String measure;
    private String value;
    
    /*---------- CONSTRUCTEURS ----------*/

    public ManualMesure(String key)
    {
        this.key = key;
        selectionMode = "MANUAL_MEASURE";
        measure = "lot";
        value = key.replace("view_lot_", Statics.EMPTY);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "key")
    public String getKey()
    {
        return key;
    }

    @XmlAttribute(name = "selectionMode")
    public String getSelectionMode()
    {
        return selectionMode;
    }

    @XmlAttribute(name = "measure")
    public String getMeasure()
    {
        return measure;
    }

    @XmlAttribute(name = "value")
    public String getValue()
    {
        return value;
    }
}
