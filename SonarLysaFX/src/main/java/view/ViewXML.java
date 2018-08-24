package view;

import javafx.scene.Node;

/**
 * INterface pour les affichage des param�tres XML
 * 
 * @author ETP8137 - Gr�goire Mathon
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
