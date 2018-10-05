package model.utilities;

import java.time.LocalDate;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IWorkItem;

import control.rtc.ControlRTC;
import control.word.ControlRapport;
import dao.DaoFactory;
import model.CompoPbApps;
import model.bdd.Anomalie;
import model.bdd.ChefService;
import model.bdd.GroupementProjet;
import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
import model.enums.EtatLot;
import model.enums.GroupeProjet;
import model.enums.TypeAction;
import model.enums.TypeInfo;
import utilities.DateConvert;
import utilities.Statics;

/**
 * Classe de contr�le des informations d'un mod�le
 * 
 * @author ETP8137 - Gr�goire mathon
 * @since 1.0
 */
public class ControlModelInfo
{
    /*---------- ATTRIBUTS ----------*/

    /** logger g�n�ral */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");

    private static final short CLARITYMINI = 5;
    private static final short CLARITYMAX = 9;
    private static final short CLARITY7 = 7;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    public ProjetClarity testProjetClarity(String codeClarity, Map<String, ProjetClarity> mapClarity)
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
     * r�cup�re les info depuis le fichier Clarity et le fichier des chefs de service pour les composants avec des probl�me ssu les codes application
     * 
     * @param compo
     */
    public void controleClarity(CompoPbApps pbApps, String anoClarity)
    {
        // R�cup�ration infox Clarity depuis fichier Excel
        if (anoClarity.isEmpty())
            return;

        Map<String, ProjetClarity> map = DaoFactory.getDao(ProjetClarity.class).readAllMap();

        // V�rification si le code Clarity de l'anomalie est bien dans la map
        if (map.containsKey(anoClarity))
        {
            pbApps.majDepuisClarity(map.get(anoClarity));
            initChefService(pbApps, map.get(anoClarity).getService());
            return;
        }

        String temp = Statics.EMPTY;
        boolean testT7 = anoClarity.startsWith("T") && anoClarity.length() == CLARITY7;

        // Sinon on it�re sur les clefs en supprimant les indices de lot, et on prend la premi�re clef correspondante
        for (Map.Entry<String, ProjetClarity> entry : map.entrySet())
        {
            String key = entry.getKey();

            // On retire les deux derni�res lettres pour les clefs de plus de 6 caract�res finissants par 0[1-9]
            if (controleKey(anoClarity, key))
            {
                pbApps.majDepuisClarity(entry.getValue());
                initChefService(pbApps, entry.getValue().getService());
                return;
            }

            // On r�cup�re la clef correxpondante la plus �lev�e dans le cas des clef commen�ants par T avec 2 caract�res manquants
            if (testT7 && key.contains(anoClarity) && key.compareTo(temp) > 0)
                temp = key;
        }

        if (!temp.isEmpty())
        {
            pbApps.majDepuisClarity(map.get(temp));
            initChefService(pbApps, map.get(temp).getService());
            return;
        }

        // Si on ne trouve pas, on renvoie juste l'anomalie avec le log d'erreur
        pbApps.setDepart(Statics.INCONNU);
        pbApps.setService(Statics.INCONNU);
        pbApps.setChefService(Statics.INCONNU);
    }


    public void controleAnoRTC(Anomalie ano) throws TeamRepositoryException
    {
        ControlRTC controlRTC = ControlRTC.INSTANCE;
        if (ano.getNumeroAnoRTC() != 0)
        {
            IWorkItem anoRTC = controlRTC.recupWorkItemDepuisId(ano.getNumeroAnoRTC());
            ano.setEtatRTC(controlRTC.recupEtatElement(anoRTC));
            if (ano.getDateCreation() == null)
                ano.setDateCreation(DateConvert.convert(LocalDate.class, anoRTC.getCreationDate()));
            if (anoRTC.getResolutionDate() != null)
                ano.setDateReso(DateConvert.convert(LocalDate.class, anoRTC.getResolutionDate()));
        }

    }
    
    /**
     * Met � jour le champ NPC si le projet en fait parti
     * 
     * @param ano
     * 
     */
    public void controleProjet(LotRTC lot, Map<String, GroupementProjet> mapGroupe)
    {
        if (mapGroupe.containsKey(lot.getProjetRTC()))
            lot.setGroupe(mapGroupe.get(lot.getProjetRTC()).getGroupe());
        else
            lot.setGroupe(GroupeProjet.VIDE);
    }
    
    /**
     * @param ano
     * @return
     * @throws TeamRepositoryException
     */
    public void controleRTC(Anomalie ano, ControlRapport controlRapport, ControlRTC controlRTC) throws TeamRepositoryException
    {
        // Controle sur l'�tat de l'anomalie (projet Clarity, lot et num�ro anomalie renseign�e
        String anoLot = ano.getLotRTC().getLot();

        // Protection contre les num�ros de lot vide
        if (anoLot == Statics.EMPTY)
            return;

        int anoLotInt = Integer.parseInt(anoLot);

        // Mise � jour de l'�tat de l'anomalie ainsi que les dates de r�solution et de cr�ation
        if (ano.getNumeroAnoRTC() != 0)
        {
            IWorkItem anoRTC = controlRTC.recupWorkItemDepuisId(ano.getNumeroAnoRTC());
            String newEtat = controlRTC.recupEtatElement(anoRTC);
            if (!newEtat.equals(ano.getEtatRTC()))
            {
                LOGGER.info("Lot : " + anoLot + " - nouvel etat anomalie : " + newEtat);
                controlRapport.addInfo(TypeInfo.ANOMAJ, anoLot, newEtat);
                ano.setEtatRTC(newEtat);
            }
            ano.setDateCreation(DateConvert.convert(LocalDate.class, anoRTC.getCreationDate()));
            if (anoRTC.getResolutionDate() != null)
                ano.setDateReso(DateConvert.convert(LocalDate.class, anoRTC.getResolutionDate()));
        }

        // Mise � jour de l'�tat du lot et de la date de mise � jour
        IWorkItem lotRTC = controlRTC.recupWorkItemDepuisId(anoLotInt);
        EtatLot etatLot = EtatLot.from(controlRTC.recupEtatElement(lotRTC));
        if (ano.getLotRTC().getEtatLot() != etatLot)
        {
            LOGGER.info("Lot : " + anoLot + " - nouvel etat Lot : " + etatLot);
            controlRapport.addInfo(TypeInfo.LOTMAJ, anoLot, etatLot.toString());

            // Si on arrive en VMOA ou que l'on passe � livr� � l'�dition directement, on met l'anomalie � relancer
            if (etatLot == EtatLot.VMOA || (ano.getLotRTC().getEtatLot() != EtatLot.VMOA && etatLot == EtatLot.EDITION))
            {
                ano.setAction(TypeAction.RELANCER);
                controlRapport.addInfo(TypeInfo.ANOARELANCER, anoLot, null);
            }
            ano.getLotRTC().setEtatLot(etatLot);
        }
        ano.getLotRTC().setDateMajEtat(controlRTC.recupDatesEtatsLot(lotRTC).get(etatLot));
    }

    /*---------- METHODES PRIVEES ----------*/

    private void initChefService(CompoPbApps pbApps, String service)
    {
        ChefService respService = DaoFactory.getDao(ChefService.class).readAllMap().get(service);
        if (respService != null)
            pbApps.setChefService(respService.getNom());
        else
            pbApps.setChefService(Statics.INCONNU);
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

    /*---------- ACCESSEURS ----------*/
}
