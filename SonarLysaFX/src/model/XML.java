package model;

import java.io.File;

/**
 * Interface de base pour les fichiers Excel
 * @author ETP8137 - Gr�goire Mathon
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
     * Controle les donn�es du fichier et renvoie une chaine de caract�re pour afficher les infos de controle.
     * @return
     */
    public String controleDonnees();
   
}