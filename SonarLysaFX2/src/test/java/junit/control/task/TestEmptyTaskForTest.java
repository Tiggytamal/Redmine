package junit.control.task;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.concurrent.TimeoutException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;
import org.testfx.api.FxToolkit;

import junit.AutoDisplayName;
import junit.JunitBase;
import model.EmptyTaskForTest;
import model.Info;
import model.ModelFactory;
import model.OptionInitAPI;
import model.enums.Severity;
import utilities.FunctionalException;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestEmptyTaskForTest extends TestAbstractTask<EmptyTaskForTest>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    protected void initImpl() throws IllegalAccessException
    {
        objetTest = new EmptyTaskForTest(true);
        initAPI(EmptyTaskForTest.class, OptionInitAPI.MOCK);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructorException(TestInfo testInfo) throws JAXBException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Préparation données pour exception
        Info infoTest = ModelFactory.build(Info.class);
        Whitebox.setInternalState(Statics.class, infoTest);

        // Appel constructeur
        try
        {
            FunctionalException e = assertThrows(FunctionalException.class, () -> new EmptyTaskForTest(true));
            assertThat(e.getSeverity()).isEqualTo(Severity.ERROR);
            assertThat(e.getMessage()).isEqualTo("Pas de connexion au serveur Sonar, merci de vous reconnecter");
        }
        finally
        {

            Whitebox.setInternalState(Statics.class, JAXBContext.newInstance(Info.class).createUnmarshaller().unmarshal(new File(JunitBase.class.getResource("/info.xml").getFile())));

        }
    }

    @Test
    public void testConstructor(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        EmptyTaskForTest task = new EmptyTaskForTest(true);
        assertThat(task.getTitre()).isEqualTo("TEST");
    }

    @Test
    public void testUpdateMessageParente(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest = new EmptyTaskForTest(true, new EmptyTaskForTest(true));
        objetTest.setBaseMessage(TESTSTRING);
        objetTest.updateMessage(TESTSTRING);
        assertThat(objetTest.getBaseMessage()).isEqualTo(TESTSTRING);
    }

    @Test
    public void testUpdateProgressParente(TestInfo testInfo) throws IllegalAccessException, InterruptedException, TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        objetTest = new EmptyTaskForTest(true, new EmptyTaskForTest(true));

        // Test initial
        assertThat(((EmptyTaskForTest) getField("tacheParente")).getProgress()).isEqualTo(-1.0d);

        // Test progress = 0
        FxToolkit.setupFixture(() -> objetTest.updateProgress(0, 1));
        assertThat(objetTest.getProgress()).isEqualTo(0.0d);
        assertThat(((EmptyTaskForTest) getField("tacheParente")).getProgress()).isEqualTo(0.0d);

        // Test progress = 1
        FxToolkit.setupFixture(() -> objetTest.updateProgress(1, 1));
        assertThat(objetTest.getProgress()).isEqualTo(1.0d);
        assertThat(((EmptyTaskForTest) getField("tacheParente")).getProgress()).isEqualTo(1.0d);
    }

    @Test
    public void testCreerObjetSonar(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
