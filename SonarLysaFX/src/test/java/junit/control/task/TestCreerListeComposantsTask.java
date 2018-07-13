package junit.control.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import control.task.CreerListeComposantsTask;
import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;

@RunWith(JfxRunner.class)
public class TestCreerListeComposantsTask extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private CreerListeComposantsTask handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        handler = new CreerListeComposantsTask();
        handler.annuler();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCreerListe() throws Exception
    {   
        // Utiliser seulement pour les tests manuels. trop de temspe n automatique.
//        Whitebox.invokeMethod(handler, "creerListeComposants");
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
