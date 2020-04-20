package fxml;

import control.task.LaunchTask;
import control.task.maj.PurgerFichiersJSONTask;
import control.watcher.ControlWatch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.enums.EtatFichierPic;
import utilities.AbstractToStringImpl;
import utilities.TechnicalException;

/**
 * Controleur de la page de gestion du traitement des fichiers PIC de suivi des assemblages.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class FichierPic extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private Button arreter;
    @FXML
    private Button demarrer;
    @FXML
    private Button purge;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    
    @FXML
    private void action(ActionEvent event)
    {
        Object source = event.getSource();

        if (!(source instanceof Button))
            throw new TechnicalException("fxml.FichierPIc.afficher - Source non gérée pour l'événement.");

        String id = ((Button) source).getId();

        switch (id)
        {
            case "arreter":
                ControlWatch.INSTANCE.stopBoucle();
                Menu.updateFichiersActif(EtatFichierPic.INACTIF);
                break;

            case "demarrer":
                ControlWatch.INSTANCE.lancementBoucle();
                Menu.updateFichiersActif(EtatFichierPic.ACTIF);
                break;

            case "purge":
                LaunchTask.startTask(new PurgerFichiersJSONTask());
                break;

            default:
                throw new TechnicalException("fxml.FichierPIc.afficher - Bouton pas géré" + id);
        }
    }

    /*---------- ACCESSEURS ----------*/
}
