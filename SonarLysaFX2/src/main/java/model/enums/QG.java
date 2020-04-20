package model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration représentant les status possible d'une QualityGate dans Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@JsonFormat (shape = Shape.STRING)
public enum QG 
{
    /*---------- ATTRIBUTS ----------*/

    OK(Valeur.OK), 
    WARN(Valeur.WARN), 
    ERROR(Valeur.ERROR), 
    NONE(Valeur.NONE),
    INCONNUE(Valeur.INCONNUE);

    private String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    QG(String valeur)
    {
        this.valeur = valeur;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @JsonValue
    public String getValeur()
    {
        return valeur;
    }

    @JsonCreator
    public static QG from(String status)
    {
        if (status == null || status.isEmpty())
            throw new IllegalArgumentException("model.enums.QG.from - valeur envoyée nulle ou vide.");

        switch (status)
        {
            case Valeur.OK:
                return OK;

            case Valeur.WARN:
                return WARN;

            case Valeur.ERROR:
                return ERROR;

            case Valeur.NONE:
                return NONE;
                
            case Valeur.INCONNUE:
                return INCONNUE;

            default:
                throw new IllegalArgumentException("model.enums.QG.from - valeur non gérée : " + status, null);
        }
    }

    /*---------- CLASSE INTERNE ----------*/

    private static final class Valeur
    {
        private static final String OK = "OK";
        private static final String NONE = "NONE";
        private static final String ERROR = "ERROR";
        private static final String WARN = "WARN";
        private static final String INCONNUE = "INCONNUE";

        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.sonarapi.Status#Valeur");
        }
    }
}
