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
import model.enums.TypeMetrique;
import model.sonarapi.Composant;
import model.sonarapi.Parametre;
import model.sonarapi.Projet;
import model.sonarapi.Vue;

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
        responseMock = Mockito.mock(Response.class);
        PowerMockito.when(responseMock, "getStatus").thenReturn(Status.FORBIDDEN.getStatusCode());
        logger = TestUtils.getMockLogger("logger");
    }
    
    @Before
    public void init()
    {
        api = PowerMockito.spy(SonarAPI.INSTANCE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test(expected = AssertException.class)
    public void createReflexion() throws InstantiationException, IllegalAccessException
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
    public void getVues() throws Exception
    {
        // Appel classique
        List<Vue> vues = api.getVues();
        assertTrue(vues != null);
        assertFalse(vues.isEmpty());

        // Mock de la réponse pour renvoyer un status non OK et passer dans le logger
        PowerMockito.when(api, APPELGET, Mockito.anyString()).thenReturn(responseMock);

        // nouvel appel et contrôle que la liste est vide et que le logger a bine été utilisé.
        vues = api.getVues();
        assertTrue(vues != null);
        assertTrue(vues.isEmpty());
        Mockito.verify(logger, Mockito.times(1)).error(Mockito.anyString());

    }
    
    @Test
    public void getVuesParNom() throws Exception
    {
        // Appel classique
        List<Projet> projets = api.getVuesParNom("APPLI MASTER ");
        assertTrue(projets != null);
        assertFalse(projets.isEmpty());
        
        // Mock de la réponse pour renvoyer un status non OK et passer dans le logger
        Method methode = Whitebox.getMethod(SonarAPI.class, APPELGET, String.class, Parametre[].class);
        PowerMockito.when(api, methode).withArguments(Mockito.anyString(), Mockito.any(Parametre.class)).thenReturn(responseMock);
        
        // nouvel appel et contrôle que la liste est vide et que le logger a bien été utilisé.
        projets = api.getVuesParNom("APPLI MASTER ");
        assertTrue(projets != null);
        assertTrue(projets.isEmpty());
        Mockito.verify(logger, Mockito.times(1)).error(Mockito.anyString());
    }
    
    @Test
    public void verificationUtilisateur() throws Exception
    {
        // Appel classique
        boolean verif = api.verificationUtilisateur();
        assertTrue(verif);
        Mockito.verify(logger, Mockito.times(1)).info("Utilisateur OK");
        
        api = PowerMockito.spy(api);
        PowerMockito.when(api, APPELGET, Mockito.anyString()).thenReturn(responseMock);
        
        verif = api.verificationUtilisateur();
        assertFalse(verif);
        Mockito.verify(logger, Mockito.times(1)).info("Utilisateur KO");       
    }
    
    @Test
    public void getMetriquesComposant()
    {
        // Appel classique sans erreur
        Composant composant = api.getMetriquesComposant("fr.ca.cat.apimanager.resources:RESS_Dossiers_Epargne_Patrimoniale_Recapitulatif_entretien_Build:14", new String[] {TypeMetrique.VULNERABILITIES.toString(), TypeMetrique.BUGS.toString()});
        assertNotNull(composant);
        assertNotNull(composant.getMetriques());
        assertFalse(composant.getMetriques().isEmpty());
        
        // Appel avec un mauvais composant. Contrôle du log de l'erreur
        composant = api.getMetriquesComposant("a", new String[] {TypeMetrique.BUGS.toString()});
        assertNotNull(composant);
        assertNotNull(composant.getMetriques());
        assertTrue(composant.getMetriques().isEmpty());
        Mockito.verify(logger, Mockito.times(1)).error(Mockito.anyString());
        
    }
    
    @Test
    public void getSecuriteComposant()
    {
        // Appel sans erreur
        api.getSecuriteComposant("fr.ca.cat.apimanager.resources:RESS_Dossiers_Epargne_Patrimoniale_Recapitulatif_entretien_Build:14");
        Mockito.verify(logger, Mockito.never()).error(Mockito.anyString());
        
        // Appel avec retour d'une erreur
        assertEquals(0, api.getSecuriteComposant("fa"));
        Mockito.verify(logger, Mockito.times(1)).error(Mockito.anyString());
    }
    
    @Test
    public void getIssuesComposant()
    {
        
    }
    
    @Test
    public void getVersionComposant()
    {
        
    }
    
    @Test
    public void getComposants()
    {
        
    }
    
    @Test
    public void getQualityGate()
    {
        
    }
    
    @Test
    public void getListQualitygate()
    {
        
    }
    
    @Test
    public void testVueExiste()
    {
        
    }
    
    @Test
    public void creerVue()
    {
        Vue vue = new Vue();
        vue.setKey("APPLI_Master_5MPR");
        vue.setName("APPLI_Master_5MPR");
        vue.setDescription("vue description");
        api.creerVue(vue);
    }

    @Test
    public void creerVueAsync()
    {
        Vue vue = new Vue();
        vue.setKey("bueKey");
        vue.setName("Vue Name sdfs df");
        vue.setDescription("vue description");
        api.creerVueAsync(vue);
    }
    
    @Test
    public void supprimerProjet()
    {
        
    }

    @Test
    public void supprimerVue()
    {
        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}