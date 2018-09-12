package control.task;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mchange.util.AssertException;

import control.excel.ControlExtractCompo;
import model.ComposantSonar;
import model.enums.OptionRecupCompo;
import model.enums.TypeColCompo;

/**
 * Tâche de création de la vue Sonar du patrimoine
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class CreerExtractComposantsSonarTask extends AbstractSonarTask
{

    /*---------- ATTRIBUTS ----------*/
   
    private static final int ETAPES = 3;
    private static final String TITRE = "Extraction composants SonarQube";
    
    private ControlExtractCompo control;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerExtractComposantsSonarTask(File file)
    {
        super(ETAPES, TITRE);
        annulable = false;
        control = new ControlExtractCompo(file);
    }
    
    /**
     * Constrcuteur snas paramètres pour les tests. Ne pas utiliser, lance une exception
     */
    public CreerExtractComposantsSonarTask()
    {
        super(ETAPES, TITRE);
        throw new AssertException();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return extractionComposants();
    }

    @Override
    public void annuler()
    {
        cancel();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Création de la vue patrimoine pour la semaine en cours.
     * 
     * @return {@code true} Si la vue a bien été créée.<br>
     *         {@code false} Si la task a été intérompue ou s'il y a eu une erreur.
     */
    private boolean extractionComposants()
    {
        Map<TypeColCompo, List<ComposantSonar>> map = new HashMap<>();

        // Récupération des composants
        List<ComposantSonar> patrimoine = new ArrayList<>(recupererComposantsSonar(OptionRecupCompo.PATRIMOINE).values());
        System.out.println("Patrimoine : " + patrimoine.size());
        map.put(TypeColCompo.PATRIMOINE, patrimoine);
                
        List<ComposantSonar> inconnus = new ArrayList<>(recupererComposantsSonar(OptionRecupCompo.INCONNU).values());
        System.out.println("inco : " + inconnus.size());
        map.put(TypeColCompo.INCONNU, inconnus);
        
        List<ComposantSonar> nonprod = new ArrayList<>(recupererComposantsSonar(OptionRecupCompo.NONPROD).values());
        System.out.println("Non prod : " + nonprod.size());
        map.put(TypeColCompo.NONPROD, nonprod);
        
        List<ComposantSonar> termines = new ArrayList<>(recupererComposantsSonar(OptionRecupCompo.TERMINE).values());
        System.out.println("Terminés : " + termines.size());
        map.put(TypeColCompo.TERMINE, termines);
        
        control.ajouterExtraction(map);

        return true;
    }

    /*---------- ACCESSEURS ----------*/
}
