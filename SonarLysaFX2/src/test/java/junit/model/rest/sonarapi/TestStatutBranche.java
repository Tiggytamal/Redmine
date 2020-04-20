package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.enums.QG;
import model.rest.sonarapi.StatutBranche;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestStatutBranche extends TestAbstractModele<StatutBranche>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetQualityGate(TestInfo testInfo)
    {
        // Test getter et setter
        assertThat(objetTest.getQualityGate()).isNull();
        objetTest.setQualityGate(QG.OK);
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.OK);

        // Protection setter null
        objetTest.setQualityGate(null);
        assertThat(objetTest.getQualityGate()).isEqualTo(QG.OK);
    }

    @Test
    public void testGetBugs(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getBugs(), i -> objetTest.setBugs(i));
    }

    @Test
    public void testGetVulnerabilites(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getVulnerabilites(), i -> objetTest.setVulnerabilites(i));
    }

    @Test
    public void testGetCodeSmells(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getCodeSmells(), i -> objetTest.setCodeSmells(i));
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
