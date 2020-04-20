package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

import junit.model.TestAbstractModele;
import model.bdd.AbstractBDDModele;

public abstract class TestAbstractBDDModele<T extends AbstractBDDModele<U>, U> extends TestAbstractModele<T>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetIdBase()
    {
        assertThat(objetTest.getIdBase()).isAtLeast(0);
    }

    @Test
    public void testGetTimeStamp() throws IllegalAccessException
    {
        // Test retour non null
        assertThat(objetTest.getTimeStamp()).isNotNull();

        // Test protection null
        setField("timeStamp", null);
        assertThat(objetTest.getTimeStamp()).isNotNull();
    }

    @Test
    public void testUpdateTimeStamp() throws IllegalAccessException
    {
        setField("timeStamp", null);
        objetTest.updateTimeStamp();
        assertThat(objetTest.getTimeStamp()).isNotNull();
    }

    /*---------- METHODES PROTECTED ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
