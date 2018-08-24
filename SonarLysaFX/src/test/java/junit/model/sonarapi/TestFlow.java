package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    public void testConstructeurException()
    {
        LOCATIONS.clear();
        new Flow(LOCATIONS);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructeurException2()
    {
        new Flow(null);
    }
    
    @Test
    public void testGetLocations()
    {
        assertEquals(LOCATIONS, modele.getLocations());
        assertNotNull(modeleNull.getLocations());
        assertEquals(0, modeleNull.getLocations().size());
    }
    
    @Test
    public void testSetLocations()
    {
        modele.setLocations(null);
        assertNotNull(modele.getLocations());
        assertTrue(modele.getLocations().isEmpty());
        modeleNull.setLocations(LOCATIONS);
        assertEquals(LOCATIONS, modeleNull.getLocations());
    }
}