package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    public void testGetStatus()
    {
        assertEquals(STATUS, modele.getStatus());
        assertNotNull(modeleNull.getStatus());
        assertTrue(modeleNull.getStatus().isEmpty());
    }
    
    @Test
    public void testSetStatus()
    {
        modele.setStatus(NEWVAL);
        assertEquals(NEWVAL, modele.getStatus());
        modeleNull.setStatus(STATUS);
        assertEquals(STATUS, modeleNull.getStatus());
    }
    
    @Test
    public void testGetMetricKeys()
    {
        assertEquals(METRICKEYS, modele.getMetricKeys());
        assertNotNull(modeleNull.getMetricKeys());
        assertTrue(modeleNull.getMetricKeys().isEmpty());
    }
    
    @Test
    public void testSetMetricKeys()
    {
        modele.setMetricKeys(NEWVAL);
        assertEquals(NEWVAL, modele.getMetricKeys());
        modeleNull.setMetricKeys(METRICKEYS);
        assertEquals(METRICKEYS, modeleNull.getMetricKeys());
    }
    
    @Test
    public void testGetComparator()
    {
        assertEquals(COMPARATOR, modele.getComparator());
        assertNotNull(modeleNull.getComparator());
        assertTrue(modeleNull.getComparator().isEmpty());
    }
    
    @Test
    public void testSetComparator()
    {
        modele.setComparator(NEWVAL);
        assertEquals(NEWVAL, modele.getComparator());
        modeleNull.setComparator(COMPARATOR);
        assertEquals(COMPARATOR, modeleNull.getComparator());
    }
    
    @Test
    public void testGetPeriodIndex()
    {
        assertEquals(PERIODEINDEX, modele.getPeriodIndex());
        assertEquals(0, modeleNull.getPeriodIndex());
    }
    
    @Test
    public void testSetPeriodIndex()
    {
        modele.setPeriodIndex(10);
        assertEquals(10, modele.getPeriodIndex());
        modeleNull.setPeriodIndex(10);
        assertEquals(10, modeleNull.getPeriodIndex());
    }
    
    @Test
    public void testGetErrorThreshold()
    {
        assertEquals(ERRORTHRESHOLD, modele.getErrorThreshold());
        assertNotNull(modeleNull.getErrorThreshold());
        assertTrue(modeleNull.getErrorThreshold().isEmpty());
    }
    
    @Test
    public void testSetErrorThreshold()
    {
        modele.setErrorThreshold(NEWVAL);
        assertEquals(NEWVAL, modele.getErrorThreshold());
        modeleNull.setErrorThreshold(ERRORTHRESHOLD);
        assertEquals(ERRORTHRESHOLD, modeleNull.getErrorThreshold());
    }
    
    @Test
    public void testGetActualValue()
    {
        assertEquals(VALUE, modele.getActualValue());
        assertNotNull(modeleNull.getActualValue());
        assertTrue(modeleNull.getActualValue().isEmpty());
    }
    
    @Test
    public void testSetActualValue()
    {
        modele.setActualValue(NEWVAL);
        assertEquals(NEWVAL, modele.getActualValue());
        modeleNull.setActualValue(VALUE);
        assertEquals(VALUE, modeleNull.getActualValue());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/   
}