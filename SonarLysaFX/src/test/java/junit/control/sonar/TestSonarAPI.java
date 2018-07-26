package junit.control.sonar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import com.mchange.util.AssertException;

import control.sonar.SonarAPI;
import junit.JunitBase;
import junit.TestUtils;
import model.enums.Param;
import model.enums.TypeMetrique;
import model.sonarapi.Composant;
import model.sonarapi.Issue;
import model.sonarapi.Parametre;
import model.sonarapi.Projet;
import model.sonarapi.QualityGate;
import model.sonarapi.Vue;
import utilities.FunctionalException;
import utilities.Statics;

public class TestSonarAPI extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private SonarAPI handler;
    private Response responseMock;
    private Logger logger;

    private static final String APPELGET = "appelWebserviceGET";

    /*---------- CONSTRUCTEURS ----------*/

    public TestSonarAPI() throws Exception
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
        handler = PowerMockito.spy(SonarAPI.INSTANCE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test(expected = AssertException.class)
    public void testCreateReflexion() throws InstantiationException, IllegalAccessException
    {
        // Contrôle que l'on ne peut pas instancier un deuxième controleur par réflexion
        try
        {
            Whitebox.getConstructor(SonarAPI.class).newInstance();
        } catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof AssertException)
                throw (AssertException) e.getCause();
        }
    }

    @Test
    public void testGetVues() throws Exception
    {
        // Appel classique
        List<Vue> vues = handler.getVues();
        assertNotNull(vues);
        assertFalse(vues.isEmpty());

        appelGetMock();

        // nouvel appel et contrôle que la liste est vide et que le logger a bine été utilisé.
        vues = handler.getVues();
        assertNotNull(vues);
        assertEquals(0, vues.size());
        testLogger();
    }

    @Test
    public void testGetVuesParNom() throws Exception
    {
        // Appel classique
        List<Projet> projets = handler.getVuesParNom("APPLI MASTER ");
        assertNotNull(projets);
        assertFalse(projets.isEmpty());

        // Mock de la réponse pour renvoyer un status non OK et passer dans le logger
        appelGetMockWithParam();

        // nouvel appel et contrôle que la liste est vide et que le logger a bien été utilisé.
        projets = handler.getVuesParNom("APPLI MASTER ");
        assertNotNull(projets);
        assertEquals(0, projets.size());
        testLogger();
    }

    @Test
    public void testVerificationUtilisateur() throws Exception
    {
        // Appel classique
        boolean verif = handler.verificationUtilisateur();
        assertTrue(verif);
        verify(logger, times(1)).info("Utilisateur OK");
        
        appelGetMock();

        verif = handler.verificationUtilisateur();
        assertFalse(verif);
        verify(logger, times(1)).info("Utilisateur KO");
    }

    @Test
    public void testGetMetriquesComposant()
    {
        // Appel classique sans erreur
        Composant composant = handler.getMetriquesComposant("fr.ca.cat.apimanager.resources:RESS_Dossiers_Epargne_Patrimoniale_Recapitulatif_entretien_Build:14",
                new String[] { TypeMetrique.VULNERABILITIES.toString(), TypeMetrique.BUGS.toString() });
        assertNotNull(composant);
        assertNotNull(composant.getMetriques());
        assertFalse(composant.getMetriques().isEmpty());

        // Appel avec un mauvais composant. Contrôle du log de l'erreur
        composant = handler.getMetriquesComposant("a", new String[] { TypeMetrique.BUGS.toString() });
        assertNull(composant);
        testLogger();
    }

    @Test
    public void testGetSecuriteComposant()
    {
        // Appel sans erreur
        handler.getSecuriteComposant("fr.ca.cat.apimanager.resources:RESS_Dossiers_Epargne_Patrimoniale_Recapitulatif_entretien_Build:14");
        testNoLogger();

        // Appel avec retour d'une erreur
        assertEquals(0, handler.getSecuriteComposant("fa"));
        testLogger();
    }

    @Test
    public void testGetIssuesComposant()
    {
        // Appel composant avec plus de 100 issues
        List<Issue> liste = handler.getIssuesComposant("fr.ca.cat:BAM_Webapp_Standard_Build:14");
        assertNotNull(liste);
        assertTrue(liste.size() > 100);

        // Appel avec retour d'une erreur
        List<Issue> listeErreur = handler.getIssuesComposant("fa");
        assertNotNull(listeErreur);
        assertEquals(0, listeErreur.size());
        testLogger();
    }
    
    @Test
    public void testGetIssuesGenerique() throws Exception
    {
        // Paramètres
        List<Parametre> params = new ArrayList<>();
        params.add(new Parametre("severities", "CRITICAL, BLOCKER"));
        params.add(new Parametre("resolved", "false"));
        params.add(new Parametre("tags", "cve"));
        params.add(new Parametre("types", "VULNERABILITY"));
        
        // Appel de la méthode et contrôle sans bouchon
        List<Issue> liste = handler.getIssuesGenerique(params);
        assertNotNull(liste);
        assertTrue(liste.size() > 100);
        
        // Bouchonnnage pour retour appel en erreur.
        PowerMockito.doReturn(responseMock).when(handler).appelWebserviceGET(Mockito.anyString(), Mockito.any());       
        PowerMockito.when(responseMock, "getStatus").thenReturn(Status.BAD_REQUEST.getStatusCode());
        
        // Appel
        liste = handler.getIssuesGenerique(params);
        assertNotNull(liste);
        assertEquals(0, liste.size());
        verify(logger, Mockito.timeout(1)).error("Erreur API : api/issues/search - Composant : ");
    }

    @Test
    public void testGetVersionComposant()
    {
        // Appel service avec composant existant
        String version = handler.getVersionComposant("fr.ca.cat:BAM_Webapp_Standard_Build:14");
        assertNotNull(version);
        assertFalse(version.isEmpty());
        assertEquals("31.0.4", version);
        testNoLogger();

        // Appel avec composant non existant
        version = handler.getVersionComposant("ab");
        assertNotNull(version);
        assertEquals(0, version.length());
        testLogger();
    }

    @Test(expected = FunctionalException.class)
    public void testGetComposants() throws Exception
    {
        // Appel du webservice
        List<Projet> liste = handler.getComposants();
        assertFalse(liste == null);
        assertFalse(liste.isEmpty());
        testNoLogger();

        // Mock de la réponse pour renvoyer un status non OK et passer dans le logger
        appelGetMockWithParam();

        // Appel de la méthode, avec catch de l'erreur pour tester le logger, puis renvoie de celle-ci pour contrôle
        try
        {
            handler.getComposants();
        } catch (FunctionalException e)
        {
            testLogger();
            throw e;
        }
    }

    @Test(expected = FunctionalException.class)
    public void testGetQualityGate()
    {
        // Récupération nom QualityGate DataStage depuis paramètres
        String nomQG = Statics.proprietesXML.getMapParams().get(Param.NOMQGDATASTAGE);
        assertFalse(nomQG.isEmpty());

        // Appel webservice de récupération de la QualityGate Sonar
        QualityGate qg = handler.getQualityGate(nomQG);
        assertFalse(qg.getId() == null);
        assertFalse(qg.getId().isEmpty());
        assertFalse(qg.getName() == null);
        assertFalse(qg.getName().isEmpty());

        // Appel avec QG non existante et retour exception
        handler.getQualityGate("a");
    }

    @Test(expected = FunctionalException.class)
    public void testGetListQualitygate() throws Exception
    {
        // Appel liste QualityGate
        List<QualityGate> liste = handler.getListQualitygate();
        assertFalse(liste == null);
        assertFalse(liste.isEmpty());

        // Appel avec retour en erreur
        appelGetMock();
        handler.getListQualitygate();
    }

    @Test
    public void testTestVueExiste() throws Exception
    {
        // Test vue existante
        boolean test = handler.testVueExiste("E31Key");
        assertTrue(test);
        
        // Test vue inconnue
        test = handler.testVueExiste("abc");
        assertFalse(test);
        
        // Test erreur appel  + log
        appelGetMockWithParam();
        handler.testVueExiste("E31Key");
        testLogger();
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testTestVueExisteException1()
    {
        handler.testVueExiste(null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testTestVueExisteException2()
    {
        handler.testVueExiste("");
    }

    @Test
    public void testCreerVue() throws Exception
    {
        // Initialisation vue
        Vue vue = new Vue();
        
        // Test avec vue vide
        assertEquals(Status.BAD_REQUEST, handler.creerVue(vue));
        
        // Initialisation vue avec données
        vue.setKey("APPLI_Master_5MPR");
        vue.setName("APPLI_Master_5MPR");
        vue.setDescription("vue description");
        
        // Mock de l'appel webservice pour ne pas créer dans SonarQube
        PowerMockito.doReturn(responseMock).when(handler).appelWebservicePOST(Mockito.anyString(), Mockito.any());       
        when(responseMock, "getStatus").thenReturn(Status.OK.getStatusCode());
        when(responseMock, "getStatusInfo").thenReturn(Status.OK);
        
        // Contrôle de la méthode
        assertEquals(Status.OK, handler.creerVue(vue));
        verify(logger, times(1)).info("Creation vue : " + vue.getKey() + " - nom : " + vue.getName() + ": HTTP " + Status.OK.getStatusCode());
    }

    @Test
    public void testCreerVueAsync()
    {
        Vue vue = new Vue();
        
        vue.setKey("bueKey");
        vue.setName("Vue Name sdfs df");
        vue.setDescription("vue description");
        Future<Response> future = new Future<Response>() {
            
            @Override
            public boolean isDone()
            {
                return false;
            }
            
            @Override
            public boolean isCancelled()
            {
                return false;
            }
            
            @Override
            public Response get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
            {
                return responseMock;
            }
            
            @Override
            public Response get() throws InterruptedException, ExecutionException
            {
                return responseMock;
            }
            
            @Override
            public boolean cancel(boolean mayInterruptIfRunning)
            {
                return false;
            }
        };
        
        PowerMockito.doReturn(future).when(handler).appelWebserviceAsyncPOST(Mockito.anyString(), Mockito.any());  
        handler.creerVueAsync(vue);
    }

    @Test
    public void testSupprimerProjet()
    {

    }

    @Test
    public void testSupprimerVue()
    {

    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Mock l'api pour retourner une réponse en erreur de l'appel du webservice GET
     * @throws Exception
     */
    private void appelGetMock() throws Exception
    {
        when(handler, APPELGET, Mockito.anyString()).thenReturn(responseMock);
    }
    
    /**
     *  Mock l'api pour retourner une réponse en erreur de l'appel du webservice GET avec des paramètres
     * @throws Exception
     */
    private void appelGetMockWithParam() throws Exception
    {
        Method methode = Whitebox.getMethod(SonarAPI.class, APPELGET, String.class, Parametre[].class);
        when(handler, methode).withArguments(Mockito.anyString(), Mockito.any(Parametre.class)).thenReturn(responseMock);
    }
    
    /**
     * Teste si le logger a été appelé pour une erreur
     */
    private void testLogger()
    {
        verify(logger, times(1)).error(Mockito.anyString());
    }
    
    /**
     * Teste sir le logger n'a pas été appelé pour récupérer une erreur
     */
    private void testNoLogger()
    {
        verify(logger, Mockito.never()).error(Mockito.anyString());
    }
    /*---------- ACCESSEURS ----------*/
}