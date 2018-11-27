package model.enums;

/**
 * Enumération possible dnas RTC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum TypeEnumRTC 
{
    ENTITERESPCORRECTION ("fr.ca.cat.attribut.entiteresponsablecorrection"),
    ENVIRONNEMENT("fr.ca.cat.attribut.environnement"),
    NATURE("NatureProbleme"),
    EDITION("fr.ca.cat.attribut.edition"),
    EDITIONSICIBLE("fr.ca.cat.attribut.editionsicible"),
    EDITIONSI("editionSI"),
    IMPORTANCE("NiveauImportance"),
    ORIGINE("Origine"),
    CLARITY("fr.ca.cat.attribut.codeprojetclarity"),
    CODECLARITY("codeprojet"),
    DATELIVHOMO("fr.ca.cat.attribut.datedelivraison"),
    TROUVEDANS("fr.ca.cat.attribut.trouvedans");
    
    private String valeur;
    
    private TypeEnumRTC(String valeur)
    {
        this.valeur = valeur;
    }
    
    public String getValeur()
    {
        return valeur;
    }
}
