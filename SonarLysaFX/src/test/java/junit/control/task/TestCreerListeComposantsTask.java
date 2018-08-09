package junit.control.task;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import control.task.CreerListeComposantsTask;
import de.saxsys.javafx.test.JfxRunner;

@RunWith(JfxRunner.class)
public class TestCreerListeComposantsTask extends AbstractTestTask<CreerListeComposantsTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        handler = new CreerListeComposantsTask();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    @Ignore ("Utilisé seulement pour les tests manuels. Trop long en automatique (30 mins).")
    public void testCreerListe() throws Exception
    {   
       assertTrue(Whitebox.invokeMethod(handler, "creerListeComposants"));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
