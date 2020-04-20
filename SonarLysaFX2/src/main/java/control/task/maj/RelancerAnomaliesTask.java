package control.task.maj;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import control.mail.ControlMail;
import control.mail.ControlMailOutlook;
import control.rtc.ControlRTC;
import control.task.AbstractTask;
import dao.Dao;
import dao.DaoFactory;
import model.bdd.DefautQualite;
import model.bdd.Utilisateur;
import model.enums.EtatDefaut;
import utilities.Statics;

/**
 * Tâche de relance en masse des anomlies RTC qui ont plus d'une semaine depuis la dernière relance/création.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class RelancerAnomaliesTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    /** Nombre d'etapes du traitement */
    private static final short ETAPES = 1;
    /** Titre de la tâche */
    private static final String TITRE = "Relance des anomalies de plus de 7 jours";
    /** Contrôle de l'arrêt de la ^tache */
    private boolean stop;

    /*---------- CONSTRUCTEURS ----------*/

    public RelancerAnomaliesTask()
    {
        super(ETAPES, TITRE);
        annulable = true;
        startTimers();
    }

    public RelancerAnomaliesTask(AbstractTask tacheParente)
    {
        super(ETAPES, TITRE, tacheParente);
        annulable = true;
        startTimers();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        stop = true;
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        // Dao
        Dao<DefautQualite, String> dao = DaoFactory.getMySQLDao(DefautQualite.class);

        // Affichage
        baseMessage = "Relance des anomalies :\n";
        updateMessage(Statics.EMPTY);

        // Récupération des anomalies concernées
        List<DefautQualite> liste = new ArrayList<>();
        for (DefautQualite dq : dao.readAll())
        {
            // On relance tous les défauts avec une anomalie de plus de 6 jours ou relancee depuis plus de 6 jours
            if (dq.getEtatDefaut() == EtatDefaut.ENCOURS && dq.getNumeroAnoRTC() != 0 && (dq.getDateRelance() != null && dq.getDateRelance().isBefore(Statics.TODAY.minusDays(6))
                    || (dq.getDateRelance() == null && dq.getDateCreation().isBefore(Statics.TODAY.minusDays(6)))))
            {
                liste.add(dq);
            }
        }

        // Affichage
        int size = liste.size();
        int i = 0;

        // Taritement de chaque défaut
        ControlMail control = new ControlMailOutlook();
        for (DefautQualite dq : liste)
        {
            if (stop)
            {
                updateProgress(1, 1);
                updateMessage("Traitement arrêté");
                break;
            }

            // Relance de l'anomalie et mise à jour du défaut
            ControlRTC.getInstance().relancerAno(dq.getNumeroAnoRTC());
            dq.setDateRelance(LocalDate.now());

            // Création mail
            Set<Utilisateur> utilisateurs = dq.getCreateurIssues();
            utilisateurs.add(dq.getLotRTC().getCpiProjet());
            control.creerMailRelanceAnoRTC(utilisateurs, dq);

            // Affichage
            i++;
            updateProgress(i, size);
            updateMessage(String.valueOf(dq.getNumeroAnoRTC()));
        }

        // Mise à jour en base de données
        sauvegarde(dao, liste);
        return Boolean.TRUE;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
