package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.enums.TypeDefautSonar;
import model.rest.sonarapi.Activation;
import model.rest.sonarapi.ParamRegle;
import model.rest.sonarapi.Regle;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestRegle extends TestAbstractModele<Regle>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetKey(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getKey(), s -> objetTest.setKey(s));
    }

    @Test
    public void testGetRepo(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getRepo(), s -> objetTest.setRepo(s));
    }

    @Test
    public void testGetNom(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNom(), s -> objetTest.setNom(s));
    }

    @Test
    public void testGetCreatedAt(TestInfo testInfo)
    {
        testSimpleLocalDateTime(testInfo, () -> objetTest.getCreatedAt(), dt -> objetTest.setCreatedAt(dt));
    }

    @Test
    public void testGetHtmlDesc(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getHtmlDesc(), s -> objetTest.setHtmlDesc(s));
    }

    @Test
    public void testGetMdDesc(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getMdDesc(), s -> objetTest.setMdDesc(s));
    }

    @Test
    public void testGetClefInterne(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getClefInterne(), s -> objetTest.setClefInterne(s));
    }

    @Test
    public void testGetSeverite(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getSeverite(), s -> objetTest.setSeverite(s));
    }

    @Test
    public void testGetStatut(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getStatut(), s -> objetTest.setStatut(s));
    }

    @Test
    public void testIsTemplate(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isTemplate(), s -> objetTest.setTemplate(s));
    }

    @Test
    public void testGetTags(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getTags(), l -> objetTest.setTags(l), TESTSTRING, "tags");
    }

    @Test
    public void testGetSysTags(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getSysTags(), l -> objetTest.setSysTags(l), TESTSTRING, "sysTags");
    }

    @Test
    public void testGetLangage(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getLangage(), s -> objetTest.setLangage(s));
    }

    @Test
    public void testGetNomLangage(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getNomLangage(), s -> objetTest.setNomLangage(s));
    }

    @Test
    public void testGetParams(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getParams(), l -> objetTest.setParams(l), new ParamRegle(), "params");
    }

    @Test
    public void testGetDefaultDebtRemFnType(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDefaultDebtRemFnType(), s -> objetTest.setDefaultDebtRemFnType(s));
    }

    @Test
    public void testGetDefaultDebtRemFnCoeff(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDefaultDebtRemFnCoeff(), s -> objetTest.setDefaultDebtRemFnCoeff(s));
    }

    @Test
    public void testGetDefaultDebtRemFnOffset(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDefaultDebtRemFnOffset(), s -> objetTest.setDefaultDebtRemFnOffset(s));
    }

    @Test
    public void testGetEffortToFixDescription(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEffortToFixDescription(), s -> objetTest.setEffortToFixDescription(s));
    }

    @Test
    public void testIsDebtOverloaded(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isDebtOverloaded(), s -> objetTest.setDebtOverloaded(s));
    }

    @Test
    public void testGetDebtRemFnType(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDebtRemFnType(), s -> objetTest.setDebtRemFnType(s));
    }

    @Test
    public void testGetDebtRemFnCoeff(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDebtRemFnCoeff(), s -> objetTest.setDebtRemFnCoeff(s));
    }

    @Test
    public void testGetDebtRemFnOffset(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDebtRemFnOffset(), s -> objetTest.setDebtRemFnOffset(s));
    }

    @Test
    public void testGetDefaultRemFnType(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDefaultRemFnType(), s -> objetTest.setDefaultRemFnType(s));
    }

    @Test
    public void testGetDefaultRemFnGapMultiplier(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDefaultRemFnGapMultiplier(), s -> objetTest.setDefaultRemFnGapMultiplier(s));
    }

    @Test
    public void testGetDefaultRemFnBaseEffort(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDefaultRemFnBaseEffort(), s -> objetTest.setDefaultRemFnBaseEffort(s));
    }

    @Test
    public void testGetRemFnType(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getRemFnType(), s -> objetTest.setRemFnType(s));
    }

    @Test
    public void testGetRemFnGapMultiplier(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getRemFnGapMultiplier(), s -> objetTest.setRemFnGapMultiplier(s));
    }

    @Test
    public void testGetRemFnBaseEffort(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getRemFnBaseEffort(), s -> objetTest.setRemFnBaseEffort(s));
    }

    @Test
    public void testIsRemFnOverloaded(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isRemFnOverloaded(), s -> objetTest.setRemFnOverloaded(s));
    }

    @Test
    public void testGetGapDescription(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getGapDescription(), s -> objetTest.setGapDescription(s));
    }

    @Test
    public void testGetScope(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getScope(), s -> objetTest.setScope(s));
    }

    @Test
    public void testIsExternal(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isExternal(), s -> objetTest.setExternal(s));
    }

    @Test
    public void testType(TestInfo testInfo)
    {
        // Test initialisation
        assertThat(objetTest.getType()).isNull();

        // Test getter et setter
        objetTest.setType(TypeDefautSonar.BUG);
        assertThat(objetTest.getType()).isEqualTo(TypeDefautSonar.BUG);

        // Protection setter null
        objetTest.setType(null);
        assertThat(objetTest.getType()).isEqualTo(TypeDefautSonar.BUG);
    }

    @Test
    public void testGetActivations(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getActivations(), l -> objetTest.setActivations(l), new Activation(), "activations");
    }
}
