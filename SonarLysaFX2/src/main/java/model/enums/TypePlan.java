package model.enums;

import control.job.AbstractJobForTask;
import control.job.JobVuesCDM;
import control.job.JobVuesCHC;
import control.job.JobStatistiques;

/**
 * Enumeration des types de planificateurs. Chaque Enumeration contient le nom et la classe du Job traitee par le planificateur
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum TypePlan implements TypeKey
{
    /*---------- ATTRIBUTS ----------*/

    VUECHC("Vues CHC", JobVuesCHC.class),
    VUECDM("Vues CHC_CDM", JobVuesCDM.class),
    STATS("Statistiques", JobStatistiques.class);

    private String valeur;
    private Class<? extends AbstractJobForTask> classJob;

    /*---------- CONSTRUCTEURS ----------*/

    TypePlan(String valeur, Class<? extends AbstractJobForTask> classJob)
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
