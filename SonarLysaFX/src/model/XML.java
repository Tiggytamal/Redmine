package model;

import java.io.File;

public interface XML
{
    /**
     * Retourne le fichier de sauvegarde de l'objet
     * @return
     */
    public File getFile();
    
    /**
     * Conbtrole les donn�es du fichier et renvoie une chaine de caract�re pour afficher les infos de controle.
     * @return
     */
    public String controleDonnees();
}
