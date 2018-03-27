package control;

import static utilities.Statics.fichiersXML;
import static utilities.Statics.logSansApp;
import static utilities.Statics.logger;
import static utilities.Statics.loginconnue;
import static utilities.Statics.lognonlistee;
import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;

import javafx.concurrent.Task;
import junit.control.ControlSonarTest;
import model.Anomalie;
import model.LotSuiviPic;
import model.enums.Matiere;
import model.enums.TypeParam;
import sonarapi.SonarAPI;
import sonarapi.model.Composant;
import sonarapi.model.Projet;
import sonarapi.model.QualityGate;
import sonarapi.model.Status;
import sonarapi.model.Vue;
import utilities.DateConvert;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.Utilities;
import utilities.enums.Severity;

public class ControlSonar
{

    /*---------- ATTRIBUTS ----------*/

    private final SonarAPI api;

    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur de base pour l'api Sonar, avec le mot de passe et le l'identifiant
     *
     * @param name
     * @param password
     */
    public ControlSonar(String name, String password)
    {
        if (name == null || name.isEmpty() || password == null)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Pas de connexion au serveur Sonar, merci de vous reconnecter");
        api = new SonarAPI(proprietesXML.getMapParams().get(TypeParam.URLSONAR), name, password);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet de cr�er les vues maintenance depuis un fichier Excel d'extraction de la Pic
     * 
     * @param file
     * @throws InvalidFormatException
     * @throws IOException
     */
    public void creerVueCDM(File file) throws InvalidFormatException, IOException
    {
        // Suprression des vues existantes possibles
        List<String> liste = new ArrayList<>();
        liste.add("2018");
        suppressionVuesMaintenance(true, liste);

        // r�cup�ration des informations du fichier Excel
        ControlPic control = new ControlPic(file);
        Map<String, List<Vue>> map = control.recupLotsCHCCDM();

        // Cr�ation des nouvelles vues
        for (Map.Entry<String, List<Vue>> entry : map.entrySet())
        {
            String key = entry.getKey();
            Vue vue = creerVue(key, key, "Vue de l'edition " + key, true);
            api.ajouterSousVues(entry.getValue(), vue);
        }
    }

    /**
     * Permet de cr�er les vues maintenance CDM depuis Sonar avec le fichier XML
     * 
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void creerVueCDM()
    {
        List<String> liste = new ArrayList<>();
        liste.add("2018");
        suppressionVuesMaintenance(true, liste);

        creerVueMaintenance(true);
    }

    /**
     * Permet de cr�er les vues CHC depuis Sonar avec le fichier XML
     * 
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void creerVueCHC()
    {
        List<String> liste = new ArrayList<>();
        liste.add("2018");
        suppressionVuesMaintenance(false, liste);

        creerVueMaintenance(false);
    }

    /**
     * Permet de cr�er une vue du patrimoine sur une semaine donn�e
     */
    public Task<Object> creerVuePatrimoine()
    {
        return new Task<Object>() {

            @Override
            protected Object call() throws Exception
            {
                // R�cup�ration des composants
                List<Projet> composants = new ArrayList<>(recupererComposantsSonar().values());

                // Date pour r�cup�rer l'ann�e et la semaine
                LocalDate date = LocalDate.now();
                TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

                String nom = "Vue patrimoine " + date.getYear() + " S" + date.get(woy);
                updateMessage("Cr�ation " + nom);
                // Cr�ation de la vue
                Vue vue = creerVue("vue_patrimoine_" + date.getYear() + "_S" + date.get(woy), nom, null, true);

                // Ajout des composants
                int size = composants.size();
                for (int i = 0; i < size; i++)
                {
                    Projet projet = composants.get(i);
                    api.ajouterProjet(projet, vue);
                    updateProgress(i, size);
                    updateMessage(projet.getNom());
                }

                return true;
            }
        };
    }

    /**
     * Cr�e les vues par application des composants dans SonarQube
     */
    @SuppressWarnings("unchecked")
    public void creerVueParApplication()
    {
        // Cr�ation de la liste des composants par application
        Map<String, List<Projet>> mapApplication;

        if (ControlSonarTest.deser)
            mapApplication = Utilities.deserialisation("d:\\mapApplis.ser", HashMap.class);
        else
        {
            mapApplication = controlerSonarQube();
            Utilities.serialisation("d:\\mapApplis.ser", mapApplication);
        }

        // Suppression des vues existantes
        List<Projet> listeVuesExistantes = api.getVuesParNom("APPLI MASTER ");
        for (Projet projet : listeVuesExistantes)
        {
            api.supprimerProjet(projet.getKey(), true);
        }

        // Parcours de la liste pour cr�er chaque vue applicative avec ses composants
        for (Map.Entry<String, List<Projet>> entry : mapApplication.entrySet())
        {
            // Cr�ation de la vue principale
            Vue vue = creerVue("APPMASTERAPP" + entry.getKey(), "APPLI MASTER " + entry.getKey(), "Liste des composants de l'application " + entry.getKey(), false);
            api.ajouterSousProjets(entry.getValue(), vue);
        }
    }

    /**
     * Remonte les composants par application de SonarQube et cr��e les logs sur les defaults.
     *
     * @return map des composants SonarQube par application
     */
    public Map<String, List<Projet>> controlerSonarQube()
    {
        // R�cup�ration des composants Sonar
        Map<String, Projet> mapProjets = recupererComposantsSonar();
        return creerMapApplication(mapProjets);
    }

    /**
     * Permet de cr�er les vues mensuelles et trimestrielle des composants mis en production
     *
     * @param file
     * @throws InvalidFormatException
     * @throws IOException
     */
    public void creerVueProduction(File file) throws InvalidFormatException, IOException
    {
        ControlPic excel = new ControlPic(file);
        Map<LocalDate, List<Vue>> mapLot = excel.recupLotsExcelPourMEP(recupererLotsSonarQube());
        excel.close();

        if (mapLot.size() == 1)
            creerVueMensuelle(mapLot);
        else if (mapLot.size() == 3)
            creerVueTrimestrielle(mapLot);
    }

    /**
     * M�thode de traitement pour mettre � jour les fichiers de suivi d'anomalies ainsi que la cr�ation de vue dans SonarQube. <br>
     * Retourne une liste de toutes les anomalies trait�es depuis Sonar
     *
     * @param composants
     * @param java
     * @param versions
     * @throws InvalidFormatException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public List<String> traitementFichierSuivi(Map<String, List<Projet>> composants, String fichier, Matiere matiere) throws InvalidFormatException, IOException
    {
        // 1. R�cup�ration des donn�es depuis les fichiers Excel.

        // Fichier des lots �dition
        Map<String, LotSuiviPic> lotsPIC = fichiersXML.getLotsPic();

        // 2. R�cup�ration des lots Sonar en erreur.
        Map<String, Set<String>> mapLotsSonar;

        Set<String> lotsSecurite = new HashSet<>();
        Set<String> lotRelease = new HashSet<>();
        List<String> retour = new ArrayList<>();

        if (ControlSonarTest.deser)
        {
            mapLotsSonar = Utilities.deserialisation("d:\\lotsSonar.ser", HashMap.class);
            lotsSecurite = Utilities.deserialisation("d:\\lotsSecurite.ser", HashSet.class);
            lotRelease = Utilities.deserialisation("d:\\lotsRelease.ser", HashSet.class);
        }
        else
        {
            mapLotsSonar = lotSonarQGError(composants, lotsSecurite, lotRelease);
            Utilities.serialisation("d:\\lotsSecurite.ser", lotsSecurite);
            Utilities.serialisation("d:\\lotsSonar.ser", mapLotsSonar);
            Utilities.serialisation("d:\\lotsRelease.ser", lotRelease);
        }

        // 3. Supression des lots d�j� cr��s et cr�ation des feuille Excel avec les nouvelles erreurs
        majFichierAnomalies(lotsPIC, mapLotsSonar, lotsSecurite, lotRelease, fichier, matiere);

        // 4. Cr�ation des vues
        for (Map.Entry<String, Set<String>> entry : mapLotsSonar.entrySet())
        {
            // Cr�ation de la vue et envoie vers SonarQube
            String nom = testNom(fichier);
            Vue vueParent = creerVue(nom.replace(" ", "") + "Key" + entry.getKey(), nom + " - Edition " + entry.getKey(), "Vue regroupant tous les lots avec des composants en erreur", true);

            for (String lot : entry.getValue())
            {
                // Ajout des sous-vue
                api.ajouterSousVue(new Vue("view_lot_" + lot, "Lot " + lot), vueParent);
                retour.add("Lot " + lot);
            }
        }
        return retour;
    }

    private String testNom(String fichier)
    {
        return fichier.replace("_", " ").split("\\.")[0];
    }

    /**
     * Mise � jour du fichier Excel des suivis d'anomalies pour les composants Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     */
    public List<String> majFichierSuiviExcelDataStage() throws InvalidFormatException, IOException
    {
        // Appel de la r�cup�ration des composants datastage avec les vesions en param�tre
        Map<String, List<Projet>> composants = recupererComposantsSonarVersion(true);

        // Mise � jour des liens des compoasnts datastage avec le bon QG
        liensQG(composants, proprietesXML.getMapParams().get(TypeParam.NOMQGDATASTAGE));

        // Traitement du fichier datastage de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(TypeParam.NOMFICHIERDATASTAGE), Matiere.DATASTAGE);
    }

    /**
     * Mise � jour du fichier Excel des suivis d'anomalies pour tous les composants non Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     */
    public List<String> majFichierSuiviExcel() throws InvalidFormatException, IOException
    {
        // Appel de la r�cup�ration des composants non datastage avec les vesions en param�tre
        Map<String, List<Projet>> composants = recupererComposantsSonarVersion(false);

        // Traitement du fichier dtastage de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(TypeParam.NOMFICHIER), Matiere.JAVA);
    }

    public void traitementSuiviExcelToutFichiers() throws InvalidFormatException, IOException
    {
        // R�cup�ration anomalies Datastage
        List<String> anoDatastage = majFichierSuiviExcelDataStage();
        // R�cup�ration anomalies Java
        List<String> anoJava = majFichierSuiviExcel();
        // Liste des anomalies sur plusieures mati�res
        List<String> anoMultiple = new ArrayList<>();
        for (String string : anoJava)
        {
            if (anoDatastage.contains(string))
                anoMultiple.add(string);
        }

        // Mise � jour des fichiers Excel
        ControlAno controlAnoJava = new ControlAno(new File(proprietesXML.getMapParams().get(TypeParam.ABSOLUTEPATH) + proprietesXML.getMapParams().get(TypeParam.NOMFICHIER)));
        ControlAno controlAnoDataStage = new ControlAno(new File(proprietesXML.getMapParams().get(TypeParam.ABSOLUTEPATH) + proprietesXML.getMapParams().get(TypeParam.NOMFICHIERDATASTAGE)));
        controlAnoJava.majMultiMatiere(anoMultiple);
        controlAnoDataStage.majMultiMatiere(anoMultiple);
    }

    /**
     * Cr�e une vue avec tous les composants Datastage
     */
    public void creerVuesDatastage()
    {
        // Appel du webservice pour remonter tous les composants
        List<Projet> projets = api.getComposants();
        Vue vue = creerVue("DSDataStageListeKey", "Liste Composants Datastage", "Vue regroupant tous les composants Datastage", true);
        for (Projet projet : projets)
        {
            if (projet.getNom().startsWith("Composant DS_"))
                api.ajouterProjet(projet, vue);
        }
    }

    /**
     * Lance la mise � jour des vues dans SonarQube. Indispenssable apr�s la cr�ation d'une nouvelle vue.
     */
    public void majVues()
    {
        api.majVues();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * R�cup�re tous les lots cr��s dans Sonar.
     *
     * @return
     */
    private Map<String, Vue> recupererLotsSonarQube()
    {
        Map<String, Vue> map = new HashMap<>();
        List<Vue> views = api.getVues();
        for (Vue view : views)
        {
            if (view.getName().startsWith("Lot "))
            {
                map.put(view.getName().substring(4), view);
            }
        }
        return map;
    }

    /**
     * Permet de r�cup�rer la derni�re version de chaque composants cr��s dans Sonar
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Projet> recupererComposantsSonar()
    {
        // Appel du webservice pour remonter tous les composants
        List<Projet> projets;

        if (ControlSonarTest.deser)
        {
            projets = Utilities.deserialisation("d:\\composants.ser", List.class);
        }
        else
        {
            projets = api.getComposants();
            Utilities.serialisation("d:\\composants.ser", projets);
        }

        // Triage ascendant de la liste par nom de projet
        projets.sort((o1, o2) -> o1.getNom().compareTo(o2.getNom()));

        // Cr�ation de la regex pour retirer les num�ros de version des composants
        Pattern pattern = Pattern.compile("^\\D*");

        // Cr�ation de la map de retour et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine
        // de caract�res cr��es par la regex comme clef dans la map.
        // Les compossant �tant tri�s par ordre alphab�tique, on va �craser tous les composants qui ont un num�ro de version obsol�te.
        Map<String, Projet> retour = new HashMap<>();

        for (Projet projet : projets)
        {
            Matcher matcher = pattern.matcher(projet.getNom());
            if (matcher.find())
            {
                retour.put(matcher.group(0), projet);
            }
        }
        return retour;
    }

    /**
     * Permet de r�cup�rer les composants de Sonar tri�s par version avec s�pration des composants datastage
     *
     * @return
     */
    private Map<String, List<Projet>> recupererComposantsSonarVersion(Boolean datastage)
    {
        // R�cup�ration des versions en param�tre
        String[] versions = proprietesXML.getMapParams().get(TypeParam.VERSIONS).split("-");

        // Appel du webservice pour remonter tous les composants
        List<Projet> projets = api.getComposants();

        // Cr�ation de la map de retour en utilisant les versions donn�es
        Map<String, List<Projet>> retour = new HashMap<>();

        for (String version : versions)
        {
            retour.put(version, new ArrayList<>());
        }

        // It�ration sur les projets pour remplir la liste de retour
        for (Projet projet : projets)
        {
            for (String version : versions)
            {
                // Pour chaque version, on teste si le composant fait parti de celle-ci. par ex : composant 15 dans version E32
                if (projet.getNom().endsWith(Utilities.transcoEdition(version)))
                {
                    // Selon que l'on regarde les composants datastage ou non, on remplie la liste en cons�quence en utilisant le filtre en param�tre. Si le Boolean est nul, on
                    // prend tous les composants
                    String filtre = proprietesXML.getMapParams().get(TypeParam.FILTREDATASTAGE);
                    if (datastage == null || datastage && projet.getNom().startsWith(filtre) || !datastage && !projet.getNom().startsWith(filtre))
                        retour.get(version).add(projet);
                }
            }
        }
        return retour;
    }

    /**
     * R�cup�re tous les composants Sonar des versions choisies avec une qualityGate en erreur.<br>
     * la clef de la map correspond � la version, et la valeur, � la liste des lots en erreur de cette version.
     *
     * @param versions
     * @return
     */
    private Map<String, Set<String>> lotSonarQGError(Map<String, List<Projet>> composants, Set<String> lotSecurite, Set<String> lotRelease)
    {
        // Cr�ation de la map de retour
        HashMap<String, Set<String>> retour = new HashMap<>();

        // It�ration sur les composants pour remplir la map de retour avec les lot en erreur par version
        for (Map.Entry<String, List<Projet>> entry : composants.entrySet())
        {
            String entryKey = entry.getKey();
            retour.put(entryKey, new TreeSet<>());

            // Iteration sur la liste des projets
            for (Projet projet : entry.getValue())
            {
                traitementProjet(projet, retour, entryKey, lotSecurite, lotRelease);
            }
        }
        return retour;
    }

    /**
     * Taritement Sonar d'un projet
     *
     * @param projet
     * @param retour
     * @param entryKey
     * @param lotSecurite
     * @param lotRelease
     */
    private void traitementProjet(Projet projet, HashMap<String, Set<String>> retour, String entryKey, Set<String> lotSecurite, Set<String> lotRelease)
    {
        String key = projet.getKey();
        // R�cup�ration du composant
        Composant composant = api.getMetriquesComposant(key, new String[] { "lot", "alert_status" });

        // R�cup�ration depuis la map des m�triques du num�ro de lot et du status de la Quality Gate
        Map<String, String> metriques = composant.getMapMetriques();
        String lot = metriques.get("lot");
        String alert = metriques.get("alert_status");

        // Si le lot a un Quality Gate en Erreur, on le rajoute � la liste et on contr�le aussi les erreurs de s�curit�.
        // S'il y en a on le rajoute aussi � la liste des lots avec des probl�mesde s�curit�.
        if (alert != null && Status.getStatus(alert) == Status.ERROR && lot != null && !lot.isEmpty())
        {
            // Ajout du lot � la liste de retour
            retour.get(entryKey).add(lot);

            // Contr�le pour v�rifier si le composant � une erreur de s�curit�, ce qui ajout le lot � la listeSecurite
            if (api.getSecuriteComposant(key) > 0)
                lotSecurite.add(lot);

            // Contr�le du composant pour voir s'il a une version release ou SNAPSHOT
            if (release(key))
                lotRelease.add(lot);
        }
    }

    /**
     * Test si un composant a une version release ou snapshot.
     *
     * @param key
     * @return
     */
    private boolean release(String key)
    {
        String version = api.getVersionComposant(key);
        return !version.contains("SNAPSHOT");
    }

    /**
     * Cr�e une map de toutes les apllications dans Sonar avec pour chacunes la liste des composants li�s.
     *
     * @param mapProjets
     * @return
     */
    private HashMap<String, List<Projet>> creerMapApplication(Map<String, Projet> mapProjets)
    {
        // Initialisation de la map
        HashMap<String, List<Projet>> mapApplications = new HashMap<>();

        // It�ration sur la liste des projets
        for (Projet projet : mapProjets.values())
        {
            // R�cup�ration du code application
            Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] { "application" });

            // Test si la liste est vide, cela veut dire que le projet n'a pas de code application.
            if (!composant.getMetriques().isEmpty())
            {
                String application = composant.getMetriques().get(0).getValue().trim().toUpperCase();

                // Si l'application n'est pas dans la PIC, on continue au projet suivant.
                if (!testAppli(application, projet.getNom()))
                {
                    continue;
                }

                // Mise � jour de la map de retour avec en clef, le code application et en valeur : la liste des projets li�s.
                if (mapApplications.keySet().contains(application))
                {
                    mapApplications.get(application).add(projet);
                }
                else
                {
                    List<Projet> liste = new ArrayList<>();
                    liste.add(projet);
                    mapApplications.put(application, liste);
                }
            }
            else
            {
                logSansApp.warn("Application non renseign�e - Composant : " + projet.getNom());
            }
        }
        return mapApplications;
    }

