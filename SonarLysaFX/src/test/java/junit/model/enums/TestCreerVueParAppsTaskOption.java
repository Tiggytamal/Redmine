package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.CreerVueParAppsTaskOption;

public class TestCreerVueParAppsTaskOption
{
    @Test
    public void testSize()
    {
        assertEquals(3, CreerVueParAppsTaskOption.values().length);
    }
}
