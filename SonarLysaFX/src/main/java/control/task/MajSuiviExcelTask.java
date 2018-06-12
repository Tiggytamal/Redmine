package control.task;

import static utilities.Statics.fichiersXML;
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

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;

import com.ibm.team.repository.common.IItemHandle;
import com.ibm.team.repository.common.TeamRepositoryException;

import application.Main;
import control.excel.ControlSuivi;
import control.excel.ExcelFactory;
import control.rtc.ControlRTC;
import control.xml.ControlXML;
import model.Anomalie;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.enums.Matiere;
import model.enums.Param;
import model.enums.ParamBool;
import model.enums.TypeColSuivi;
import model.enums.TypeFichier;
import model.enums.TypeMetrique;
import model.sonarapi.Composant;
import model.sonarapi.Metrique;
import model.sonarapi.Periode;
import model.sonarapi.Projet;
import model.sonarapi.QualityGate;
import model.sonarapi.Status;
import model.sonarapi.Vue;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
public class MajSuiviExcelTask extends SonarTask
{
    /*---------- ATTRIBUTS ----------*/

    private TypeMaj typeMaj;

    /*---------- CONSTRUCTEURS ----------*/

    public MajSuiviExcelTask(TypeMaj typeMaj)
    {
        super(6);
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

    /**
     * 
     * @return
     * @throws IOException
     * @throws TeamRepositoryException
     * @throws JAXBException
     */
    private boolean majSuiviExcel() throws IOException, TeamRepositoryException, JAXBException
    {
        // Mise � jour du fichier RTC � la date du jour.
        majFichierRTC();
        
        updateProgress(-1, 0);
        etapePlus();
        
        // Mise � jour d'un fichier ou des deux , selon le type de mise � jour.
        switch (typeMaj)
        {
            case SUIVI:
                majFichierSuiviExcel();
                break;

            case DATASTAGE:
                majFichierSuiviExcelDataStage();
                break;
                
            case COBOL:
                majFichierSuiviExcelCOBOL();
                break;

            case DOUBLE:
                traitementSuiviExcelToutFichiers();
                break;
        }
        return true;
    }

    /**
     * 
     * @throws TeamRepositoryException
     * @throws JAXBException
     */
    private void majFichierRTC() throws TeamRepositoryException, JAXBException
    {
        ControlRTC control = ControlRTC.INSTANCE;
        Map<String, LotSuiviRTC> map = new HashMap<>();
        map.putAll(fichiersXML.getLotsRTC());
        List<IItemHandle> handles = control.recupLotsRTC(false, null);
        if (handles.isEmpty())
            throw new TechnicalException("La liste des lots RTC est vide!", null);
        
        int i = 0;
        int size = handles.size();
        String base = "R�cup�ration RTC - Traitement lot : ";
        String fin = "Nbre de lots trait�s : ";
        for (IItemHandle handle : handles)
        {
            // R�cup�ration de l'objet complet depuis l'handle de la requ�te
            LotSuiviRTC lot = control.creerLotSuiviRTCDepuisHandle(handle);
            updateProgress(++i, size);
            updateMessage(new StringBuilder(base).append(lot.getLot()).append(Statics.NL).append(fin).append(i).toString());
            map.put(lot.getLot(), lot);
        }

        Statics.fichiersXML.majMapDonnees(TypeFichier.LOTSRTC, map);
        new ControlXML().saveParam(Statics.fichiersXML);       
    }

    /**
     * Traitement des deux fichiers Excel de Suivi.
     * 
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private void traitementSuiviExcelToutFichiers() throws IOException
    {
        // R�cup�ration anomalies Datastage
        List<String> anoDatastage = majFichierSuiviExcelDataStage();

        // R�cup�ration anomalies Java
        initEtape(5);
        List<String> anoJava = majFichierSuiviExcel();

        // Liste des anomalies sur plusieures mati�res
        List<String> anoMultiple = new ArrayList<>();
        for (String string : anoDatastage)
        {
            if (anoJava.contains(string))
                anoMultiple.add(string);
        }

        // Mise � jour des fichiers Excel
        ControlSuivi controlAnoJava =   ExcelFactory.getControlleur(TypeColSuivi.class, new File(proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + proprietesXML.getMapParams().get(Param.NOMFICHIER)));
        ControlSuivi controlAnoDataStage = ExcelFactory.getControlleur(TypeColSuivi.class, new File(proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + proprietesXML.getMapParams().get(Param.NOMFICHIERDATASTAGE)));
        controlAnoJava.majMultiMatiere(anoMultiple);
        controlAnoDataStage.majMultiMatiere(anoMultiple);
        controlAnoJava.close();
        controlAnoDataStage.close();
    }

    /**
     * Mise � jour du fichier Excel des suivi des anomalies pour les composants Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private List<String> majFichierSuiviExcelDataStage() throws IOException
    {
        // Appel de la r�cup�ration des composants datastage avec les vesions en param�tre
        Map<String, List<Projet>> composants = recupererComposantsSonarVersion(Matiere.DATASTAGE);

        // Mise � jour des liens des composants datastage avec le bon QG
        etapePlus();
        liensQG(composants.values(), proprietesXML.getMapParams().get(Param.NOMQGDATASTAGE));

        // Traitement du fichier datastage de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERDATASTAGE), Matiere.DATASTAGE);
    }

    /**
     * Mise � jour du fichier Excel du suivi des anomalies pour tous les composants COBOL
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private List<String> majFichierSuiviExcelCOBOL() throws IOException
    {
        // Appel de la r�cup�ration des composants non datastage avec les vesions en param�tre
        Map<String, List<Projet>> composants = recupererComposantsSonarVersion(Matiere.COBOL);
        etapePlus();

        // Traitement du fichier de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIERCOBOL), Matiere.COBOL);
    }
    
    /**
     * Mise � jour du fichier Excel des suivis d'anomalies pour tous les composants non Datastage
     *
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private List<String> majFichierSuiviExcel() throws IOException
    {
        // Appel de la r�cup�ration des composants non datastage avec les vesions en param�tre
        Map<String, List<Projet>> composants = recupererComposantsSonarVersion(Matiere.JAVA);
        etapePlus();

        // Traitement du fichier de suivi
        return traitementFichierSuivi(composants, proprietesXML.getMapParams().get(Param.NOMFICHIER), Matiere.JAVA);
    }

    /**
     * M�thode de traitement pour mettre � jour les fichiers de suivi d'anomalies ainsi que la cr�ation de vue dans SonarQube. <br>
     * Retourne une liste de tous les lots Sonar en erreur.
     *
     * @param composants
     * @param java
     * @param versions
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */

    @SuppressWarnings("unchecked")
    private List<String> traitementFichierSuivi(Map<String, List<Projet>> composants, String fichier, Matiere matiere) throws IOException
    {
        // 1. R�cup�ration des donn�es depuis les fichiers Excel.

        // Fichier des lots �dition        
        Map<String, LotSuiviRTC> lotsRTC = fichiersXML.getLotsRTC();

        // 2. R�cup�ration des lots Sonar en erreur.
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
        updateMessage("Mise � jour du fichier Excel...");
        updateProgress(-1, 1);

        // 3. Supression des lots d�j� cr��s et cr�ation des feuille Excel avec les nouvelles erreurs
        majFichierAnomalies(lotsRTC, mapLotsSonar, lotsSecurite, lotRelease, fichier, matiere);

        updateProgress(1, 1);
        etapePlus();

        // 4. Cr�ation des vues si le param�trage est activ�
        if (proprietesXML.getMapParamsBool().get(ParamBool.VUESSUIVI))
        {
            for (Map.Entry<String, Set<String>> entry : mapLotsSonar.entrySet())
            {
                // Cr�ation de la vue, gestion du message et ajout � la liste de vues cr��es en cas d'annulation
                String nom = fichier.replace("_", " ").split("\\.")[0];
                String nomVue = nom + " - Edition " + entry.getKey();
                String base = "Cr�ation Vue " + nomVue + Statics.NL;
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
     * Permet de mettre � jour le fichier des anomalies Sonar, en allant chercher les nouvelles dans Sonar et en v�rifiant celles qui ne sont plus d'actualit�.
     *
     * @param lotsRTC
     *            Fichier excel d'extraction de la PIC de tous les lots.
     * @param mapLotsSonar
     *            map des lots Sonar avec une quality Gate en erreur
     * @param lotsSecurite
     * @param lotRelease
     * @param matiere
     * @throws InvalidFormatException
     * @throws IOException
     * @throws TeamRepositoryException
     */
    private void majFichierAnomalies(Map<String, LotSuiviRTC> lotsRTC, Map<String, Set<String>> mapLotsSonar, Set<String> lotsSecurite, Set<String> lotRelease, String fichier, Matiere matiere)
            throws IOException
    {
        // Controleur
        String name = proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + fichier;
        ControlSuivi controlAno = ExcelFactory.getControlleur(TypeColSuivi.class, new File(name));

        // Lecture du fichier pour remonter les anomalies en cours.
        List<Anomalie> listeLotenAno = controlAno.recupDonneesDepuisExcel();

        // Cr�ation de la liste des lots d�j� dans le fichier
        Map<String, Anomalie> lotsDejaDansFichier = creationNumerosLots(listeLotenAno, lotsRTC);

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
                LotSuiviRTC lot = lotsRTC.get(numeroLot);
                if (lot == null)
                {
                    lognonlistee.warn("Un lot du fichier Excel n'est pas connu dans RTC : " + numeroLot);
                    continue;
                }

                // On ajoute, soit le lot dans la liste des anos d�j� cr��es soit, on ajoute une nouvelle anomalie dans la liste des anoACeer.
                if (lotsDejaDansFichier.keySet().contains(numeroLot))
                    anoDejacrees.add(lotsDejaDansFichier.get(numeroLot));
                else
                    anoACreer.add(ModelFactory.getModelWithParams(Anomalie.class, lot));
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
        Composant composant = api.getMetriquesComposant(key,
                new String[] { TypeMetrique.LOT.toString(), TypeMetrique.QG.toString(), TypeMetrique.DUPLICATION.toString(), TypeMetrique.BLOQUANT.toString(), TypeMetrique.CRITIQUE.toString() });

        // R�cup�ration depuis la map des m�triques du num�ro de lot et du status de la Quality Gate
        Map<TypeMetrique, Metrique> metriques = composant.getMapMetriques();
        String lot = metriques.computeIfAbsent(TypeMetrique.LOT, t -> new Metrique(TypeMetrique.LOT, null)).getValue();

        // V�rification que le lot est bien valoris� et controle le QG
        if (lot != null && !lot.isEmpty() && controleMetriques(metriques))
        {
            // Ajout du lot � la liste de retour s'il y a des d�faults critiques ou bloquants ou de duplication de code
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
     * Controle si le QG est en erreur, et v�rifie qu'il y a bine au moins une erreur bloquante, critique, ou une duplication de code.
     * 
     * @param metriques
     * @return
     */
    private boolean controleMetriques(Map<TypeMetrique, Metrique> metriques)
    {
        String alert = metriques.computeIfAbsent(TypeMetrique.QG, t -> new Metrique(TypeMetrique.QG, null)).getValue();
        List<Periode> bloquants = getListPeriode(metriques, TypeMetrique.BLOQUANT);
        List<Periode> critiques = getListPeriode(metriques, TypeMetrique.CRITIQUE);
        List<Periode> duplication = getListPeriode(metriques, TypeMetrique.DUPLICATION);
        return alert != null && Status.from(alert) == Status.ERROR && (recupLeakPeriod(bloquants) > 0 || recupLeakPeriod(critiques) > 0 || recupLeakPeriod(duplication) > 3);
    }

    /**
     * Remonte une liste de p�riode en prot�geant des nullPointer
     * 
     * @param metriques
     * @param type
     * @return
     */
    private List<Periode> getListPeriode(Map<TypeMetrique, Metrique> metriques, TypeMetrique type)
    {
        return metriques.computeIfAbsent(type, t -> new Metrique(type, null)).getListePeriodes();
    }

    /**
     * Remonte la valeur du premier index de la p�riode qui correspond � la leakPeriod
     * 
     * @param periodes
     * @return
     */
    private float recupLeakPeriod(List<Periode> periodes)
    {
        if (periodes == null)
            return 0f;

        for (Periode periode : periodes)
        {
            if (periode.getIndex() == 1)
                return Float.valueOf(periode.getValeur());
        }
        return 0f;
    }

    /**
     * Permet de cr�er une map num�ros de lots d�j� en anomalie et met � jour les {@code Anomalie} depuis les infos de la Pic
     *
     * @param listeLotenAno
     *            liste des {@code Anomalie} d�j� connues
     * @param lotsRTC
     *            map des lots connus de la Pic.
     * @return
     */
    private Map<String, Anomalie> creationNumerosLots(List<Anomalie> listeLotenAno, Map<String, LotSuiviRTC> lotsRTC)
    {
        Map<String, Anomalie> retour = new HashMap<>();

        // Iteration sur la liste des anomalies
        for (Anomalie ano : listeLotenAno)
        {
            String anoLot = ano.getLot();

            if (anoLot.startsWith("Lot "))
                anoLot = anoLot.substring(4);

            // Mise � jour des donn�es depuis la PIC
            LotSuiviRTC lotRTC = lotsRTC.get(anoLot);

            if (lotRTC != null)
                ano.majDepuisRTC(lotRTC);

            retour.put(anoLot, ano);
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
        SUIVI("Maj Fichier de Suivi JAVA"), 
        DATASTAGE("Maj Fichier de Suivi DataStage"), 
        DOUBLE("Maj Fichiers de Suivi"),
        COBOL("Maj Fichier de Suivi COBOL");

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