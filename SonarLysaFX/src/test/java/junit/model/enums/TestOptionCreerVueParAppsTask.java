package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.OptionCreerVueParAppsTask;

public class TestOptionCreerVueParAppsTask implements TestEnums
{  
    @Test
    @Override
    public void testSize()
    {
        assertEquals(3, OptionCreerVueParAppsTask.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(OptionCreerVueParAppsTask.ALL, OptionCreerVueParAppsTask.valueOf(OptionCreerVueParAppsTask.ALL.toString()));
        assertEquals(OptionCreerVueParAppsTask.FICHIERS, OptionCreerVueParAppsTask.valueOf(OptionCreerVueParAppsTask.FICHIERS.toString()));
    }
}