    /**
     * V�rifie qu'une application d'un composant Sonar est pr�sente dans la liste des applications de la PIC.
     *
     * @param application
     *            Application enregistr�e pour le composant dans Sonar.
     * @param nom
     *            Nom du composant Sonar.
     */
    private boolean testAppli(String application, String nom)
    {
        if (application.equals(Statics.INCONNUE))
        {
            loginconnue.warn("Application : INCONNUE - Composant : " + nom);
            return false;
        }

        Map<String, Boolean> vraiesApplis = fichiersXML.getMapApplis();

        if (vraiesApplis.keySet().contains(application))
        {
            if (vraiesApplis.get(application))
            {
                return true;
            }
            lognonlistee.warn("Application obsol�te : " + application + " - composant : " + nom);
            return false;
        }
        lognonlistee.warn("Application n'existant pas dans le r�f�renciel : " + application + " - composant : " + nom);
        return false;
    }

    /**
     * Cr�e la vue Sonar pour une recherche trimetrielle des composants mis en production .
     *
     * @param mapLot
     */
    private void creerVueTrimestrielle(Map<LocalDate, List<Vue>> mapLot)
    {
        // Cr�ation des variables. Transfert de la HashMap dans une TreeMap pour trier les dates.
        List<Vue> lotsTotal = new ArrayList<>();
        Map<LocalDate, List<Vue>> treeLot = new TreeMap<>(mapLot);
        Iterator<Entry<LocalDate, List<Vue>>> iter = treeLot.entrySet().iterator();
        StringBuilder builderNom = new StringBuilder();
        StringBuilder builderDate = new StringBuilder();
        List<String> dates = new ArrayList<>();

        // It�ration sur la map pour regrouper tous les lots dans la m�me liste.
        // Cr�e le nom du fichier sous la forme TEMP MMM-MMM-MMM yyyy(-yyyy)
        while (iter.hasNext())
        {
            Entry<LocalDate, List<Vue>> entry = iter.next();

            // Regroupe tous les lots dans la m�me liste.
            lotsTotal.addAll(entry.getValue());
            LocalDate clef = entry.getKey();

            builderNom.append(DateConvert.dateFrancais(clef, "MMM"));
            if (iter.hasNext())
            {
                builderNom.append("-");
            }

            String date = DateConvert.dateFrancais(clef, "yyyy");
            if (!dates.contains(date))
            {
                dates.add(date);
                builderDate.append(date);
                if (iter.hasNext())
                {
                    builderDate.append("-");
                }
            }
        }

        if (builderDate.charAt(builderDate.length() - 1) == '-')
        {
            builderDate.deleteCharAt(builderDate.length() - 1);
        }

        String nom = builderNom.toString();
        String date = builderDate.toString();

        // Cr�ation de la vue et envoie vers SonarQube
        Vue vue = creerVue(new StringBuilder("MEPMEP").append(nom).append(date).toString(), new StringBuilder("TEP ").append(nom).append(Statics.SPACE).append(date).toString(),
                new StringBuilder("Vue des lots mis en production pendant les mois de ").append(nom).append(Statics.SPACE).append(date).toString(), true);

        // Ajout des sous-vue
        api.ajouterSousVues(lotsTotal, vue);
    }

