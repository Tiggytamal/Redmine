package control.task;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.rtc.ControlRTC;
import control.xml.ControlXML;
import model.LotSuiviRTC;
import model.enums.TypeFichier;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

public class MajFichierRTCTask extends SonarTask
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

    private boolean majFichierRTC() throws TeamRepositoryException, JAXBException
    {
        ControlRTC control = ControlRTC.INSTANCE;
        Map<String, LotSuiviRTC> map = new HashMap<>();
        List<?> handles = control.recupLotsRTC(remiseAZero, date);
        if (!handles.isEmpty())
            throw new FunctionalException(Severity.SEVERITY_ERROR, "La liste des lots RTC est vide!");
        int i = 0;

        for (Object object : handles)
        {
            // R�cup�ration de l'objet complet depuis l'handle de la requ�te
            LotSuiviRTC lot = control.creerLotSuiviRTCDepuisHandle(object);
            updateProgress(++i, handles.size());
            updateMessage("Traitement lot : " + lot.getLot());
            map.put(lot.getLot(), lot);
        }

        Statics.fichiersXML.majMapDonnees(TypeFichier.LOTSRTC, map);
        new ControlXML().saveParam(Statics.fichiersXML);
        return true;
    }

    /*---------- ACCESSEURS ----------*/
}