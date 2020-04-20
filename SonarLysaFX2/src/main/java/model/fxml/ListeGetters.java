package model.fxml;

/**
 * Tag pour rgrouper les enumerations des modeles FXML
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public interface ListeGetters
{
    /**
     * Nom du paramètre à utiliser pour valoriser les données de cette colonne
     * 
     * @return
     *         Le nom du paramètre.
     */
    String getNomParam();

    /**
     * Nom de la methode à utiliser pour récupérer la donnée depuis la propriété
     * 
     * @return
     *         Le nom de la méthode.
     */
    String getNomMethode();

    /**
     * Style à utiliser depuis le css pour l'en-tete de la colonne
     * 
     * @return
     *         Le style à utiliser.
     */
    String getStyle();

    /**
     * Groupe général de la colonne
     * 
     * @return
     *         Le groupe
     */
    String getGroupe();

    /**
     * Titre de la colonne à afficher
     * 
     * @return
     *         Le titre de la colonne.
     */
    String getAffichage();

    /**
     * Gestion de l'affichage par defaut de la colonne
     * 
     * @return
     *         L'affichage par défaut.
     */
    boolean isAffParDefaut();
}