    /**
     * Cr�e la vue Sonar pour une recherche mensuelle des composants mis en production .
     *
     * @param mapLot
     */
    private void creerVueMensuelle(final Map<LocalDate, List<Vue>> mapLot)
    {
        Iterator<Entry<LocalDate, List<Vue>>> iter = mapLot.entrySet().iterator();
        Entry<LocalDate, List<Vue>> entry = iter.next();
        String nomVue = new StringBuilder("MEP ").append(DateConvert.dateFrancais(entry.getKey(), "MMMM yyyy")).toString();

        // Cr�ation de la vue principale
        Vue vue = creerVue(new StringBuilder("MEPMEP").append(DateConvert.dateFrancais(entry.getKey(), "MMMMyyyy")).append("Key").toString(), nomVue,
                new StringBuilder("Vue des lots mis en production pendant le mois de ").append(entry.getKey()).toString(), true);

        // Ajout des sous-vue
        api.ajouterSousVues(entry.getValue(), vue);
    }

    /**
     * Cr�e une vue dans Sonar avec suppression ou non de la vue pr�c�dente.
     *
     * @param key
     * @param name
     * @param description
     * @param suppression
     * @return
     */
    private Vue creerVue(String key, String name, String description, boolean suppression)
    {
        // Contr�le
        if (key == null || key.isEmpty() || name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("Le nom et la clef de la vue sont obligatoires");
        }

        // Cr�ation de la vue
        Vue vue = new Vue();
        vue.setKey(key);
        vue.setName(name);

        // Ajout de la description si elle est valoris�e
        if (description != null)
        {
            vue.setDescription(description);
        }

        // Suppresison de la vue pr�cedente
        if (suppression)
        {
            api.supprimerProjet(vue, false);
        }

        // Appel de l'API Sonar
        api.creerVue(vue);

        return vue;
    }

