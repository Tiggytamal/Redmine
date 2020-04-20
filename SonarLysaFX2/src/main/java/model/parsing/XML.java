package model.parsing;

import java.io.File;

/**
 * Interface de base pour les fichiers Excel
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public interface XML
{
    /**
     * Retourne le fichier de sauvegarde de l'objet
     * 
     * @return la fichier XML
     */
    File getFile();

    /**
     * Controle les données du fichier et renvoie une chaine de caractere pour afficher les infos de contrôle.
     * 
     * @return le contrôle des données
     */
    String controleDonnees();
}
