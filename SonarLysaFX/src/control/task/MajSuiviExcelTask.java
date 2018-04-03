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
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;

import control.ControlAno;
import control.parent.SonarTask;
import junit.control.ControlSonarTest;
import model.Anomalie;
import model.LotSuiviPic;
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
        super();
        this.typeMaj = typeMaj;
    }
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annuler()
    {
        switch (typeMaj)
        {
            case SUIVI :

                break;

            case DATASTAGE :

                break;

            case DOUBLE :

                break;
        }
    }

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
            case SUIVI :
                majFichierSuiviExcel();
                break;

            case DATASTAGE :
                majFichierSuiviExcelDataStage();
                break;

            case DOUBLE :
                traitementSuiviExcelToutFichiers();
                break;
        }
        return true;
    }

    private void traitementSuiviExcelToutFichiers() throws InvalidFormatException, IOException
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
     * Mise � jour du fichier Excel des suivis d'anomalies pour les composants Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     */
    private List<String> majFichierSuiviExcelDataStage() throws InvalidFormatException, IOException
    {
        // Appel de la r�cup�ration des composants datastage avec les vesions en param�tre
        Map<String, List<Projet>> composants = recupererComposantsSonarVersion(true);

        // Mise � jour des liens des composants datastage avec le bon QG
        liensQG(composants.values(), proprietesXML.getMapParams().get(TypeParam.NOMQGDATASTAGE));

        // Traitement du fichier datastage de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(TypeParam.NOMFICHIERDATASTAGE), Matiere.DATASTAGE);
    }

    /**
     * Mise � jour du fichier Excel des suivis d'anomalies pour tous les composants non Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     */
    private List<String> majFichierSuiviExcel() throws InvalidFormatException, IOException
    {
        // Appel de la r�cup�ration des composants non datastage avec les vesions en param�tre
        Map<String, List<Projet>> composants = recupererComposantsSonarVersion(false);

        // Traitement du fichier de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(TypeParam.NOMFICHIER), Matiere.JAVA);
    }

    /**
     * M�thode de traitement pour mettre � jour les fichiers de suivi d'anomalies ainsi que la cr�ation de vue dans
     * SonarQube. <br>
     * Retourne une liste de toutes les anomalies trait�es depuis Sonar
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
            Set<String> lotsSecurite2 = Utilities.deserialisation("d:\\lotsSecurite.ser", HashSet.class);
            Utilities.serialisation("d:\\lotsSonar.ser", mapLotsSonar);
            Map<String, Set<String>> mapLotsSonar2 = Utilities.deserialisation("d:\\lotsSonar.ser", HashMap.class);
            Utilities.serialisation("d:\\lotsRelease.ser", lotRelease);
            Set<String> lotRelease2 = Utilities.deserialisation("d:\\lotsRelease.ser", HashSet.class);
            System.out.println("lot securite : " + lotsSecurite.size());
            System.out.println("lot securite2 : " + lotsSecurite2.size());
            System.out.println("mapLotsSonar : " + mapLotsSonar.size());
            System.out.println("mapLotsSonar2 : " + mapLotsSonar2.size());
            System.out.println("lotRelease : " + lotRelease.size());
            System.out.println("lotRelease2 : " + lotRelease2.size());
        }

        updateMessage("Mise � jour du fichier Excel...");
        
        // 3. Supression des lots d�j� cr��s et cr�ation des feuille Excel avec les nouvelles erreurs
        majFichierAnomalies(lotsPIC, mapLotsSonar, lotsSecurite, lotRelease, fichier, matiere);

        // 4. Cr�ation des vues
        for (Map.Entry<String, Set<String>> entry : mapLotsSonar.entrySet())
        {
            // Cr�ation de la vue et envoie vers SonarQube
            String nom = prepareNom(fichier);
            Vue vueParent = creerVue(nom.replace(" ", "") + "Key" + entry.getKey(), nom + " - Edition " + entry.getKey(),
                    "Vue regroupant tous les lots avec des composants en erreur", true);

            // Message
            String base = "Cr�ation Vue " + vueParent.getName() + Statics.NL;
            updateMessage(base);
            int i = 0;
            int size = entry.getValue().size();
            
            for (String lot : entry.getValue())
            {
                // Ajout des sous-vue
                String nomLot = "Lot " + lot;
                api.ajouterSousVue(new Vue("view_lot_" + lot, nomLot), vueParent);
                retour.add(nomLot);
                
                // Message
                updateMessage(base + "Ajout : " + nomLot);
                updateProgress(i, size);
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
     * Permet de mettre � jour le fichier des anomalies Sonar, en allant chercher les nouvelles dans Sonar et en
     * v�rifiant celles qui ne sont plus d'actualit�.
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
        String name = proprietesXML.getMapParams().get(TypeParam.ABSOLUTEPATH) + fichier;
        ControlAno controlAno = new ControlAno(new File(name));

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

                // On va chercher les informations de ce lot dans le fichier des lots de la PIC. Si on ne les trouve
                // pas, il faudra mettre � jour ce fichier
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
     * Permet de cr�er la liste des num�ros de lots d�j� en anomalie et met � jour les {@code Anomalie} depuis les infos
     * de la Pic
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
     * Permet de lier tous les composants sonar � une QG particuli�re
     *
     * @param composants
     * @param nomQG
     */
    private void liensQG(Collection<List<Projet>> composants, String nomQG)
    {
        // R�cup�ration de l'Id de la QualityGate
        QualityGate qg = api.getQualityGate(nomQG);

        // Pr�paration message
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
        SUIVI ("Maj Fichier de Suivi"), 
        DATASTAGE ("Maj Fichier de Suivi DataStage"), 
        DOUBLE ("Maj Fichiers de Suivi");
        
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