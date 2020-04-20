package model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Enumeration représentant les types de branches possibles
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
@JsonFormat(shape = Shape.STRING)
public enum TypeBranche
{
    /*---------- ATTRIBUTS ----------*/

    LONG(Valeur.LONG),
    SHORT(Valeur.SHORT);

    private String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    TypeBranche(String valeur)
    {
        this.valeur = valeur;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static TypeBranche from(String valeur)
    {
        if (valeur == null || valeur.isEmpty())
            throw new IllegalArgumentException("model.enums.TypeBranche.from - valeur envoyée nulle ou vide.", null);

        switch (valeur)
        {
            case Valeur.SHORT:
                return SHORT;

            case Valeur.LONG:
                return LONG;

            default:
                throw new IllegalArgumentException("model.enums.TypeBranche.from - valeur non gérée : " + valeur, null);
        }
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getValeur()
    {
        return valeur;
    }

    /*---------- CLASSE INTERNE ----------*/

    private static final class Valeur
    {
        private static final String SHORT = "SHORT";
        private static final String LONG = "LONG";

        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.enums.TypeBranche#Valeur");
        }
    }
}
