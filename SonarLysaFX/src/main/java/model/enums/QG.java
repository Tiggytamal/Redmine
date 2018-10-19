package model.enums;

/**
 * Enum�ration repr�sentant les status possible d'une QualityGate dans Sonar.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public enum QG 
{
    /*---------- ATTRIBUTS ----------*/

    OK(Valeur.OK), 
    WARN(Valeur.WARN), 
    ERROR(Valeur.ERROR), 
    NONE(Valeur.NONE);

    private String valeur;

    /*---------- CONSTRUCTEURS ----------*/

    private QG(String valeur)
    {
        this.valeur = valeur;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public String getValeur()
    {
        return valeur;
    }

    public static QG from(String status)
    {
        if (status == null || status.isEmpty())
            throw new IllegalArgumentException("model.enums.sonarapi.Status - argument vide ou nul.");

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

            default:
                throw new IllegalArgumentException("model.enums.sonarapi.Status inconnu : " + status, null);
        }
    }

    /*---------- CLASSE INTERNE ----------*/

    private static final class Valeur
    {
        private static final String OK = "OK";
        private static final String NONE = "NONE";
        private static final String ERROR = "ERROR";
        private static final String WARN = "WARN";

        private Valeur()
        {
            throw new AssertionError("Classe non instanciable : model.sonarapi.Status#Valeur");
        }
    }
}
