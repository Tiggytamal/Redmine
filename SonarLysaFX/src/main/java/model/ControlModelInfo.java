package model;

import static utilities.Statics.fichiersXML;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.mail.ControlMail;
import model.enums.TypeInfoMail;
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

    private static final short CLARITYMINI = 5;
    private static final short CLARITYMAX = 9;
    private static final short CLARITY7 = 7;

    /** logger composants avec application INCONNUE */
    private static final Logger LOGINCONNUE = LogManager.getLogger("inconnue-log");

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Contr�le si le code clarity de l'anomalie est bien dans le fichier Excel et renseigne les informations depuis celui-ci
     * 
     * @param ano
     */
    public void controleClarity(Anomalie ano, ControlMail controlMail)
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
        controlMail.addInfo(TypeInfoMail.CLARITYINCONNU, ano.getLot(), anoClarity);
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
