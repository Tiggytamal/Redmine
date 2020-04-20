package control.task.action;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import control.task.AbstractTask;
import dao.Dao;
import model.bdd.AbstractBDDModele;
import model.enums.Action;

/**
 * Classe mère abstraite de toutes les tâches gérant les actions en base de données.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 * @param <T>
 *        Classe du modèle en base de données.
 * @param <A>
 *        Enumération des actions possibles sur le modèle. * @param <D>
 *        Dao pour enregistrer les modifications en table.
 */
public abstract class AbstractTraiterActionTask<T extends AbstractBDDModele<U>, A extends Action, D extends Dao<T, U>, U> extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    protected A action;
    protected List<T> objets;
    protected D dao;
    protected Function<T, U> fonction;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractTraiterActionTask(A action, List<T> objets, int etapes, String titres, Function<T, U> function)
    {
        this(action, etapes, titres, function);
        this.objets = objets;
    }

    protected AbstractTraiterActionTask(A action, T dq, int etapes, String titres, Function<T, U> function)
    {
        this(action, etapes, titres, function);
        this.objets = new ArrayList<>();
        objets.add(dq);
    }

    private AbstractTraiterActionTask(A action, int etapes, String titres, Function<T, U> fonction)
    {
        super(etapes, titres);
        this.action = action;
        this.fonction = fonction;
        startTimers();
    }

    /*---------- METHODS ABSTRAITES ----------*/

    /**
     * Méthode principal de traitement des actions. Implémentée pour chaque Classe de modèle.
     * 
     * @param objet
     *              Objet à traiter.
     */
    protected abstract void traitementAction(T objet);

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas d'implementaion pour le momment.
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        // Affichage
        int i = 0;
        int size = objets.size();
        long debut = System.currentTimeMillis();
        baseMessage = "Traitement élément :\n";

        for (T objet : objets)
        {
            traitementAction(objet);

            // Affichage
            updateMessage(fonction.apply(objet).toString());
            i++;
            updateProgress(i, size);
            if (size > 0)
                calculTempsRestant(debut, i, size);
        }

        updateProgress(1, 1);
        return Boolean.TRUE;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
