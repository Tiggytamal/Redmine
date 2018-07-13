package junit.control.sonar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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

    private SonarAPI api;
    private Response responseMock;
    private Logger logger;

    private static final String APPELGET = "appelWebserviceGET";

    /*---------- CONSTRUCTEURS ----------*/

    public TestSonarAPI() throws Exception
    {
        // Mock de la réponse pour forcer les retours en erreur des webservices
        responseMock = Mockito.mock(Response.class);
        PowerMockito.when(responseMock, "getStatus").thenReturn(Status.FORBIDDEN.getStatusCode());
        
        // Mock du logger pour vérifier les appels à celui-ci
        logger = TestUtils.getMockLogger(SonarAPI.class, "LOGGER");
    }

    @Before
    public void init()
    {
        api = PowerMockito.spy(SonarAPI.INSTANCE);
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
        List<Vue> vues = api.getVues();
        assertTrue(vues != null);
        assertFalse(vues.isEmpty());

        appelGetMock();

        // nouvel appel et contrôle que la liste est vide et que le logger a bine été utilisé.
        vues = api.getVues();
        assertTrue(vues != null);
        assertTrue(vues.isEmpty());
        testLogger();
    }

    @Test
    public void testGetVuesParNom() throws Exception
    {
        // Appel classique
        List<Projet> projets = api.getVuesParNom("APPLI MASTER ");
        assertTrue(projets != null);
        assertFalse(projets.isEmpty());

        // Mock de la réponse pour renvoyer un status non OK et passer dans le logger
        appelGetMockWithParam();

        // nouvel appel et contrôle que la liste est vide et que le logger a bien été utilisé.
        projets = api.getVuesParNom("APPLI MASTER ");
        assertTrue(projets != null);
        assertTrue(projets.isEmpty());
        testLogger();
    }

    @Test
    public void testVerificationUtilisateur() throws Exception
    {
        // Appel classique
        boolean verif = api.verificationUtilisateur();
        assertTrue(verif);
        Mockito.verify(logger, Mockito.times(1)).info("Utilisateur OK");
        
        appelGetMock();

        verif = api.verificationUtilisateur();
        assertFalse(verif);
        Mockito.verify(logger, Mockito.times(1)).info("Utilisateur KO");
    }

    @Test
    public void testGetMetriquesComposant()
    {
        // Appel classique sans erreur
        Composant composant = api.getMetriquesComposant("fr.ca.cat.apimanager.resources:RESS_Dossiers_Epargne_Patrimoniale_Recapitulatif_entretien_Build:14",
                new String[] { TypeMetrique.VULNERABILITIES.toString(), TypeMetrique.BUGS.toString() });
        assertNotNull(composant);
        assertNotNull(composant.getMetriques());
        assertFalse(composant.getMetriques().isEmpty());

        // Appel avec un mauvais composant. Contrôle du log de l'erreur
        composant = api.getMetriquesComposant("a", new String[] { TypeMetrique.BUGS.toString() });
        assertNotNull(composant);
        assertNotNull(composant.getMetriques());
        assertTrue(composant.getMetriques().isEmpty());
        testLogger();
    }

    @Test
    public void testGetSecuriteComposant()
    {
        // Appel sans erreur
        api.getSecuriteComposant("fr.ca.cat.apimanager.resources:RESS_Dossiers_Epargne_Patrimoniale_Recapitulatif_entretien_Build:14");
        testNoLogger();

        // Appel avec retour d'une erreur
        assertEquals(0, api.getSecuriteComposant("fa"));
        testLogger();
    }

    @Test
    public void testGetIssuesComposant()
    {
        // Appel composant avec plus de 100 issues
        List<Issue> liste = api.getIssuesComposant("fr.ca.cat:BAM_Webapp_Standard_Build:14");
        assertFalse(liste == null);
        assertTrue(liste.size() > 100);

        // Appel avec retour d'une erreur
        List<Issue> listeErreur = api.getIssuesComposant("fa");
        assertFalse(listeErreur == null);
        assertTrue(listeErreur.isEmpty());
        testLogger();
    }

    @Test
    public void testGetVersionComposant()
    {
        // Appel service avec composant existant
        String version = api.getVersionComposant("fr.ca.cat:BAM_Webapp_Standard_Build:14");
        assertTrue(version != null);
        assertFalse(version.isEmpty());
        assertEquals("31.0.4", version);
        testNoLogger();

        // Appel avec composant non existant
        version = api.getVersionComposant("ab");
        assertTrue(version != null);
        assertTrue(version.isEmpty());
        testLogger();
    }

    @Test(expected = FunctionalException.class)
    public void testGetComposants() throws Exception
    {
        // Appel du webservice
        List<Projet> liste = api.getComposants();
        assertFalse(liste == null);
        assertFalse(liste.isEmpty());
        testNoLogger();

        // Mock de la réponse pour renvoyer un status non OK et passer dans le logger
        appelGetMockWithParam();

        // Appel de la méthode, avec catch de l'erreur pour tester le logger, puis renvoie de celle-ci pour contrôle
        try
        {
            api.getComposants();
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
        QualityGate qg = api.getQualityGate(nomQG);
        assertFalse(qg.getId() == null);
        assertFalse(qg.getId().isEmpty());
        assertFalse(qg.getName() == null);
        assertFalse(qg.getName().isEmpty());

        // Appel avec QG non existante et retour exception
        api.getQualityGate("a");
    }

    @Test(expected = FunctionalException.class)
    public void testGetListQualitygate() throws Exception
    {
        // Appel liste QualityGate
        List<QualityGate> liste = api.getListQualitygate();
        assertFalse(liste == null);
        assertFalse(liste.isEmpty());

        // Appel avec retour en erreur
        appelGetMock();
        api.getListQualitygate();
    }

    @Test
    public void testTestVueExiste() throws Exception
    {
        // Test vue existante
        boolean test = api.testVueExiste("E31Key");
        assertTrue(test);
        
        // Test vue inconnue
        test = api.testVueExiste("abc");
        assertFalse(test);
        
        // Test erreur appel  + log
        appelGetMockWithParam();
        api.testVueExiste("E31Key");
        testLogger();
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testTestVueExisteException1()
    {
        api.testVueExiste(null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testTestVueExisteException2()
    {
        api.testVueExiste("");
    }

    @Test
    public void testCreerVue()
    {
        Vue vue = new Vue();
        vue.setKey("APPLI_Master_5MPR");
        vue.setName("APPLI_Master_5MPR");
        vue.setDescription("vue description");
        api.creerVue(vue);
    }
    
    @Test
    public void testCreerVueException()
    {
        
    }

    @Test
    public void testCreerVueAsync()
    {
        Vue vue = new Vue();
        vue.setKey("bueKey");
        vue.setName("Vue Name sdfs df");
        vue.setDescription("vue description");
        api.creerVueAsync(vue);
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
        PowerMockito.when(api, APPELGET, Mockito.anyString()).thenReturn(responseMock);
    }
    
    /**
     *  Mock l'api pour retourner une réponse en erreur de l'appel du webservice GET avec des paramètres
     * @throws Exception
     */
    private void appelGetMockWithParam() throws Exception
    {
        Method methode = Whitebox.getMethod(SonarAPI.class, APPELGET, String.class, Parametre[].class);
        PowerMockito.when(api, methode).withArguments(Mockito.anyString(), Mockito.any(Parametre.class)).thenReturn(responseMock);
    }
    
    /**
     * Teste si le logger a été appelé pour une erreur
     */
    private void testLogger()
    {
        Mockito.verify(logger, Mockito.times(1)).error(Mockito.anyString());
    }
    
    /**
     * Teste sir le logger n'a pas été appelé pour récupérer une erreur
     */
    private void testNoLogger()
    {
        Mockito.verify(logger, Mockito.never()).error(Mockito.anyString());
    }
    /*---------- ACCESSEURS ----------*/
}