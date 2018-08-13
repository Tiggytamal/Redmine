package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.task.CreerListeComposantsTask;
import de.saxsys.javafx.test.JfxRunner;
import model.ComposantSonar;
import model.sonarapi.Composant;

@RunWith(JfxRunner.class)
public class TestCreerListeComposantsTask extends AbstractTestTask<CreerListeComposantsTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init() throws IllegalAccessException
    {
        handler = new CreerListeComposantsTask();
        initAPI(CreerListeComposantsTask.class, true);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testCreerListeComposants() throws Exception
    {        
        // Pr�paration des mocks
        mockAPIGetSomething(() -> api.getComposants());
        Mockito.when(api.getMetriquesComposant(Mockito.anyString(), Mockito.any(String[].class))).thenReturn(new Composant());
        
        // Appel de la m�thode
        Map<String, ComposantSonar> retour = Whitebox.invokeMethod(handler, "creerListeComposants");
        
        // Contr�le
        assertNotNull(retour);
        assertFalse(retour.isEmpty());
        assertEquals(api.getComposants().size(), retour.size());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
