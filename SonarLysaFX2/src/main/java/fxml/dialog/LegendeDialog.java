package fxml.dialog;

import application.Main;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Fenêtre d'affichage de la légende de chaque tableau.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class LegendeDialog extends AbstractBaseDialog<Boolean, VBox>
{
    /*---------- ATTRIBUTS ----------*/

    private String fxml;

    /*---------- CONSTRUCTEURS ----------*/

    public LegendeDialog(String fxml)
    {
        super("Légende", "/fxml/dialog/LegendeDialog.fxml");

        this.fxml = fxml;
        init();
        initModality(Modality.NONE);
    }

    protected void initImpl()
    {
        switch (fxml)
        {
            case "/fxml/bdd/DefaultQualiteBDD.fxml":
                initDq();
                break;

            case "/fxml/bdd/ComposantBDD.fxml":
                initCompo();
                break;

            case "/fxml/bdd/ComposantPlanteBDD.fxml":
                initCompoPlante();
                break;

            case "/fxml/bdd/AnomalieRTCBDD.fxml":
                initAno();
                break;

            case "/fxml/bdd/UtilisateurBDD.fxml":
                initUser();
                break;

            default:
                throw new TechnicalException("Méthode fxml.dialog.LegendeDialog.init - fxml non traité : " + fxml);
        }
        pane.getButtonTypes().add(ButtonType.OK);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/

    /**
     * Légendes pour le tableau des défauts qualité.
     */
    private void initDq()
    {
        initLegende("DqOrange.PNG", " : Nouveau Défaut Qualité");
        initLegende("DqBleu.PNG", " : Défaut Qualité sur le code application");
        initLegende("DqBlanc.PNG", " : Défaut Qualité en cours");
        initLegende("DqJaune.PNG", " : Défaut Qualité avec date de Mep dans moins de 3 semaines");
        initLegende("DqRed.PNG", " : Défaut Qualité avec date de Mep dépassée");
        initLegende("DqVert.PNG", " : Défaut Qualité corrigé");
        initLegende("DqGris.PNG", " : Défaut Qualité avec anomalie close");
    }

    /**
     * Légendes pour le tableau des composants.
     */
    private void initCompo()
    {
        initLegende("CBlanc.PNG", " : Composant OK");
        initLegende("CJaune.PNG", " : Composant version KO");
        initLegende("COrange.PNG", " : Composant en doublon");
    }

    /**
     * Légendes pour le tableau des composants en erreur.
     */
    private void initCompoPlante()
    {
        initLegende("CpVert.PNG", " : Composant OK");
        initLegende("CpBlanc.PNG", " : Composant purgé");
        initLegende("CpJaune.PNG", " : Composant KO");
    }

    /**
     * Légendes pour le tableau des anomalies RTC non Sonarlysa.
     */
    private void initAno()
    {
        node.getChildren().add(new Label("Pas d'affichage spécifique pour les lignes de cette table"));
    }

    /**
     * Légendes pour le tableau des utilisaters RTC.
     */
    private void initUser()
    {
        initLegende("UJaune.PNG", " : Utilisateur désactivé pour l'envoi de mails");
    }

    /**
     * Initialisation d'une légende et ajout à la Node parent.
     * 
     * @param imageUrl
     *                 Adresse de l'image à afficher.
     * @param texte
     *                 Texte descriptif à afficher.
     */
    private void initLegende(String imageUrl, String texte)
    {
        ImageView image = new ImageView(new Image(Main.class.getResourceAsStream(Statics.ROOT + imageUrl)));
        Label label = new Label(texte);
        Region region = new Region();
        region.setPrefHeight(5);
        node.getChildren().addAll(new HBox(image, label), region);
    }

    /*---------- ACCESSEURS ----------*/
}
