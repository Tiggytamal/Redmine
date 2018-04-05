package control.task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.util.Map.Entry;

import control.ControlPic;
import control.parent.SonarTask;
import sonarapi.model.Vue;
import utilities.DateConvert;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

public class CreerVueProductionTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/

    private File file;
    private String vueKey;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    public static final String TITRE = "Vue MEP/TEP";

    /*---------- CONSTRUCTEURS ----------*/

    public CreerVueProductionTask(File file)
    {
        super(3);
        this.file = file;
    }

    public CreerVueProductionTask(LocalDate dateDebut, LocalDate dateFin)
    {
        super(4);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annuler()
    {
        if (vueKey != null && !vueKey.isEmpty())
            api.supprimerProjet(vueKey, true);

    }

    @Override
    protected Boolean call() throws Exception
    {
        return creerVueProduction();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    /**
     * Création des vues de mise en production des lots
     * 
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    private boolean creerVueProduction() throws IOException, InvalidFormatException
    {
        Map<LocalDate, List<Vue>> mapLot;
        if (file != null)
        {
            // Traitement données fichier Excel
            ControlPic excel = new ControlPic(file);
            Map<String, Vue> mapSonar = recupererLotsSonarQube();
            etapePlus();
            updateMessage("Traitement Fichier Excel...");
            mapLot = excel.recupLotsExcelPourMEP(mapSonar);
            updateMessage("Traitement Fichier Excel OK");
            excel.close();
        }
        else
        {
            mapLot = recupLotSonarPourMEP(dateDebut, dateFin);
        }

        // Création des vues menseulles ou trimestrielles
        if (mapLot.size() == 1)
            creerVueMensuelle(mapLot);
        else if (mapLot.size() == 3)
            creerVueTrimestrielle(mapLot);
        else
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Le fichier Excel donné ou les dates fournies ne sont ni mensuels ni trimetriels.");
        return true;
    }

    private Map<LocalDate, List<Vue>> recupLotSonarPourMEP(LocalDate dateDebut, LocalDate dateFin)
    {
        throw new FunctionalException(Severity.SEVERITY_INFO, "Pas encore implémenté");
    }

    /**
     * Récupère tous les lots créés dans Sonar.
     *
     * @return
     */
    private Map<String, Vue> recupererLotsSonarQube()
    {
        updateMessage("Récupérations des lots dans Sonar...");
        Map<String, Vue> map = new HashMap<>();
        List<Vue> views = api.getVues();
        for (Vue view : views)
        {
            if (view.getName().startsWith("Lot "))
                map.put(view.getName().substring(4), view);
            updateMessage("Récupérations des lots dans Sonar OK");
        }
        return map;

    }

    /**
     * Crée la vue Sonar pour une recherche mensuelle des composants mis en production .
     *
     * @param mapLot
     */
    private void creerVueMensuelle(final Map<LocalDate, List<Vue>> mapLot)
    {
        // Iteration pour récupérer le premier élément de la map
        Iterator<Entry<LocalDate, List<Vue>>> iter = mapLot.entrySet().iterator();
        Entry<LocalDate, List<Vue>> entry = iter.next();

        // Création de la vue principale

        String nomVue = new StringBuilder("MEP ").append(DateConvert.dateFrancais(entry.getKey(), "yyyy.MM - MMMM")).toString();
        vueKey = new StringBuilder("MEPMEP").append(DateConvert.dateFrancais(entry.getKey(), "MMyyyy")).append("Key").toString();
        etapePlus();
        String base = "Vue " + nomVue + Statics.NL;
        updateMessage(base);
        Vue vueParent = creerVue(vueKey, nomVue, new StringBuilder("Vue des lots mis en production pendant le mois de ").append(DateConvert.dateFrancais(entry.getKey(), "MMMM yyyy")).toString(),
                true);

        // Ajout des sous-vue
        int i = 0;
        int size = entry.getValue().size();
        for (Vue vue : entry.getValue())
        {
            updateMessage(base + "ajout : " + vue.getName());
            updateProgress(++i, size);
            api.ajouterSousVue(vue, vueParent);
        }
    }

    /**
     * Crée la vue Sonar pour une recherche trimetrielle des composants mis en production .
     *
     * @param mapLot
     */
    private void creerVueTrimestrielle(Map<LocalDate, List<Vue>> mapLot)
    {
        // Création des variables. Transfert de la HashMap dans une TreeMap pour trier les dates.
        List<Vue> lotsTotal = new ArrayList<>();
        Map<LocalDate, List<Vue>> treeLot = new TreeMap<>(mapLot);
        Iterator<Entry<LocalDate, List<Vue>>> iter = treeLot.entrySet().iterator();
        StringBuilder builderNom = new StringBuilder();
        StringBuilder builderDate = new StringBuilder();
        List<String> dates = new ArrayList<>();

        // Itération sur la map pour regrouper tous les lots dans la même liste.
        // Crée le nom du fichier sous la forme TEMP yyyy(-yyyy) MMM-MMM-MMM
        while (iter.hasNext())
        {
            Entry<LocalDate, List<Vue>> entry = iter.next();

            // Regroupe tous les lots dans la même liste.
            lotsTotal.addAll(entry.getValue());
            LocalDate clef = entry.getKey();

            builderNom.append(DateConvert.dateFrancais(clef, "MMM"));
            if (iter.hasNext())
                builderNom.append("-");

            String date = DateConvert.dateFrancais(clef, "yyyy");
            if (!dates.contains(date))
            {
                dates.add(date);
                builderDate.append(date);
                if (iter.hasNext())
                    builderDate.append("-");
            }
        }

        if (builderDate.charAt(builderDate.length() - 1) == '-')
            builderDate.deleteCharAt(builderDate.length() - 1);

        String nom = builderNom.toString();
        String date = builderDate.toString();

        // Création de la vue et envoie vers SonarQube
        vueKey = new StringBuilder("MEPMEP").append(date).append(nom).toString();
        String nomVue = new StringBuilder("TEP ").append(date).append(Statics.SPACE).append(nom).toString();
        etapePlus();
        String base = "Vue " + nomVue + Statics.NL;
        updateMessage(base);
        Vue vueParent = creerVue(vueKey, nomVue,
                new StringBuilder("Vue des lots mis en production pendant les mois de ").append(nom).append(Statics.SPACE).append(date).toString(), true);

        // Ajout des sous-vue
        int i =0;
        int size = lotsTotal.size();
        for (Vue vue : lotsTotal)
        {
            updateMessage(base + "ajout : " + vue.getName());
            updateProgress(++i, size);
            api.ajouterSousVue(vue, vueParent);
        }
    }
}
