package model.enums;

import java.io.Serializable;

/**
 * Types des fichiers Excel utilisés
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public enum TypeFichier implements Serializable , TypeKey
{
    APPS, 
    CLARITY, 
    RESPSERVICE, 
    EDITION,
    LOTSRTC;
}
