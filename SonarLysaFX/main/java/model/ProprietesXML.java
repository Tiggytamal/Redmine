package model;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.enums.ParamBool;
import model.enums.TypeCol;
import model.enums.TypeColApps;
import model.enums.TypeColChefServ;
import model.enums.TypeColClarity;
import model.enums.TypeColEdition;
import model.enums.TypeColPic;
import model.enums.TypeColSuivi;
import model.enums.Param;
import model.enums.ParamSpec;
import model.enums.TypePlan;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Fichier regroupant les param�tres XML du programme
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 */
@XmlRootElement
public class ProprietesXML implements XML, Modele
{
    /*---------- ATTRIBUTS ----------*/

    // Map des param�tres
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
    
    // Map planificateurs
    private Map<TypePlan, Planificateur> mapPlans;

    private static final String NOMFICHIER = "\\proprietes.xml";
    private static final String RESOURCE = "/resources/proprietes.xml";

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
        mapPlans = new EnumMap<>(TypePlan.class);
        mapParamsBool = new EnumMap<>(ParamBool.class);
        mapParamsSpec = new EnumMap<>(ParamSpec.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String controleDonnees()
    {
        StringBuilder builder = new StringBuilder("Chargement param�tres :").append(Statics.NL);

        // Message OK
        if (!controleCols(builder) || !controleParams(builder) || !controlePlanificateurs(builder))
            builder.append("Merci de changer les param�tres en option ou de recharger le fichier par d�faut.");

        return builder.toString();
    }

    /**
     * Retourne toutes les map de gestions des colonnes
     * @param typeColClass
     * @return
     */
    @SuppressWarnings ("rawtypes")
    public <T extends Enum<T> & TypeCol> Map getMap(Class<T> typeColClass)
    {
        switch (typeColClass.getName())
        {
            case "model.enums.TypeColSuivi" :
                return mapColsSuivi;
            
            case "model.enums.TypeColClarity" :
                return mapColsClarity;
                
            case "model.enums.TypeColChefServ" :
                return mapColsChefServ;
            
            case "model.enums.TypeColPic" :
                return mapColsPic;
                
            case "model.enums.TypeColEdition" :
                return mapColsEdition;
                
            case "model.enums.TypeColApps" :
                return mapColsApps;
                
            default :
                throw new TechnicalException("Type non g�r� :" + typeColClass.toString(), null);
        }        
    }
    
    /**
     * Retourne la liste des colonnes avec clefs et valeurs invers�es
     * @return
     */
    public <T extends Enum<T> & TypeCol> Map<String, T> getMapColsInvert(Class<T> typeColClass)
    {
        Map<String, T> retour = new HashMap<>();
        for (Object entry : getMap(typeColClass).entrySet())
        {
            @SuppressWarnings ({ "unchecked", "rawtypes" })
            Map.Entry<T, String> test = (Map.Entry) entry;
            retour.put((String)test.getValue(), (T)test.getKey());
        }
        return retour;
    }
    
    /*---------- ACCESSEURS ----------*/

    @XmlElementWrapper
    @XmlElement (name = "mapParams", required = false)
    public Map<Param, String> getMapParams()
    {
        return mapParams;
    }

    @XmlElementWrapper
    @XmlElement (name = "mapParamsBool", required = false)
    public Map<ParamBool, Boolean> getMapParamsBool()
    {
        return mapParamsBool;
    }
    
    @XmlElementWrapper
    @XmlElement (name = "mapPlans", required = false)
    public Map<TypePlan, Planificateur> getMapPlans()
    {
        return mapPlans;
    }
    
    @XmlElementWrapper
    @XmlElement (name = "mapColsSuivi", required = false)
    private Map<TypeColSuivi, String> getMapColsSuivi()
    {
        return mapColsSuivi;
    }    
    
    @XmlElementWrapper
    @XmlElement (name = "mapColsClarity", required = false)
    private Map<TypeColClarity, String> getMapColsClarity()
    {
        return mapColsClarity;
    }
    
    @XmlElementWrapper
    @XmlElement (name = "mapColsChefServ", required = false)
    private Map<TypeColChefServ, String> getMapColsChefServ()
    {
        return mapColsChefServ;
    }
    
    @XmlElementWrapper
    @XmlElement (name = "mapColsPic", required = false)
    private Map<TypeColPic, String> getMapColsPic()
    {
        return mapColsPic;
    }
    
    @XmlElementWrapper
    @XmlElement (name = "mapColsEdition", required = false)
    private Map<TypeColEdition, String> getMapColsEdition()
    {
        return mapColsEdition;
    }
    
    @XmlElementWrapper
    @XmlElement (name = "mapColsApps", required = false)
    private Map<TypeColApps, String> getMapColsApps()
    {
        return mapColsApps;
    }
    
    @XmlElementWrapper
    @XmlElement (name = "mapParamsSpec", required = false)
    public Map<ParamSpec, String> getMapParamsSpec()
    {
        return mapParamsSpec;
    }

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


        // Renvoi du booleen
        if (builderErreurs.length() == 0)
        {
            builder.append("Nom colonnes OK").append(Statics.NL);
            return true;
        }
        else
        {
            builder.append("Certaines colonnes sont mal renseign�es :").append(Statics.NL).append(builderErreurs.toString());
            return false;
        }
    }
    
    /**
     * M�thodes g�n�rique pour controler les valeur d'une map
     * @param builderErreurs
     * @param typeCol
     * @param mapCols
     */
    private <T extends Enum<T> & TypeCol> void controleMap(StringBuilder builderErreurs, Class<T> typeCol, Map<T, String> mapCols)
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
        // Contr�le des param�tres
        StringBuilder builderErreurs = new StringBuilder();
        
        // Param�tres classiques
        for (Param typeParam : Param.values())
        {
            if (mapParams.get(typeParam) == null || mapParams.get(typeParam).isEmpty())
                builderErreurs.append(typeParam.toString()).append(Statics.NL);
        }
        
        // Param�tres sp�ciaux
        for (ParamSpec typeParam : ParamSpec.values())
        {
            if (mapParamsSpec.get(typeParam) == null || mapParamsSpec.get(typeParam).isEmpty())
                builderErreurs.append(typeParam.toString()).append(Statics.NL);
        }
        
        if (builderErreurs.length() == 0)
        {
            builder.append("Param�tres OK").append(Statics.NL);
            return true;
        }
        else
        {
            builder.append("Certains param�tres sont mal renseign�s :").append(Statics.NL).append(builderErreurs.toString());
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
        // Contr�le des planificateurs
        StringBuilder builderErreurs = new StringBuilder();
        for (TypePlan typePlan : TypePlan.values())
        {
            if (mapPlans.get(typePlan) == null)
                builderErreurs.append(typePlan.toString()).append(Statics.NL);
        }
        if (builderErreurs.length() == 0)
        {
            builder.append("Planificateurs OK").append(Statics.NL);
            return true;
        }
        else
        {
            builder.append("Certains planificateurs ne sont pas param�tr�s :").append(Statics.NL).append(builderErreurs.toString());
            return false;
        }
    }
}