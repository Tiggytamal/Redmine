package control.task.portfolio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import control.task.AbstractTask;
import model.bdd.ComposantBase;
import model.enums.OptionGestionErreur;
import model.enums.Param;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ObjetSonar;
import utilities.Statics;

/**
 * Tâche de création du portfolio regroupant tous les composants DataStage
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class CreerPortfolioDataStageTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Création Vue Datastage";
    private static final int ETAPES = 2;

    private ObjetSonar pf;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerPortfolioDataStageTask()
    {
        super(ETAPES, TITRE);
        annulable = true;
        startTimers();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        if (pf != null)
        {
            api.supprimerObjetSonar(pf, OptionGestionErreur.OUI);
        }
    }

    @Override
    protected Boolean call() throws Exception
    {
        // Appel du webservice pour remonter tous les composants
        updateMessage(RECUPCOMPOSANTS);

        if (isCancelled())
            return Boolean.FALSE;

        // Création de la vue avec maj du message
        etapePlus();
        String nom = "Composants Datastage";
        StringBuilder builder = new StringBuilder("Création vue ");
        updateMessage(builder.append(nom).toString());
        pf = creerObjetSonar("DSDataStageListeKey", nom, "Vue regroupant tous les composants Datastage", TypeObjetSonar.PORTFOLIO, true);
        baseMessage = builder.append(" OK.").append(Statics.NL).append("Ajout : ").toString();

        // Récupération de la dernière version de chaque composant
        Map<String, ComposantBase> map = recupererComposantsSonar();

        // tri pour supprimer tous les composants non DataStage
        List<ComposantBase> liste = new ArrayList<>();
        for (ComposantBase compo : map.values())
        {
            if (compo.getNom().startsWith(Statics.proprietesXML.getMapParams().get(Param.FILTREDATASTAGE)))
                liste.add(compo);
        }

        int i = 0;
        int size = liste.size();
        long debut = System.currentTimeMillis();

        for (ComposantBase compo : liste)
        {
            if (isCancelled())
                return Boolean.FALSE;

            api.ajouterSousProjet(pf, compo.getKey());

            // Affichage
            i++;
            calculTempsRestant(debut, i, size);
            updateProgress(i, size);
            updateMessage(compo.getNom());
        }
        api.calculObjetSonar(pf);
        
        return Boolean.TRUE;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
