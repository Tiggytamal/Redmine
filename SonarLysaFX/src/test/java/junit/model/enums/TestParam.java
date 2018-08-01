package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import model.enums.Param;

public class TestParam implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(Param.FILTREDATASTAGE, Param.valueOf(Param.FILTREDATASTAGE.toString()));        
    }
    
    @Test
    @Override
    public void testSize()
    {
        assertEquals(18, Param.values().length);
    }
    
    @Test
    public void testGetNom()
    {
        assertFalse(Param.FILTREDATASTAGE.getNom().isEmpty());
        assertFalse(Param.FILTRECOBOL.getNom().isEmpty());
        assertFalse(Param.ABSOLUTEPATH.getNom().isEmpty());
        assertFalse(Param.NOMFICHIERJAVA.getNom().isEmpty());
        assertFalse(Param.NOMFICHIERDATASTAGE.getNom().isEmpty());
        assertFalse(Param.NOMFICHIERAPPLI.getNom().isEmpty());
        assertFalse(Param.ABSOLUTEPATHHISTO.getNom().isEmpty());
        assertFalse(Param.LIENSLOTS.getNom().isEmpty());
        assertFalse(Param.LIENSANOS.getNom().isEmpty());
        assertFalse(Param.NOMQGDATASTAGE.getNom().isEmpty());
        assertFalse(Param.URLSONAR.getNom().isEmpty());
        assertFalse(Param.URLRTC.getNom().isEmpty());
        assertFalse(Param.RTCLOTCHC.getNom().isEmpty());
        assertFalse(Param.NOMFICHIERCOBOL.getNom().isEmpty());
        assertFalse(Param.IPMAIL.getNom().isEmpty());
        assertFalse(Param.PORTMAIL.getNom().isEmpty());
        assertFalse(Param.AQPMAIL.getNom().isEmpty());
        assertFalse(Param.NBREPURGE.getNom().isEmpty());
    }
}
