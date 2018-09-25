package model;

import static utilities.Statics.NL;

import java.io.File;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import junit.JunitBase;
import model.bdd.LotRTC;
import model.enums.TypeFichier;
import model.utilities.AbstractModele;
import model.utilities.XML;
import utilities.DateConvert;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe représentant le fichier XML de sauvegarde des données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class FichiersXML extends AbstractModele implements XML
{
    /*---------- ATTRIBUTS ----------*/

    //Constantes statiques
    private static final String NOMFICHIER = "\\fichiers.xml";
    private static final String RESOURCE = "/fichiers.xml";
    
    private Map<TypeFichier, String> dateMaj;
    private Map<String, LotRTC> mapLotsRTC;
    private Map<String, String> mapProjetsNpc;

    private boolean controleOK;

    /*---------- CONSTRUCTEURS ----------*/

    FichiersXML()
    {
        mapLotsRTC = new HashMap<>();
        dateMaj = new EnumMap<>(TypeFichier.class);
        mapProjetsNpc = new HashMap<>();
        controleOK = true;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public File getFile()
    {
        return new File(Statics.JARPATH + NOMFICHIER);
    }

    @Override
    public File getResource()
    {
        return new File(JunitBase.class.getResource(RESOURCE).getFile());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public FichiersXML majMapDonnees(TypeFichier typeFichier, Map map)
    {
        if (map == null)
            return this;
        
        switch (typeFichier)
        {
            case LOTSRTC:
                mapLotsRTC.clear();
                mapLotsRTC.putAll(map);
                setDateFichier(typeFichier);
                break;
                
            case NPC:
                mapProjetsNpc.clear();
                mapProjetsNpc.putAll(map);
                setDateFichier(typeFichier);
                break;

            default:
                throw new TechnicalException("FichiersXML.majMapDonnees - Type de fichier non géré :" + typeFichier.toString(), null);
        }

        return this;
    }

    @Override
    public String controleDonnees()
    {
        StringBuilder builder = new StringBuilder("Chargement fichiers Excel :").append(NL);
        controleOK = true;

        // Contrôle Lots RTC
        controleMap(mapLotsRTC, builder, "lots RTC", TypeFichier.LOTSRTC);

        if (!controleOK)
            builder.append("Merci de recharger le(s) fichier(s) de paramétrage.").append(NL);

        return builder.append(NL).toString();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Permet de mettre à jour la date à laquel le fichier a été mis à jour.
     * 
     * @param fichier
     *            Type du fichier mis à jour
     */
    private void setDateFichier(TypeFichier fichier)
    {
        dateMaj.put(fichier, LocalDate.now().format(DateConvert.FORMATTER));
    }

    /**
     * Permet de controler si une map est vide ou non, et met à jour le message.
     * 
     * @param map
     * @param builder
     * @param texte
     * @param typeFichier
     */
    private void controleMap(@SuppressWarnings("rawtypes") Map map, StringBuilder builder, String texte, TypeFichier typeFichier)
    {
        if (map.isEmpty())
        {
            builder.append(texte).append(" non chargé(e).").append(NL);
            controleOK = false;
        }
        else
            builder.append(texte).append(" chargé(e)s. Dernière Maj : ").append(dateMaj.get(typeFichier)).append(NL);
    }

    /*---------- ACCESSEURS ----------*/

    @XmlElementWrapper
    @XmlElement(name = "dateMaj", required = false)
    public Map<TypeFichier, String> getDateMaj()
    {
        return dateMaj;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapLotsRTC", required = false)
    public Map<String, LotRTC> getMapLotsRTC()
    {
        return mapLotsRTC;
    }
    
    @XmlElementWrapper
    @XmlElement(name = "mapProjetsNpc", required = false)
    public Map<String, String> getMapProjetsNpc()
    {
        return mapProjetsNpc;
    }
}
