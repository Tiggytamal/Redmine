package control.task.maj;

import control.rest.ControlRepack;
import control.task.AbstractTask;
import model.bdd.ComposantBase;

/**
 * Mise à jour de la date de repack des composants depuis l'APi de la PIC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class MajComposRepackTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Mise à jour des Lots RTC";
    private static final int ETAPES = 1;

    /*---------- CONSTRUCTEURS ----------*/

    public MajComposRepackTask()
    {
        super(ETAPES, TITRE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas d'implementation pour le moment
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    public Boolean call() throws Exception
    {
        majComposRepack();
        return sauvegarde(daoCompo, getMapCompo().values()) > 0;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Itération sur la map des composants pour traiter les repacks.
     * 
     * @return
     *         Vrai s'il n'y a pas de plantage.
     */
    private boolean majComposRepack()
    {
        ControlRepack control = new ControlRepack();
        // Itération sur tous les composants Sonar
        for (ComposantBase compo : getMapCompo().values())
        {
            // Mise à jour de la date de repack du composant.
            compo.majDateRepack(control.getRepacksComposant(compo));

            // Mise à jour de la date de repack du lot lié au composant.
            if (compo.getDateRepack() != null)
                compo.getLotRTC().majDateRepack(compo.getDateRepack());
        }
        return true;
    }

    /*---------- ACCESSEURS ----------*/
}
