package fxml.factory;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import model.fxml.AbstractFXMLModele;
import utilities.Statics;
import utilities.Utilities;

public class TableCellFXML<T extends AbstractFXMLModele<I>, I> extends TableCell<T, Object>
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantage */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @SuppressWarnings("unchecked")
    @Override
    protected void updateItem(Object item, boolean empty)
    {
        super.updateItem(item, empty);
        setText(null);

        // Mise à zero si l'objet est vide ou null
        if (item == null || empty)
            setGraphic(null);

        // Contrôle pour créer un hyperliens si on a une ObervableList
        else if (item instanceof ObservableList)
            traitementListe((ObservableList<String>) item);
        else if (item instanceof CheckBox)
        {
            setGraphic((CheckBox) item);
            setAlignment(Pos.CENTER);
        }

        // Mise à jour des autres cellules
        else
            // Mise à jour du texte de la cellule
            setText(item.toString());
    }

    /*---------- METHODES PRIVEES ----------*/

    private void traitementListe(ObservableList<String> liste)
    {
        // Protection liste vide
        if (liste.isEmpty())
        {
            setGraphic(null);
            setText(Statics.EMPTY);
            return;
        }

        String donnee = liste.get(0);
        String liens = liste.get(1);

        // S'il n'y a pas de liens sur la liste, on ajoute simplement du texte
        if (liens.isEmpty())
        {
            setGraphic(null);

            // Gestion des numéros d'anomalies non existant
            setText("0".equals(donnee) ? Statics.EMPTY : donnee);
        }
        else
        {
            Hyperlink hyperlink;

            // Création de l'hyperliens avec ouverture de l'explorateur vers l'adresse du paramètre
            hyperlink = new Hyperlink(donnee);
            hyperlink.setOnAction(evt -> {
                try
                {
                    if (Desktop.isDesktopSupported())
                        Desktop.getDesktop().browse(new URL(liens).toURI());
                }
                catch (IOException | URISyntaxException e)
                {
                    LOGPLANTAGE.error(Statics.EMPTY, e);
                }
            });

            // Ajout de l'hyperliens sur la cellule
            setGraphic(hyperlink);
        }
    }

    /*---------- ACCESSEURS ----------*/
}
