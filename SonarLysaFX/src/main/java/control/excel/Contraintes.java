package control.excel;

import model.enums.TypeAction;

/**
 * Interface gérant l'initialisation des contrôleurs avec des contraintes. De base initialise les contraintes des actions sur un fichier Excel.
 * 
 * 
 * @author ETP8137 - Grégoire
 * @since 1.0
 */
public interface Contraintes
{
    /**
     * Initialisation liste des contraintes depuis l'énumération
     */
    default String[] initContraintes()
    {
        String[] contraintes = new String[TypeAction.values().length];
        for (int i = 0; i < contraintes.length; i++)
        {
            contraintes[i] = TypeAction.values()[i].getValeur();
        }
        return contraintes;
    }
}
