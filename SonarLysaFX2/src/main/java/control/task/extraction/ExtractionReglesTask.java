package control.task.extraction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import control.excel.ControlExtractRegles;
import control.excel.ExcelFactory;
import model.enums.ColRegle;
import model.rest.sonarapi.DetailRegle;
import model.rest.sonarapi.ParamAPI;
import model.rest.sonarapi.Regle;

/**
 * Tâche d'extraction de toutes les règles SonarQube tagées SQL.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class ExtractionReglesTask extends AbstractExtractionTask<ControlExtractRegles>
{
    /*---------- ATTRIBUTS ----------*/

    private static final int ETAPES = 3;
    private static final String TITRE = "Extraction Regles";

    /*---------- CONSTRUCTEURS ----------*/

    public ExtractionReglesTask(File file)
    {
        super(ETAPES, TITRE);
        control = ExcelFactory.getWriter(ColRegle.class, file);
        annulable = false;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        // Map de tri
        Map<String, Regle> map = new HashMap<>();

        // Récupération régles avec tags sql
        List<ParamAPI> liste = new ArrayList<>();
        liste.add(new ParamAPI("tags", "sql"));
        ajoutMap(liste, map);

        // Récupération avec sql dans le titre
        liste = new ArrayList<>();
        liste.add(new ParamAPI("q", "sql"));
        ajoutMap(liste, map);

        // Ecriture fichier
        control.creationExtraction(map.values(), this);
        
        // Sauvegarde
        return sauvegarde();
    }

    /*---------- METHODES PRIVEES ----------*/

    private void ajoutMap(List<ParamAPI> liste, Map<String, Regle> map)
    {
        List<Regle> regles = api.getReglesGenerique(liste);
        for (Regle regle : regles)
        {
            DetailRegle details = api.getDetailsRegle(regle.getKey());
            regle.setActivations(details.getActivations());
            map.put(regle.getKey(), regle);
        }
    }
    
    /*---------- ACCESSEURS ----------*/

}
