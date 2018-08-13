package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.ParameterizedType;
import java.util.Base64;
import java.util.function.Supplier;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.sonar.SonarAPI;
import control.task.AbstractSonarTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;
import model.enums.Param;
import model.sonarapi.Parametre;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

/**
 * Classe abstraite générique pour le test des classes de traitement des tâches.<br>
 * Initialise le handler, ainsi que le mock de l'api Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * @param <T>
 *            Classe concrète de traitement d'une tâche
 */
@RunWith(JfxRunner.class)
public abstract class AbstractTestTask<T extends AbstractSonarTask> extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    protected T handler;
    protected SonarAPI api;

    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testAnnuler() throws IllegalAccessException
    {
        // Pas d'action sur la fonction annuler
        handler.annuler();
        assertFalse(handler.isCancelled());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = FunctionalException.class)
    public void testConstructeurException() throws IllegalAccessException, ClassNotFoundException, InstantiationException
    {
        // Permet de récuperer la classe sous forme de type paramétré
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On récupère les paramètres de classe (ici T et R), on prend le premier (T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];

        // Modification pseudo pour créationd e l'erreur fonctionnelle
        String temp = Statics.info.getPseudo();
        Statics.info.setPseudo(null);

        // Instantiate the Parameter and initialize it.
        try
        {
            handler = (T) Class.forName(parameterClassName).newInstance();
        }
        catch (FunctionalException e)
        {
            assertEquals("Pas de connexion au serveur Sonar, merci de vous reconnecter", e.getMessage());
            assertEquals(Severity.ERROR, e.getSeverity());
            throw e;
        }
        finally
        {
            // Remise de la bonne valeur du pseudo
            Statics.info.setPseudo(temp);
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    protected void initAPI(Class<T> clazz, boolean mock) throws IllegalAccessException
    {
        if (mock)
        {
            api = Mockito.mock(SonarAPI.class);            
            Whitebox.getField(clazz, "api").set(handler, api);
        }
        else
            api = SonarAPI.INSTANCE;
    }
    
    /**
     * Permet d'utiliser une vrai méthode get de l'api SonarAPI avec le mock. ex: getComposants, getVues(). La méthode doit avoir un objet en retour
     * 
     * @throws IllegalAccessException
     */
    protected <R> void mockAPIGetSomething(Supplier<R> supplier) throws IllegalAccessException
    {
        // Init du codeUser
        StringBuilder builder = new StringBuilder(Statics.info.getPseudo());
        builder.append(":");
        builder.append(Statics.info.getMotDePasse());
        Whitebox.getField(SonarAPI.class, "codeUser").set(api, Base64.getEncoder().encodeToString(builder.toString().getBytes()));
        
        // init du webtarget
        WebTarget webTarget = ClientBuilder.newClient().target(Statics.proprietesXML.getMapParams().get(Param.URLSONAR));
        Whitebox.getField(SonarAPI.class, "webTarget").set(api, webTarget);

        // Vrai appel webservice
        Parametre param = new Parametre("search", "composant ");
        Mockito.when(api.appelWebserviceGET(Mockito.anyString(), Mockito.refEq(param))).thenCallRealMethod();
        Mockito.when(api.appelWebserviceGET(Mockito.anyString())).thenCallRealMethod();
        
        // Vrai appel getComposant
        Mockito.when(supplier.get()).thenCallRealMethod();
    }

    /*---------- ACCESSEURS ----------*/

}
