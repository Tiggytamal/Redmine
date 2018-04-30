package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import control.quartz.JobAnomaliesSonar;
import control.quartz.JobVuesCDM;
import control.quartz.JobVuesCHC;
import model.enums.TypePlan;

public class TestTypePlan
{
    @Test
    public void testSize()
    {
        assertEquals(3, TypePlan.values().length);
    }
    
    @Test
    public void testToString()
    {
        assertEquals("Suivi Hebdo", TypePlan.SUIVIHEBDO.toString());
        assertEquals("Vues CHC", TypePlan.VUECHC.toString());
        assertEquals("Vues CHC_CDM", TypePlan.VUECDM.toString());
    }
    
    @Test
    public void getClassJob()
    {
        assertEquals(JobAnomaliesSonar.class, TypePlan.SUIVIHEBDO.getClassJob());
        assertEquals(JobVuesCHC.class, TypePlan.VUECHC.getClassJob());
        assertEquals(JobVuesCDM.class, TypePlan.VUECDM.getClassJob());
    }
}
