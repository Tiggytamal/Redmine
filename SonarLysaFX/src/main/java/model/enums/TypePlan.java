package model.enums;

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
public enum TypePlan implements TypeKey 
{
    /*---------- ATTRIBUTS ----------*/

    SUIVIHEBDO("Suivi Hebdo", JobAnomaliesSonar.class), 
    VUECHC("Vues CHC", JobVuesCHC.class), 
    VUECDM("Vues CHC_CDM", JobVuesCDM.class);

    private String valeur;
    private Class<? extends AbstractJobForTask> classJob;

    /*---------- CONSTRUCTEURS ----------*/
    
    private TypePlan(String valeur, Class<? extends AbstractJobForTask> classJob)
    {
        this.valeur = valeur;
        this.classJob = classJob;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    public String getValeur()
    {
        return valeur;
    }

    public Class<? extends AbstractJobForTask> getClassJob()
    {
        return classJob;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
