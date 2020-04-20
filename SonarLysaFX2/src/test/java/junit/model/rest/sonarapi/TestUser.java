package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.User;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestUser extends TestAbstractModele<User>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetLogin(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLogin(), s -> objetTest.setLogin(s));
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetActive(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getActive(), s -> objetTest.setActive(s));
    }

    @Test
    public void testGetEmail(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEmail(), s -> objetTest.setEmail(s));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
