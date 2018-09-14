package control.task;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
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
public class CreerExtractComposantsSonarTask extends AbstractTask
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
        extractionComposants();
        return sauvegarde();
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
    private void extractionComposants()
    {
        Map<TypeColCompo, List<ComposantSonar>> map = new EnumMap<>(TypeColCompo.class);

        // Récupération des composants
        List<ComposantSonar> patrimoine = new ArrayList<>(recupererComposantsSonar(OptionRecupCompo.PATRIMOINE).values());
        map.put(TypeColCompo.PATRIMOINE, patrimoine);
                
        List<ComposantSonar> inconnus = new ArrayList<>(recupererComposantsSonar(OptionRecupCompo.INCONNU).values());
        map.put(TypeColCompo.INCONNU, inconnus);
        
        List<ComposantSonar> nonprod = new ArrayList<>(recupererComposantsSonar(OptionRecupCompo.NONPROD).values());
        map.put(TypeColCompo.NONPROD, nonprod);
        
        List<ComposantSonar> termines = new ArrayList<>(recupererComposantsSonar(OptionRecupCompo.TERMINE).values());
        map.put(TypeColCompo.TERMINE, termines);
        
        // Mise à jour du fichier Excel
        control.ajouterExtraction(map);
    }
    
    private boolean sauvegarde()
    {
        return control.write();
    }

    /*---------- ACCESSEURS ----------*/
}
