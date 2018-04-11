package model.enums;

/**
 * Regroupe les diff�rents m�ttriques utilis�s dasn SonarQube
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public enum TypeMetrique
{
    LOT("lot"),
    QG("alert_status"),
    DUPLICATION("duplicated_lines_density"),
    BLOQUANT("new_blocker_violations"),
    CRITIQUE("new_critical_violations"),
    APPLI("application"),
    EDITION("edition");
    
    private final String valeur;
    
    private TypeMetrique(String valeur)
    {
        this.valeur = valeur;
    }
    
    @Override
    public String toString()
    {
        return valeur;
    }
}