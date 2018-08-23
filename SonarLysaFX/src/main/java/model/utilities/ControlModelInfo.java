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
    
    /** logger composants avec application INCONNUE */
    private static final Logger LOGINCONNUE = LogManager.getLogger("inconnue-log");
    
    private static final short CLARITYMINI = 5;
    private static final short CLARITYMAX = 9;
    private static final short CLARITY7 = 7;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Contr�le si le code clarity de l'anomalie est bien dans le fichier Excel et renseigne les informations depuis celui-ci
     * 
     * @param ano
     */
    public void controleClarity(Anomalie ano, ControlRapport controlRapport)
    {
        // R�cup�ration infox Clarity depuis fichier Excel
        String anoClarity = ano.getProjetClarity();
        if (anoClarity.isEmpty())
            return;
        Map<String, InfoClarity> map = fichiersXML.getMapClarity();

        // V�rification si le code Clarity de l'anomalie est bien dans la map
        if (map.containsKey(anoClarity))
        {
            ano.majDepuisClarity(map.get(anoClarity));
            return;
        }

        String temp = Statics.EMPTY;
        boolean testT7 = anoClarity.startsWith("T") && anoClarity.length() == CLARITY7;

        // Sinon on it�re sur les clefs en supprimant les indices de lot, et on prend la premi�re clef correspondante
        for (Map.Entry<String, InfoClarity> entry : map.entrySet())
        {
            String key = entry.getKey();

            // On retire les deux derni�res lettres pour les clefs de plus de 6 caract�res finissants par 0[1-9]
            if (controleKey(anoClarity, key))
            {
                ano.majDepuisClarity(entry.getValue());
                return;
            }

            // On r�cup�re la clef correxpondante la plus �lev�e dans le cas des clef commen�ants par T avec 2 caract�res manquants
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
     * r�cup�re les info depuis le fichier Clarity et le fichier des chefs de service pour les composants avec des probl�me ssu les codes application
     * 
     * @param compo
     */
    public void controleClarity(CompoPbApps pbApps, String anoClarity)
    {
        // R�cup�ration infox Clarity depuis fichier Excel
        if (anoClarity.isEmpty())
            return;
        
        Map<String, InfoClarity> map = fichiersXML.getMapClarity();

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
        for (Map.Entry<String, InfoClarity> entry : map.entrySet())
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
    
    /**
     * @param ano
     * @return
     * @throws TeamRepositoryException
     */
    public void controleRTC(Anomalie ano, ControlRapport controlMail, ControlRTC controlRTC) throws TeamRepositoryException
    {
        // Controle sur l'�tat de l'anomalie (projet Clarity, lot et num�ro anomalie renseign�e
        String anoLot = ano.getLot().substring(Statics.SBTRINGLOT);
        int anoLotInt = Integer.parseInt(anoLot);

        // Controle si le projet RTC est renseign�. Sinon on le r�cup�re depuis Jazz avec le num�ro de lot
        if (ano.getProjetRTC().isEmpty())
            ano.setProjetRTC(controlRTC.recupProjetRTCDepuisWiLot(anoLotInt));

        // Mise � jour de l'�tat de l'anomalie ainsi que les dates de r�solution et de cr�ation
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

        // Mise � jour de l'�tat du lot et de la date de mise � jour
        IWorkItem lotRTC = controlRTC.recupWorkItemDepuisId(anoLotInt);
        EtatLot etatLot = EtatLot.from(controlRTC.recupEtatElement(lotRTC));
        if (ano.getEtatLot() != etatLot)
        {
            LOGGER.info("Lot : " + anoLot + " - nouvel etat Lot : " + etatLot);
            controlMail.addInfo(TypeInfo.LOTMAJ, anoLot, etatLot.toString());

            // Si on arrive en VMOA ou que l'on passe � livr� � l'�dition directement, on met l'anomalie � relancer
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
     * Met � jour le responsable de service depuis les informations du fichier XML, si le service est renseign�.<br>
     * Remonte un warning si le service n'est pas connu
     * 
     * @param ano
     * @param mapRespService
     */
    public void controleChefDeService(Anomalie ano, ControlRapport controlMail)
    {
        // Controle d�finition du service pour l'anomalie
        String anoServ = ano.getService();
        if (anoServ.isEmpty())
            return;

        // Recherche du responsable dans les param�tres et remont�e d'info si non trouv�.
        Map<String, RespService> mapRespService = fichiersXML.getMapRespService();
        if (mapRespService.containsKey(anoServ))
            ano.setResponsableService(mapRespService.get(anoServ).getNom());
        else
        {
            LOGINCONNUE.warn("Pas de responsable de service trouv� pour ce service : " + ano.getService());
            controlMail.addInfo(TypeInfo.SERVICESSANSRESP, ano.getLot(), ano.getService());
        }
    }
    
    /**
     * Met � jour le champ NPC si le projet en fait parti
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
