package model.utilities;

import static utilities.Statics.fichiersXML;

import java.time.LocalDate;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IWorkItem;

import control.rtc.ControlRTC;
import control.word.ControlRapport;
import model.Anomalie;
import model.CompoPbApps;
import model.InfoClarity;
import model.RespService;
import model.enums.EtatLot;
import model.enums.TypeAction;
import model.enums.TypeInfo;
import utilities.DateConvert;
import utilities.Statics;

/**
 * Classe de contrôle des informations d'un modèle
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
public class ControlModelInfo
{
    /*---------- ATTRIBUTS ----------*/

    /** logger général */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    
    /** logger composants avec application INCONNUE */
    private static final Logger LOGINCONNUE = LogManager.getLogger("inconnue-log");
    
    private static final short CLARITYMINI = 5;
    private static final short CLARITYMAX = 9;
    private static final short CLARITY7 = 7;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Contrôle si le code clarity de l'anomalie est bien dans le fichier Excel et renseigne les informations depuis celui-ci
     * 
     * @param ano
     */
    public void controleClarity(Anomalie ano, ControlRapport controlRapport)
    {
        // Récupération infox Clarity depuis fichier Excel
        String anoClarity = ano.getProjetClarity();
        if (anoClarity.isEmpty())
            return;
        Map<String, InfoClarity> map = fichiersXML.getMapClarity();

        // Vérification si le code Clarity de l'anomalie est bien dans la map
        if (map.containsKey(anoClarity))
        {
            ano.majDepuisClarity(map.get(anoClarity));
            return;
        }

        String temp = Statics.EMPTY;
        boolean testT7 = anoClarity.startsWith("T") && anoClarity.length() == CLARITY7;

        // Sinon on itère sur les clefs en supprimant les indices de lot, et on prend la première clef correspondante
        for (Map.Entry<String, InfoClarity> entry : map.entrySet())
        {
            String key = entry.getKey();

            // On retire les deux dernières lettres pour les clefs de plus de 6 caractères finissants par 0[1-9]
            if (controleKey(anoClarity, key))
            {
                ano.majDepuisClarity(entry.getValue());
                return;
            }

            // On récupère la clef correxpondante la plus élevée dans le cas des clef commençants par T avec 2 caractères manquants
            if (testT7 && key.contains(anoClarity) && key.compareTo(temp) > 0)
                temp = key;
        }

        if (!temp.isEmpty())
        {
            ano.majDepuisClarity(map.get(temp));
            return;
        }

        // Si on ne trouve pas, on renvoie juste l'anomalie avec le log d'erreur
        LOGINCONNUE.warn("Code Clarity inconnu : " + anoClarity + " - " + ano.getLot());
        controlRapport.addInfo(TypeInfo.CLARITYINCONNU, ano.getLot(), anoClarity);
        ano.setDepartement(Statics.INCONNU);
        ano.setService(Statics.INCONNU);
        ano.setDirection(Statics.INCONNUE);
        ano.setResponsableService(Statics.INCONNU);
    }

    /**
     * récupère les info depuis le fichier Clarity et le fichier des chefs de service pour les composants avec des problème ssu les codes application
     * 
     * @param compo
     */
    public void controleClarity(CompoPbApps pbApps, String anoClarity)
    {
        // Récupération infox Clarity depuis fichier Excel
        if (anoClarity.isEmpty())
            return;
        
        Map<String, InfoClarity> map = fichiersXML.getMapClarity();

        // Vérification si le code Clarity de l'anomalie est bien dans la map
        if (map.containsKey(anoClarity))
        {
            pbApps.majDepuisClarity(map.get(anoClarity));
            initChefService(pbApps, map.get(anoClarity).getService());
            return;
        }

        String temp = Statics.EMPTY;
        boolean testT7 = anoClarity.startsWith("T") && anoClarity.length() == CLARITY7;

        // Sinon on itère sur les clefs en supprimant les indices de lot, et on prend la première clef correspondante
        for (Map.Entry<String, InfoClarity> entry : map.entrySet())
        {
            String key = entry.getKey();

            // On retire les deux dernières lettres pour les clefs de plus de 6 caractères finissants par 0[1-9]
            if (controleKey(anoClarity, key))
            {
                pbApps.majDepuisClarity(entry.getValue());
                initChefService(pbApps, entry.getValue().getService());
                return;
            }

            // On récupère la clef correxpondante la plus élevée dans le cas des clef commençants par T avec 2 caractères manquants
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
    
    /**
     * @param ano
     * @return
     * @throws TeamRepositoryException
     */
    public void controleRTC(Anomalie ano, ControlRapport controlMail, ControlRTC controlRTC) throws TeamRepositoryException
    {
        // Controle sur l'état de l'anomalie (projet Clarity, lot et numéro anomalie renseignée
        String anoLot = ano.getLot().substring(Statics.SBTRINGLOT);
        int anoLotInt = Integer.parseInt(anoLot);

        // Controle si le projet RTC est renseigné. Sinon on le récupère depuis Jazz avec le numéro de lot
        if (ano.getProjetRTC().isEmpty())
            ano.setProjetRTC(controlRTC.recupProjetRTCDepuisWiLot(anoLotInt));

        // Mise à jour de l'état de l'anomalie ainsi que les dates de résolution et de création
        if (ano.getNumeroAnomalie() != 0)
        {
            IWorkItem anoRTC = controlRTC.recupWorkItemDepuisId(ano.getNumeroAnomalie());
            String newEtat = controlRTC.recupEtatElement(anoRTC);
            if (!newEtat.equals(ano.getEtat()))
            {
                LOGGER.info("Lot : " + anoLot + " - nouvel etat anomalie : " + newEtat);
                controlMail.addInfo(TypeInfo.ANOMAJ, anoLot, newEtat);
                ano.setEtat(newEtat);
            }
            ano.setDateCreation(DateConvert.convert(LocalDate.class, anoRTC.getCreationDate()));
            if (anoRTC.getResolutionDate() != null)
                ano.setDateReso(DateConvert.convert(LocalDate.class, anoRTC.getResolutionDate()));
        }

        // Mise à jour de l'état du lot et de la date de mise à jour
        IWorkItem lotRTC = controlRTC.recupWorkItemDepuisId(anoLotInt);
        EtatLot etatLot = EtatLot.from(controlRTC.recupEtatElement(lotRTC));
        if (ano.getEtatLot() != etatLot)
        {
            LOGGER.info("Lot : " + anoLot + " - nouvel etat Lot : " + etatLot);
            controlMail.addInfo(TypeInfo.LOTMAJ, anoLot, etatLot.toString());

            // Si on arrive en VMOA ou que l'on passe à livré à l'édition directement, on met l'anomalie à relancer
            if (etatLot == EtatLot.VMOA || (ano.getEtatLot() != EtatLot.VMOA && etatLot == EtatLot.EDITION))
            {
                ano.setAction(TypeAction.RELANCER);
                controlMail.addInfo(TypeInfo.ANOARELANCER, anoLot, null);
            }
            ano.setEtatLot(etatLot);
        }
        ano.setDateMajEtat(controlRTC.recupDatesEtatsLot(lotRTC).get(etatLot));
    }
    
    /**
     * Met à jour le responsable de service depuis les informations du fichier XML, si le service est renseigné.<br>
     * Remonte un warning si le service n'est pas connu
     * 
     * @param ano
     * @param mapRespService
     */
    public void controleChefDeService(Anomalie ano, ControlRapport controlMail)
    {
        // Controle définition du service pour l'anomalie
        String anoServ = ano.getService();
        if (anoServ.isEmpty())
            return;

        // Recherche du responsable dans les paramètres et remontée d'info si non trouvé.
        Map<String, RespService> mapRespService = fichiersXML.getMapRespService();
        if (mapRespService.containsKey(anoServ))
            ano.setResponsableService(mapRespService.get(anoServ).getNom());
        else
        {
            LOGINCONNUE.warn("Pas de responsable de service trouvé pour ce service : " + ano.getService());
            controlMail.addInfo(TypeInfo.SERVICESSANSRESP, ano.getLot(), ano.getService());
        }
    }
    
    /**
     * Met à jour le champ NPC si le projet en fait parti
     * 
     * @param ano
     * 
     */
    public void controleNPC(Anomalie ano)
    {
        if (Statics.fichiersXML.getMapProjetsNpc().containsKey(ano.getProjetRTC()))
            ano.setNpc("X");
        else
            ano.setNpc(Statics.EMPTY);
    }

    /*---------- METHODES PRIVEES ----------*/

    private void initChefService(CompoPbApps pbApps, String service)
    {
        RespService respService = Statics.fichiersXML.getMapRespService().get(service);
        if (respService != null)
            pbApps.setChefService(respService.getNom());
        else
            pbApps.setChefService(Statics.INCONNU);
    }

    /**
     * Contrôle les valeurs du code Clarity en prenant en compte les différentes erreurs possibles
     * 
     * @param anoClarity
     * @param key
     * @return
     */
    private boolean controleKey(String anoClarity, String key)
    {
        // Retourne une clef avec 6 caractères maximum si celle-ci finie par un numéro de lot
        String smallkey = key.length() > CLARITYMINI && key.matches(".*0[0-9E]$") ? key.substring(0, CLARITYMINI + 1) : key;

        // Contrôle si la clef est de type T* ou P*.
        String newKey = key.length() == CLARITYMAX && (key.startsWith("T") || key.startsWith("P")) ? key.substring(0, CLARITYMAX - 1) : smallkey;

        // Retourne la clef clairity de l'anomalie avec 6 caractères maximum si celle-ci finie par un numéro de lot
        String smallClarity = anoClarity.length() > CLARITYMINI && anoClarity.matches(".*0[0-9E]$") ? anoClarity.substring(0, CLARITYMINI + 1) : anoClarity;

        // Contrôle si la clef est de type T* ou P*.
        String newClarity = anoClarity.length() == CLARITYMAX && (anoClarity.startsWith("T") || anoClarity.startsWith("P")) ? anoClarity.substring(0, CLARITYMAX - 1) : smallClarity;

        // remplace le dernier du coade Clarity par 0.
        String lastClarity = anoClarity.replace(anoClarity.charAt(anoClarity.length() - 1), '0');

        return anoClarity.equalsIgnoreCase(newKey) || newClarity.equalsIgnoreCase(key) || lastClarity.equalsIgnoreCase(key);
    }

    /*---------- ACCESSEURS ----------*/
}
