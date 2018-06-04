package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Condition;

public class TestCondition
{
    /*---------- ATTRIBUTS ----------*/

    private Condition modele;
    private Condition modeleNull;
    private static final String STATUS = "status";
    private static final String METRICKEYS = "keys";
    private static final String COMPARATOR = "comparator";
    private static final int PERIODEINDEX = 55;
    private static final String ERRORTHRESHOLD = "threshold";
    private static final String VALUE = "value";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Condition(STATUS, METRICKEYS, COMPARATOR, PERIODEINDEX, ERRORTHRESHOLD, VALUE);
        modeleNull = new Condition();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void getStatus()
    {
        assertEquals(STATUS, modele.getStatus());
        assertNull(modeleNull.getStatus());
    }
    
    @Test
    public void setStatus()
    {
        modele.setStatus(NEWVAL);
        assertEquals(NEWVAL, modele.getStatus());
        modeleNull.setStatus(STATUS);
        assertEquals(STATUS, modeleNull.getStatus());
    }
    
    @Test
    public void getMetricKeys()
    {
        assertEquals(METRICKEYS, modele.getMetricKeys());
        assertNull(modeleNull.getMetricKeys());
    }
    
    @Test
    public void setMetricKeys()
    {
        modele.setMetricKeys(NEWVAL);
        assertEquals(NEWVAL, modele.getMetricKeys());
        modeleNull.setMetricKeys(METRICKEYS);
        assertEquals(METRICKEYS, modeleNull.getMetricKeys());
    }
    
    @Test
    public void getComparator()
    {
        assertEquals(COMPARATOR, modele.getComparator());
        assertNull(modeleNull.getComparator());
    }
    
    @Test
    public void setComparator()
    {
        modele.setComparator(NEWVAL);
        assertEquals(NEWVAL, modele.getComparator());
        modeleNull.setComparator(COMPARATOR);
        assertEquals(COMPARATOR, modeleNull.getComparator());
    }
    
    @Test
    public void getPeriodIndex()
    {
        assertEquals(PERIODEINDEX, modele.getPeriodIndex());
        assertEquals(0, modeleNull.getPeriodIndex());
    }
    
    @Test
    public void setPeriodIndex()
    {
        modele.setPeriodIndex(10);
        assertEquals(10, modele.getPeriodIndex());
        modeleNull.setPeriodIndex(10);
        assertEquals(10, modeleNull.getPeriodIndex());
    }
    
    @Test
    public void getErrorThreshold()
    {
        assertEquals(ERRORTHRESHOLD, modele.getErrorThreshold());
        assertNull(modeleNull.getErrorThreshold());
    }
    
    @Test
    public void setErrorThreshold()
    {
        modele.setErrorThreshold(NEWVAL);
        assertEquals(NEWVAL, modele.getErrorThreshold());
        modeleNull.setErrorThreshold(ERRORTHRESHOLD);
        assertEquals(ERRORTHRESHOLD, modeleNull.getErrorThreshold());
    }
    
    @Test
    public void getActualValue()
    {
        assertEquals(VALUE, modele.getActualValue());
        assertNull(modeleNull.getActualValue());
    }
    
    @Test
    public void setActualValue()
    {
        modele.setActualValue(NEWVAL);
        assertEquals(NEWVAL, modele.getActualValue());
        modeleNull.setActualValue(VALUE);
        assertEquals(VALUE, modeleNull.getActualValue());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/   
}