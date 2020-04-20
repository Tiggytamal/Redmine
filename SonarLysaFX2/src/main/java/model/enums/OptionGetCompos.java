package model.enums;

/**
 * Enumération permettatn de gérer le type de mise à jour des composant SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public enum OptionGetCompos 
{
    /** MAJ de tous les composant plus les branches presents dans SonarQube */
    COMPLETE,
    /** MAJ des composants analyses depuis la dernière mise à jour sans prise en compte des branches*/
    MINIMALE,
    /** MAJ des composants et des branches analyses depuis la dernière mise à jour sans prise en compte des branches*/
    PARTIELLE;
}
