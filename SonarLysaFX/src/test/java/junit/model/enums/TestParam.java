package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import model.enums.Param;

public class TestParam
{
    @Test
    public void testSize()
    {
        assertEquals(12, Param.values().length);
    }
    
    @Test
    public void testToString()
    {
        assertFalse(Param.FILTREDATASTAGE.toString().isEmpty());
        assertFalse(Param.ABSOLUTEPATH.toString().isEmpty());
        assertFalse(Param.NOMFICHIER.toString().isEmpty());
        assertFalse(Param.NOMFICHIERDATASTAGE.toString().isEmpty());
        assertFalse(Param.ABSOLUTEPATHHISTO.toString().isEmpty());
        assertFalse(Param.LIENSLOTS.toString().isEmpty());
        assertFalse(Param.LIENSANOS.toString().isEmpty());
        assertFalse(Param.NOMQGDATASTAGE.toString().isEmpty());
        assertFalse(Param.URLSONAR.toString().isEmpty());
        assertFalse(Param.URLRTC.toString().isEmpty());
        assertFalse(Param.RTCLOTCHC.toString().isEmpty());
        assertFalse(Param.NOMFICHIERCOBOL.toString().isEmpty());
    }
}
