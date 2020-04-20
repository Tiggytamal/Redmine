package junit.control.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.rest.SonarAPI5;
import control.rest.SonarAPI67;
import junit.JunitBase;
import junit.TestUtils;
import model.Info;
import model.ModelFactory;
import model.rest.sonarapi.Projet;
import model.rest.sonarapi67.Portfolio;
import model.rest.sonarapi67.QualityProfile;
import model.rest.sonarapi67.Rule;
import model.rest.sonarapi67.VueAppli;
import utilities.Statics;

public class TestSonarAPI67 extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private SonarAPI67 controlAPI;
    private Response responseMock;
    private Logger logger;

    /*---------- CONSTRUCTEURS ----------*/

    public TestSonarAPI67() throws Exception
    {
        // Mock de la réponse pour forcer les retours en erreur des webservices
        responseMock = Mockito.mock(Response.class);
        when(responseMock, "getStatus").thenReturn(Status.FORBIDDEN.getStatusCode());

        // Mock du logger pour vérifier les appels à celui-ci
        logger = TestUtils.getMockLogger(SonarAPI5.class, "LOGGER");

        Info info = ModelFactory.build(Info.class);
        info.setMotDePasse("admin");
        info.setPseudo("admin");
        Whitebox.setInternalState(Statics.class, info);
    }

    @Before
    public void init()
    {
        controlAPI = SonarAPI67.INSTANCE;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConnexionUtilisateur()
    {
        controlAPI.connexionUtilisateur();
    }

    @Test
    public void testGetComposants()
    {
        List<Projet> liste = controlAPI.getComposants();
        assertNotNull(liste);
        assertFalse(liste.isEmpty());
    }

    @Test
    public void testGetQualityProfileByName()
    {
        QualityProfile qp = controlAPI.getQualityProfileByName("Crédit Agricole (SECAPI) - JAVA");
        assertNotNull(qp);
        assertEquals("Crédit Agricole (SECAPI) - JAVA", qp.getName());
    }

    @Test
    public void testGetRulesByQualityProfile()
    {
        QualityProfile qp = controlAPI.getQualityProfileByName("Crédit Agricole (SECAPI) - JAVA");
        List<Rule> liste = controlAPI.getRulesByQualityProfile(qp);
        assertNotNull(liste);
        assertFalse(liste.isEmpty());
    }

    @Test
    public void testGetVuesParNom()
    {
        List<Projet> liste = controlAPI.getVuesParNom("Branche");
        assertNotNull(liste);
    }

    @Test
    public void testCreerPortFolio()
    {
        Portfolio pf = new Portfolio("APPLITESTKey", "APPLI TEST3", "Description");
        Status status = controlAPI.creerPortFolio(pf);
        assertEquals(Status.OK, status);
    }

    @Test
    public void testCreerVueApplication()
    {
        VueAppli vue = new VueAppli();
        vue.setName("APPLI TEST");
        vue.setKey("KEY");
        Status status = controlAPI.creerVueApplication(vue);
        assertEquals(Status.OK, status);
    }
    
    @Test
    public void testMajVues()
    {
        controlAPI.majVues();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
