package junit.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import utilities.TechnicalException;
import utilities.enums.Severity;

public class TestTechnicalException
{
    private TechnicalException handler;
    
    @Before
    public void init()
    {
        handler  = new TechnicalException("message", new RuntimeException());
    }
    
    @Test
    public void testRunTime()
    {
        // Test si l'exception est bien de type RunTime
        assertTrue(handler instanceof RuntimeException);
    }
    
    @Test
    public void testGetSeverity()
    {
        // Test que la sévérité est bine ERROR
        assertEquals(Severity.ERROR, handler.getSeverity());
    }
    
    @Test
    public void testGetMessage()
    {
        // Test le message de l'exception
        assertEquals("message", handler.getMessage());
    }
}
