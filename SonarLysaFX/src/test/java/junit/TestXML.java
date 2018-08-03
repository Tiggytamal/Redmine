package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interface représentant les méthodes communes aux tests sur les fichiers XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public interface TestXML
{
    public void testGetResource();
    
    public void testGetFile();
    
    /**
     * Test la présence d'une chaîne dans le retour du contrôle des map avec la fonction donnée
     * 
     * @param regex
     *            Chaîne de caractère à tester
     * @param nbre
     *            Nombre d'occurences attendues pour la chaîne
     * @param retourControle
     *            String de retour de la méthode de contrôle
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
     * Test la présence d'une chaîne dans le retour du contrôle des map avec un assertEquals
     * 
     * @param regex
     *            Chaîne de caractère à tester
     * @param nbre
     *            Nombre d'occurences attendues pour la chaîne
     * @param retourControle
     *            String de retour de la méthode de contrôle
     */
    default void regexControleEquals(String regex, int nbre, String retourControle)
    {
        regexControle(regex, nbre, retourControle, (a,b) -> assertEquals(regex + "- Attendu au moins " + a + " fois.", a,b));
    }
    
    /**
     * Test la présence d'une chaîne dans le retour du contrôle des map avec un assertEquals
     * 
     * @param regex
     *            Chaîne de caractère à tester
     * @param nbre
     *            Nombre d'occurences attendues pour la chaîne
     * @param retourControle
     *            String de retour de la méthode de contrôle
     */
    default void regexControleAtLeast(String regex, int nbre, String retourControle)
    {
        regexControle(regex, nbre, retourControle, (a,b) -> assertTrue(regex + "- Attendu au moins " + a + " fois.", a <= b));
    }
}
