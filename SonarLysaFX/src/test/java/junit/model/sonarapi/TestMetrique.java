package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.enums.TypeMetrique;
import model.sonarapi.Metrique;
import model.sonarapi.Periode;

public class TestMetrique
{
    /*---------- ATTRIBUTS ----------*/

    private Metrique modele;
    private Metrique modeleNull;
    private static final String VALEUR = "valeur";
    private static final TypeMetrique TYPE = TypeMetrique.BUGS;
    private static final List<Periode> PERIODES = new ArrayList<>();
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        PERIODES.add(new Periode());
        modele = new Metrique(TYPE, VALEUR);
        modeleNull = new Metrique();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testConstructeurType()
    {
        modele = new Metrique(TYPE);
        assertNotNull(modele.getListePeriodes());
        assertNull(modele.getValue());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructeurSimpleException()
    {
        modele = new Metrique(null, null);
    }
    
    
    @Test
    public void getValue()
    {
        assertEquals(VALEUR, modele.getValue());
        assertNull(modeleNull.getValue());
    }
    
    @Test
    public void setValue()
    {
        modele.setValue(NEWVAL);
        assertEquals(NEWVAL, modele.getValue());
        modeleNull.setValue(VALEUR);
        assertEquals(VALEUR, modeleNull.getValue());
    }
    
    @Test
    public void getMetric()
    {
        assertEquals(TYPE, modele.getMetric());
        assertNull(modeleNull.getMetric());
    }
    
    @Test
    public void setMetric()
    {
        modele.setMetric(null);
        assertNull(modele.getMetric());
        modeleNull.setMetric(TYPE);
        assertEquals(TYPE, modeleNull.getMetric());
    }
    
    @Test
    public void getListePeriodes()
    {
        assertNotNull(modele.getListePeriodes());
        assertTrue(modele.getListePeriodes().isEmpty());
        assertNotNull(modeleNull.getListePeriodes());
        assertTrue(modeleNull.getListePeriodes().isEmpty());
    }

    @Test
    public void setListePeriodes()
    {
        modele.setListePeriodes(null);
        assertNotNull(modele.getListePeriodes());
        modeleNull.setListePeriodes(PERIODES);
        assertEquals(PERIODES, modeleNull.getListePeriodes());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
