package model;

import java.io.File;

/**
 * Interface de base pour les fichiers Excel
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public interface XML
{
    /**
     * Retourne le fichier de sauvegarde de l'objet
     * @return
     */
    public File getFile();
    
    /**
     * Controle les données du fichier et renvoie une chaine de caractère pour afficher les infos de controle.
     * @return
     */
    public String controleDonnees();
   
}