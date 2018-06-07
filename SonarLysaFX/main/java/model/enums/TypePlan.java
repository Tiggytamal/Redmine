package model.enums;

import java.io.Serializable;

import control.quartz.JobAnomaliesSonar;
import control.quartz.JobVuesCDM;
import control.quartz.JobVuesCHC;
import control.task.JobForTask;

/**
 * Enumération des types de planificateurs. Chaque Enumération contient le nom et la classe du Job traitée par le planificateur
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum TypePlan implements Serializable, TypeKey 
{
    /*---------- ATTRIBUTS ----------*/

    SUIVIHEBDO("Suivi Hebdo", JobAnomaliesSonar.class), 
    VUECHC("Vues CHC", JobVuesCHC.class), 
    VUECDM("Vues CHC_CDM", JobVuesCDM.class);

    private String string;
    private Class<? extends JobForTask> classJob;

    /*---------- CONSTRUCTEURS ----------*/
    
    private TypePlan(String string, Class<? extends JobForTask> classJob)
    {
        this.string = string;
        this.classJob = classJob;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    @Override
    public String toString()
    {
        return string;
    }

    public Class<? extends JobForTask> getClassJob()
    {
        return classJob;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
