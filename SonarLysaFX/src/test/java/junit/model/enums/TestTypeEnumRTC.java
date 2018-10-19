package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeEnumRTC;

/**
 * Classe de test de l'énumération TypeEnumRTC
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestTypeEnumRTC implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(9, TypeEnumRTC.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("fr.ca.cat.attribut.entiteresponsablecorrection", TypeEnumRTC.ENTITERESPCORRECTION.getValeur());
        assertEquals("fr.ca.cat.attribut.environnement", TypeEnumRTC.ENVIRONNEMENT.getValeur());
        assertEquals("NatureProbleme", TypeEnumRTC.NATURE.getValeur());
        assertEquals("fr.ca.cat.attribut.edition", TypeEnumRTC.EDITION.getValeur());
        assertEquals("fr.ca.cat.attribut.editionsicible", TypeEnumRTC.EDITIONSICIBLE.getValeur());
        assertEquals("NiveauImportance", TypeEnumRTC.IMPORTANCE.getValeur());
        assertEquals("Origine", TypeEnumRTC.ORIGINE.getValeur());
        assertEquals("fr.ca.cat.attribut.codeprojetclarity", TypeEnumRTC.CLARITY.getValeur());
        assertEquals("fr.ca.cat.attribut.datedelivraison", TypeEnumRTC.DATELIVHOMO.getValeur());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeEnumRTC.IMPORTANCE, TypeEnumRTC.valueOf(TypeEnumRTC.IMPORTANCE.toString()));
    }
}
