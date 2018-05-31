package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Flow;
import model.sonarapi.Location;

public class TestFlow
{
    /*---------- ATTRIBUTS ----------*/

    private Flow modele;
    private Flow modeleNull;
    private static final List<Location> LOCATIONS = new ArrayList<>();
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        LOCATIONS.add(new Location());
        modele = new Flow(LOCATIONS);
        modeleNull = new Flow();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test (expected = IllegalArgumentException.class)
    public void constructeurException()
    {
        LOCATIONS.clear();
        new Flow(LOCATIONS);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void constructeurException2()
    {
        new Flow(null);
    }
    
    @Test
    public void getLocations()
    {
        assertEquals(LOCATIONS, modele.getLocations());
        assertNotNull(modeleNull.getLocations());
        assertTrue(modeleNull.getLocations().isEmpty());
    }
    
    @Test
    public void setLocations()
    {
        modele.setLocations(null);
        assertNull(modele.getLocations());
        modeleNull.setLocations(LOCATIONS);
        assertEquals(LOCATIONS, modeleNull.getLocations());
    }
}