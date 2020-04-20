package junit.control.task;

import static com.google.common.truth.Truth.assertThat;

import java.time.LocalDate;
import java.util.Base64;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.testfx.framework.junit5.ApplicationExtension;

import control.rest.SonarAPI;
import control.task.AbstractTask;
import dao.DaoComposantBase;
import junit.AutoDisplayName;
import junit.JunitBase;
import model.OptionInitAPI;
import model.enums.Param;
import model.rest.sonarapi.ParamAPI;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public abstract class TestAbstractTask<T extends AbstractTask> extends JunitBase<T>
{
    /*---------- ATTRIBUTS ----------*/

    protected SonarAPI api;
    protected DaoComposantBase daoCompoMock;

    /*---------- CONSTRUCTEURS ----------*/

    @BeforeEach
    @Override
    public final void init() throws Exception
    {
        initImpl();

        // Mock du dao des composants
        daoCompoMock = Mockito.mock(DaoComposantBase.class);
        initDataBase();
        Mockito.when(daoCompoMock.readAll()).thenReturn(databaseXML.getCompos());
        Mockito.when(daoCompoMock.recupKeyComposTEP(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(databaseXML.getKeyDateMEPs());
        setField("daoCompo", daoCompoMock);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    protected void testAnnuler(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Pas d'action sur la fonction annuler
        objetTest.annuler();
        assertThat(objetTest.isCancelled()).isFalse();
    }

    @Test
    public void testUpdateMessage(TestInfo testInfo) throws TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test message null
        objetTest.setBaseMessage(null);
        objetTest.updateMessage(null);
        assertThat(objetTest.getBaseMessage()).isEmpty();

        objetTest.setBaseMessage(TESTSTRING);
        objetTest.updateMessage(TESTSTRING);
        assertThat(objetTest.getBaseMessage()).isEqualTo(TESTSTRING);
    }

    /*---------- METHODES PROTECTED ----------*/

    /**
     * Permet d'initialiser d'autres objets
     * 
     * @throws Exception
     *                   Prévoit les diverses exceptions lancées par les initialisations.
     */
    protected abstract void initImpl() throws Exception;

    /**
     * Initialise l'api Sonar, et crée le mock au besoin.
     * 
     * @param clazz
     *               Classe à tester
     * @param option
     *               MOCK si besoin de créer le mock de l'api.
     * @throws IllegalAccessException
     *                                Remontée par les accès Whitebox.
     */
    protected void initAPI(Class<T> clazz, OptionInitAPI option) throws IllegalAccessException
    {
        if (option == OptionInitAPI.MOCK)
        {
            api = Mockito.mock(SonarAPI.class);

            // Init du codeUser
            StringBuilder builder = new StringBuilder(Statics.info.getPseudo());
            builder.append(":");
            builder.append(Statics.info.getMotDePasse());
            Whitebox.getField(SonarAPI.class, "codeUser").set(api, "Basic " + Base64.getEncoder().encodeToString(builder.toString().getBytes()));

            // init du webtarget
            WebTarget webTarget = ClientBuilder.newClient().target(Statics.proprietesXML.getMapParams().get(Param.URLSONAR));
            Whitebox.getField(SonarAPI.class, "webTarget").set(api, webTarget);

            // Ajout du mock à l'instance de la classe testee
            Whitebox.getField(clazz, "api").set(objetTest, api);
        }
        else
        {
            api = SonarAPI.build();
            Whitebox.getField(clazz, "api").set(objetTest, api);
        }
    }

    /**
     * Permet d'utiliser une vrai methode get de l'api SonarAPI avec le mock. La methode doit avoir un objet en retour<br>
     * ex : {@code mockAPIGetSomething(() -> api.getComposants(Mockito.eq(OptionGetCompos.MINIMALE), Mockito.any()));}
     * 
     * @param supplier
     *                 Méthode à remplacer.
     * @throws IllegalAccessException
     *                                Remontée par les accès Whitebox.
     */
    protected void mockAPIGetSomething(Supplier<?> supplier) throws IllegalAccessException
    {
        // Vrai appel webservice
        Mockito.when(api.appelWebserviceGET(Mockito.anyString(), Mockito.any(ParamAPI[].class))).thenCallRealMethod();
        Mockito.when(api.appelWebserviceGET(Mockito.anyString())).thenCallRealMethod();

        // Vrai appel getComposant
        Mockito.when(supplier.get()).thenCallRealMethod();
    }

    /*---------- ACCESSEURS ----------*/

}
