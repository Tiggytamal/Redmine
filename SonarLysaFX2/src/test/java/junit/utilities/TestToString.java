package junit.utilities;

import org.junit.jupiter.api.DisplayNameGeneration;

import junit.AutoDisplayName;
import utilities.AbstractToStringImpl;

/**
 * Classe de test pour le toString
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
@DisplayNameGeneration(AutoDisplayName.class)
public class TestToString extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    private String a;
    private String b;

    /*---------- CONSTRUCTEURS ----------*/

    public TestToString(String a, String b)
    {
        this.a = a;
        this.b = b;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getA()
    {
        return a;
    }

    public String getB()
    {
        return b;
    }
}
