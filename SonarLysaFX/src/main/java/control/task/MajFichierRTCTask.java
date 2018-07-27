package control.task;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IWorkItemHandle;

import control.rtc.ControlRTC;
import control.xml.ControlXML;
import model.LotSuiviRTC;
import model.enums.TypeFichier;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

public class MajFichierRTCTask extends AbstractSonarTask
{
    /*---------- ATTRIBUTS ----------*/

    private LocalDate date;
    private boolean remiseAZero;

    /*---------- CONSTRUCTEURS ----------*/

    public MajFichierRTCTask(LocalDate date, boolean remiseAZero)
    {
        super(1);
        this.date = date;
        this.remiseAZero = remiseAZero;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return majFichierRTC();
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean majFichierRTC() throws TeamRepositoryException
    {
        ControlRTC control = ControlRTC.INSTANCE;
        List<?> handles = control.recupLotsRTC(remiseAZero, date);
        if (handles.isEmpty())
            throw new FunctionalException(Severity.ERROR, "La liste des lots RTC est vide!");

        int i = 0;
        int size = handles.size();
        String base = "Récupération RTC - Traitement lot : ";
        String fin = "Nbre de lots traités : ";
        String sur = " sur ";
        Map<String, LotSuiviRTC> map = new HashMap<>();
        for (Object handle : handles)
        {
            // Récupération de l'objet complet depuis l'handle de la requête
            LotSuiviRTC lot = control.creerLotSuiviRTCDepuisHandle((IWorkItemHandle) handle);
            i++;
            updateProgress(i, size);
            updateMessage(new StringBuilder(base).append(lot.getLot()).append(Statics.NL).append(fin).append(i).append(sur).append(size).toString());
            map.put(lot.getLot(), lot);
        }

        Statics.fichiersXML.majMapDonnees(TypeFichier.LOTSRTC, map);
        new ControlXML().saveParam(Statics.fichiersXML);
        return true;
    }

    /*---------- ACCESSEURS ----------*/
}
