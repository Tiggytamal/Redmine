package model.enums;

import java.io.Serializable;

import control.quartz.JobAnomaliesSonar;
import control.quartz.JobVuesCDM;
import control.quartz.JobVuesCHC;
import control.task.AbstractJobForTask;

/**
 * Enum�ration des types de planificateurs. Chaque Enum�ration contient le nom et la classe du Job trait�e par le planificateur
 * 
 * @author ETP8137 - Gr�goire Mathon
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
    private Class<? extends AbstractJobForTask> classJob;

    /*---------- CONSTRUCTEURS ----------*/
    
    private TypePlan(String string, Class<? extends AbstractJobForTask> classJob)
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

    public Class<? extends AbstractJobForTask> getClassJob()
    {
        return classJob;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
