package junit.utilities.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import junit.model.enums.TestEnums;
import utilities.enums.Severity;

public class TestSeverity implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(Severity.INFO, Severity.valueOf(Severity.INFO.toString()));  
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, Severity.values().length);  
    }
}
