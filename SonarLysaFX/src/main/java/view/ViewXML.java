package view;

import javafx.scene.Node;

/**
 * INterface pour les affichage des paramètres XML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 * @param <T>
 * @param <Y>
 */
public interface ViewXML<T, Y extends Node>
{
    public T getType();
    
    public Y getField();
}
