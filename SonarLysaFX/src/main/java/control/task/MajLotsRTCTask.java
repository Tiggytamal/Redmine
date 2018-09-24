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
 * T�che permettant de mettre � jour le fichier XML des lots RTC.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public class MajLotsRTCTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Mise � jour des Lots RTC";
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
     * Constructeur sans param�tres pour les tests. Ne pas utiliser, lance une exception
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
        String base = "R�cup�ration RTC - Traitement lot : ";
        String fin = "Nbre de lots trait�s : ";
        String sur = " sur ";
        long debut = System.currentTimeMillis();

        // Initialisation de la map depuis les informations de la base de donn�es
        Map<String, LotSuiviRTC> retour = dao.readAllMap();

        for (IWorkItemHandle handle : handles)
        {
            // R�cup�ration de l'objet complet depuis l'handle de la requ�te
            LotSuiviRTC lotRTC = control.creerLotSuiviRTCDepuisHandle(handle);

            String lot = lotRTC.getLot();

            // ON saute tous les lotRTC sans num�ro de lot.
            if (lotRTC.getLot().isEmpty())
                continue;

            // On rajoute les lots non �xistants � la map et on mets � jour les autres avec les nouvelles donn�es
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
