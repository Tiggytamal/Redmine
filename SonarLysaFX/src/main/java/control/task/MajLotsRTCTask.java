package control.task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.mchange.util.AssertException;

import control.rtc.ControlRTC;
import dao.DaoFactory;
import dao.DaoLotRTC;
import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
import model.utilities.ControlModelInfo;
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
    private DaoLotRTC dao;

    /*---------- CONSTRUCTEURS ----------*/

    public MajLotsRTCTask(LocalDate date, boolean remiseAZero)
    {
        super(1, TITRE);
        this.date = date;
        this.remiseAZero = remiseAZero;
        dao = DaoFactory.getDao(LotRTC.class);
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
        Map<String, LotRTC> map = majLotsRTC();
        return sauvegarde(map);
    }

    @Override
    public void annuler()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private Map<String, LotRTC> majLotsRTC() throws TeamRepositoryException
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
        Map<String, LotRTC> retour = dao.readAllMap();
        Map<String, ProjetClarity> mapClarity = DaoFactory.getDao(ProjetClarity.class).readAllMap();

        for (IWorkItemHandle handle : handles)
        {
            // R�cup�ration de l'objet complet depuis l'handle de la requ�te
            LotRTC lotRTC = control.creerLotSuiviRTCDepuisHandle(handle);
            
            // On saute tous les lotRTC sans num�ro de lot.
            if (lotRTC.getLot().isEmpty())
                continue;

            // R�cup�ration du code Clarity depuis RTC
            String codeClarity = lotRTC.getProjetClarityString();

            // Test du projet Clarity par rapport � la base de donn�es et valorisation de la donn�e
            ProjetClarity projetClarity = new ControlModelInfo().testProjetClarity(codeClarity, mapClarity);
            lotRTC.setProjetClarity(projetClarity);

            String lot = lotRTC.getLot();

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
    private boolean sauvegarde(Map<String, LotRTC> map)
    {
        if (remiseAZero)
            dao.resetTable();
        return dao.persist(map.values()) > 0;
    }

    /*---------- ACCESSEURS ----------*/
}
