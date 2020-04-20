package junit.control.rest;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.powermock.api.mockito.PowerMockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.Logger;
import org.mockito.Mockito;

import control.rest.AbstractControlRest;
import junit.JunitBase;

public abstract class TestAbstractControlRest<T extends AbstractControlRest> extends JunitBase<T>
{
    /*---------- ATTRIBUTS ----------*/

    /** Réponse mockée d'un webservice avec une erreur */
    protected Response responseMock;

    /** Mock d'un logger pour tester les apples à celui-ci */
    protected Logger loggerMock;

    /*---------- CONSTRUCTEURS ----------*/

    public TestAbstractControlRest() throws Exception
    {
        // Mock de la reponse pour forcer les retours en erreur des webservices
        responseMock = Mockito.mock(Response.class);
        when(responseMock.getStatus()).thenReturn(Status.FORBIDDEN.getStatusCode());
    }

    /*---------- METHODES ABSTRAITES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    /**
     * Méthode de contrôle de protection des instances singleton
     * 
     * @param message
     *                Message d'erreur à contrôler.
     * @throws NoSuchMethodException
     *                               Exception renvoyée par l'introspection.
     */
    protected void testConstructor_Exception(String message) throws NoSuchMethodException
    {
        // Préparation Constructor
        Constructor<? extends AbstractControlRest> constructor = objetTest.getClass().getDeclaredConstructor();
        constructor.setAccessible(true);

        // Appel méthode et contrôle de l'exception
        InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> constructor.newInstance());
        assertThat(e.getCause()).isNotNull();
        assertThat(e.getCause()).isInstanceOf(AssertionError.class);
        assertThat(e.getCause().getMessage()).isEqualTo(message);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
