package model.enums;

/**
 * Regroupe les différents méttriques utilisés dasn SonarQube
 * @author ETP8137 - Grégoire Mathon
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