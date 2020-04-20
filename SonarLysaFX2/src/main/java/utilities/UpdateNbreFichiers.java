package utilities;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.MenuItem;
import model.enums.Param;
import model.enums.Severity;

public class UpdateNbreFichiers implements Runnable
{
    /*---------- ATTRIBUTS ----------*/
    
    private SimpleStringProperty nbreFichiersTextProperty;
    private MenuItem fichierPic;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public UpdateNbreFichiers(SimpleStringProperty nbreFichiersTextProperty, MenuItem fichierPic)
    {
        this.nbreFichiersTextProperty = nbreFichiersTextProperty;
        this.fichierPic = fichierPic;
    }
    /*---------- METHODES PUBLIQUES ----------*/
    

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                // Contrôle toute les secondes
                Thread.sleep(Statics.SECOND);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }

            // Récupération des fichiers actuellement sur le quai
            String[] fichiers = new File(Statics.proprietesXML.getMapParams().get(Param.REPSONAR)).list();

            // Si on a pas les droits sur le quai, la liste sera null, on protège et on stoppe la boucle inutile
            if (fichiers == null)
            {
                Platform.runLater(() -> nbreFichiersTextProperty.set("inconnu"));
                fichierPic.setDisable(true);
                throw new FunctionalException(Severity.ERROR, "Vous n'avez pas accès au quai de dépôt. Blocage du traitement des fichiers.");
            }

            // Affichage du nombre de fichiers sur le quai
            Platform.runLater(() -> nbreFichiersTextProperty.set(String.valueOf(fichiers.length)));
        }
    }       
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
