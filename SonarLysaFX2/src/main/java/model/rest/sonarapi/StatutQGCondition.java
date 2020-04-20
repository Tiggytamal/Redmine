package model.rest.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;

/**
 * Classe de modele représentant une condition d'un metrique en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement
public class StatutQGCondition extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String status;
    private String metricKey;
    private String comparator;
    private int periodIndex;
    private String errorThreshold;
    private String warningThreshold;
    private String actualValue;

    /*---------- CONSTRUCTEURS ----------*/    
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "status")
    public String getStatus()
    {
        return getString(status);
    }
    
    public void setStatus(String status)
    {
        this.status = getString(status);
    }

    @XmlAttribute(name = "metricKey")
    public String getMetricKey()
    {
        return getString(metricKey);
    }
    
    public void setMetricKey(String metricKey)
    {
        this.metricKey = getString(metricKey);
    }

    @XmlAttribute(name = "comparator")
    public String getComparator()
    {
        return getString(comparator);
    }
    
    public void setComparator(String comparator)
    {
        this.comparator = getString(comparator);
    }

    @XmlAttribute(name = "periodIndex")
    public int getPeriodIndex()
    {
        return periodIndex;
    }
    
    public void setPeriodIndex(int periodIndex)
    {
        this.periodIndex = periodIndex;
    }

    @XmlAttribute(name = "errorThreshold")
    public String getErrorThreshold()
    {
        return getString(errorThreshold);
    }
    
    public void setErrorThreshold(String errorThreshold)
    {
        this.errorThreshold = getString(errorThreshold);
    }
    
    @XmlAttribute(name = "warningThreshold")
    public String getWarningThreshold()
    {
        return getString(warningThreshold);
    }
    
    public void setWarningThreshold(String warningThreshold)
    {
        this.warningThreshold = getString(warningThreshold);
    }

    @XmlAttribute(name = "actualValue")
    public String getActualValue()
    {
        return getString(actualValue);
    }

    public void setActualValue(String actualValue)
    {
        this.actualValue = getString(actualValue);
    }    
}
