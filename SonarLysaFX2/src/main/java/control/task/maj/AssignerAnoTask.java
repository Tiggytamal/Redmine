package control.task.maj;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import control.mail.ControlMailOutlook;
import control.mail.ControlMail;
import control.rest.SonarAPI;
import control.task.AbstractTask;
import dao.Dao;
import dao.DaoFactory;
import model.bdd.IssueBase;
import model.bdd.Utilisateur;
import utilities.Statics;

/**
 * Tâche d'assignation des défauts SonarQube pas encore assignés à cause d'un compte non créé.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class AssignerAnoTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    /** Nombre d'etapes du traitement */
    private static final short ETAPES = 1;
    /** Titre de la tâche */
    private static final String TITRE = "Assignation des défauts SonarQube";

    private boolean stop;
    private SonarAPI sonarAPI;
    private Dao<IssueBase, String> dao;
    private ControlMail controlMail;

    /*---------- CONSTRUCTEURS ----------*/

    public AssignerAnoTask()
    {
        super(ETAPES, TITRE);
        dao = DaoFactory.getMySQLDao(IssueBase.class);
        sonarAPI = SonarAPI.build();
        controlMail = new ControlMailOutlook();
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
        Set<Utilisateur> liste = assigner();
        return controlMail.creerMailAssignerDefautSonar(liste);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Assigner des Issues dans SonarQube.
     * 
     * @return
     *         Liste des utilisateurs avec des anomlies non assignées.
     */
    private Set<Utilisateur> assigner()
    {
        List<IssueBase> issues = dao.readAll();
        Set<Utilisateur> retour = new HashSet<>();

        // Affichage
        int size = issues.size();
        int i = 0;
        int j = 0;
        baseMessage = "Assignation défauts SonarQube :\n";
        updateMessage(Statics.EMPTY);

        // Itération sur les issues pour essayer de les assigner à leur créateur. - on ne reteste pas un utilisateur deux fois.
        for (IssueBase issue : issues)
        {
            // Arret traitement
            if (stop)
                return retour;

            if (!retour.contains(issue.getUtilisateur()) && sonarAPI.assignerIssue(issue))
            {
                i++;
                dao.delete(issue);
            }
            else
                retour.add(issue.getUtilisateur());

            j++;
            updateProgress(j, size);
        }

        updateMessage(i + " anomalies assignées dans SonarQube sur " + size);
        return retour;
    }

    /*---------- ACCESSEURS ----------*/

}
