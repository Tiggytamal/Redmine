package model.enums;

/**
 * Options de la m�thode AbstractSonarTask.recupererPatrimoineSonar
 * 
 * @author ETP8137 - Gr�goire mathon
 * @since 1.0
 *
 */
public enum OptionRecupCompo 
{
    /** patrimoine */
    PATRIMOINE,
    /** Composants avec lots termin�s*/
    TERMINE,
    /** Composants non mis en production */
    NONPROD,
    /** Composants dont on a pas l'�tat de mise en produciton */
    INCONNU,
    /** Derni�re version de chaque composant */
    DERNIERE;

}
