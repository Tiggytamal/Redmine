package fxml;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Interface permettant de gérer l'affichage d'une sous-vue fxml.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public interface SubView
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    /**
     * Permet de gérer l'affichage entre les différentes options de la page.
     * Utilise l'id des Node utilisées pour déterminer la page à afficher.
     * 
     * @param event
     *              event récupéré au moment du clic d'un bouton.
     * @throws IOException
     *                     Exception lancée sur une erreur lors de la récupération des fichiers fxml.
     */
    @FXML
    void afficher(ActionEvent event) throws IOException;

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
