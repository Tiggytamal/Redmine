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

import model.enums.TypeFichier;
import utilities.DateConvert;
import utilities.Statics;
import utilities.TechnicalException;

@XmlRootElement
public class FichiersXML implements XML, Modele
{
    /*---------- ATTRIBUTS ----------*/

    private Map<String, InfoClarity> mapClarity;
    private Map<String, LotSuiviPic> lotsPic;
    private Map<String, Boolean> mapApplis;
    private Map<String, RespService> mapRespService;
    private Map<TypeFichier, String> dateMaj;
    private Map<String, String> mapEditions;
    private Map<String, LotSuiviRTC> lotsRTC;
    private boolean controleOK;
    private static final String NOMFICHIER = "\\fichiers.xml";
    private static final String RESOURCE = "/resources/fichiers.xml";

    /*---------- CONSTRUCTEURS ----------*/

    FichiersXML()
    {
        mapClarity = new HashMap<>();
        lotsPic = new HashMap<>();
        lotsRTC = new HashMap<>();
        mapApplis = new HashMap<>();
        mapRespService = new HashMap<>();
        dateMaj = new EnumMap<>(TypeFichier.class);
        mapEditions = new HashMap<>();
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
        return new File(getClass().getResource(RESOURCE).getFile());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void majMapDonnees(TypeFichier typeFichier, Map map)
    {
        switch (typeFichier)
        {
            case APPS:
                mapApplis.clear();
                mapApplis.putAll(map);
                setDateFichier(typeFichier);
                break;

            case CLARITY:
                mapClarity.clear();
                mapClarity.putAll(map);
                setDateFichier(typeFichier);
                break;

            case EDITION:
                mapEditions.clear();
                mapEditions.putAll(map);
                setDateFichier(typeFichier);
                break;

            case LOTSPICS:
                lotsPic.clear();
                lotsPic.putAll(map);
                setDateFichier(typeFichier);
                break;

            case RESPSERVICE:
                mapRespService.clear();
                mapRespService.putAll(map);
                setDateFichier(typeFichier);
                break;
                
            case LOTSRTC:
                lotsRTC.clear();
                lotsRTC.putAll(map);
                setDateFichier(typeFichier);
                break;

            default:
                throw new TechnicalException("FichiersXML.majMapDonnees - Type de fichier non g�r� :" + typeFichier.toString(), null);
        }
    }

    /**
     * Permet de mettre � jour la date � laquel le fichier a �t� mis � jour.
     * 
     * @param fichier
     *            Type du fichier mis � jour
     */
    public void setDateFichier(TypeFichier fichier)
    {
        dateMaj.put(fichier, LocalDate.now().format(DateConvert.FORMATTER));
    }

    @Override
    public String controleDonnees()
    {
        StringBuilder builder = new StringBuilder("Chargement fichiers Excel :").append(NL);

        // Contr�le lots Pic
        controleMap(lotsPic, builder, "Lots Pic", TypeFichier.LOTSPICS);

        // Contr�le liste application
        controleMap(mapApplis, builder, "Liste des applications", TypeFichier.APPS);

        // Contr�le Referentiel Clarity
        controleMap(mapClarity, builder, "Referentiel Clarity", TypeFichier.CLARITY);

        // Contr�le Referentiel Clarity
        controleMap(mapRespService, builder, "Responsables de services", TypeFichier.RESPSERVICE);

        // Contr�le Editions
        controleMap(mapEditions, builder, "Editions Pic", TypeFichier.EDITION);
        
        // Contr�le Lots RTC
        controleMap(lotsRTC, builder, "lots RTC", TypeFichier.LOTSRTC);

        if (!controleOK)
            builder.append("Merci de recharger le(s) fichier(s) de param�trage.").append(NL);

        return builder.append(NL).toString();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Permet de controler si une map est vide ou non, et met � jour le message.
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
            builder.append(texte).append(" non charg�(e)s.").append(NL);
            controleOK = false;
        }
        else
            builder.append(texte).append(" charg�(e)s. Derni�re Maj : ").append(dateMaj.get(typeFichier)).append(NL);
    }

    /*---------- ACCESSEURS ----------*/

    @XmlElementWrapper
    @XmlElement(name = "mapApplis", required = false)
    public Map<String, Boolean> getMapApplis()
    {
        return mapApplis;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapClarity", required = false)
    public Map<String, InfoClarity> getMapClarity()
    {
        return mapClarity;
    }

    @XmlElementWrapper
    @XmlElement(name = "maplotsPic", required = false)
    public Map<String, LotSuiviPic> getLotsPic()
    {
        return lotsPic;
    }

    @XmlElementWrapper
    @XmlElement(name = "dateMaj", required = false)
    public Map<TypeFichier, String> getDateMaj()
    {
        return dateMaj;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapRespService", required = false)
    public Map<String, RespService> getMapRespService()
    {
        return mapRespService;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapEditions", required = false)
    public Map<String, String> getMapEditions()
    {
        return mapEditions;
    }
    
    @XmlElementWrapper
    @XmlElement(name = "mapLotsRTC", required = false)
    public Map<String, LotSuiviRTC> getLotsRTC()
    {
        return lotsRTC;
    }
}