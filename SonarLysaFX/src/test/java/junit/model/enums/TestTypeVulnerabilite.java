package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeVulnerabilite;

public class TestTypeVulnerabilite implements TestEnums
{

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeVulnerabilite.OUVERTE, TypeVulnerabilite.valueOf(TypeVulnerabilite.OUVERTE.toString()));    
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(2, TypeVulnerabilite.values().length);
    }
    
    @Test
    public void testGetBooleen()
    {
        assertEquals("false", TypeVulnerabilite.OUVERTE.getBooleen());        
        assertEquals("true", TypeVulnerabilite.RESOLUE.getBooleen());   
    }
    
    @Test
    public void TestGetNomSheet()
    {
        assertEquals("Ouvertes", TypeVulnerabilite.OUVERTE.getNomSheet());   
        assertEquals("Résolues", TypeVulnerabilite.RESOLUE.getNomSheet());   
    }
}
