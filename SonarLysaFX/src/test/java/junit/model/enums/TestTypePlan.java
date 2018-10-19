package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import control.quartz.JobAnomaliesSonar;
import control.quartz.JobVuesCDM;
import control.quartz.JobVuesCHC;
import model.enums.TypePlan;

/**
 * Classe de test de l'énumération TypePlan
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestTypePlan implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(3, TypePlan.values().length);
    }
    
    @Test
    public void testToString()
    {
        assertEquals("Suivi Hebdo", TypePlan.SUIVIHEBDO.getValeur());
        assertEquals("Vues CHC", TypePlan.VUECHC.getValeur());
        assertEquals("Vues CHC_CDM", TypePlan.VUECDM.getValeur());
    }
    
    @Test
    public void testGetClassJob()
    {
        assertEquals(JobAnomaliesSonar.class, TypePlan.SUIVIHEBDO.getClassJob());
        assertEquals(JobVuesCHC.class, TypePlan.VUECHC.getClassJob());
        assertEquals(JobVuesCDM.class, TypePlan.VUECDM.getClassJob());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypePlan.VUECDM, TypePlan.valueOf(TypePlan.VUECDM.toString()));    
    }
}
