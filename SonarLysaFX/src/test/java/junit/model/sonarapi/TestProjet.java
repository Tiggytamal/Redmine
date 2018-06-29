package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Projet;

public class TestProjet
{
    /*---------- ATTRIBUTS ----------*/

    private Projet modele;
    private Projet modeleNull;
    private static final String ID = "10";
    private static final String KEY = "20";
    private static final String NOM = "30";
    private static final String SC = "40";
    private static final String QU = "50";
    private static final String LOT = "60";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        modele = new Projet(ID, KEY, NOM, SC, QU, LOT);
        modeleNull = new Projet();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetKey()
    {
        assertEquals(KEY, modele.getKey());
        assertNull(modeleNull.getKey());
    }
    
    @Test
    public void testSetKey()
    {
        modele.setKey(NEWVAL);
        assertEquals(NEWVAL, modele.getKey());
        modeleNull.setKey(KEY);
        assertEquals(KEY, modeleNull.getKey());
    }
    
    @Test
    public void testGetId()
    {
        assertEquals(ID, modele.getId());
        assertNull(modeleNull.getId());
    }
    
    @Test
    public void testSetId()
    {
        modele.setId(NEWVAL);
        assertEquals(NEWVAL, modele.getId());
        modeleNull.setId(ID);
        assertEquals(ID, modeleNull.getId());
    }
    
    @Test
    public void testGetNom()
    {
        assertEquals(NOM, modele.getNom());
        assertNull(modeleNull.getNom());
    }
    
    @Test
    public void testSetNom()
    {
        modele.setNom(NEWVAL);
        assertEquals(NEWVAL, modele.getNom());
        modeleNull.setNom(NOM);
        assertEquals(NOM, modeleNull.getNom());
    }
    
    @Test
    public void testGetSc()
    {
        assertEquals(SC, modele.getSc());
        assertNull(modeleNull.getSc());
    }
    
    @Test
    public void testSetSc()
    {
        modele.setSc(NEWVAL);
        assertEquals(NEWVAL, modele.getSc());
        modeleNull.setSc(SC);
        assertEquals(SC, modeleNull.getSc());
    }
    
    @Test
    public void testGetQu()
    {
        assertEquals(QU, modele.getQu());
        assertNull(modeleNull.getQu());
    }
    
    @Test
    public void testSetQu()
    {
        modele.setQu(NEWVAL);
        assertEquals(NEWVAL, modele.getQu());
        modeleNull.setQu(QU);
        assertEquals(QU, modeleNull.getQu());
    }
    
    @Test
    public void testGetLot()
    {
        assertEquals(LOT, modele.getLot());
        assertNull(modeleNull.getLot());
    }
    
    @Test
    public void testSetLot()
    {
        modele.setLot(NEWVAL);
        assertEquals(NEWVAL, modele.getLot());
        modeleNull.setLot(LOT);
        assertEquals(LOT, modeleNull.getLot());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
