package junit.control.sonar;

import static org.powermock.api.mockito.PowerMockito.when;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import control.sonar.SonarAPI;
import control.sonar.SonarAPI67;
import junit.JunitBase;
import junit.TestUtils;
import model.sonarapi.Projet;

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
        // Mock de la réponse pour forcer les retours en erreur des webservices
        responseMock = Mockito.mock(Response.class);
        when(responseMock, "getStatus").thenReturn(Status.FORBIDDEN.getStatusCode());
        
        // Mock du logger pour vérifier les appels à celui-ci
        logger = TestUtils.getMockLogger(SonarAPI.class, "LOGGER");
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
