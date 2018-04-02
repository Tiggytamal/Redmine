package model;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.enums.TypeCol;
import model.enums.TypeColSuivi;
import model.enums.TypeParam;
import model.enums.TypePlan;
import utilities.Statics;

@XmlRootElement
public class ProprietesXML implements XML
{
    /*---------- ATTRIBUTS ----------*/

    private Map<TypeParam, String> mapParams;

    private Map<TypeColSuivi, String> mapColonnesSuivi;

    private Map<TypePlan, Planificateur> mapPlans;

    private static final String NOMFICHIER = "\\proprietes.xml";

    /*---------- CONSTRUCTEURS ----------*/

    public ProprietesXML()
    {
        mapParams = new EnumMap<>(TypeParam.class);
        mapColonnesSuivi = new EnumMap<>(TypeColSuivi.class);
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

    /*---------- ACCESSEURS ----------*/

    @XmlElementWrapper
    @XmlElement (name = "mapParams", required = false)
    public Map<TypeParam, String> getMapParams()
    {
        return mapParams;
    }

    @XmlElementWrapper
    @XmlElement (name = "mapColonnesSuivi", required = false)
    public Map<TypeColSuivi, String> getMapColonnesSuivi()
    {
        return mapColonnesSuivi;
    }    

    @SuppressWarnings ("unchecked")
    public <T extends Enum<T> & TypeCol> Map<T, String> getMapColonnes(Class<T> typeColClass)
    {
        switch (typeColClass.getName())
        {
            case "model.enums.TypeColSuivi" :
                return (Map<T, String>) mapColonnesSuivi;

            default :
                return new HashMap<>();
        }
        
    }

    @XmlElementWrapper
    @XmlElement (name = "mapPlans", required = false)
    public Map<TypePlan, Planificateur> getMapPlans()
    {
        return mapPlans;
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
        for (TypeColSuivi typeCol : TypeColSuivi.values())
        {
            if (mapColonnesSuivi.get(typeCol) == null || mapColonnesSuivi.get(typeCol).isEmpty())
                builderErreurs.append(typeCol.toString()).append(Statics.NL);
        }

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