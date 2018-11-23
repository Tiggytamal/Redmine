package junit.control.rest;

import static org.powermock.api.mockito.PowerMockito.when;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import control.rest.SonarAPI5;
import control.rest.SonarAPI67;
import junit.JunitBase;
import junit.TestUtils;
import model.rest.sonarapi.Projet;

public class TestSonarAPI67 extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private SonarAPI67 handler;
    private Response responseMock;
    private Logger logger;

    private static final String APPELGET = "appelWebserviceGET";
    
    /*---------- CONSTRUCTEURS ----------*/

    public TestSonarAPI67() throws Exception
    {
        // Mock de la r�ponse pour forcer les retours en erreur des webservices
        responseMock = Mockito.mock(Response.class);
        when(responseMock, "getStatus").thenReturn(Status.FORBIDDEN.getStatusCode());
        
        // Mock du logger pour v�rifier les appels � celui-ci
        logger = TestUtils.getMockLogger(SonarAPI5.class, "LOGGER");
    }

    @Before
    public void init()
    {
        handler = PowerMockito.spy(SonarAPI67.INSTANCE);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testConnexionUtilisateur()
    {
        handler.connexionUtilisateur();
    }
    
    @Test
    public void testGetComposants()
    {
        List<Projet> liste = handler.getComposants();
        System.out.println(liste.size());
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}