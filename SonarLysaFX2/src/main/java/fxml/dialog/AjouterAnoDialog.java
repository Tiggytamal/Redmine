package fxml.dialog;

import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IWorkItem;

import control.rtc.ControlRTC;
import dao.Dao;
import dao.DaoFactory;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.ModelFactory;
import model.bdd.AnomalieRTC;
import model.bdd.ProjetClarity;
import model.bdd.Utilisateur;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Fenêtre premettant d'ajouter une anomalie RTC non générée par SonarLysa.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class AjouterAnoDialog extends AbstractAjouterDialog<AnomalieRTC, Integer>
{
    /*---------- ATTRIBUTS ----------*/

    // Nodes
    private TextField numeroAnoField;
    private TextField commentairesField;
    private TextField matiereField;
    private TextField editionField;
    private TextField codeClarityField;
    private TextField typeDefautField;
    private DatePicker dateCreationPicker;
    private DatePicker dateRelancePicker;
    private DatePicker dateResoPicker;
    private TextField idCpiField;

    // Données
    private String etatAno;
    private String titre;
    private ProjetClarity clarity;
    private Utilisateur cpiLot;
    private AnomalieRTC ano;

    // Daos
    private Dao<Utilisateur, String> daoUser;
    private Dao<ProjetClarity, String> daoClarity;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur pour la création d'une nouvelle anomalie
     */
    public AjouterAnoDialog()
    {
        this("Créer Nouvelle Anomalie RTC");
        ano = ModelFactory.build(AnomalieRTC.class);
    }

    /**
     * Constructeur pour la mise à jour d'une anomlie RTC existante
     * 
     * @param ano
     *            Anomalie à traiter.
     */
    public AjouterAnoDialog(AnomalieRTC ano)
    {
        this("Modifier Nouvelle Anomalie RTC");

        // Mise à jour des données avec celle de l'anomalie
        this.ano = ano;
        numeroAnoField.setText(String.valueOf(ano.getNumero()));
        commentairesField.setText(ano.getCommentaire());
        matiereField.setText(ano.getMatiere());
        typeDefautField.setText(ano.getTypeDefaut());
        dateResoPicker.setValue(ano.getDateReso());
        dateCreationPicker.setValue(ano.getDateCreation());
        dateRelancePicker.setValue(ano.getDateRelance());
        editionField.setText(ano.getEdition());
        if (ano.getClarity() != null)
            codeClarityField.setText(ano.getClarity().getCode());
        if (ano.getCpiLot() != null)
            idCpiField.setText(ano.getCpiLot().getIdentifiant());
    }

    private AjouterAnoDialog(String titre)
    {
        super(titre);

        // Daos
        daoUser = DaoFactory.getMySQLDao(Utilisateur.class);
        daoClarity = DaoFactory.getMySQLDao(ProjetClarity.class);

        // Numéro d'anomalie
        numeroAnoField = creerIntegerField("numeroAnoField", "6 chiffres", "Numéro de l'anomalie RTC", "Numéro à 6 chiffres de l'anomalie RTC");

        // Remarques
        commentairesField = creerTextField("commentairesField", "Remarques");

        // Matière
        matiereField = creerTextField("matiereField", "Matière");

        // Matière
        typeDefautField = creerTextField("typeDefautField", "Type défaut");

        // Date résolution
        dateResoPicker = creerDateField("dateResoPicker", "Date résolution");

        // Date création
        dateCreationPicker = creerDateField("dateCreationPicker", "Date création");

        // Date relance
        dateRelancePicker = creerDateField("dateRelancePicker", "Date relance");

        // Edition
        editionField = creerTextField("editionField", "Edition");

        // Projet Clarity
        codeClarityField = creerTextField("codeClarityField", "Code projet clarity");

        // Cpi Lot
        idCpiField = creerTextField("idCpiField", "Id cpi du lot");
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected boolean controle()
    {
        // initialisation données
        donnesIncorrectes = Statics.EMPTY;
        boolean retour = true;

        // Contrôle anomalie
        if (numeroAnoField.getText().isEmpty())
        {
            retour = false;
            donnesIncorrectes += "Le numéro de l'anomalie n'est pas renseigné.\n";
        }
        else
        {
            int numero = 0;
            try
            {
                numero = (Integer) numeroAnoField.getTextFormatter().getValue();
                IWorkItem wi = ControlRTC.getInstance().recupWorkItemDepuisId(numero);
                etatAno = ControlRTC.getInstance().recupEtatElement(wi);
                titre = wi.getHTMLSummary().getPlainText();
            }
            catch (NullPointerException e)
            {
                retour = false;
                donnesIncorrectes += "Le numéro de l'anomalie est erroné.\n";
            }
            catch (TeamRepositoryException e2)
            {
                throw new TechnicalException("Méthode view.dialog.AjouterAnoDialog.controle - Erreur au moment de récupérer l'anomalie : " + numero, e2);
            }
        }

        // Contrôle date création
        if (dateCreationPicker.getValue() == null)
        {
            retour = false;
            donnesIncorrectes += "La date de création du lot doit être renseignée.\n";
        }

        // Contrôle projet Clarity
        if (!codeClarityField.getText().isEmpty())
        {
            clarity = daoClarity.readAllMap().get(codeClarityField.getText());

            if (clarity == null)
            {
                retour = false;
                donnesIncorrectes += "Le code clarity n'est pas connu dans la base de données.\n";
            }
        }

        // Contrôle cpi du lot
        if (!controlCpiLot())
            retour = false;

        return retour;
    }

    /**
     * Contrôle si l'id du cpi donnée est valide et création de l'utilisateur
     * 
     * @return
     *         Vrai si le cpi est valide.
     */
    private boolean controlCpiLot()
    {
        String idCpi = idCpiField.getText();

        // Si la zone est vide, on renvoie true. La valeur n'est pas obligatoire
        if (idCpi.isEmpty())
            return true;

        // On essaie de récupérer le cpi depuis la basse de données
        cpiLot = daoUser.readAllMap().get(idCpi);

        // Si le cpi est valorisé, on arrête le traitement sinon on teste de chercher dans RTC
        if (cpiLot != null)
            return true;

        try
        {
            IContributor contrib = ControlRTC.getInstance().recupContributorDepuisId(idCpi);
            if (contrib != null)
            {
                cpiLot = Utilisateur.build(contrib);
                daoUser.persist(cpiLot);
                return true;
            }
        }
        catch (TeamRepositoryException e)
        {
            throw new TechnicalException("Méthode view.dialog.AjouterAnoDialog.controle - Erreur au moment de récupérer le contributeur : " + idCpi, e);
        }

        // Si l'on a pas réussi, on remonte l'erreur
        donnesIncorrectes += "Aucun utilisateur n'a été trouvé depuis l'id : " + idCpi + '\n';
        return false;
    }

    @Override
    protected AnomalieRTC retourObjet()
    {
        ano.setTitre(titre);
        ano.setNumero(Integer.parseInt(numeroAnoField.getText()));
        ano.setEtatAno(etatAno);
        ano.setCommentaire(commentairesField.getText());
        ano.setDateCreation(dateCreationPicker.getValue());
        ano.setDateRelance(dateRelancePicker.getValue());
        ano.setDateReso(dateResoPicker.getValue());
        ano.setMatiere(matiereField.getText());
        ano.setTypeDefaut(typeDefautField.getText());
        ano.setEdition(editionField.getText());
        ano.setClarity(clarity);
        ano.setCpiLot(cpiLot);
        return ano;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
