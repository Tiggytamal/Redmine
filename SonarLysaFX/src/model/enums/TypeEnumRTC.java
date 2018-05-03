package model.enums;

public enum TypeEnumRTC 
{
    ENTITERESPCORRECTION ("fr.ca.cat.attribut.entiteresponsablecorrection"),
    ENVIRONNEMENT("fr.ca.cat.attribut.environnement"),
    NATURE("NatureProbleme"),
    EDITION("fr.ca.cat.attribut.edition"),
    EDITIONSICIBLE("fr.ca.cat.attribut.editionsicible"),
    IMPORTANCE("NiveauImportance"),
    ORIGINE("Origine"),
    CLARITY("fr.ca.cat.attribut.codeprojetclarity");
    
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