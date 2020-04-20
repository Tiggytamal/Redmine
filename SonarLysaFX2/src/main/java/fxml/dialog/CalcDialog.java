package fxml.dialog;

import java.time.LocalDate;

import dao.DaoDefautQualite;
import dao.DaoFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.VBox;
import model.bdd.DefautQualite;
import model.enums.Severity;
import utilities.FunctionalException;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Fenêtre pour calculer les statiques en rapport avec SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class CalcDialog extends AbstractBaseDialog<Boolean, VBox>
{
    /*---------- ATTRIBUTS ----------*/

    private DaoDefautQualite dao;

    @FXML
    private DatePicker datePicker;
    @FXML
    private VBoxCalc crees;
    @FXML
    private VBoxCalc abandonnees;
    @FXML
    private VBoxCalc obsoletes;
    @FXML
    private VBoxCalc closes;
    @FXML
    private VBoxCalc enCours;
    @FXML
    private VBoxCalc enCoursTotal;
    @FXML
    private RadioButton radioMensuel;
    @FXML
    private RadioButton radioTrimestriel;
    @FXML
    private Button ok;

    /*---------- CONSTRUCTEURS ----------*/

    public CalcDialog()
    {
        super("Calculatrice", "/fxml/dialog/CalcDialog.fxml");

        dao = DaoFactory.getMySQLDao(DefautQualite.class);
        init();
        setResizable(false);
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    public void implShortCut()
    {
        Utilities.creerRaccourciClavierBouton(ok, pane.getScene(), new KeyCodeCombination(KeyCode.ENTER));
    }

    @Override
    protected void initImpl()
    {
        // pas de traitement sur cette classe
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Calcul est affichages des statistiques. Utilise des requêtes depuis la base de données.
     */
    @FXML
    private void calculer()
    {
        LocalDate dateDebut = datePicker.getValue();
        if (dateDebut == null || dateDebut.getDayOfMonth() != 1)
            throw new FunctionalException(Severity.INFO, "Veuillez choisir le premier jour d'un mois pour effectuer les calculs.");

        LocalDate dateFin = null;
        if (radioMensuel.isSelected())
            dateFin = dateDebut.plusMonths(1).minusDays(1);
        else if (radioTrimestriel.isSelected())
            dateFin = dateDebut.plusMonths(3).minusDays(1);
        else
            throw new TechnicalException("fxml.dialog.CalcDialog.calculer - Erreur selection radioBouton.");

        // Calcul nbre anomalies créées
        crees.setValeur(String.valueOf(dao.crees(dateDebut, dateFin)));

        // Calcul Nbre anomalies abandonnées
        long valeur = dao.abandonnees(dateDebut, dateFin);
        long valeurSecu = dao.abandonneesSecu(dateDebut, dateFin);
        abandonnees.setValeur(valeur + " - " + valeurSecu);

        // Calcul Nbre anomalies obsolètes
        valeur = dao.obsoletes(dateDebut, dateFin);
        valeurSecu = dao.obsoletesSecu(dateDebut, dateFin);
        obsoletes.setValeur(valeur + " - " + valeurSecu);

        // Calcul Nbre anomalies closes
        valeur = dao.closes(dateDebut, dateFin);
        valeurSecu = dao.closesSecu(dateDebut, dateFin);
        closes.setValeur(valeur + " - " + valeurSecu);

        // Calcul Nbre anomalies en cours
        valeur = dao.enCours(dateDebut, dateFin);
        valeurSecu = dao.enCoursSecu(dateDebut, dateFin);
        enCours.setValeur(valeur + " - " + valeurSecu);

        // Calcul Nbre anomalies en cours total
        enCoursTotal.setValeur(String.valueOf(dao.enCoursTotal()));
    }

    /*---------- ACCESSEURS ----------*/
}
