package control.task.extraction;

import static utilities.Statics.EMPTY;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import control.excel.ControlExtractCompo;
import control.excel.ExcelFactory;
import model.bdd.ComposantBase;
import model.enums.ColCompo;

/**
 * Tâche d'extraction des composants SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class ExtractionComposantsSonarTask extends AbstractExtractionTask<ControlExtractCompo>
{

    /*---------- ATTRIBUTS ----------*/

    private static final int ETAPES = 2;
    private static final String TITRE = "Extraction composants SonarQube";
    private static final Pattern NUMEROTATION = Pattern.compile("^.*\\d\\d$");

    /** Pattern pour retirer les numéros de version des composants */
    private static final Pattern PATTERNNOMCOMPO = Pattern.compile("^\\D+(\\d+\\D+){0,1}");

    /*---------- CONSTRUCTEURS ----------*/

    public ExtractionComposantsSonarTask(File file)
    {
        super(ETAPES, TITRE);
        annulable = false;
        control = ExcelFactory.getWriter(ColCompo.class, file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        cancel();
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        // Affichage
        baseMessage = "Récupération des composants.";
        updateMessage(EMPTY);

        // Récupération des composants
        Map<String, List<ComposantBase>> tousLesCompos = recupererToutesVersionsComposantsSonar();

        // Affichage
        etapePlus();
        baseMessage = "Création du fichier.";
        updateMessage(EMPTY);
        updateProgress(0, 1);

        // Mise à jour du fichier Excel
        control.creerFichierExtraction(tousLesCompos, this);

        // Affichage
        updateProgress(1, 1);
        etapePlus();
        baseMessage = "Sauvegarde du fichier.";
        updateMessage(EMPTY);

        // Sauvegarde
        return sauvegarde();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Récupération de tous les composants SonarQube, rangés par versions.
     * 
     * @return
     *         Map de tous les composants SonarQube, regroupant toutes les versions de chaque composant.
     */
    private Map<String, List<ComposantBase>> recupererToutesVersionsComposantsSonar()
    {
        // initialisation des composants avec ordonnancement selon les noms et la version des composants
        List<ComposantBase> compos = new ArrayList<>(getMapCompo().values());

        compos.sort((o1, o2) -> {

            // Variables
            String nomO1 = o1.getNom();
            String nomO2 = o2.getNom();
            boolean bO1 = NUMEROTATION.matcher(nomO1).matches();
            boolean bO2 = NUMEROTATION.matcher(nomO2).matches();

            // Traitement
            if (!bO1 && !bO2)
                return 0;
            if (!bO1)
                return -1;
            if (!bO2)
                return 1;
            return Integer.valueOf(nomO2.substring(nomO2.length() - 2, nomO2.length())).compareTo(Integer.valueOf(nomO1.substring(nomO1.length() - 2, nomO1.length())));
        });

        // Map de retour. La clef est le nom d'un composant sans indices. la list reprend toutes les versions du composant dans SonarQube
        Map<String, List<ComposantBase>> retour = new HashMap<>();

        for (ComposantBase compo : compos)
        {
            // On ne prend pas en compte les branches non master dans le traitement
            if (!"master".equals(compo.getBranche()))
                continue;

            Matcher matcher = PATTERNNOMCOMPO.matcher(compo.getNom());

            if (matcher.find())
                retour.computeIfAbsent(matcher.group(0).trim(), l -> new ArrayList<>()).add(compo);
        }

        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}
