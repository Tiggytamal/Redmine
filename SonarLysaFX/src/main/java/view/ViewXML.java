package view;

import javafx.scene.Node;

public interface ViewXML<T, Y extends Node>
{
    public T getType();
    
    public Y getField();
}
