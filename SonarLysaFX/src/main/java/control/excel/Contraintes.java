package control.excel;

import model.enums.TypeAction;

/**
 * Interface g�rant l'initialisation des contr�leurs avec des contraintes. De base initialise les contraintes des actions sur un fichier Excel.
 * 
 * 
 * @author ETP8137 - Gr�goire
 * @since 1.0
 */
public interface Contraintes
{
    /**
     * Initialisation liste des contraintes depuis l'�num�ration
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
