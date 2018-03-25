package model.enums;

import java.io.Serializable;

public enum TypeCol implements Serializable, TypeKey
{
    DIRECTION("Direction"), DEPARTEMENT("D�partement"), SERVICE("Service"), RESPSERVICE("Responsable Service"), CLARITY("Code Clarity"), LIBELLE("Libell� projet"), CPI(
            "Chef de projet du lot"), EDITION("Edition"), LOT("Num�ro du lot"), ENV("Etat du lot"), ANOMALIE("Num�ro anomalie"), ETAT("Etat de l'anomalie"), SECURITE(
                    "S�curit�"), REMARQUE("Remarque"), VERSION("Version"), DATECREATION(
                            "Date de cr�ation"), DATERELANCE("Date de relance"), TRAITE("Trait�"), FILIERE("Fili�re"), MANAGER("Manager"), MATIERE("Mati�re");

    private String string;

    private TypeCol(String string)
    {
        this.string = string;
    }

    @Override
    public String toString()
    {
        return string;
    }
}
