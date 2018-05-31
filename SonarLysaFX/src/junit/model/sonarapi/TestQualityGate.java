package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static junit.TestUtils.NOTNULL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.QualityGate;

public class TestQualityGate
{
    /*---------- ATTRIBUTS ----------*/

    private QualityGate modele;
    private QualityGate modeleNull;
    private static final String ID = "10";
    private static final String NAME = "20";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new QualityGate(ID, NAME);
        modeleNull = new QualityGate();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void getId()
    {
        assertEquals(ID, modele.getId());
        assertNull(modeleNull.getId());
    }
    
    @Test
    public void setId()
    {
        modele.setId(NEWVAL);
        assertEquals(NEWVAL, modele.getId());
        modeleNull.setId(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getId());
    }
    
    @Test
    public void getName()
    {
        assertEquals(NAME, modele.getName());
        assertNull(modeleNull.getName());
    }
    
    @Test
    public void setName()
    {
        modele.setName(NEWVAL);
        assertEquals(NEWVAL, modele.getName());
        modeleNull.setName(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getName());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
