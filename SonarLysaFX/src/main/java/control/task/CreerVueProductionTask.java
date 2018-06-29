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

import com.ibm.team.repository.common.TeamRepositoryException;

import java.util.Map.Entry;

import control.excel.ControlPic;
import control.excel.ExcelFactory;
import control.rtc.ControlRTC;
import model.enums.EtatLot;
import model.enums.TypeColPic;
import model.sonarapi.Vue;
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
        annulable = true;
    }

    public CreerVueProductionTask(LocalDate dateDebut, LocalDate dateFin)
    {
        super(4);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        annulable = true;
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
    private boolean creerVueProduction() throws IOException
    {
        Map<LocalDate, List<Vue>> mapLot;
        etapePlus();

        // Récupération des données
        Map<String, Vue> mapSonar = recupererLotsSonarQube();
        if (file != null)
        {
            // Traitement données fichier Excel
            ControlPic excel = ExcelFactory.getControlleur(TypeColPic.class, file);

            // Message
            if (isCancelled())
                return false;
            updateMessage("Traitement Fichier Excel...");

            mapLot = excel.recupLotsExcelPourMEP(mapSonar);

            updateMessage("Traitement Fichier Excel OK");
            excel.close();
        }
        else
            mapLot = recupLotRTCPourMEP(dateDebut, dateFin, mapSonar);

        // Création des vues mensuelles ou trimestrielles
        if (mapLot.size() == 1)
            creerVueMensuelle(mapLot);
        else if (mapLot.size() == 3)
            creerVueTrimestrielle(mapLot);
        else
            throw new FunctionalException(Severity.ERROR, "Le fichier Excel donné ou les dates fournies ne sont ni mensuels ni trimetriels.");
        return true;
    }

    /**
     * Récupération des lots RTC pour création des vues.
     * 
     * @param dateDebut
     *            Date limite inférieure pour la livraison à l'édition
     * @param dateFin
     *            Date limite supérieure pour la livraison à l'édition
     * @param mapSonar
     *            Map de tous le slots Sonar
     * @return
     */
    private Map<LocalDate, List<Vue>> recupLotRTCPourMEP(LocalDate dateDebut, LocalDate dateFin, Map<String, Vue> mapSonar)
    {
        Map<LocalDate, List<Vue>> retour = new HashMap<>();

        // Affichage fenêtre
        String base = "Traitement RTC :";
        int size = mapSonar.size();
        int i = 0;

        // Itération sur les lots Sonar
        for (Map.Entry<String, Vue> entry : mapSonar.entrySet())
        {
            Map<EtatLot, LocalDate> map;
            try
            {
                // Récupération des états du lot depuis RTC
                map = ControlRTC.INSTANCE.recupDatesEtatsLot(ControlRTC.INSTANCE.recupWorkItemDepuisId(Integer.valueOf(entry.getKey())));
                updateMessage(base + Statics.NL + "Lot " + entry.getKey());
                updateProgress(++i, size);
            } catch (TeamRepositoryException e)
            {
                Statics.LOGGER.error("Erreur au moment de l'appel RTC pour récupérer un lot : méthode control.task.CreerVueProductionTask.recupLotSonarPourMEP - " + entry.getKey());
                Statics.LOGPLANTAGE.error(e);
                continue;
            }

            // Récupération de la date de livraison à l'édition
            LocalDate date = map.get(EtatLot.EDITION);
            if (date != null && ((date.isAfter(dateDebut) && date.isBefore(dateFin)) || date.isEqual(dateDebut) || date.isEqual(dateFin)))
            {
                // Création d'une nouvelle date au 1er du mois qui servira du clef à la map.
                LocalDate clef = LocalDate.of(date.getYear(), date.getMonth(), 1);
                if (!retour.keySet().contains(clef))
                    retour.put(clef, new ArrayList<>());
                retour.get(clef).add(entry.getValue());
            }
        }

        return retour;
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
        if (isCancelled())
            return;

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
            if (isCancelled())
                return;

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
        if (isCancelled())
            return;

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
        Vue vueParent = creerVue(vueKey, nomVue, new StringBuilder("Vue des lots mis en production pendant les mois de ").append(nom).append(Statics.SPACE).append(date).toString(), true);

        // Ajout des sous-vue
        int i = 0;
        int size = lotsTotal.size();
        for (Vue vue : lotsTotal)
        {
            if (isCancelled())
                return;

            updateMessage(base + "ajout : " + vue.getName());
            updateProgress(++i, size);
            api.ajouterSousVue(vue, vueParent);
        }
    }
}