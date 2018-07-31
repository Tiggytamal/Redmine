package model.sonarapi;

public enum Status 
{
    /*---------- ATTRIBUTS ----------*/

    OK(Valeur.OK), 
    WARN(Valeur.WARN), 
    ERROR(Valeur.ERROR), 
    NONE(Valeur.NONE);

    private String string;

    /*---------- CONSTRUCTEURS ----------*/

    private Status(String string)
    {
        this.string = string;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String toString()
    {
        return string;
    }

    public static Status from(String status)
    {
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
