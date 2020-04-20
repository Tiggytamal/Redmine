package junit;

import static com.google.common.truth.Truth.assertWithMessage;

import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.TestInfo;

/**
 * Interface représentant les methodes communes aux tests sur les fichiers XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public interface TestXML
{
    public void testGetFile(TestInfo testInfo);

    /**
     * Test la presence d'une chaîne dans le retour du contrôle des map avec la fonction donnée.
     * 
     * @param regex
     *                       Chaîne de caractères à tester.
     * @param nbre
     *                       Nombre d'occurences attendues pour la chaîne.
     * @param retourControle
     *                       String de retour de la méthode de contrôle.
     * @param testFunction
     *                       Assertion à effectuer.
     */
    default void regexControle(String regex, int nbre, String retourControle, BiConsumer<Integer, Integer> testFunction)
    {
        Matcher matcher = Pattern.compile(regex.replace("(", "\\(").replace(")", "\\)").replace(".", "\\.")).matcher(retourControle);
        int i = 0;
        while (matcher.find())
        {
            i++;
        }
        testFunction.accept(nbre, i);
    }

    /**
     * Test la presence d'une chaîne dans le retour du contrôle des map avec un assertThat
     * 
     * @param regex
     *                       Chaene de caractere à tester
     * @param nbre
     *                       Nombre d'occurences attendues pour la chaîne
     * @param retourControle
     *                       String de retour de la methode de contrôle
     */
    default void regexControleEquals(String regex, int nbre, String retourControle)
    {
        regexControle(regex, nbre, retourControle, (a, b) -> assertWithMessage(regex + "Attendu " + a + " fois, mais obtenu " + b + " fois.").that(b).isEqualTo(a));
    }

    /**
     * Test la presence d'une chaîne dans le retour du contrôle des map avec un assertThat
     * 
     * @param regex
     *                       Chaene de caractere à tester
     * @param nbre
     *                       Nombre d'occurences attendues pour la chaîne
     * @param retourControle
     *                       String de retour de la methode de contrôle
     */
    default void regexControleAtLeast(String regex, int nbre, String retourControle)
    {
        regexControle(regex, nbre, retourControle, (a, b) -> assertWithMessage(regex + "- Attendu au moins " + a + " fois.").that(a).isAtLeast(a));
    }
}
