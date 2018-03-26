package model.enums;

import java.io.Serializable;

import org.quartz.Job;

import control.quartz.JobAnomaliesSonar;
import control.quartz.JobVuesCDM;
import control.quartz.JobVuesCHC;

public enum TypePlan implements Serializable, TypeKey
{
    SUIVIHEBDO("Suivi Hebdo", JobAnomaliesSonar.class), VUECHC("Vues CHC", JobVuesCHC.class), VUECDM("Vues CHC_CDM", JobVuesCDM.class);

    private String string;
    private Class<? extends Job> clazz;

    private TypePlan(String string, Class<? extends Job> clazz)
    {
        this.string = string;
        this.clazz = clazz;
    }

    @Override
    public String toString()
    {
        return string;
    }
    
    public Class<? extends Job> getClazz()
    {
        return clazz;
    }
}
