package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interface repr�sentant les m�thodes communes aux tests sur les fichiers XML
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public interface TestXML
{
    public void testGetResource();
    
    public void testGetFile();
    
    /**
     * Test la pr�sence d'une cha�ne dans le retour du contr�le des map avec la fonction donn�e
     * 
     * @param regex
     *            Cha�ne de caract�re � tester
     * @param nbre
     *            Nombre d'occurences attendues pour la cha�ne
     * @param retourControle
     *            String de retour de la m�thode de contr�le
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
     * Test la pr�sence d'une cha�ne dans le retour du contr�le des map avec un assertEquals
     * 
     * @param regex
     *            Cha�ne de caract�re � tester
     * @param nbre
     *            Nombre d'occurences attendues pour la cha�ne
     * @param retourControle
     *            String de retour de la m�thode de contr�le
     */
    default void regexControleEquals(String regex, int nbre, String retourControle)
    {
        regexControle(regex, nbre, retourControle, (a,b) -> assertEquals(regex + "- Attendu au moins " + a + " fois.", a,b));
    }
    
    /**
     * Test la pr�sence d'une cha�ne dans le retour du contr�le des map avec un assertEquals
     * 
     * @param regex
     *            Cha�ne de caract�re � tester
     * @param nbre
     *            Nombre d'occurences attendues pour la cha�ne
     * @param retourControle
     *            String de retour de la m�thode de contr�le
     */
    default void regexControleAtLeast(String regex, int nbre, String retourControle)
    {
        regexControle(regex, nbre, retourControle, (a,b) -> assertTrue(regex + "- Attendu au moins " + a + " fois.", a <= b));
    }
}
