package model.enums;

/**
 * Enumeration possible dnas RTC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum EnumRTC 
{
    ENTITERESPCORRECTION ("fr.ca.cat.attribut.entiteresponsablecorrection"),
    ENVIRONNEMENT("fr.ca.cat.attribut.environnement"),
    NATURE("NatureProbleme"),
    EDITION("fr.ca.cat.attribut.edition"),
    EDITIONSICIBLE("fr.ca.cat.attribut.editionsicible"),
    COMPTERENDU("fr.ca.cat.attribut.comptesrendus"),
    EDITIONSI("editionSI"),
    IMPORTANCE("NiveauImportance"),
    CRITICITE("fr.ca.cat.attribut.criticite"),
    ORIGINE("Origine"),
    ORIGINE2("fr.ca.cat.attribut.origine"),
    CLARITY("fr.ca.cat.attribut.codeprojetclarity"),
    CODECLARITY("codeprojet"),
    DATELIVHOMO("fr.ca.cat.attribut.datedelivraison"),
    TROUVEDANS("fr.ca.cat.attribut.trouvedans");
    
    private String valeur;
    
    EnumRTC(String valeur)
    {
        this.valeur = valeur;
    }
    
    public String getValeur()
    {
        return valeur;
    }
}
