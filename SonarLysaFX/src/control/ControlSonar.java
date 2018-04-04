package control;

import static utilities.Statics.fichiersXML;
import static utilities.Statics.info;
import static utilities.Statics.logger;
import static utilities.Statics.lognonlistee;
import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;

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
import utilities.FunctionalException;
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
    
    /**
     * Constructeur depuis les données de l'utilisateur
     *
     * @param name
     * @param password
     */
    public ControlSonar()
    {
        if (!info.controle())
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Pas de connexion au serveur Sonar, merci de vous reconnecter");
        api = new SonarAPI(proprietesXML.getMapParams().get(TypeParam.URLSONAR), info.getPseudo(), info.getMotDePasse());
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Méthode de traitement pour mettre à jour les fichiers de suivi d'anomalies ainsi que la création de vue dans
     * SonarQube. <br>
     * Retourne une liste de toutes les anomalies traitées depuis Sonar
     *
     * @param composants
     * @param java
     * @param versions
     * @throws InvalidFormatException
     * @throws IOException
     */
    @SuppressWarnings ("unchecked")
    private List<String> traitementFichierSuivi(Map<String, List<Projet>> composants, String fichier, Matiere matiere) throws InvalidFormatException, IOException
    {
        // 1. Récupération des données depuis les fichiers Excel.

        // Fichier des lots édition
        Map<String, LotSuiviPic> lotsPIC = fichiersXML.getLotsPic();

        // 2. Récupération des lots Sonar en erreur.
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

        // 3. Supression des lots déjà créés et création des feuille Excel avec les nouvelles erreurs
        majFichierAnomalies(lotsPIC, mapLotsSonar, lotsSecurite, lotRelease, fichier, matiere);

        // 4. Création des vues
        for (Map.Entry<String, Set<String>> entry : mapLotsSonar.entrySet())
        {
            // Création de la vue et envoie vers SonarQube
            String nom = prepareNom(fichier);
            Vue vueParent = creerVue(nom.replace(" ", "") + "Key" + entry.getKey(), nom + " - Edition " + entry.getKey(),
                    "Vue regroupant tous les lots avec des composants en erreur", true);

            for (String lot : entry.getValue())
            {
                // Ajout des sous-vue
                api.ajouterSousVue(new Vue("view_lot_" + lot, "Lot " + lot), vueParent);
                retour.add("Lot " + lot);
            }
        }
        return retour;
    }

    private String prepareNom(String fichier)
    {
        return fichier.replace("_", " ").split("\\.")[0];
    }

    /**
     * Mise à jour du fichier Excel des suivis d'anomalies pour les composants Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     */
    public List<String> majFichierSuiviExcelDataStage() throws InvalidFormatException, IOException
    {
        // Appel de la récupération des composants datastage avec les vesions en paramètre
        Map<String, List<Projet>> composants = recupererComposantsSonarVersion(true);

        // Mise à jour des liens des compoasnts datastage avec le bon QG
        liensQG(composants, proprietesXML.getMapParams().get(TypeParam.NOMQGDATASTAGE));

        // Traitement du fichier datastage de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(TypeParam.NOMFICHIERDATASTAGE), Matiere.DATASTAGE);
    }

    /**
     * Mise à jour du fichier Excel des suivis d'anomalies pour tous les composants non Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     */
    public List<String> majFichierSuiviExcel() throws InvalidFormatException, IOException
    {
        // Appel de la récupération des composants non datastage avec les vesions en paramètre
        Map<String, List<Projet>> composants = recupererComposantsSonarVersion(false);

        // Traitement du fichier dtastage de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(TypeParam.NOMFICHIER), Matiere.JAVA);
    }

    public void traitementSuiviExcelToutFichiers() throws InvalidFormatException, IOException
    {
        // Récupération anomalies Datastage
        List<String> anoDatastage = majFichierSuiviExcelDataStage();
        // Récupération anomalies Java
        List<String> anoJava = majFichierSuiviExcel();
        // Liste des anomalies sur plusieures matières
        List<String> anoMultiple = new ArrayList<>();
        for (String string : anoJava)
        {
            if (anoDatastage.contains(string))
                anoMultiple.add(string);
        }

        // Mise à jour des fichiers Excel
        ControlAno controlAnoJava = new ControlAno(new File(proprietesXML.getMapParams().get(TypeParam.ABSOLUTEPATH) + proprietesXML.getMapParams().get(TypeParam.NOMFICHIER)));
        ControlAno controlAnoDataStage = new ControlAno(
                new File(proprietesXML.getMapParams().get(TypeParam.ABSOLUTEPATH) + proprietesXML.getMapParams().get(TypeParam.NOMFICHIERDATASTAGE)));
        controlAnoJava.majMultiMatiere(anoMultiple);
        controlAnoDataStage.majMultiMatiere(anoMultiple);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Permet de récupérer les composants de Sonar triés par version avec sépration des composants datastage
     *
     * @return
     */
    private Map<String, List<Projet>> recupererComposantsSonarVersion(Boolean datastage)
    {
        // Récupération des versions en paramètre
        String[] versions = proprietesXML.getMapParams().get(TypeParam.VERSIONS).split("-");

        // Appel du webservice pour remonter tous les composants
        @SuppressWarnings("unchecked")
        List<Projet> projets = Utilities.recuperation(ControlSonarTest.deser, List.class, "d:\\composants.ser", () -> api.getComposants());

        // Création de la map de retour en utilisant les versions données
        Map<String, List<Projet>> retour = new HashMap<>();

        for (String version : versions)
        {
            retour.put(version, new ArrayList<>());
        }

        // Itération sur les projets pour remplir la liste de retour
        for (Projet projet : projets)
        {
            for (String version : versions)
            {
                // Pour chaque version, on teste si le composant fait parti de celle-ci. par ex : composant 15 dans
                // version E32
                if (projet.getNom().endsWith(Utilities.transcoEdition(version)))
                {
                    // Selon que l'on regarde les composants datastage ou non, on remplie la liste en conséquence en
                    // utilisant le filtre en paramètre. Si le Boolean est nul, on
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
     * Récupère tous les composants Sonar des versions choisies avec une qualityGate en erreur.<br>
     * la clef de la map correspond à la version, et la valeur, à la liste des lots en erreur de cette version.
     *
     * @param versions
     * @return
     */
    private Map<String, Set<String>> lotSonarQGError(Map<String, List<Projet>> composants, Set<String> lotSecurite, Set<String> lotRelease)
    {
        // Création de la map de retour
        HashMap<String, Set<String>> retour = new HashMap<>();

        // Itération sur les composants pour remplir la map de retour avec les lot en erreur par version
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
     * Traitement Sonar d'un projet
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
        // Récupération du composant
        Composant composant = api.getMetriquesComposant(key, new String[] { "lot", "alert_status" });

        // Récupération depuis la map des métriques du numéro de lot et du status de la Quality Gate
        Map<String, String> metriques = composant.getMapMetriques();
        String lot = metriques.get("lot");
        String alert = metriques.get("alert_status");

        // Si le lot a un Quality Gate en Erreur, on le rajoute à la liste et on contrôle aussi les erreurs de sécurité.
        // S'il y en a on le rajoute aussi à la liste des lots avec des problèmesde sécurité.
        if (alert != null && Status.getStatus(alert) == Status.ERROR && lot != null && !lot.isEmpty())
        {
            // Ajout du lot à la liste de retour
            retour.get(entryKey).add(lot);

            // Contrôle pour vérifier si le composant à une erreur de sécurité, ce qui ajout le lot à la listeSecurite
            if (api.getSecuriteComposant(key) > 0)
                lotSecurite.add(lot);

            // Contrôle du composant pour voir s'il a une version release ou SNAPSHOT
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
     * Crée une vue dans Sonar avec suppression ou non de la vue précédente.
     *
     * @param key
     * @param name
     * @param description
     * @param suppression
     * @return
     */
    private Vue creerVue(String key, String name, String description, boolean suppression)
    {
        // Contrôle
        if (key == null || key.isEmpty() || name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("Le nom et la clef de la vue sont obligatoires");
        }

        // Création de la vue
        Vue vue = new Vue();
        vue.setKey(key);
        vue.setName(name);

        // Ajout de la description si elle est valorisée
        if (description != null)
        {
            vue.setDescription(description);
        }

        // Suppresison de la vue précedente
        if (suppression)
        {
            api.supprimerProjet(vue, false);
        }

        // Appel de l'API Sonar
        api.creerVue(vue);

        return vue;
    }

    /**
     * Permet de lier tous les composants sonar à une QG particulière
     *
     * @param composants
     * @param nomQG
     */
    private void liensQG(Map<String, List<Projet>> composants, String nomQG)
    {
        // Récupération de l'Id de la QualityGate
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
     * Permet de mettre à jour le fichier des anomalies Sonar, en allant chercher les nouvelles dans Sonar et en
     * vérifiant celles qui ne sont plus d'actualité.
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
    private void majFichierAnomalies(Map<String, LotSuiviPic> mapLotsPIC, Map<String, Set<String>> mapLotsSonar, Set<String> lotsSecurite, Set<String> lotRelease, String fichier,
            Matiere matiere) throws InvalidFormatException, IOException
    {
        // Controleur
        ControlAno controlAno = new ControlAno(new File(proprietesXML.getMapParams().get(TypeParam.ABSOLUTEPATH) + fichier));

        // Lecture du fichier pour remonter les anomalies en cours.
        List<Anomalie> listeLotenAno = controlAno.listAnomaliesSurLotsCrees();

        // Création de la liste des lots déjà dans le fichier
        List<String> lotsDejaDansFichier = creationNumerosLots(listeLotenAno, mapLotsPIC);

        // Liste des anomalies à ajouter après traitement
        List<Anomalie> anoAajouter = new ArrayList<>();

        // Mise dans un Set de tous les lots en erreur venus de Sonar indépendement de la version des composants.
        Set<String> lotsEnErreur = new TreeSet<>();
        for (Set<String> value : mapLotsSonar.values())
        {
            lotsEnErreur.addAll(value);
        }

        // Itération sur les lots en erreurs venant de Sonar pour chaque version de composants (13, 14, ...)
        for (Entry<String, Set<String>> entry : mapLotsSonar.entrySet())
        {
            List<Anomalie> anoACreer = new ArrayList<>();
            List<Anomalie> anoDejacrees = new ArrayList<>();

            // Iteration sur toutes les anomalies venant de Sonar pour chaque version
            for (String numeroLot : entry.getValue())
            {

                // On va chercher les informations de ce lot dans le fichier des lots de la PIC. Si on ne les trouve
                // pas, il faudra mettre à jour ce fichier
                LotSuiviPic lot = mapLotsPIC.get(numeroLot);
                if (lot == null)
                {
                    lognonlistee.warn("Mettre à jour le fichier Pic - Lots : " + numeroLot + " non listé");
                    continue;
                }
                Anomalie ano = new Anomalie(lot);

                // On ajoute le lot soit, dans la liste des anos déjà créées soit, dans celle des anos à créer.
                if (lotsDejaDansFichier.contains(numeroLot))

                    anoDejacrees.add(ano);
                else
                    anoACreer.add(ano);
            }

            // Mise à jour de la feuille des anomalies pour chaque version de composants
            anoAajouter.addAll(controlAno.createSheetError(entry.getKey(), anoACreer, anoDejacrees));
        }

        // Sauvegarde fichier et maj feuille principale
        Sheet sheet = controlAno.sauvegardeFichier(fichier);

        // Mis à jour de la feuille principale
        controlAno.majFeuillePrincipale(listeLotenAno, anoAajouter, lotsEnErreur, lotsSecurite, lotRelease, sheet, matiere);

        // Fermeture controleur
        controlAno.close();
    }

    /**
     * Permet de créer la liste des numéros de lots déjà en anomalie et met à jour les {@code Anomalie} depuis les infos
     * de la Pic
     *
     * @param listeLotenAno
     *            liste des {@code Anomalie} déjà connues
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

            // Mise à jour des données depuis la PIC
            LotSuiviPic lotPic = mapLotsPIC.get(string);

            if (lotPic != null)
                ano.majDepuisPic(lotPic);
            else
                logger.warn("Un lot du fichier Excel n'est pas connu dans la Pic : " + string);

            retour.add(string);
        }
        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}