    /**
     * Permet de lier tous les composants sonar � une QG particuli�re
     *
     * @param composants
     * @param nomQG
     */
    private void liensQG(Map<String, List<Projet>> composants, String nomQG)
    {
        // R�cup�ration de l'Id de la QualityGate
        QualityGate qg = api.getQualityGate(nomQG);

        // Iteration sur tous les composants pour les associer au QualityGate
        for (Map.Entry<String, List<Projet>> entry : composants.entrySet())
        {
            for (Projet projet : entry.getValue())
            {
                api.associerQualitygate(projet, qg);
            }
        }

    }

    /**
     * Permet de mettre � jour le fichier des anomalies Sonar, en allant chercher les nouvelles dans Sonar et en v�rifiant celles qui ne sont plus d'actualit�.
     *
     * @param mapLotsPIC
     *            Fichier excel d'extraction de la PIC de tous les lots.
     * @param mapLotsSonar
     *            map des lots Sonar avec une quality Gate en erreur
     * @param lotsSecurite
     * @param lotRelease
     * @param matiere
     * @throws InvalidFormatException
     * @throws IOException
     */
    private void majFichierAnomalies(Map<String, LotSuiviPic> mapLotsPIC, Map<String, Set<String>> mapLotsSonar, Set<String> lotsSecurite, Set<String> lotRelease, String fichier, Matiere matiere)
            throws InvalidFormatException, IOException
    {
        // Controleur
        ControlAno controlAno = new ControlAno(new File(proprietesXML.getMapParams().get(TypeParam.ABSOLUTEPATH) + fichier));

        // Lecture du fichier pour remonter les anomalies en cours.
        List<Anomalie> listeLotenAno = controlAno.listAnomaliesSurLotsCrees();

        // Cr�ation de la liste des lots d�j� dans le fichier
        List<String> lotsDejaDansFichier = creationNumerosLots(listeLotenAno, mapLotsPIC);

        // Liste des anomalies � ajouter apr�s traitement
        List<Anomalie> anoAajouter = new ArrayList<>();

        // Mise dans un Set de tous les lots en erreur venus de Sonar ind�pendement de la version des composants.
        Set<String> lotsEnErreur = new TreeSet<>();
        for (Set<String> value : mapLotsSonar.values())
        {
            lotsEnErreur.addAll(value);
        }

        // It�ration sur les lots en erreurs venant de Sonar pour chaque version de composants (13, 14, ...)
        for (Entry<String, Set<String>> entry : mapLotsSonar.entrySet())
        {
            List<Anomalie> anoACreer = new ArrayList<>();
            List<Anomalie> anoDejacrees = new ArrayList<>();

            // Iteration sur toutes les anomalies venant de Sonar pour chaque version
            for (String numeroLot : entry.getValue())
            {

                // On va chercher les informations de ce lot dans le fichier des lots de la PIC. Si on ne les trouve pas, il faudra mettre � jour ce fichier
                LotSuiviPic lot = mapLotsPIC.get(numeroLot);
                if (lot == null)
                {
                    lognonlistee.warn("Mettre � jour le fichier Pic - Lots : " + numeroLot + " non list�");
                    continue;
                }
                Anomalie ano = new Anomalie(lot);

                // On ajoute le lot soit, dans la liste des anos d�j� cr��es soit, dans celle des anos � cr�er.
                if (lotsDejaDansFichier.contains(numeroLot))

                    anoDejacrees.add(ano);
                else
                    anoACreer.add(ano);
            }

            // Mise � jour de la feuille des anomalies pour chaque version de composants
            anoAajouter.addAll(controlAno.createSheetError(entry.getKey(), anoACreer, anoDejacrees));
        }

        // Sauvegarde fichier et maj feuille principale
        Sheet sheet = controlAno.sauvegardeFichier(fichier);

        // Mis � jour de la feuille principale
        controlAno.majFeuillePrincipale(listeLotenAno, anoAajouter, lotsEnErreur, lotsSecurite, lotRelease, sheet, matiere);

        // Fermeture controleur
        controlAno.close();
    }

