package model.enums;

import java.io.Serializable;

public enum TypeCol implements Serializable, TypeKey
{
    DIRECTION("Direction"), DEPARTEMENT("Département"), SERVICE("Service"), RESPSERVICE("Responsable Service"), CLARITY("Code Clarity"), LIBELLE("Libellé projet"), CPI(
            "Chef de projet du lot"), EDITION("Edition"), LOT("Numéro du lot"), ENV("Etat du lot"), ANOMALIE("Numéro anomalie"), ETAT("Etat de l'anomalie"), SECURITE(
                    "Sécurité"), REMARQUE("Remarque"), VERSION("Version"), DATECREATION(
                            "Date de création"), DATERELANCE("Date de relance"), TRAITE("Traité"), FILIERE("Filière"), MANAGER("Manager"), MATIERE("Matière");

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
