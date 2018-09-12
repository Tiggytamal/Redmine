package model.enums;

/**
 * Options de la méthode AbstractSonarTask.recupererPatrimoineSonar
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 *
 */
public enum OptionRecupCompo 
{
    /** patrimoine */
    PATRIMOINE,
    /** Composants avec lots terminés*/
    TERMINE,
    /** Composants non mis en production */
    NONPROD,
    /** Composants dont on a pas l'état de mise en produciton */
    INCONNU,
    /** Dernière version de chaque composant */
    DERNIERE;

}
