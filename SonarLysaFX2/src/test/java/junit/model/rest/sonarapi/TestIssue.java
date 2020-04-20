package junit.model.rest.sonarapi;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import junit.model.TestAbstractModele;
import model.enums.TypeDefautSonar;
import model.enums.TypeResolution;
import model.rest.sonarapi.Commentaire;
import model.rest.sonarapi.Issue;
import model.rest.sonarapi.TextRange;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestIssue extends TestAbstractModele<Issue>
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
    public void testGetRegle(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getRegle(), s -> objetTest.setRegle(s));
    }

    @Test
    public void testGetSeverite(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getSeverite(), s -> objetTest.setSeverite(s));
    }

    @Test
    public void testGetComposant(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getComposant(), s -> objetTest.setComposant(s));
    }

    @Test
    public void testGetProjet(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getProjet(), s -> objetTest.setProjet(s));
    }

    @Test
    public void testGetSousProjet(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getSousProjet(), s -> objetTest.setSousProjet(s));
    }

    @Test
    public void testGetLigne(TestInfo testInfo)
    {
        testSimpleInteger(testInfo, () -> objetTest.getLigne(), i -> objetTest.setLigne(i));
    }

    @Test
    public void testGetTextRange(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation
        assertThat(objetTest.getTextRange()).isNotNull();

        // Test getter et setter
        TextRange textRange = new TextRange();
        textRange.setEndLine(1);
        objetTest.setTextRange(textRange);
        assertThat(objetTest.getTextRange()).isEqualTo(textRange);

        // Protection setter null
        objetTest.setTextRange(null);
        assertThat(objetTest.getTextRange()).isEqualTo(textRange);

        // Protection introspection null
        setField("textRange", null);
        assertThat(objetTest.getTextRange()).isNotNull();
    }

    @Test
    public void testGetResolution(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation null
        assertThat(objetTest.getResolution()).isNull();

        // Test getter et setter
        objetTest.setResolution(TypeResolution.FALSEPOSITIVE);
        assertThat(objetTest.getResolution()).isEqualTo(TypeResolution.FALSEPOSITIVE);
    }

    @Test
    public void testGetStatut(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getStatut(), s -> objetTest.setStatut(s));
    }

    @Test
    public void testGetMessage(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getMessage(), s -> objetTest.setMessage(s));
    }

    @Test
    public void testEffort(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEffort(), s -> objetTest.setEffort(s));
    }

    @Test
    public void testDebt(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getDebt(), s -> objetTest.setDebt(s));
    }

    @Test
    public void testGetAuteur(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getAuteur(), s -> objetTest.setAuteur(s));
    }

    @Test
    public void testGetAssigne(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getAssigne(), s -> objetTest.setAssigne(s));
    }

    @Test
    public void testGetTags(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getTags(), l -> objetTest.setTags(l), TESTSTRING, "tags");
    }

    @Test
    public void testGetCommentaires(TestInfo testInfo) throws IllegalAccessException
    {
        testSimpleList(testInfo, () -> objetTest.getCommentaires(), l -> objetTest.setCommentaires(l), new Commentaire(), "commentaires");
    }

    @Test
    public void testGetDateCreation(TestInfo testInfo)
    {
        testSimpleLocalDateTime(testInfo, () -> objetTest.getDateCreation(), ld -> objetTest.setDateCreation(ld));
    }

    @Test
    public void testGetDateMaj(TestInfo testInfo)
    {
        testSimpleLocalDateTime(testInfo, () -> objetTest.getDateMaj(), ld -> objetTest.setDateMaj(ld));
    }

    @Test
    public void testGetDateCloture(TestInfo testInfo)
    {
        testSimpleLocalDateTime(testInfo, () -> objetTest.getDateCloture(), ld -> objetTest.setDateCloture(ld));
    }

    @Test
    public void testGetType(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation null
        assertThat(objetTest.getType()).isNull();

        // Test getter et setter
        objetTest.setType(TypeDefautSonar.BUG);
        assertThat(objetTest.getType()).isEqualTo(TypeDefautSonar.BUG);

        // Protection setter null
        objetTest.setType(null);
        assertThat(objetTest.getType()).isEqualTo(TypeDefautSonar.BUG);
    }

    @Test
    public void testIsFromHotspot(TestInfo testInfo)
    {
        testSimpleBoolean(testInfo, () -> objetTest.isFromHotspot(), b -> objetTest.setFromHotspot(b));
    }

}
