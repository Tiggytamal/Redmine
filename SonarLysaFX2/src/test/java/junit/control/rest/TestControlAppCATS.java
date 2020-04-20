package junit.control.rest;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import control.rest.ControlAppCATS;
import junit.AutoDisplayName;
import junit.TestUtils;
import model.rest.sonarapi.ParamAPI;
import utilities.FunctionalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestControlAppCATS extends TestAbstractControlRest<ControlAppCATS>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    public TestControlAppCATS() throws Exception
    {
        super();
    }

    @Override
    @BeforeEach
    public void init()
    {
        objetTest = ControlAppCATS.INSTANCE;

        // Mock du logger pour vérifier les appels à celui-ci
        loggerMock = TestUtils.getMockLogger(ControlAppCATS.class, "LOGPLANTAGE");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructor(TestInfo testInfo) throws Throwable
    {
        LOGGER.debug(testInfo.getDisplayName());

        testConstructor_Exception("control.rest.ControlAppCATS - Singleton, instanciation interdite!");
    }
    
    @Test
    public void testTestApplicationExist_Vide(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test application vide
        assertThat(objetTest.testApplicationExiste(EMPTY)).isFalse();
    }
    
    @Test
    public void testTestApplicationExist_Existe(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test application existante
        assertThat(objetTest.testApplicationExiste("V360")).isTrue();
    }
    
    @Test
    public void testTestApplicationExist_Non_existante(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test application non existante
        assertThat(objetTest.testApplicationExiste("V361")).isFalse();
    }
    
    @Test
    public void testTestApplicationExist_Exception_API(TestInfo testInfo)
    {
        // Test application
        objetTest = PowerMockito.spy(objetTest);
        PowerMockito.doReturn(responseMock).when(objetTest).appelWebserviceGET(Mockito.eq("api/v1/applications/exist"), Mockito.any(ParamAPI[].class));
        
        // Test exception - erreur api
        assertThrows(FunctionalException.class, () -> objetTest.testApplicationExiste("V360"));
        verify(loggerMock).error("Impossible de remonter les informations du code application V360 - API : api/v1/applications/exist");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
