package model.enums;

import java.io.Serializable;

public enum TypePlan implements Serializable, TypeKey
{
    SUIVIHEBDO("Suivi Hebdo"), VUECHC("Vues CHC"), VUECDM("Vues CHC_CDM");

    private String string;

    private TypePlan(String string)
    {
        this.string = string;
    }

    @Override
    public String toString()
    {
        return string;
    }
}
