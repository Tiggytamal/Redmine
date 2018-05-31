package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static junit.TestUtils.NOTNULL;
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
    public void getKey()
    {
        assertEquals(KEY, modele.getKey());
        assertNull(modeleNull.getKey());
    }
    
    @Test
    public void setKey()
    {
        modele.setKey(NEWVAL);
        assertEquals(NEWVAL, modele.getKey());
        modeleNull.setKey(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getKey());
    }
    
    @Test
    public void getId()
    {
        assertEquals(ID, modele.getId());
        assertNull(modeleNull.getId());
    }
    
    @Test
    public void setId()
    {
        modele.setId(NEWVAL);
        assertEquals(NEWVAL, modele.getId());
        modeleNull.setId(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getId());
    }
    
    @Test
    public void getNom()
    {
        assertEquals(NOM, modele.getNom());
        assertNull(modeleNull.getNom());
    }
    
    @Test
    public void setNom()
    {
        modele.setNom(NEWVAL);
        assertEquals(NEWVAL, modele.getNom());
        modeleNull.setNom(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getNom());
    }
    
    @Test
    public void getSc()
    {
        assertEquals(SC, modele.getSc());
        assertNull(modeleNull.getSc());
    }
    
    @Test
    public void setSc()
    {
        modele.setSc(NEWVAL);
        assertEquals(NEWVAL, modele.getSc());
        modeleNull.setSc(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getSc());
    }
    
    @Test
    public void getQu()
    {
        assertEquals(QU, modele.getQu());
        assertNull(modeleNull.getQu());
    }
    
    @Test
    public void setQu()
    {
        modele.setQu(NEWVAL);
        assertEquals(NEWVAL, modele.getQu());
        modeleNull.setQu(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getQu());
    }
    
    @Test
    public void getLot()
    {
        assertEquals(LOT, modele.getLot());
        assertNull(modeleNull.getLot());
    }
    
    @Test
    public void setLot()
    {
        modele.setLot(NEWVAL);
        assertEquals(NEWVAL, modele.getLot());
        modeleNull.setLot(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getLot());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
