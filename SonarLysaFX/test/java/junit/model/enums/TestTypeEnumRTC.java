package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeEnumRTC;

public class TestTypeEnumRTC
{
    @Test
    public void testSize()
    {
        assertEquals(8, TypeEnumRTC.values().length);
    }
    
    @Test
    public void testToString()
    {
        assertEquals("fr.ca.cat.attribut.entiteresponsablecorrection", TypeEnumRTC.ENTITERESPCORRECTION.toString());
        assertEquals("fr.ca.cat.attribut.environnement", TypeEnumRTC.ENVIRONNEMENT.toString());
        assertEquals("NatureProbleme", TypeEnumRTC.NATURE.toString());
        assertEquals("fr.ca.cat.attribut.edition", TypeEnumRTC.EDITION.toString());
        assertEquals("fr.ca.cat.attribut.editionsicible", TypeEnumRTC.EDITIONSICIBLE.toString());
        assertEquals("NiveauImportance", TypeEnumRTC.IMPORTANCE.toString());
        assertEquals("Origine", TypeEnumRTC.ORIGINE.toString());
        assertEquals("fr.ca.cat.attribut.codeprojetclarity", TypeEnumRTC.CLARITY.toString());
    }
}
