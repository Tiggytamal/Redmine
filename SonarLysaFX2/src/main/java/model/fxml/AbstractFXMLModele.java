package model.fxml;

import javafx.scene.control.CheckBox;
import model.AbstractModele;

/**
 * Interface pour marquer les classe de modele pourl'affichage des données de la base.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public abstract class AbstractFXMLModele<I> extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    protected static final String SELECTION = "Selection";

    /** Checkbox pour sélectionner les objets dans le tableau */
    protected final CheckBox selected;

    /** Enumération pour paramétrer l'affichage */
    protected ListeGetters[] listeGetters;

    /*---------- CONSTRUCTEURS ----------*/

    public AbstractFXMLModele()
    {
        selected = new CheckBox();
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Retourne les valeurs de l'énumération pour l'affichage des colonnes et la récupération des données depuis le modèle.
     * 
     * @return
     *         L'énumération
     */
    public abstract ListeGetters[] getListeGetters();

    /**
     * Remonte l'index de l'objet correspondant en base de données. Le traitement varie selon chaque classe de modèle.
     * 
     * @return
     *         L'index de l'objet.
     */
    public abstract I getIndex();

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    /**
     * Retourne la checkbox de selection de l'objet dans le tableau JavaFX
     * 
     * @return
     *         vrai si l'objet a été selectioné.
     */
    public CheckBox getSelected()
    {
        return selected;
    }
}
