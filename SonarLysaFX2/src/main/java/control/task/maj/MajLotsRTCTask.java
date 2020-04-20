package control.task.maj;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

import com.ibm.team.repository.common.TeamRepositoryException;

import application.Main;
import control.rtc.ControlRTC;
import control.task.AbstractTask;
import dao.DaoFactory;
import javafx.application.Platform;
import model.bdd.Edition;
import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
import model.enums.Severity;
import utilities.Statics;
import utilities.Utilities;

/**
 * Teche permettant de mettre à jour la table des lots RTC, en reprenant tout depuis RTC.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class MajLotsRTCTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final Logger LOGCONSOLE = Utilities.getLogger("console-log");
    private static final String TITRE = "Mise à jour des Lots RTC";
    private static final short CLARITYMINI = 5;
    private static final short CLARITYMAX = 9;
    private static final short CLARITY7 = 7;

    private static final Pattern PATTERNS = Pattern.compile("-S");
    private static final Pattern PATTERN009 = Pattern.compile(".*0[0-9E]$");

    private LocalDate date;

    /*---------- CONSTRUCTEURS ----------*/

    public MajLotsRTCTask(LocalDate date, AbstractTask tacheParente)
    {
        super(1, TITRE, tacheParente);
        init(date);
    }

    public MajLotsRTCTask(LocalDate date)
    {
        super(1, TITRE);
        init(date);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas de traitement d'annulation
    }

    /**
     * Mise à jour de l'édition et du projet Clarity d'un seul lot RTC.
     * 
     * @param lotRTC
     *               Lot à traiter.
     */
    public void majLotRTC(LotRTC lotRTC)
    {
        // Récupération du code Clarity depuis RTC
        String codeClarity = lotRTC.getProjetClarityString();

        // Test du projet Clarity par rapport à la base de données et valorisation de la donnée
        if (!codeClarity.isEmpty())
            lotRTC.setProjetClarity(testProjetClarity(codeClarity, getMapClarity()));
        else
            lotRTC.setProjetClarity(Statics.CLARITYINCONNU);

        // Récupération du code edition depuis RTC
        String editionString = lotRTC.getEditionString();

        // Test de l'édition par rapport à la base de données et valorisation de la donnée
        if (!editionString.isEmpty())
            lotRTC.setEdition(testEdition(editionString, getMapEdition()));
        else
            lotRTC.setEdition(Statics.EDITIONINCONNUE);

    }

    @Override
    public Boolean call() throws Exception
    {
        Map<String, LotRTC> map = majLotsRTC();
        return sauvegarde(map);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Initalisation
     * 
     * @param date
     *             Date en paramètre du traitement.
     */
    private void init(LocalDate date)
    {
        this.date = date;
        annulable = true;
        startTimers();
    }

    /**
     * Mise à jour complète de la table des lots RTC depuis RTC
     * 
     * @return
     *         Une map des lots classés par numéro.
     * @throws TeamRepositoryException
     *                                 Exception RTC.
     */
    private Map<String, LotRTC> majLotsRTC() throws TeamRepositoryException
    {
        // Récupération de tous les lots RTC créés aprés la date donnée.
        List<LotRTC> lotsRTC = ControlRTC.getInstance().recupLotsRTC(date, this);
        if (lotsRTC.isEmpty())
            Platform.runLater(() -> Main.createAlertForFX(Severity.INFO, null, "Pas de nouveaux lots RTC mis à jour depuis le dernier traitement."));

        // Variables
        int i = 0;
        int size = lotsRTC.size();
        baseMessage = "Traitement des lot : ";
        long debut = System.currentTimeMillis();

        // Initialisation de la map depuis les informations de la base de données
        Map<String, LotRTC> retour = DaoFactory.getMySQLDao(LotRTC.class).readAllMap();

        for (LotRTC lotRTC : lotsRTC)
        {
            // On saute tous les lotRTC sans numero de lot.
            if (lotRTC.getNumero().isEmpty())
                continue;

            LOGCONSOLE.debug("Traitement lots RTC : " + i + " - " + size + " - lot : " + lotRTC.getNumero());

            majLotRTC(lotRTC);

            String lot = lotRTC.getNumero();

            // On rajoute les lots non existants à la map et on mets à jour les autres avec les nouvelles données
            if (!retour.containsKey(lot))
                retour.put(lot, lotRTC);
            else
                retour.get(lot).update(lotRTC);

            // Affichage
            i++;
            calculTempsRestant(debut, i, size);
            updateProgress((double) i, (double) size);
            updateMessage(lot);
        }
        return retour;
    }

    /**
     * Test si l'édition est presente dans la base de données. Retourne la valeur dans ce cas, ou on crée une nouvelle edition et on la rajoute à la map.
     * 
     * @param editionString
     *                      Nom de l'édition depuis RTC.
     * @param mapEdition
     *                      Map des éditions en base de données.
     * @return
     *         L'édition qui sera enregistrée en base de données.
     */
    private Edition testEdition(String editionString, Map<String, Edition> mapEdition)
    {
        if (editionString.startsWith("CHC_CDM"))
            editionString = editionString.replace("CHC_CDM", "CDM");

        editionString = PATTERNS.matcher(editionString).replaceAll(Statics.EMPTY);
        if (!mapEdition.containsKey(editionString))
        {
            Edition edition = Edition.getEditionInconnue(editionString);
            mapEdition.put(edition.getMapIndex(), edition);
            return edition;
        }
        else
            return mapEdition.get(editionString);
    }

    /**
     * Test si le code Clarity est present dans la base de données. Retourne la valeur dans ce cas,
     * sinon on crée un nouveau code Clarity et on le rajoute à la map.
     * 
     * @param codeClarity
     *                    Code Clarity depuis RTC.
     * @param mapClarity
     *                    Map des codes Clarity en base de données.
     * @return
     *         Le projet Clarity qui sera enregistré en base de données.
     */
    private ProjetClarity testProjetClarity(String codeClarity, Map<String, ProjetClarity> mapClarity)
    {
        // Verification si le code Clarity est bien dans la map
        ProjetClarity retour = mapClarity.get(codeClarity);
        if (retour != null)
            return retour;

        String temp = Statics.EMPTY;
        boolean testT7 = codeClarity.startsWith("T") && codeClarity.length() == CLARITY7;

        // Sinon on itere sur les clefs en supprimant les indices de lot, et on prend la première clef correspondante
        for (Map.Entry<String, ProjetClarity> entry : mapClarity.entrySet())
        {
            String key = entry.getKey();

            // On retire les deux dernières lettres pour les clefs de plus de 6 caractères finissants par 0[1-9]
            if (controleKey(codeClarity, key))
                return entry.getValue();

            // On récupère la clef correxpondante la plus élevèe dans le cas des clef commençants par T avec 2 caractères manquants
            if (testT7 && key.contains(codeClarity) && key.compareTo(temp) > 0)
                temp = key;
        }

        if (!temp.isEmpty())
            return mapClarity.get(temp);

        // Si on trouve rien, ajout d'un projet Clarity inconnu
        ProjetClarity inconnu = ProjetClarity.getProjetClarityInconnu(codeClarity);
        mapClarity.put(inconnu.getMapIndex(), inconnu);
        return inconnu;
    }

    /**
     * Contrôle les valeurs du code Clarity en prenant en compte les différentes erreurs possibles.
     * 
     * @param codeClarity
     *                    Code Clarity à tester.
     * @param key
     *                    Clef à tester.
     * @return
     *         Vrai si le code est bon.
     */
    private boolean controleKey(String codeClarity, String key)
    {
        // Retourne une clef avec 6 caracteres maximum si celle-ci finie par un numero de lot
        String smallkey = key.length() > CLARITYMINI && PATTERN009.matcher(key).matches() ? key.substring(0, CLARITYMINI + 1) : key;

        // Contrôle si la clef est de type T* ou P*.
        String newKey = key.length() == CLARITYMAX && (key.startsWith("T") || key.startsWith("P")) ? key.substring(0, CLARITYMAX - 1) : smallkey;

        // Retourne la clef clairity de l'anomalie avec 6 caracteres maximum si celle-ci finie par un numero de lot
        String smallClarity = codeClarity.length() > CLARITYMINI && PATTERN009.matcher(codeClarity).matches() ? codeClarity.substring(0, CLARITYMINI + 1) : codeClarity;

        // Contrôle si la clef est de type T* ou P*.
        String newClarity = codeClarity.length() == CLARITYMAX && (codeClarity.startsWith("T") || codeClarity.startsWith("P")) ? codeClarity.substring(0, CLARITYMAX - 1) : smallClarity;

        // remplace le dernier du code Clarity par 0.
        String lastClarity = codeClarity.replace(codeClarity.charAt(codeClarity.length() - 1), '0');

        return codeClarity.equalsIgnoreCase(newKey) || newClarity.equalsIgnoreCase(key) || lastClarity.equalsIgnoreCase(key);
    }

    /**
     * Sauvegarde du fichier en base de données.
     * 
     * @param map
     *            Map des lotRTC à sauvegarder.
     * @return
     *         Vrai si la persistance est OK.
     */
    private boolean sauvegarde(Map<String, LotRTC> map)
    {
        return sauvegarde(DaoFactory.getMySQLDao(LotRTC.class), map.values()) > 0;
    }

    /*---------- ACCESSEURS ----------*/
}
