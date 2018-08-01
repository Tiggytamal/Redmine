package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.CreerVueParAppsTaskOption;

public class TestCreerVueParAppsTaskOption implements TestEnums
{  
    @Test
    @Override
    public void testSize()
    {
        assertEquals(3, CreerVueParAppsTaskOption.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(CreerVueParAppsTaskOption.ALL, CreerVueParAppsTaskOption.valueOf(CreerVueParAppsTaskOption.ALL.toString()));
        assertEquals(CreerVueParAppsTaskOption.FICHIER, CreerVueParAppsTaskOption.valueOf(CreerVueParAppsTaskOption.FICHIER.toString()));
    }
}