    /**
     * Permet de cr�er la liste des num�ros de lots d�j� en anomalie et met � jour les {@code Anomalie} depuis les infos de la Pic
     *
     * @param listeLotenAno
     *            liste des {@code Anomalie} d�j� connues
     * @param mapLotsPIC
     *            map des lots connus de la Pic.
     * @return
     */
    private List<String> creationNumerosLots(List<Anomalie> listeLotenAno, Map<String, LotSuiviPic> mapLotsPIC)
    {
        List<String> retour = new ArrayList<>();

        // Iteration sur la liste des anomalies
        for (Anomalie ano : listeLotenAno)
        {
            String string = ano.getLot();

            if (string.startsWith("Lot "))
                string = string.substring(4);

            // Mise � jour des donn�es depuis la PIC
            LotSuiviPic lotPic = mapLotsPIC.get(string);

            if (lotPic != null)
                ano.majDepuisPic(lotPic);
            else
                logger.warn("Un lot du fichier Excel n'est pas connu dans la Pic : " + string);

            retour.add(string);
        }
        return retour;
    }

    /**
     * 
     * @param cdm
     * @param annees
     */
    private void suppressionVuesMaintenance(boolean cdm, List<String> annees)
    {
        String base;
        // On it�re sur chacune des ann�es
        for (String annee : annees)
        {
            // pr�paration de la base de la clef
            if (cdm)
                base = "CHC_CDM" + annee;
            else
                base = "CHC" + annee;

            // Suprression des vues existantes possibles
            for (int i = 1; i < 53; i++)
            {
                api.supprimerProjet(new StringBuilder(base).append("-S").append(String.format("%02d", i)).append("Key").toString(), false);
            }
        }
    }

