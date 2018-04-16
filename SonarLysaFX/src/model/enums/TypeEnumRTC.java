package model.enums;

public enum TypeEnumRTC 
{
    ENTITERESPCORRECTION ("fr.ca.cat.attribut.entiteresponsablecorrection"),
    ENVIRONNEMENT("fr.ca.cat.attribut.environnement"),
    NATURE("NatureProbleme"),
    EDITION("fr.ca.cat.attribut.edition"),
    IMPORTANCE("NiveauImportance"),
    ORIGINE("Origine");
    
    private String valeur;
    
    private TypeEnumRTC(String valeur)
    {
        this.valeur = valeur;
    }
    
    @Override
    public String toString()
    {
        return valeur;
    }
}