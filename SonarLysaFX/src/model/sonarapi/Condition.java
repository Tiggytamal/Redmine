package model.sonarapi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Condition implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String status;
    private String metricKeys;
    private String comparator;
    private int periodIndex;
    private String errorThreshold;
    private String actualValue;

    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "status")
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @XmlAttribute(name = "metricKey")
    public String getMetricKeys()
    {
        return metricKeys;
    }

    public void setMetricKeys(String metricKeys)
    {
        this.metricKeys = metricKeys;
    }

    @XmlAttribute(name = "comparator")
    public String getComparator()
    {
        return comparator;
    }

    public void setComparator(String comparator)
    {
        this.comparator = comparator;
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
        return errorThreshold;
    }

    public void setErrorThreshold(String errorThreshold)
    {
        this.errorThreshold = errorThreshold;
    }

    @XmlAttribute(name = "actualValue")
    public String getActualValue()
    {
        return actualValue;
    }

    public void setActualValue(String actualValue)
    {
        this.actualValue = actualValue;
    }
}