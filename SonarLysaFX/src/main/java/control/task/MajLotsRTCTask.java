package control.task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.mchange.util.AssertException;

import application.Main;
import control.rtc.ControlRTC;
import dao.DaoFactory;
import dao.DaoLotRTC;
import javafx.application.Platform;
import model.bdd.Edition;
import model.bdd.Produit;
import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
import model.enums.GroupeProduit;
import utilities.Statics;
import utilities.enums.Severity;

/**
 * T�che permettant de mettre � jour le fichier XML des lots RTC.
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
public class MajLotsRTCTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final Logger LOGCONSOLE = LogManager.getLogger("console-log");
    private static final String TITRE = "Mise � jour des Lots RTC";
    private static final short CLARITYMINI = 5;
    private static final short CLARITYMAX = 9;
    private static final short CLARITY7 = 7;

    private LocalDate date;
    private DaoLotRTC dao;

    /*---------- CONSTRUCTEURS ----------*/

    public MajLotsRTCTask(LocalDate date)
    {
        super(1, TITRE);
        this.date = date;
        dao = DaoFactory.getDao(LotRTC.class);
        annulable = true;
        startTimers();
    }

    /**
     * Constructeur sans param�tres pour les tests. Ne pas utiliser, lance une exception
     */
    public MajLotsRTCTask()
    {
        super(1, TITRE);
        throw new AssertException();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public Boolean call() throws Exception
    {
        Map<String, LotRTC> map = majLotsRTC();
        return sauvegarde(map);
    }

    @Override
    public void annulerImpl()
    {
        // Pas de traitement d'annulation
    }

    /*---------- METHODES PRIVEES ----------*/

    private Map<String, LotRTC> majLotsRTC() throws TeamRepositoryException
    {
        ControlRTC control = ControlRTC.INSTANCE;
        List<LotRTC> lotsRTC = control.recupLotsRTC(date, this);
        if (lotsRTC.isEmpty())
            Platform.runLater(() -> Main.createAlert(Severity.INFO, null, "Pas de nouveaux lots RTC mis � jour depuis le dernier traitement."));

        // VAriables
        int i = 0;
        int size = lotsRTC.size();
        baseMessage = "Traitement des lot : ";
        long debut = System.currentTimeMillis();

        // Initialisation de la map depuis les informations de la base de donn�es
        Map<String, LotRTC> retour = dao.readAllMap();
        Map<String, ProjetClarity> mapClarity = DaoFactory.getDao(ProjetClarity.class).readAllMap();
        Map<String, Produit> mapGroupe = DaoFactory.getDao(Produit.class).readAllMap();
        Map<String, Edition> mapEdition = DaoFactory.getDao(Edition.class).readAllMap();

        for (LotRTC lotRTC : lotsRTC)
        {
            // On saute tous les lotRTC sans num�ro de lot.
            if (lotRTC.getLot().isEmpty())
                continue;

            LOGCONSOLE.debug("Traitement lots RTC : " + i + " - " + size + " - lot : " + lotRTC.getLot());

            // R�cup�ration du code Clarity depuis RTC
            String codeClarity = lotRTC.getProjetClarityString();

            // Test du projet Clarity par rapport � la base de donn�es et valorisation de la donn�e
            ProjetClarity projetClarity = testProjetClarity(codeClarity, mapClarity);
            lotRTC.setProjetClarity(projetClarity);

            // R�cup�ration du code �dition depuis RTC
            String editionString = lotRTC.getEditionString();

            // Test de l'�dition par rapport � la base de donn�es et valorisation de la donn�e
            Edition edition = testEdition(editionString, mapEdition);
            lotRTC.setEdition(edition);

            // Controle du groupe
            if (mapGroupe.containsKey(lotRTC.getProjetRTC()))
                lotRTC.setGroupeProduit(mapGroupe.get(lotRTC.getProjetRTC()).getGroupe());
            else
                lotRTC.setGroupeProduit(GroupeProduit.AUCUN);

            String lot = lotRTC.getLot();

            // On rajoute les lots non �xistants � la map et on mets � jour les autres avec les nouvelles donn�es
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
     * Test si l'�dition est pr�sente dans la base de donn�es. Retourne la valeur dans ce cas, ou cr�e uen nouvelle �dition et la rajoute � la map.
     * 
     * @param editionString
     * @param mapEdition
     * @return
     */
    private Edition testEdition(String editionString, Map<String, Edition> mapEdition)
    {
        if (editionString.startsWith("CDM") && !editionString.startsWith("CHC_CDM"))
            editionString = editionString.replace("CDM", "CHC_CDM");

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
     * Test si le code Clarity est pr�sent dans la base de donn�es. Retourne la valeur dans ce cas, ou cr�e un nouveau code Clarity et le rajoute � la map.
     * 
     * @param codeClarity
     * @param mapClarity
     * @return
     */
    private ProjetClarity testProjetClarity(String codeClarity, Map<String, ProjetClarity> mapClarity)
    {
        // V�rification si le code Clarity est bien dans la map
        if (mapClarity.containsKey(codeClarity))
        {
            return mapClarity.get(codeClarity);
        }

        String temp = Statics.EMPTY;
        boolean testT7 = codeClarity.startsWith("T") && codeClarity.length() == CLARITY7;

        // Sinon on it�re sur les clefs en supprimant les indices de lot, et on prend la premi�re clef correspondante
        for (Map.Entry<String, ProjetClarity> entry : mapClarity.entrySet())
        {
            String key = entry.getKey();

            // On retire les deux derni�res lettres pour les clefs de plus de 6 caract�res finissants par 0[1-9]
            if (controleKey(codeClarity, key))
                return entry.getValue();

            // On r�cup�re la clef correxpondante la plus �lev�e dans le cas des clef commen�ants par T avec 2 caract�res manquants
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
     * Contr�le les valeurs du code Clarity en prenant en compte les diff�rentes erreurs possibles
     * 
     * @param anoClarity
     * @param key
     * @return
     */
    private boolean controleKey(String anoClarity, String key)
    {
        // Retourne une clef avec 6 caract�res maximum si celle-ci finie par un num�ro de lot
        String smallkey = key.length() > CLARITYMINI && key.matches(".*0[0-9E]$") ? key.substring(0, CLARITYMINI + 1) : key;

        // Contr�le si la clef est de type T* ou P*.
        String newKey = key.length() == CLARITYMAX && (key.startsWith("T") || key.startsWith("P")) ? key.substring(0, CLARITYMAX - 1) : smallkey;

        // Retourne la clef clairity de l'anomalie avec 6 caract�res maximum si celle-ci finie par un num�ro de lot
        String smallClarity = anoClarity.length() > CLARITYMINI && anoClarity.matches(".*0[0-9E]$") ? anoClarity.substring(0, CLARITYMINI + 1) : anoClarity;

        // Contr�le si la clef est de type T* ou P*.
        String newClarity = anoClarity.length() == CLARITYMAX && (anoClarity.startsWith("T") || anoClarity.startsWith("P")) ? anoClarity.substring(0, CLARITYMAX - 1) : smallClarity;

        // remplace le dernier du coade Clarity par 0.
        String lastClarity = anoClarity.replace(anoClarity.charAt(anoClarity.length() - 1), '0');

        return anoClarity.equalsIgnoreCase(newKey) || newClarity.equalsIgnoreCase(key) || lastClarity.equalsIgnoreCase(key);
    }

    /**
     * Sauvegarde du fichier
     * 
     * @return
     */
    private boolean sauvegarde(Map<String, LotRTC> map)
    {
        boolean retour = dao.persist(map.values()) > 0;
        dao.majDateDonnee();
        return retour;
    }

    /*---------- ACCESSEURS ----------*/
}