    /**
     * Cr�e les vues CHC ou CDM depuis Sonar et le fichier XML. {@code true} pour les vues CDM et {@code false} pour les vues CHC
     * 
     * @param cdm
     */
    private void creerVueMaintenance(boolean cdm)
    {
        // R�cup�ration du fichier XML des editions
        Map<String, String> mapEditions;
        if (cdm)
            mapEditions = fichiersXML.getMapCDM();
        else
            mapEditions = fichiersXML.getMapCHC();

        Map<String, Set<String>> mapVuesACreer = new HashMap<>();

        Map<String, List<Projet>> mapProjets = recupererComposantsSonarVersion(null);

        // Transfert de la map en une liste avec tous ls projets
        List<Projet> tousLesProjets = new ArrayList<>();
        for (List<Projet> projets : mapProjets.values())
        {
            tousLesProjets.addAll(projets);
        }

        for (Projet projet : tousLesProjets)
        {
            // R�cup�ration de l'�dition du composant sou forme num�rique xx.yy.zz.tt et du num�ro de lot
            Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] { "edition", "lot" });

            // R�cup�ration depuis la map des m�triques du num�ro de lot et du status de la Quality Gate
            Map<String, String> metriques = composant.getMapMetriques();
            String lot = metriques.get("lot");
            String edition = metriques.get("edition");

            // V�rification qu'on a bien un num�ro de lot et que dans le fichier XML, l'�dition du composant est pr�sente
            if (lot != null && !lot.isEmpty() && edition != null && mapEditions.keySet().contains(edition))
            {
                String keyCHC = mapEditions.get(edition);
                if (!mapVuesACreer.keySet().contains(keyCHC))
                    mapVuesACreer.put(keyCHC, new HashSet<>());
                mapVuesACreer.get(keyCHC).add(lot);
            }
        }

        for (Map.Entry<String, Set<String>> entry : mapVuesACreer.entrySet())
        {
            Vue parent = new Vue(entry.getKey() + "Key", entry.getKey());
            api.creerVue(parent);
            for (String lot : entry.getValue())
            {
                api.ajouterSousVue(new Vue("view_lot_" + lot, "Lot " + lot), parent);
            }
        }
    }

    /*---------- ACCESSEURS ----------*/
}