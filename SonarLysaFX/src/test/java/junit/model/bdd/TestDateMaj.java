package junit.model.bdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalTime;

import org.junit.Test;

import junit.model.AbstractTestModel;
import model.bdd.DateMaj;
import model.enums.TypeDonnee;

public class TestDateMaj extends AbstractTestModel<DateMaj>
{
    /*---------- ATTRIBUTS ----------*/

    private LocalTime time;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void initImpl()
    {
        time = LocalTime.now();
        objetTest = DateMaj.build(TypeDonnee.APPS, today, time);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildException()
    {
        DateMaj.build(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildException2()
    {
        DateMaj.build(TypeDonnee.APPS, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildException3()
    {
        DateMaj.build(TypeDonnee.APPS, today, null);
    }

    @Test
    public void testBuild()
    {
        DateMaj dateMaj = DateMaj.build(TypeDonnee.APPS, today, time);
        assertEquals(objetTest.getTypeDonnee(), dateMaj.getTypeDonnee());
        assertEquals(objetTest.getDate(), dateMaj.getDate());
        assertEquals(objetTest.getHeure(), dateMaj.getHeure());
    }

    @Test
    public void testGetMapIndex()
    {
        assertEquals(objetTest.getTypeDonnee().toString(), objetTest.getMapIndex());
    }

    @Test
    public void testGetTypeDonnee()
    {
        // Test getter
        assertEquals(TypeDonnee.APPS, objetTest.getTypeDonnee());
    }

    @Test
    public void testGetDate()
    {
        // Test getter et setter
        assertEquals(today, objetTest.getDate());
        objetTest.setDate(today.minusMonths(1l));
        assertEquals(today.minusMonths(1l), objetTest.getDate());

        // Protection null
        objetTest.setDate(null);
        assertEquals(today.minusMonths(1l), objetTest.getDate());

    }

    @Test
    public void testGetHeure()
    {
        // Test getter et setter
        assertEquals(time, objetTest.getHeure());
        objetTest.setHeure(time.minusHours(1l));
        assertEquals(time.minusHours(1l), objetTest.getHeure());

        // Protection null
        objetTest.setHeure(null);
        assertEquals(time.minusHours(1l), objetTest.getHeure());
    }
    
    @Test
    public void testGetTimeStamp()
    {
        assertNotNull(objetTest.getTimeStamp());
    }
    
    @Test
    public void testGetIdBase()
    {
        // Valeur à zéro pour un objet non enregistré dans la base
        assertEquals(0, objetTest.getIdBase());       
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
