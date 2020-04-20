package junit;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.DisplayNameGenerator;

import utilities.Statics;

/**
 * Classe automatique de génération des DisplayName dans les classes de test
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class AutoDisplayName implements DisplayNameGenerator
{
    /*---------- ATTRIBUTS ----------*/

    private static final Pattern PATTERNCLASS = Pattern.compile("Test");
    private static final Pattern PATTERNMETHOD = Pattern.compile("test");
    private static final Pattern PATTERNUNDERSCORE = Pattern.compile("_");

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    @Override
    public String generateDisplayNameForClass(Class<?> testClass)
    {
        return PATTERNCLASS.matcher(testClass.getSimpleName()).replaceFirst(Statics.EMPTY);
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass)
    {
        return nestedClass.getSimpleName().replaceFirst("Test", Statics.EMPTY);
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod)
    {
        // Supprime le test devant le nom de la méthode et remplace la première capitale par une minuscule
        String retour = PATTERNMETHOD.matcher(testMethod.getName()).replaceFirst(Statics.EMPTY);
        retour = PATTERNUNDERSCORE.matcher(testMethod.getName()).replaceFirst(Statics.TIRET2);
        return StringUtils.uncapitalize(retour).replace('_', ' ');
    }

}
