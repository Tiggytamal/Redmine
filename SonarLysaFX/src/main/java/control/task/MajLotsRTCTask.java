package control.task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.mchange.util.AssertException;

import control.rtc.ControlRTC;
import dao.DaoLotSuiviRTC;
import model.LotSuiviRTC;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

/**
 * Tâche permettant de mettre à jour le fichier XML des lots RTC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class MajLotsRTCTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Mise à jour des Lots RTC";
    private LocalDate date;
    private boolean remiseAZero;
    private DaoLotSuiviRTC dao;

    /*---------- CONSTRUCTEURS ----------*/

    public MajLotsRTCTask(LocalDate date, boolean remiseAZero)
    {
        super(1, TITRE);
        this.date = date;
        this.remiseAZero = remiseAZero;
        dao = new DaoLotSuiviRTC();
    }

    /**
     * Constructeur sans paramètres pour les tests. Ne pas utiliser, lance une exception
     */
    public MajLotsRTCTask()
    {
        super(1, TITRE);
        throw new AssertException();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        Map<String, LotSuiviRTC> map = majLotsRTC();
        return sauvegarde(map);
    }

    @Override
    public void annuler()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private Map<String, LotSuiviRTC> majLotsRTC() throws TeamRepositoryException
    {
        ControlRTC control = ControlRTC.INSTANCE;
        List<IWorkItemHandle> handles = control.recupLotsRTC(remiseAZero, date);
        if (handles.isEmpty())
            throw new FunctionalException(Severity.ERROR, "La liste des lots RTC est vide!");

        // VAriables
        int i = 0;
        int size = handles.size();
        String base = "Récupération RTC - Traitement lot : ";
        String fin = "Nbre de lots traités : ";
        String sur = " sur ";
        long debut = System.currentTimeMillis();

        // Initialisation de la map depuis les informations de la base de données
        Map<String, LotSuiviRTC> retour = dao.readAllMap();

        for (IWorkItemHandle handle : handles)
        {
            // Récupération de l'objet complet depuis l'handle de la requête
            LotSuiviRTC lotRTC = control.creerLotSuiviRTCDepuisHandle(handle);

            String lot = lotRTC.getLot();

            // ON saute tous les lotRTC sans numéro de lot.
            if (lotRTC.getLot().isEmpty())
                continue;

            // On rajoute les lots non éxistants à la map et on mets à jour les autres avec les nouvelles données
            if (!retour.containsKey(lot))
                retour.put(lot, lotRTC);
            else
                retour.get(lot).update(lotRTC);
            
            // Affichage
            i++;
            updateProgress(i, size);
            updateMessage(new StringBuilder(base).append(lot).append(Statics.NL).append(fin).append(i).append(sur).append(size).append(affichageTemps(debut, i, size)).toString());
        }
        return retour;
    }

    /**
     * Sauvegarde du fichier
     * 
     * @return
     */
    private boolean sauvegarde(Map<String, LotSuiviRTC> map)
    {
        if (remiseAZero)
            dao.resetTable();
        return dao.update(map.values());
    }

    /*---------- ACCESSEURS ----------*/
}
