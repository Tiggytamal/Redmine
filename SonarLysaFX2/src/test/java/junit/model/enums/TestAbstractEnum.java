package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import junit.JunitBase;

public abstract class TestAbstractEnum<T extends Enum<T>> extends JunitBase<T>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws Exception
    {
        // Pas de préparation pour les énumérations
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Permet de tester le constructeur d'une énumération
     * 
     * @param testInfo
     *                 testInfo de la méthode à tester.
     */
    public abstract void testConstructeur(TestInfo testInfo);

    /**
     * Permet de tester la taille de l'énumération
     * 
     * @param testInfo
     *                 testInfo de la méthode à tester.
     */
    public abstract void testLength(TestInfo testInfo);

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    /**
     * Permet de tester la protection contre l'instanciation des classes internes de valeurs.
     * 
     * @param clazz
     *              Classe à tester.
     * @param testInfo
     *                 testInfo de la méthode à tester.           
     * @throws ClassNotFoundException
     *                                Exception lancée par l'introspection.
     * @throws InstantiationException
     *                                Exception lancée par l'introspection.
     * @throws IllegalAccessException
     *                                Exception lancée par l'introspection.
     */
    protected void testValeurInstanciation(Class<T> clazz, TestInfo testInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        Class<Object> inner = Whitebox.getInnerClassType(clazz, "Valeur");
        Constructor<Object> constructor = Whitebox.getConstructor(inner);
        constructor.setAccessible(true);

        // Test protection instanciation classe interne
        InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> constructor.newInstance());
        assertThat(e.getCause().getClass()).isEqualTo(AssertionError.class);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
