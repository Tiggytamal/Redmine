package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.ParameterizedType;
import java.util.Base64;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.rest.SonarAPI5;
import control.task.AbstractTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;
import model.enums.Param;
import model.rest.sonarapi.Parametre;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

/**
 * Classe abstraite g�n�rique pour le test des classes de traitement des t�ches.<br>
 * Initialise le handler, ainsi que le mock de l'api Sonar.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 * @param <T>
 *            Classe concr�te de traitement d'une t�che
 */
@RunWith(JfxRunner.class)
public abstract class AbstractTestTask<T extends AbstractTask> extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    protected T handler;
    protected SonarAPI5 api;

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
        // Permet de r�cuperer la classe sous forme de type param�tr�
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On r�cup�re les param�tres de classe (ici T et R), on prend le premier (T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];

        // Modification pseudo pour cr�ationd e l'erreur fonctionnelle
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

    /**
     * Initialise l'api Sonar, et cr�e le mock au besoin.
     * 
     * @param clazz
     *            Classe � tester
     * @param mock
     *            true si besoin de cr�er le mock de l'api.
     * @throws IllegalAccessException
     */
    protected void initAPI(Class<T> clazz, boolean mock) throws IllegalAccessException
    {
        if (mock)
        {
            api = Mockito.mock(SonarAPI5.class);

            // Init du codeUser
            StringBuilder builder = new StringBuilder(Statics.info.getPseudo());
            builder.append(":");
            builder.append(Statics.info.getMotDePasse());
            Whitebox.getField(SonarAPI5.class, "codeUser").set(api, Base64.getEncoder().encodeToString(builder.toString().getBytes()));

            // init du webtarget
            WebTarget webTarget = ClientBuilder.newClient().target(Statics.proprietesXML.getMapParams().get(Param.URLSONAR));
            Whitebox.getField(SonarAPI5.class, "webTarget").set(api, webTarget);

            // Ajout du mock � l'instance de la classe test�e
            Whitebox.getField(clazz, "api").set(handler, api);
        }
        else
        {
            api = SonarAPI5.INSTANCE;
            Whitebox.getField(clazz, "api").set(handler, api);
        }
    }

    /**
     * Permet d'utiliser une vrai m�thode get de l'api SonarAPI avec le mock. ex: getComposants, getVues(). La m�thode doit avoir un objet en retour
     * 
     * @throws IllegalAccessException
     */
    protected <R> void mockAPIGetSomething(Supplier<R> supplier) throws IllegalAccessException
    {
        // Vrai appel webservice
        Mockito.when(api.appelWebserviceGET(Mockito.anyString(), Mockito.any(Parametre[].class))).thenCallRealMethod();
        Mockito.when(api.appelWebserviceGET(Mockito.anyString())).thenCallRealMethod();

        // Vrai appel getComposant
        Mockito.when(supplier.get()).thenCallRealMethod();
    }

    /**
     * Permet d'utiliser une vrai m�thode get de l'api SonarAPI avec le mock. ex: getComposants, getVues(). La m�thode doit avoir un objet en retour
     * 
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    protected <R, S> S mockAPIGetSomethingWithParam(Function<R, S> fonction, R r) throws IllegalAccessException
    {
        // Vrai appel webservice
        Mockito.when(api.appelWebserviceGET(Mockito.anyString(), Mockito.any(Parametre[].class))).thenCallRealMethod();

        // Vrai appel getComposant
        return (S) Mockito.when(fonction.apply(r)).thenCallRealMethod();
    }

    /*---------- ACCESSEURS ----------*/

}
