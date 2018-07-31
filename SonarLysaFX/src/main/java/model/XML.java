package model;

import java.io.File;

/**
 * Interface de base pour les fichiers Excel
 * 
 * @author ETP8137 - Gr�goire Mathon
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
     * Controle les donn�es du fichier et renvoie une chaine de caract�re pour afficher les infos de controle.
     * 
     * @return le contr�le des donn�es
     */
    public String controleDonnees();
}
