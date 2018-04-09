package control.task;

import static utilities.Statics.fichiersXML;
import static utilities.Statics.logger;
import static utilities.Statics.lognonlistee;
import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;

import application.Main;
import control.excel.ControlSuivi;
import model.Anomalie;
import model.LotSuiviPic;
import model.ModelFactory;
import model.enums.Matiere;
import model.enums.TypeParam;
import sonarapi.model.Composant;
import sonarapi.model.Projet;
import sonarapi.model.QualityGate;
import sonarapi.model.Status;
import sonarapi.model.Vue;
import utilities.Statics;
import utilities.Utilities;

public class MajSuiviExcelTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/

    private TypeMaj typeMaj;

    /*---------- CONSTRUCTEURS ----------*/

    public MajSuiviExcelTask(TypeMaj typeMaj)
    {
        super(5);
        this.typeMaj = typeMaj;
        annulable = false;
    }
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        return majSuiviExcel();
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean majSuiviExcel() throws InvalidFormatException, IOException
    {
        switch (typeMaj)
        {
            case SUIVI:
                majFichierSuiviExcel();
                break;

            case DATASTAGE:
                majFichierSuiviExcelDataStage();
                break;

            case DOUBLE:
                traitementSuiviExcelToutFichiers();
                break;
        }
        return true;
    }

    /**
     * Traitement des deux fichiers Excel de Suivi.
     * 
     * @throws InvalidFormatException
     * @throws IOException
     */
    private void traitementSuiviExcelToutFichiers() throws InvalidFormatException, IOException
    {
        // Récupération anomalies Datastage
        List<String> anoDatastage = majFichierSuiviExcelDataStage();
        
        // Récupération anomalies Java
        initEtape(5);
        List<String> anoJava = majFichierSuiviExcel();
        
        // Liste des anomalies sur plusieures matières
        List<String> anoMultiple = new ArrayList<>();
        for (String string : anoDatastage)
        {
            if (anoJava.contains(string))
                anoMultiple.add(string);
        }

        // Mise à jour des fichiers Excel
        ControlSuivi controlAnoJava = new ControlSuivi(new File(proprietesXML.getMapParams().get(TypeParam.ABSOLUTEPATH) + proprietesXML.getMapParams().get(TypeParam.NOMFICHIER)));
        ControlSuivi controlAnoDataStage = new ControlSuivi(new File(proprietesXML.getMapParams().get(TypeParam.ABSOLUTEPATH) + proprietesXML.getMapParams().get(TypeParam.NOMFICHIERDATASTAGE)));
        controlAnoJava.majMultiMatiere(anoMultiple);
        controlAnoDataStage.majMultiMatiere(anoMultiple);
        controlAnoJava.close();
        controlAnoDataStage.close();
    }

    /**
     * Mise à jour du fichier Excel des suivis d'anomalies pour les composants Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     */
    private List<String> majFichierSuiviExcelDataStage() throws InvalidFormatException, IOException
    {
        // Appel de la récupération des composants datastage avec les vesions en paramètre
        Map<String, List<Projet>> composants = recupererComposantsSonarVersion(true);

        // Mise à jour des liens des composants datastage avec le bon QG
        etapePlus();
        liensQG(composants.values(), proprietesXML.getMapParams().get(TypeParam.NOMQGDATASTAGE));

        // Traitement du fichier datastage de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(TypeParam.NOMFICHIERDATASTAGE), Matiere.DATASTAGE);
    }

    /**
     * Mise à jour du fichier Excel des suivis d'anomalies pour tous les composants non Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     */
    private List<String> majFichierSuiviExcel() throws InvalidFormatException, IOException
    {
        // Appel de la récupération des composants non datastage avec les vesions en paramètre
        Map<String, List<Projet>> composants = recupererComposantsSonarVersion(false);
        etapePlus();
        
        // Traitement du fichier de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(TypeParam.NOMFICHIER), Matiere.JAVA);
    }

    /**
     * Méthode de traitement pour mettre à jour les fichiers de suivi d'anomalies ainsi que la création de vue dans SonarQube. <br>
     * Retourne une liste de tous les lots Sonar en erreur.
     *
     * @param composants
     * @param java
     * @param versions
     * @throws InvalidFormatException
     * @throws IOException
     */

    @SuppressWarnings("unchecked")
    private List<String> traitementFichierSuivi(Map<String, List<Projet>> composants, String fichier, Matiere matiere) throws InvalidFormatException, IOException
    {
        // 1. Récupération des données depuis les fichiers Excel.

        // Fichier des lots édition
        Map<String, LotSuiviPic> lotsPIC = fichiersXML.getLotsPic();

        // 2. Récupération des lots Sonar en erreur.
        Map<String, Set<String>> mapLotsSonar;

        Set<String> lotsSecurite = new HashSet<>();
        Set<String> lotRelease = new HashSet<>();

        etapePlus();
        if (Main.DESER)
        {
            mapLotsSonar = Utilities.deserialisation("d:\\lotsSonar" + matiere.toString() + ".ser", HashMap.class);
            lotsSecurite = Utilities.deserialisation("d:\\lotsSecurite" + matiere.toString() + ".ser", HashSet.class);
            lotRelease = Utilities.deserialisation("d:\\lotsRelease" + matiere.toString() + ".ser", HashSet.class);
        }
        else
        {
            mapLotsSonar = lotSonarQGError(composants, lotsSecurite, lotRelease);
            Utilities.serialisation("d:\\lotsSecurite" + matiere.toString() + ".ser", lotsSecurite);
            Utilities.serialisation("d:\\lotsSonar" + matiere.toString() + ".ser", mapLotsSonar);
            Utilities.serialisation("d:\\lotsRelease" + matiere.toString() + ".ser", lotRelease);
        }

        etapePlus();
        updateMessage("Mise à jour du fichier Excel...");
        updateProgress(-1, 1);
        // 3. Supression des lots déjà créés et création des feuille Excel avec les nouvelles erreurs
        majFichierAnomalies(lotsPIC, mapLotsSonar, lotsSecurite, lotRelease, fichier, matiere);

        etapePlus();
        // 4. Création des vues
        for (Map.Entry<String, Set<String>> entry : mapLotsSonar.entrySet())
        {            
            // Création de la vue, gestion du message et ajout à la liste de vues créées en cas d'annulation
            String nom = prepareNom(fichier);
            String nomVue = nom + " - Edition " + entry.getKey();
            String base = "Création Vue " + nomVue + Statics.NL;
            updateMessage(base);            
            Vue vueParent = creerVue(nom.replace(" ", "") + "Key" + entry.getKey(), nomVue, "Vue regroupant tous les lots avec des composants en erreur", true);
            
            // Ajout des sous-vue
            int i = 0;
            int size = entry.getValue().size();
            for (String lot : entry.getValue())
            {
                // Traitement + message
                String nomLot = "Lot " + lot;
                updateMessage(base + "Ajout : " + nomLot);
                updateProgress(++i, size);
                api.ajouterSousVue(new Vue("view_lot_" + lot, nomLot), vueParent);
            }
        }
        
        // Traitement liste de retour
        List<String> retour = new ArrayList<>();
        for (Set<String> liste : mapLotsSonar.values())
        {
            retour.addAll(liste);
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

            String base = "Traitement Version : " + entryKey + Statics.NL;
            updateMessage(base);
            int i = 0;
            int size = entry.getValue().size();

            // Iteration sur la liste des projets
            for (Projet projet : entry.getValue())
            {
                traitementProjet(projet, retour, entryKey, lotSecurite, lotRelease, base);
                updateProgress(++i, size);
            }
        }
        return retour;
    }

    /**
     * Permet de mettre à jour le fichier des anomalies Sonar, en allant chercher les nouvelles dans Sonar et en vérifiant celles qui ne sont plus d'actualité.
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
        String name = proprietesXML.getMapParams().get(TypeParam.ABSOLUTEPATH) + fichier;
        ControlSuivi controlAno = new ControlSuivi(new File(name));

        // Lecture du fichier pour remonter les anomalies en cours.
        List<Anomalie> listeLotenAno = controlAno.recupDonneesDepuisExcel();

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
                Anomalie ano = ModelFactory.getModelWithParams(Anomalie.class, lot);

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

    private String prepareNom(String fichier)
    {
        return fichier.replace("_", " ").split("\\.")[0];
    }

    /**
     * Traitement Sonar d'un projet
     *
     * @param projet
     * @param retour
     * @param entryKey
     * @param lotSecurite
     * @param lotRelease
     * @param base
     */
    private void traitementProjet(Projet projet, HashMap<String, Set<String>> retour, String entryKey, Set<String> lotSecurite, Set<String> lotRelease, String base)
    {
        String key = projet.getKey();

        updateMessage(base + projet.getNom());
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
     * Permet de créer la liste des numéros de lots déjà en anomalie et met à jour les {@code Anomalie} depuis les infos de la Pic
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
     * Permet de lier tous les composants sonar à une QG particulière
     *
     * @param composants
     * @param nomQG
     */
    private void liensQG(Collection<List<Projet>> composants, String nomQG)
    {
        // Récupération de l'Id de la QualityGate
        QualityGate qg = api.getQualityGate(nomQG);

        // Préparation message
        String base = "Association avec le QG DataStage :" + Statics.NL;
        int i = 0;
        int size = 0;
        for (List<Projet> liste : composants)
        {
            size += liste.size();
        }

        // Iteration sur tous les composants pour les associer au QualityGate
        for (List<Projet> liste : composants)
        {
            for (Projet projet : liste)
            {
                // Message
                updateMessage(base + projet.getNom());
                updateProgress(++i, size);

                api.associerQualitygate(projet, qg);
            }
        }
    }

    /*---------- ACCESSEURS ----------*/

    public enum TypeMaj 
    {
        SUIVI("Maj Fichier de Suivi"), 
        DATASTAGE("Maj Fichier de Suivi DataStage"), 
        DOUBLE("Maj Fichiers de Suivi");

        private String string;

        private TypeMaj(String string)
        {
            this.string = string;
        }

        @Override
        public String toString()
        {
            return string;
        }
    }
}