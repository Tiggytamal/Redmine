package fxml.factory;

import java.time.LocalDate;
import java.util.Optional;

import control.task.action.TraiterActionDefautQualiteHistoriqueTask;
import dao.Dao;
import fxml.bdd.DefautQualiteHistoriqueBDD;
import fxml.dialog.RemarqueDialog;
import javafx.scene.control.MenuItem;
import model.bdd.DefautQualite;
import model.enums.ActionDqH;
import model.enums.EtatAnoRTC;
import model.enums.EtatAnoRTCProduit;
import model.enums.TypeDefautQualite;
import model.fxml.DefautQualiteFXML;

public class DefautQualiteHistoriqueFXMLTableRow extends FXMLTableRow<DefautQualiteFXML, String, ActionDqH, Dao<DefautQualite, String>, DefautQualite, DefautQualiteHistoriqueBDD>
{
    /*---------- ATTRIBUTS ----------*/

    // Couleurs des lignes
    private static final String ORANGE = "-fx-background-color:orange";
    private static final String YELLOW = "-fx-background-color:lightyellow";
    private static final String RED = "-fx-background-color:sandybrown";
    private static final String GREEN = "-fx-background-color:lightgreen";
    private static final String BLUE = "-fx-background-color:lightblue";
    private static final String GRAY = "-fx-background-color:lightgray";

    /*---------- CONSTRUCTEURS ----------*/

    public DefautQualiteHistoriqueFXMLTableRow(DefautQualiteHistoriqueBDD control, Dao<DefautQualite, String> dao)
    {
        super(dao, control, ActionDqH.values());

        MenuItem modifItem = new MenuItem("Modifier Remarques");
        modifItem.setOnAction(event -> {

            // Récupération du defaut en base de donnees
            DefautQualite dq = dao.recupEltParIndex(getItem().getIndex());

            // Création de la fenêtre pour modifier les remarques du defaut.
            Optional<String> result = new RemarqueDialog(getItem()).showAndWait();
            result.ifPresent(s -> { dq.setRemarque(result.get()); dao.persist(dq); });

            control.refreshList(control.getPredicate());

        });
        rowMenu.getItems().add(modifItem);

        creerMenu(a -> new TraiterActionDefautQualiteHistoriqueTask(a, dao.recupEltParIndex(getItem().getIndex())));
    }
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void updateItem(DefautQualiteFXML item, boolean empty)
    {
        super.updateItem(item, empty);

        if (item == null || empty)
            return;

        String dateMepPrev = item.getDateMepPrev();

        // Ligne verte si le qualitygate est bon
        if ("OK".equals(item.getQg()) && "OK".equals(item.getAppliOK()))
            setStyle(GREEN);

        else if (item.getEtatRTC().equals(EtatAnoRTC.CLOSE.getValeur()) || item.getEtatRTC().equals(EtatAnoRTCProduit.CLOSE.getValeur()))
            setStyle(GRAY);

        // Ligne orange le défaut n'a pas encore été traité : ni numéro d'anomalie, ni remarque
        else if ("0".equals(item.getNumeroAnoRTC().get(0)) && item.getRemarque().isEmpty())
            setStyle(ORANGE);

        else if (TypeDefautQualite.APPLI.toString().equals(item.getTypeDefaut()))
            setStyle(BLUE);

        // Ligne jaune foncée si la date de mise en production est passée
        else if (!dateMepPrev.isEmpty() && LocalDate.now().isAfter(LocalDate.parse(dateMepPrev)))
            setStyle(RED);

        // Ligne jaune si la date de mise en prod est dans moins de 3 semaines
        else if (!dateMepPrev.isEmpty() && LocalDate.now().plusWeeks(3).isAfter(LocalDate.parse(dateMepPrev)))
            setStyle(YELLOW);
        else
            setStyle(null);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
