package fxml.node;

import javafx.scene.Node;

/**
 * Interface pour les affichages des paramètres XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 * @param <T>
 *        Classe du paramètre à affciher.
 * 
 * @param <Y>
 *        Node permettant l'affichage du paramètre.
 */
public interface ViewXML<T, Y extends Node>
{
    T getType();

    Y getField();
}
