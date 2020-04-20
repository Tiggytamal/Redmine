package junit.model.fxml;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import junit.AutoDisplayName;
import model.fxml.Styles;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestStyles
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructeur() throws InstantiationException, IllegalAccessException, InvocationTargetException
    {
        // Contrôle que la classe n'est pas instanciable
        @SuppressWarnings("unchecked")
        Constructor<Styles> constructor = (Constructor<Styles>) Styles.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> constructor.newInstance());
        assertThat(e.getCause()).isInstanceOf(AssertionError.class);
        assertThat(e.getCause().getMessage()).isEqualTo("class model.fxml.Styles non instanciable");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
