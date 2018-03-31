package model.enums;

import java.io.Serializable;

import org.quartz.Job;

import control.quartz.JobAnomaliesSonar;
import control.quartz.JobVuesCDM;
import control.quartz.JobVuesCHC;

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
    private Class<? extends Job> classJob;

    /*---------- CONSTRUCTEURS ----------*/
    
    private TypePlan(String string, Class<? extends Job> classJob)
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

    public Class<? extends Job> getClassJob()
    {
        return classJob;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
