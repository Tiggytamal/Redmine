package fxml.dialog;

import java.io.IOException;

import application.MainScreen;
import fxml.ImplShortCut;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;
import utilities.TechnicalException;

/**
 * Classe mère abstraite de toutes les fenêtres de dialogue, permettant de créer objet à enregistrer en base de données.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 * @param <T>
 *        Classe du modèle à traiter.
 * @param <N>
 *        Classe du Node parent fxml pour la fenêtre.
 */
public abstract class AbstractBaseDialog<T, N extends Region> extends Dialog<T> implements ImplShortCut
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    protected N node;
    @FXML
    protected DialogPane pane;
    @FXML
    private ButtonType close;

    private Button closeButton;
    private String titre;

    /*---------- CONSTRUCTEURS ----------*/

    public AbstractBaseDialog(String titre, String fxml)
    {
        this.titre = titre;
        try
        {
            FXMLLoader loader = new FXMLLoader(AbstractBaseDialog.class.getResource("BaseDialog.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }
        catch (IOException e)
        {
            throw new TechnicalException(getClass().getName() + " - Impossible d'instancier la classe.", e);
        }

        chargerFXML(fxml);
        initOwner(MainScreen.ROOT.getScene().getWindow());
        implShortCut();
    }

    /**
     * Initialisation de la fenêtre: bouton de fermeture invisible et titre.
     */
    @FXML
    private void initialize()
    {
        // Ajout d'un bouton close caché pour permettre la fermeture du dialog avec la croix
        closeButton = (Button) pane.lookupButton(close);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
        closeButton.setId("closeButton");
        setTitle(titre);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void implShortCut()
    {
        // Pas d'implémentation de base. Chaque classe surchargera au besoin.
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Implémentation JAVA particulière pour chaque fenêtre.
     */
    protected abstract void initImpl();

    /*---------- METHODES PROTECTED ----------*/

    /**
     * Chargement du fichier fxml de l'implémentation particulière de chaque classe fille.
     * 
     * @param fxml
     *             Adresse du fichier fxml cible.
     */
    private void chargerFXML(String fxml)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            loader.setController(this);
            loader.load();
        }
        catch (IOException e)
        {
            throw new TechnicalException(getClass().getName() + " - Impossible d'instancier la classe.", e);
        }
    }

    /**
     * Affichage du bouton de fermeture de la fenêtre.
     */
    protected void affCancel()
    {
        closeButton.setVisible(true);
    }

    /**
     * Appel de l'initalisation particulière de chaque classe avec ajout au Node parent.
     */
    protected final void init()
    {
        initImpl();
        pane.setContent(node);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
