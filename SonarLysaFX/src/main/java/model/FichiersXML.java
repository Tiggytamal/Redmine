package model;

import static utilities.Statics.NL;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import junit.JunitBase;
import model.enums.TypeFichier;
import utilities.DateConvert;
import utilities.Statics;
import utilities.TechnicalException;

@XmlRootElement
public class FichiersXML implements XML, Modele
{
    /*---------- ATTRIBUTS ----------*/

    private Map<String, InfoClarity> mapClarity;
    private Map<String, Application> mapApplis;
    private Map<String, RespService> mapRespService;
    private Map<TypeFichier, String> dateMaj;
    private Map<String, String> mapEditions;
    private Map<String, LotSuiviRTC> lotsRTC;
    private Map<String, ComposantSonar> mapComposSonar;

    private boolean controleOK;
    private static final String NOMFICHIER = "\\fichiers.xml";
    private static final String RESOURCE = "/fichiers.xml";

    /*---------- CONSTRUCTEURS ----------*/

    FichiersXML()
    {
        mapClarity = new HashMap<>();
        lotsRTC = new HashMap<>();
        mapApplis = new HashMap<>();
        mapRespService = new HashMap<>();
        mapComposSonar = new HashMap<>();
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
        return new File(JunitBase.class.getResource(RESOURCE).getFile());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public FichiersXML majMapDonnees(TypeFichier typeFichier, Map map)
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

            case SONAR:
                mapComposSonar.clear();
                mapComposSonar.putAll(map);
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

        // Contrôle liste application
        controleMap(mapApplis, builder, "Liste des applications", TypeFichier.APPS);

        // Contrôle Referentiel Clarity
        controleMap(mapClarity, builder, "Referentiel Clarity", TypeFichier.CLARITY);

        // Contrôle Referentiel Clarity
        controleMap(mapRespService, builder, "Responsables de services", TypeFichier.RESPSERVICE);

        // Contrôle Editions
        controleMap(mapEditions, builder, "Editions Pic", TypeFichier.EDITION);

        // Contrôle Lots RTC
        controleMap(lotsRTC, builder, "lots RTC", TypeFichier.LOTSRTC);

        // Contrôle Composants Sonar
        controleMap(mapComposSonar, builder, "Composants Sonar", TypeFichier.SONAR);

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
            builder.append(texte).append(" non chargé(e)s.").append(NL);
            controleOK = false;
        }
        else
            builder.append(texte).append(" chargé(e)s. Dernière Maj : ").append(dateMaj.get(typeFichier)).append(NL);
    }

    /*---------- ACCESSEURS ----------*/

    @XmlElementWrapper
    @XmlElement(name = "mapApplis", required = false)
    public Map<String, Application> getMapApplis()
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

    @XmlElementWrapper
    @XmlElement(name = "mapComposSonar", required = false)
    public Map<String, ComposantSonar> getMapComposSonar()
    {
        return mapComposSonar;
    }

    /**
     * Retourne la map des composants Sonar sous forme de liste
     * 
     * @return
     */
    @XmlTransient
    public List<ComposantSonar> getListComposants()
    {
        List<ComposantSonar> retour = new ArrayList<>();
        retour.addAll(mapComposSonar.values());
        return retour;
    }
}
