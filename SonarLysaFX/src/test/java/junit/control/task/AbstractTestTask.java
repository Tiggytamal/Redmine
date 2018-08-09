package junit.control.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.ParameterizedType;

import org.junit.Test;

import control.task.AbstractSonarTask;
import junit.JunitBase;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

public abstract class AbstractTestTask<T extends AbstractSonarTask> extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    protected T handler;

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
    /*---------- ACCESSEURS ----------*/

}
