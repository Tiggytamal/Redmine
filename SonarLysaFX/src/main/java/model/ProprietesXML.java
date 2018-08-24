package model;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import junit.JunitBase;
import model.enums.Param;
import model.enums.ParamBool;
import model.enums.ParamSpec;
import model.enums.TypeColApps;
import model.enums.TypeColChefServ;
import model.enums.TypeColClarity;
import model.enums.TypeColEdition;
import model.enums.TypeColNPC;
import model.enums.TypeColPic;
import model.enums.TypeColR;
import model.enums.TypeColSuivi;
import model.enums.TypeColVul;
import model.enums.TypePlan;
import model.utilities.AbstractModele;
import model.utilities.XML;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Fichier regroupant les paramètres XML du programme
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
public class ProprietesXML extends AbstractModele implements XML
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final String NOMFICHIER = "\\proprietes.xml";
    private static final String RESOURCE = "/proprietes.xml";
    
    // Map des paramètres
    private Map<Param, String> mapParams;
    private Map<ParamBool, Boolean> mapParamsBool;
    private Map<ParamSpec, String> mapParamsSpec;

    // Map des colonnes
    private Map<TypeColSuivi, String> mapColsSuivi;
    private Map<TypeColClarity, String> mapColsClarity;
    private Map<TypeColChefServ, String> mapColsChefServ;
    private Map<TypeColPic, String> mapColsPic;
    private Map<TypeColEdition, String> mapColsEdition;
    private Map<TypeColApps, String> mapColsApps;
    private Map<TypeColNPC, String> mapColsNPC;
    
    // Map des colonnes avec indice pour écriture
    private Map<TypeColVul, Colonne> mapColsVul;

    // Map planificateurs
    private Map<TypePlan, Planificateur> mapPlans;

    /*---------- CONSTRUCTEURS ----------*/

    ProprietesXML()
    {
        mapParams = new EnumMap<>(Param.class);
        mapColsSuivi = new EnumMap<>(TypeColSuivi.class);
        mapColsClarity = new EnumMap<>(TypeColClarity.class);
        mapColsChefServ = new EnumMap<>(TypeColChefServ.class);
        mapColsPic = new EnumMap<>(TypeColPic.class);
        mapColsEdition = new EnumMap<>(TypeColEdition.class);
        mapColsApps = new EnumMap<>(TypeColApps.class);
        mapColsNPC = new EnumMap<>(TypeColNPC.class);
        mapColsVul = new EnumMap<>(TypeColVul.class);
        mapPlans = new EnumMap<>(TypePlan.class);
        mapParamsBool = new EnumMap<>(ParamBool.class);
        mapParamsSpec = new EnumMap<>(ParamSpec.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String controleDonnees()
    {
        StringBuilder builder = new StringBuilder("Chargement paramètres :").append(Statics.NL);

        boolean bool1 = controleCols(builder);
        boolean bool2 = controleParams(builder);
        boolean bool3 = controlePlanificateurs(builder);
        
        // Message OK
        if (!bool1 || !bool2 || !bool3)
            builder.append("Merci de changer les paramètres en option ou de recharger les fichiers de paramétrage.");

        return builder.toString();
    }

    /**
     * Retourne toutes les map de gestions des colonnes
     * 
     * @param typeColClass
     * @return
     */
    @SuppressWarnings({ "unchecked" })
    public <T extends Enum<T>> Map<T, String> getEnumMapR(Class<T> typeColClass)
    {
        switch (typeColClass.getName())
        {
            case "model.enums.TypeColSuivi" :
                return (Map<T, String>) getMapColsSuivi();

            case "model.enums.TypeColClarity" :
                return (Map<T, String>) getMapColsClarity();

            case "model.enums.TypeColChefServ" :
                return (Map<T, String>) getMapColsChefServ();

            case "model.enums.TypeColPic" :
                return (Map<T, String>) getMapColsPic();

            case "model.enums.TypeColEdition" :
                return (Map<T, String>) getMapColsEdition();

            case "model.enums.TypeColApps" :
                return (Map<T, String>) getMapColsApps();
                
            case "model.enums.TypeColNPC" :
                return (Map<T, String>) getMapColsNPC();

            default:
                throw new TechnicalException("Type non géré :" + typeColClass.toString(), null);
        }
    }
    
    /**
     * Retourne toutes les map de gestions des colonnes
     * 
     * @param typeColClass
     * @return
     */
    @SuppressWarnings({ "unchecked" })
    public <T extends Enum<T>> Map<T, Colonne> getEnumMapW(Class<T> typeColClass)
    {
        switch (typeColClass.getName())
        {
            case "model.enums.TypeColVul" :
                return (Map<T, Colonne>) getMapColsVul();
                
//            case "model.enums.TypecolPbApps" :
//                return (Map<T, Colonne>) getMapColsPbApps();

            default:
                throw new TechnicalException("Type non géré :" + typeColClass.toString(), null);
        }
    }

    /**
     * Retourne la liste des colonnes avec clefs et valeurs inversées
     * 
     * @return
     */
    public <T extends Enum<T> & TypeColR> Map<String, T> getMapColsInvert(Class<T> typeColClass)
    {
        Map<String, T> retour = new HashMap<>();
        for (Object entry : getEnumMapR(typeColClass).entrySet())
        {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            Map.Entry<T, String> test = (Map.Entry) entry;
            retour.put((String) test.getValue(), (T) test.getKey());
        }
        return retour;
    }

    /*---------- ACCESSEURS ----------*/

    @XmlElementWrapper
    @XmlElement(name = "mapParams", required = false)
    public Map<Param, String> getMapParams()
    {
        return mapParams;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapParamsBool", required = false)
    public Map<ParamBool, Boolean> getMapParamsBool()
    {
        return mapParamsBool;
    }
    
    @XmlElementWrapper
    @XmlElement(name = "mapParamsSpec", required = false)
    public Map<ParamSpec, String> getMapParamsSpec()
    {
        return mapParamsSpec;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapPlans", required = false)
    public Map<TypePlan, Planificateur> getMapPlans()
    {
        return mapPlans;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsSuivi", required = false)
    private Map<TypeColSuivi, String> getMapColsSuivi()
    {
        return mapColsSuivi;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsClarity", required = false)
    private Map<TypeColClarity, String> getMapColsClarity()
    {
        return mapColsClarity;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsChefServ", required = false)
    private Map<TypeColChefServ, String> getMapColsChefServ()
    {
        return mapColsChefServ;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsPic", required = false)
    private Map<TypeColPic, String> getMapColsPic()
    {
        return mapColsPic;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsEdition", required = false)
    private Map<TypeColEdition, String> getMapColsEdition()
    {
        return mapColsEdition;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsApps", required = false)
    private Map<TypeColApps, String> getMapColsApps()
    {
        return mapColsApps;
    }
    
    @XmlElementWrapper
    @XmlElement(name = "mapColsNPC", required = false)
    private Map<TypeColNPC, String> getMapColsNPC()
    {
        return mapColsNPC;
    }
    
    @XmlElementWrapper
    @XmlElement(name = "mapColsVul", required = false)
    private Map<TypeColVul, Colonne> getMapColsVul()
    {
        return mapColsVul;
    }

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

    /*---------- METHODES PRIVEES ----------*/

    private boolean controleCols(StringBuilder builder)
    {
        // Parcours de tous les types de colonnes
        StringBuilder builderErreurs = new StringBuilder();

        controleMap(builderErreurs, TypeColSuivi.class, mapColsSuivi);
        controleMap(builderErreurs, TypeColClarity.class, mapColsClarity);
        controleMap(builderErreurs, TypeColChefServ.class, mapColsChefServ);
        controleMap(builderErreurs, TypeColPic.class, mapColsPic);
        controleMap(builderErreurs, TypeColEdition.class, mapColsEdition);
        controleMap(builderErreurs, TypeColApps.class, mapColsApps);
        controleMap(builderErreurs, TypeColNPC.class, mapColsNPC);

        // Renvoi du booleen
        if (builderErreurs.length() == 0)
        {
            builder.append("Nom colonnes OK").append(Statics.NL);
            return true;
        }
        else
        {
            builder.append("Certaines colonnes sont mal renseignées :").append(Statics.NL).append(builderErreurs.toString());
            return false;
        }
    }

    /**
     * Méthodes générique pour controler les valeur d'une map
     * 
     * @param builderErreurs
     * @param typeCol
     * @param mapCols
     */
    private <T extends Enum<T> & TypeColR> void controleMap(StringBuilder builderErreurs, Class<T> typeCol, Map<T, String> mapCols)
    {
        for (T type : typeCol.getEnumConstants())
        {
            if (mapCols.get(type) == null || mapCols.get(type).isEmpty())
                builderErreurs.append(type.getValeur()).append(Statics.NL);
        }
    }

    /**
     * 
     * @param builder
     * @return
     */
    private boolean controleParams(StringBuilder builder)
    {
        // Contrôle des paramètres
        StringBuilder builderErreurs = new StringBuilder();

        // Paramètres classiques
        for (Param param : Param.values())
        {
            if (mapParams.get(param) == null || mapParams.get(param).isEmpty())
                builderErreurs.append(param.getNom()).append(Statics.NL);
        }

        // Paramètres spéciaux
        for (ParamSpec param : ParamSpec.values())
        {
            if (mapParamsSpec.get(param) == null || mapParamsSpec.get(param).isEmpty())
                builderErreurs.append(param.getNom()).append(Statics.NL);
        }
        
        // Paramètres booléens
        for (ParamBool param : ParamBool.values())
        {
            if (mapParamsBool.get(param) == null)
                builderErreurs.append(param.getNom()).append(Statics.NL);
        }

        if (builderErreurs.length() == 0)
        {
            builder.append("Paramètres OK").append(Statics.NL);
            return true;
        }
        else
        {
            builder.append("Certains paramètres sont mal renseignés :").append(Statics.NL).append(builderErreurs.toString());
            return false;
        }
    }

    /**
     * 
     * @param
     * @return
     */
    private boolean controlePlanificateurs(StringBuilder builder)
    {
        // Contrôle des planificateurs
        StringBuilder builderErreurs = new StringBuilder();
        for (TypePlan typePlan : TypePlan.values())
        {
            if (mapPlans.get(typePlan) == null)
                builderErreurs.append(typePlan.getValeur()).append(Statics.NL);
        }
        if (builderErreurs.length() == 0)
        {
            builder.append("Planificateurs OK").append(Statics.NL);
            return true;
        }
        else
        {
            builder.append("Certains planificateurs ne sont pas paramétrés :").append(Statics.NL).append(builderErreurs.toString());
            return false;
        }
    }
}
