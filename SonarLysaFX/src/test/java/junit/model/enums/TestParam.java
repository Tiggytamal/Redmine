package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import model.enums.Param;

/**
 * Classe de test de l'énumération Param
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
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
        assertEquals(25, Param.values().length);
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
        assertFalse(Param.NOMFICHIERPBAPPLI.getNom().isEmpty());
        assertFalse(Param.ABSOLUTEPATHHISTO.getNom().isEmpty());
        assertFalse(Param.ABSOLUTEPATHRAPPORT.getNom().isEmpty());
        assertFalse(Param.LIENSLOTS.getNom().isEmpty());
        assertFalse(Param.LIENSANOS.getNom().isEmpty());
        assertFalse(Param.NOMQGDATASTAGE.getNom().isEmpty());
        assertFalse(Param.URLSONAR.getNom().isEmpty());
        assertFalse(Param.URLRTC.getNom().isEmpty());
        assertFalse(Param.URLREPACK.getNom().isEmpty());
        assertFalse(Param.RTCLOTCHC.getNom().isEmpty());
        assertFalse(Param.NOMFICHIERCOBOL.getNom().isEmpty());
        assertFalse(Param.IPMAIL.getNom().isEmpty());
        assertFalse(Param.PORTMAIL.getNom().isEmpty());
        assertFalse(Param.AQPMAIL.getNom().isEmpty());
        assertFalse(Param.NBREPURGE.getNom().isEmpty());
        assertFalse(Param.NOMFICHIERANDROID.getNom().isEmpty());
        assertFalse(Param.NOMFICHIERIOS.getNom().isEmpty());
        assertFalse(Param.FILTREANDROID.getNom().isEmpty());
        assertFalse(Param.FILTREIOS.getNom().isEmpty());
    }
}
