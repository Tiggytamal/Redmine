package model;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.enums.TypeCol;
import model.enums.TypeColChefServ;
import model.enums.TypeColClarity;
import model.enums.TypeColEdition;
import model.enums.TypeColPic;
import model.enums.TypeColSuivi;
import model.enums.TypeParam;
import model.enums.TypePlan;
import utilities.Statics;

/**
 * Fichier regroupant les paramètres XML du programme
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
public class ProprietesXML implements XML
{
    /*---------- ATTRIBUTS ----------*/

    private Map<TypeParam, String> mapParams;
    private Map<TypeColSuivi, String> mapColonnesSuivi;    
    private Map<TypeColClarity, String> mapColonnesClarity;   
    private Map<TypeColChefServ, String> mapColonnesChefServ;
    private Map<TypeColPic, String> mapColonnesPic;
    private Map<TypeColEdition, String> mapColonnesEdition;   
    private Map<TypePlan, Planificateur> mapPlans;

    private static final String NOMFICHIER = "\\proprietes.xml";

    /*---------- CONSTRUCTEURS ----------*/

    public ProprietesXML()
    {
        mapParams = new EnumMap<>(TypeParam.class);
        mapColonnesSuivi = new EnumMap<>(TypeColSuivi.class);
        mapColonnesClarity = new EnumMap<>(TypeColClarity.class);
        mapColonnesChefServ = new EnumMap<>(TypeColChefServ.class);
        mapColonnesPic = new EnumMap<>(TypeColPic.class);
        mapColonnesEdition = new EnumMap<>(TypeColEdition.class);
        mapPlans = new EnumMap<>(TypePlan.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String controleDonnees()
    {
        StringBuilder builder = new StringBuilder("Chargement paramètres :").append(Statics.NL);

        // Message OK
        if (!controleColonnes(builder) || !controleParams(builder) || !controlePlanificateurs(builder))
            builder.append("Merci de changer les paramètres en option ou de recharger le fichier par défaut.");

        return builder.toString();
    }


    @SuppressWarnings ("unchecked")
    public <T extends Enum<T> & TypeCol> Map<T, String> getMapColonnes(Class<T> typeColClass)
    {
        switch (typeColClass.getName())
        {
            case "model.enums.TypeColSuivi" :
                return (Map<T, String>) mapColonnesSuivi;
            
            case "model.enums.TypeColClarity" :
                return (Map<T,String>) mapColonnesClarity;
                
            case "model.enums.TypeColChefServ" :
                return (Map<T,String>) mapColonnesChefServ;
            
            case "model.enums.TypeColPic" :
                return (Map<T,String>) mapColonnesPic;
                
            case "model.enums.TypeColEdition" :
                return (Map<T,String>) mapColonnesEdition;
                
            default :
                return new HashMap<>();
        }
        
    }
    
    /**
     * Retourne la liste des colonnes avec clefs et valeurs inversées
     * @return
     */
    public <T extends Enum<T> & TypeCol> Map<String, T> getMapColonnesInvert(Class<T> typeColClass)
    {
        Map<String, T> retour = new HashMap<>();
        for (Map.Entry<T, String> entry : getMapColonnes(typeColClass).entrySet())
        {
            retour.put(entry.getValue(), entry.getKey());
        }
        return retour;
    }
    
    /*---------- ACCESSEURS ----------*/

    @XmlElementWrapper
    @XmlElement (name = "mapParams", required = false)
    public Map<TypeParam, String> getMapParams()
    {
        return mapParams;
    }

    @XmlElementWrapper
    @XmlElement (name = "mapPlans", required = false)
    public Map<TypePlan, Planificateur> getMapPlans()
    {
        return mapPlans;
    }
    
    @XmlElementWrapper
    @XmlElement (name = "mapColonnesSuivi", required = false)
    public Map<TypeColSuivi, String> getMapColonnesSuivi()
    {
        return mapColonnesSuivi;
    }    
    
    @XmlElementWrapper
    @XmlElement (name = "mapColonnesClarity", required = false)
    public Map<TypeColClarity, String> getMapColonnesClarity()
    {
        return mapColonnesClarity;
    }
    
    @XmlElementWrapper
    @XmlElement (name = "mapColonnesChefServ", required = false)
    public Map<TypeColChefServ, String> getMapColonnesChefServ()
    {
        return mapColonnesChefServ;
    }
    
    @XmlElementWrapper
    @XmlElement (name = "mapColonnesPic", required = false)
    public Map<TypeColPic, String> getMapColonnesPic()
    {
        return mapColonnesPic;
    }
    
    @XmlElementWrapper
    @XmlElement (name = "mapColonnesEdition", required = false)
    public Map<TypeColEdition, String> getMapColonnesEdition()
    {
        return mapColonnesEdition;
    }

    @Override
    public File getFile()
    {
        return new File(Statics.JARPATH + NOMFICHIER);
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean controleColonnes(StringBuilder builder)
    {
        // Parcours de tous les types de colonnes
        StringBuilder builderErreurs = new StringBuilder();

        controleMap(builderErreurs, TypeColSuivi.class, mapColonnesSuivi);
        controleMap(builderErreurs, TypeColClarity.class, mapColonnesClarity);
        controleMap(builderErreurs, TypeColChefServ.class, mapColonnesChefServ);
        controleMap(builderErreurs, TypeColPic.class, mapColonnesPic);
        controleMap(builderErreurs, TypeColEdition.class, mapColonnesEdition);


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
     * @param builderErreurs
     * @param typeCol
     * @param mapColonnes
     */
    private <T extends Enum<T> & TypeCol> void controleMap(StringBuilder builderErreurs, Class<T> typeCol, Map<T, String> mapColonnes)
    {
        for (T type : typeCol.getEnumConstants())
        {            
            if (mapColonnes.get(type) == null || mapColonnes.get(type).isEmpty())
                builderErreurs.append(type.getValeur()).append(Statics.NL);
        }
    }

    private boolean controleParams(StringBuilder builder)
    {
        // Contrôle des paramètres
        StringBuilder builderErreurs = new StringBuilder();
        for (TypeParam typeParam : TypeParam.values())
        {
            if (mapParams.get(typeParam) == null || mapParams.get(typeParam).isEmpty())
                builderErreurs.append(typeParam.toString()).append(Statics.NL);
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
    
    private boolean controlePlanificateurs(StringBuilder builder)
    {
        // Contrôle des planificateurs
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
            builder.append("Certains planificateurs ne sont pas paramétrés :").append(Statics.NL).append(builderErreurs.toString());
            return false;
        }
    }
}