package junit.fxml;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.matcher.control.TextMatchers;

import application.MainScreen;
import control.rtc.ControlRTC;
import control.watcher.ControlWatch;
import dao.DaoComposantBase;
import fxml.Menu;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import junit.AutoDisplayName;
import junit.TestAbstractFXML;
import model.enums.EtatFichierPic;
import model.enums.Severity;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
@ExtendWith(ApplicationExtension.class)
public class TestMenu extends TestAbstractFXML<Menu>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        initFromFXML("/fxml/Menu.fxml");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testDeco(TestInfo testInfo, FxRobot robot) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        ControlRTC.build().connexionSimple();
        HBox box = (HBox) getField("box");

        FxToolkit.setupFixture(() -> {
            try
            {
                // Changement des boutons pour venir à un état connecté avec le raccourci clavier
                box.getChildren().remove((Button) getField("connexion"));
                box.getChildren().add((Button) getField("deConnexion"));
                stage.getScene().getAccelerators().put(ControlWatch.INSTANCE.getRaccourciFichierPic(), ControlWatch.INSTANCE.getSwitchBoucle()); 
                
                // Test deconnexion
                Whitebox.invokeMethod(objetTest, "deco");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        // Test blocage des boutons
        assertThat(((MenuItem) getField("mensuel")).isDisable()).isTrue();
        assertThat(((MenuItem) getField("issue")).isDisable()).isTrue();
        assertThat(((MenuItem) getField("fichierPic")).isDisable()).isTrue();
        assertThat(((MenuItem) getField("majCompos")).isDisable()).isTrue();
        assertThat(((MenuItem) getField("defautsQualite")).isDisable()).isTrue();
        assertThat(((MenuItem) getField("composants")).isDisable()).isTrue();
        assertThat(((MenuItem) getField("composPlantes")).isDisable()).isTrue();
        assertThat(((MenuItem) getField("calculatrice")).isDisable()).isTrue();
        assertThat(((MenuItem) getField("anos")).isDisable()).isTrue();
        assertThat(((MenuItem) getField("users")).isDisable()).isTrue();
        assertThat(((MenuItem) getField("rtc")).isDisable()).isTrue();
        assertThat(((MenuItem) getField("extraction")).isDisable()).isTrue();
        assertThat(((MenuItem) getField("autres")).isDisable()).isTrue();

        // Test remise à zéro des infos de connexion
        assertThat(Statics.info.getPseudo()).isEmpty();
        assertThat(Statics.info.getMotDePasse()).isEmpty();
        
        // Test suppression du raccourci clavier
        assertThat(stage.getScene().getAccelerators()).doesNotContainKey(ControlWatch.INSTANCE.getRaccourciFichierPic());
        
        // test remise bouton connexion
        assertThat(box.getChildren()).contains((Button) getField("connexion"));
    }

    @Test
    public void testAfficher_CreationPortfolioTrimestriel(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "mensuel", "Créations", "Création Portfolio Trimestriel");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/Créations/Création Portfolio Trimestriel");
    }

    @Test
    public void testAfficher_CreationAutrePortfolios(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "autres", "Créations", "Création Autres Portfolios");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/Créations/Création Autres Portfolios");
    }

    @Test
    public void testAfficher_Maintenance(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "maintenance", "Créations", "Création Portfolio Maintenance");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/Créations/Création Portfolio Maintenance");
    }

    @Test
    public void testAfficher_GestionFichierPic(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "fichierPic", "Outils", "Gestion fichier PIC");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/Outils/Gestion fichier PIC");
    }

    @Test
    public void testAfficher_MajLotsRTC(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "rtc", "Outils", "Mise à jour lots RTC");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/Outils/Mise à jour lots RTC");
    }

    @Test
    public void testAfficher_Extractions(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "extraction", "Créations", "Extractions");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/Créations/Extractions");
    }

    @Test
    public void testAfficher_DefautsQualite(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "defautsQualite", "BDD", "Défaults Qualité");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/BDD/Défaults Qualité");
    }

    @Test
    public void testAfficher_Composants(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "composants", "BDD", "Composants");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/BDD/Composants");
    }

    @Test
    public void testAfficher_ComposantsPlantes(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "composPlantes", "BDD", "Composants Plantés");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/BDD/Composants Plantés");
    }

    @Test
    public void testAfficher_AnomaliesRTC(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "anos", "BDD", "Anomalies RTC");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/BDD/Anomalies RTC");
    }

    @Test
    public void testAfficher_UtilisateursRTC(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "users", "BDD", "Utilisateurs RTC");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/BDD/Utilisateurs RTC");
    }
    
    @Test
    public void testAfficher_Graphique(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "graphique", "Statistiques", "Graphique");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/Statistiques/Graphique");
    }
    
    @Test
    public void testAfficher_Couverture(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelAfficher(robot, "pourcentage", "Statistiques", "Pourcentage");

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(GridPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/Statistiques/Pourcentage");
    }

    @Test
    public void testAfficher_Options(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test options - On met la connexion vide pour voir que l'on ne bloque pas les options
        Statics.info.setMotDePasse(Statics.EMPTY);
        robot.clickOn(TextMatchers.hasText("Outils"));
        robot.clickOn(TextMatchers.hasText("Options"));

        // Contrôles
        assertThat(MainScreen.ROOT.getCenter()).isInstanceOf(SplitPane.class);
        assertThat(stage.getTitle()).isEqualTo(Statics.NOMAPPLI + "/Outils/Options");
    }

    @Test
    public void testAfficher_Options_Exception_item_non_gere(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test Exception - MenuItem inconnu
        MenuItem item = new MenuItem("Test Erreur");
        item.setId("item inconnu");
        Statics.info.setMotDePasse(KEY);
        TechnicalException e = assertThrows(TechnicalException.class, () -> objetTest.afficher(new ActionEvent(item, null)));
        assertThat(e.getMessage()).isEqualTo("Méthode control.view.MenuViewControl.afficher - MenuItem pas géré : item inconnu");
    }

    @Test
    public void testAfficher_Options_Exception_pas_de_connexion(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test Exceptions - pas de connexion
        MenuItem item = new MenuItem("Test Erreur");
        Statics.info.setMotDePasse(Statics.EMPTY);
        item.setId("mensuel");
        FunctionalException f = assertThrows(FunctionalException.class, () -> objetTest.afficher(new ActionEvent(item, null)));
        assertThat(f.getMessage()).isEqualTo("Pas de connexion au serveur Sonar, Merci de vous connecter");
        assertThat(f.getSeverity()).isEqualTo(Severity.ERROR);

        // Test pas de connexion mais click sur option
        TechnicalException e = assertThrows(TechnicalException.class, () -> objetTest.afficher(new ActionEvent(new Object(), null)));
        assertThat(e.getMessage()).contains("Source non gérée : ");
    }

    @Test
    public void testAfficher_Options_Exception_source_non_geree(TestInfo testInfo, FxRobot robot) throws IOException, InterruptedException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test pas de connexion mais click sur option
        TechnicalException e = assertThrows(TechnicalException.class, () -> objetTest.afficher(new ActionEvent(new Object(), null)));
        assertThat(e.getMessage()).contains("Source non gérée : ");
    }

    @Test
    public void testAfficher_Alert_Maj_compos(TestInfo testInfo, FxRobot robot) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Activation des boutons pour éviter une connexion
        ((MenuItem) getField("majCompos")).setDisable(false);

        // Test majCompos
        robot.clickOn(TextMatchers.hasText("Outils"));
        robot.clickOn(TextMatchers.hasText("Mise à jour composants"));

        // Fermeture fenêtre - Sert de test pour voir si la fenêtre de confirmation est ouverte
        // On recherche un bouton annuler activé pour on essaie de cliquer dessus.
        robot.lookup(TextMatchers.hasText("Annuler")).queryAll().stream().filter(n -> !n.isDisabled()).findFirst().ifPresent(n -> robot.clickOn(n));
    }

    @Test
    public void testAfficher_Alert_Assigner_anomalies(TestInfo testInfo, FxRobot robot) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Activation des boutons pour éviter une connexion
        ((MenuItem) getField("issue")).setDisable(false);

        // Test issue
        robot.clickOn(TextMatchers.hasText("Outils"));
        robot.clickOn(TextMatchers.hasText("Assigner anos SonarQube"));

        // Fermeture fenêtre - Sert de test pour voir si la fenêtre de confirmation est ouverte
        // On recherche un bouton annuler activé pour on essaie de cliquer dessus.
        robot.lookup(TextMatchers.hasText("Annuler")).queryAll().stream().filter(n -> !n.isDisabled()).findFirst().ifPresent(n -> robot.clickOn(n));
    }

    @Test
    @Disabled(TESTMANUEL + " - Plante les appels POST à SonarQube")
    public void testAide(TestInfo testInfo, FxRobot robot) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Click pour l'affichage de l'aide
        robot.clickOn(TextMatchers.hasText("Outils"));
        robot.clickOn(TextMatchers.hasText("Aide"));

        Alert aide = (Alert) getField("aidePanel");

        // Contrôles
        assertThat(aide).isNotNull();
        assertThat(aide.isShowing()).isTrue();
        assertThat(aide.getTitle()).isEqualTo("Aide");
        assertThat(aide.getHeaderText()).isNull();
        assertThat(aide.getContentText()).isNull();
        assertThat(aide.getDialogPane().getContent()).isNotNull();
        assertThat(aide.getDialogPane().getContent()).isInstanceOf(WebView.class);
        assertThat(aide.getDialogPane().getButtonTypes()).containsExactly(ButtonType.CLOSE);

        // Fermeture
        robot.clickOn(TextMatchers.hasText("Fermer"));
    }

    @Test
    public void testUpdateNombreLignes_Noir(TestInfo testInfo, FxRobot robot) throws IllegalAccessException, TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelUpdateNombreLignes(17999999L);

        // Contrôle texte et couleur
        assertThat(((SimpleStringProperty) getField("ldcTextProperty")).get()).isNotNull();
        assertThat(((SimpleStringProperty) getField("ldcTextProperty")).get()).isEqualTo("17999999");
        assertThat(((SimpleStringProperty) getField("ldcStyleProperty")).get()).isNotNull();
        assertThat(((SimpleStringProperty) getField("ldcStyleProperty")).get()).isEqualTo("-fx-text-fill: rgb(255,255,255)");
    }

    @Test
    public void testUpdateNombreLignes_Vert(TestInfo testInfo, FxRobot robot) throws IllegalAccessException, TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelUpdateNombreLignes(18000001L);

        // Contrôle texte et couleur
        assertThat(((SimpleStringProperty) getField("ldcTextProperty")).get()).isNotNull();
        assertThat(((SimpleStringProperty) getField("ldcTextProperty")).get()).isEqualTo("18000001");
        assertThat(((SimpleStringProperty) getField("ldcStyleProperty")).get()).isNotNull();
        assertThat(((SimpleStringProperty) getField("ldcStyleProperty")).get()).isEqualTo("-fx-text-fill: rgb(0,255,0)");
    }

    @Test
    public void testUpdateNombreLignes_Jaune(TestInfo testInfo, FxRobot robot) throws IllegalAccessException, TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelUpdateNombreLignes(19000001L);

        // Contrôle texte et couleur
        assertThat(((SimpleStringProperty) getField("ldcTextProperty")).get()).isNotNull();
        assertThat(((SimpleStringProperty) getField("ldcTextProperty")).get()).isEqualTo("19000001");
        assertThat(((SimpleStringProperty) getField("ldcStyleProperty")).get()).isNotNull();
        assertThat(((SimpleStringProperty) getField("ldcStyleProperty")).get()).isEqualTo("-fx-text-fill: rgb(255,255,0)");
    }

    @Test
    public void testUpdateNombreLignes_Rouge(TestInfo testInfo, FxRobot robot) throws IllegalAccessException, TimeoutException
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareEtAppelUpdateNombreLignes(19500001L);

        // Contrôle texte et couleur
        assertThat(((SimpleStringProperty) getField("ldcTextProperty")).get()).isNotNull();
        assertThat(((SimpleStringProperty) getField("ldcTextProperty")).get()).isEqualTo("19500001");
        assertThat(((SimpleStringProperty) getField("ldcStyleProperty")).get()).isNotNull();
        assertThat(((SimpleStringProperty) getField("ldcStyleProperty")).get()).isEqualTo("-fx-text-fill: rgb(255,0,0)");
    }

    @Test
    public void testUpdateFichiersActif_Actif(TestInfo testInfo) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test traitement actif
        FxToolkit.setupFixture(() -> Menu.updateFichiersActif(EtatFichierPic.ACTIF));

        // Contrôle texte vert
        assertThat(((SimpleStringProperty) getField("nbreFichiersStyleProperty")).get()).isNotNull();
        assertThat(((SimpleStringProperty) getField("nbreFichiersStyleProperty")).get()).isEqualTo("-fx-text-fill: rgb(0,255,0)");
    }

    @Test
    public void testUpdateFichiersActif_Inactif(TestInfo testInfo) throws TimeoutException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test traitement inactif
        FxToolkit.setupFixture(() -> Menu.updateFichiersActif(EtatFichierPic.INACTIF));

        // Contrôle texte rouge
        assertThat(((SimpleStringProperty) getField("nbreFichiersStyleProperty")).get()).isNotNull();
        assertThat(((SimpleStringProperty) getField("nbreFichiersStyleProperty")).get()).isEqualTo("-fx-text-fill: rgb(255,0,0)");
    }

    @Test
    public void testMdp_Exception(TestInfo testInfo, FxRobot robot)
    {
        LOGGER.debug(testInfo.getDisplayName());

        FunctionalException e = assertThrows(FunctionalException.class, () -> Whitebox.invokeMethod(objetTest, "testMdP", "ETP8136", "28H02m89"));
        assertThat(e.getMessage()).isEqualTo("Utilisateur incorrect");
        assertThat(e.getSeverity()).isEqualTo(Severity.INFO);
    }

    @Test
    public void testMdp() throws Exception
    {
        ControlRTC.build().logout();
        FxToolkit.setupFixture(() -> Whitebox.invokeMethod(objetTest, "testMdP", "ETP8137", "28H02m894,;:!"));

        // Test déblocage des boutons
        assertThat(((MenuItem) getField("mensuel")).isDisable()).isFalse();
        assertThat(((MenuItem) getField("issue")).isDisable()).isFalse();
        assertThat(((MenuItem) getField("fichierPic")).isDisable()).isFalse();
        assertThat(((MenuItem) getField("majCompos")).isDisable()).isFalse();
        assertThat(((MenuItem) getField("defautsQualite")).isDisable()).isFalse();
        assertThat(((MenuItem) getField("composants")).isDisable()).isFalse();
        assertThat(((MenuItem) getField("composPlantes")).isDisable()).isFalse();
        assertThat(((MenuItem) getField("calculatrice")).isDisable()).isFalse();
        assertThat(((MenuItem) getField("anos")).isDisable()).isFalse();
        assertThat(((MenuItem) getField("users")).isDisable()).isFalse();
        assertThat(((MenuItem) getField("rtc")).isDisable()).isFalse();
        assertThat(((MenuItem) getField("extraction")).isDisable()).isFalse();
        assertThat(((MenuItem) getField("autres")).isDisable()).isFalse();

        // Test enregistrement infos de connexion
        assertThat(Statics.info.getPseudo()).isNotEmpty();
        assertThat(Statics.info.getMotDePasse()).isNotEmpty();
    }

    /*---------- METHODES PRIVEES ----------*/

    private void prepareEtAppelUpdateNombreLignes(long nbreLignes) throws TimeoutException
    {
        // Création du mock de l'appel vers la base de données
        DaoComposantBase mock = Mockito.mock(DaoComposantBase.class);
        Whitebox.setInternalState(Menu.class, mock);
        Mockito.when(mock.recupNombreLigne()).thenReturn(nbreLignes);

        // Appel de la méthode
        FxToolkit.setupFixture(() -> Menu.updateNbreLignes());
    }

    private void prepareEtAppelAfficher(FxRobot robot, String bouton, String menu, String sousMenu) throws IllegalAccessException
    {
        ((MenuItem) getField(bouton)).setDisable(false);

        // Test users
        robot.clickOn(TextMatchers.hasText(menu));
        robot.clickOn(TextMatchers.hasText(sousMenu));
    }
    /*---------- ACCESSEURS ----------*/
}
