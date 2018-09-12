package control.task;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.mchange.util.AssertException;

import control.rtc.ControlRTC;
import control.xml.ControlXML;
import model.LotSuiviRTC;
import model.enums.TypeFichier;
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
public class MajLotsRTCTask extends AbstractSonarTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Mise � jour des Lots RTC";
    private LocalDate date;
    private boolean remiseAZero;

    /*---------- CONSTRUCTEURS ----------*/

    public MajLotsRTCTask(LocalDate date, boolean remiseAZero)
    {
        super(1, TITRE);
        this.date = date;
        this.remiseAZero = remiseAZero;
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
        return majFichierRTC();
    }
    
    @Override
    public void annuler()
    {
        // Pas de traitement d'annulation        
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean majFichierRTC() throws TeamRepositoryException
    {
        ControlRTC control = ControlRTC.INSTANCE;
        List<IWorkItemHandle> handles = control.recupLotsRTC(remiseAZero, date);
        if (handles.isEmpty())
            throw new FunctionalException(Severity.ERROR, "La liste des lots RTC est vide!");

        int i = 0;
        int size = handles.size();
        String base = "R�cup�ration RTC - Traitement lot : ";
        String fin = "Nbre de lots trait�s : ";
        String sur = " sur ";
        
        // Initialisation - en cas de remise � z�ro on prend une ma pvi�rge sinon on r�cup�re le fichier existant comme base.
        Map<String, LotSuiviRTC> map = null;
        
        if (remiseAZero)
            map = new HashMap<>();
        else
            map = Statics.fichiersXML.getMapLotsRTC();
        
        for (IWorkItemHandle handle : handles)
        {
            // R�cup�ration de l'objet complet depuis l'handle de la requ�te
            LotSuiviRTC lot = control.creerLotSuiviRTCDepuisHandle(handle);
            i++;
            
            updateProgress(i, size);
            updateMessage(new StringBuilder(base).append(lot.getLot()).append(Statics.NL).append(fin).append(i).append(sur).append(size).toString());
            
            if (!lot.getLot().isEmpty())
                map.put(lot.getLot(), lot);
        }

        Statics.fichiersXML.majMapDonnees(TypeFichier.LOTSRTC, map);
        new ControlXML().saveParam(Statics.fichiersXML);
        return true;
    }

    /*---------- ACCESSEURS ----------*/
}
