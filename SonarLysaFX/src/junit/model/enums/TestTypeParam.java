package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import model.enums.TypeParam;

public class TestTypeParam
{
    @Test
    public void testSize()
    {
        assertEquals(12, TypeParam.values().length);
    }
    
    @Test
    public void testToString()
    {
        assertFalse(TypeParam.VERSIONS.toString().isEmpty());
        assertFalse(TypeParam.FILTREDATASTAGE.toString().isEmpty());
        assertFalse(TypeParam.ABSOLUTEPATH.toString().isEmpty());
        assertFalse(TypeParam.NOMFICHIER.toString().isEmpty());
        assertFalse(TypeParam.NOMFICHIERDATASTAGE.toString().isEmpty());
        assertFalse(TypeParam.ABSOLUTEPATHHISTO.toString().isEmpty());
        assertFalse(TypeParam.LIENSLOTS.toString().isEmpty());
        assertFalse(TypeParam.LIENSANOS.toString().isEmpty());
        assertFalse(TypeParam.NOMQGDATASTAGE.toString().isEmpty());
        assertFalse(TypeParam.URLSONAR.toString().isEmpty());
        assertFalse(TypeParam.URLRTC.toString().isEmpty());
        assertFalse(TypeParam.RTCLOTCHC.toString().isEmpty());
    }
}
