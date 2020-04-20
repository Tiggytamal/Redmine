package junit.model.rest.sonarapi;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.rest.sonarapi.QualityGate;
import model.rest.sonarapi.QualityGates;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestQualityGates extends TestAbstractModele<QualityGates>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetListeQualityGates(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getListeQualityGates(), l -> objetTest.setListeQualityGates(l), new QualityGate(), "listeQualityGates");
    }

    @Test
    public void testGetDefaut(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getDefaut(), i -> objetTest.setDefaut(i));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
