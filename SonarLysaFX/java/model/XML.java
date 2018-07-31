package model;

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
    public File getFile();

    /**
     * Retourne la ressource de sauvegarde de l'objet
     * 
     * @return la ressource
     */
    public File getResource();

    /**
     * Controle les données du fichier et renvoie une chaine de caractère pour afficher les infos de controle.
     * 
     * @return le contrôle des données
     */
    public String controleDonnees();
}
