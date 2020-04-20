package model.parsing;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;
import model.Colonne;
import model.Planificateur;
import model.enums.ColAppliDir;
import model.enums.ColChefServ;
import model.enums.ColClarity;
import model.enums.ColCompo;
import model.enums.ColEdition;
import model.enums.ColPic;
import model.enums.ColR;
import model.enums.ColRegle;
import model.enums.ColVul;
import model.enums.ColW;
import model.enums.Param;
import model.enums.ParamBool;
import model.enums.ParamSpec;
import model.enums.TypePlan;
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

    // Map des paramètres
    private Map<Param, String> mapParams;
    private Map<ParamBool, Boolean> mapParamsBool;
    private Map<ParamSpec, String> mapParamsSpec;

    // Map des colonnes
    private Map<ColClarity, String> mapColsClarity;
    private Map<ColChefServ, String> mapColsChefServ;
    private Map<ColEdition, String> mapColsEdition;
    private Map<ColAppliDir, String> mapColsAppliDir;
    private Map<ColPic, String> mapColsPic;
    private Map<ColRegle, Colonne> mapColsRegle;

    // Map des colonnes avec indice pour écriture
    private Map<ColVul, Colonne> mapColsVul;
    private Map<ColCompo, Colonne> mapColsCompo;

    // Map planificateurs
    private Map<TypePlan, Planificateur> mapPlans;

    private File file;

    /*---------- CONSTRUCTEURS ----------*/

    ProprietesXML()
    {
        mapParams = new EnumMap<>(Param.class);
        mapColsClarity = new EnumMap<>(ColClarity.class);
        mapColsChefServ = new EnumMap<>(ColChefServ.class);
        mapColsEdition = new EnumMap<>(ColEdition.class);
        mapColsAppliDir = new EnumMap<>(ColAppliDir.class);
        mapColsVul = new EnumMap<>(ColVul.class);
        mapColsCompo = new EnumMap<>(ColCompo.class);
        mapColsRegle = new EnumMap<>(ColRegle.class);
        mapColsPic = new EnumMap<>(ColPic.class);
        mapPlans = new EnumMap<>(TypePlan.class);
        mapParamsBool = new EnumMap<>(ParamBool.class);
        mapParamsSpec = new EnumMap<>(ParamSpec.class);
        file = new File(Statics.JARPATH + NOMFICHIER);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void update(ProprietesXML update)
    {
        mapParams = update.mapParams;
        mapParamsBool = update.mapParamsBool;
        mapParamsSpec = update.mapParamsSpec;
        mapColsClarity = update.mapColsClarity;
        mapColsChefServ = update.mapColsChefServ;
        mapColsEdition = update.mapColsEdition;
        mapColsAppliDir = update.mapColsAppliDir;
        mapColsPic = update.mapColsPic;
        mapColsRegle = update.mapColsRegle;
        mapColsVul = update.mapColsVul;
        mapColsCompo = update.mapColsCompo;
        mapPlans = update.mapPlans;
    }
    
    @Override
    public File getFile()
    {
        return file;
    }

    @Override
    public String controleDonnees()
    {
        StringBuilder builder = new StringBuilder("Chargement paramètres :").append(Statics.NL);

        boolean bool1 = controleCols(builder);
        boolean bool2 = controleParams(builder);
        boolean bool3 = controlePlanificateurs(builder);

        // Message OK
        if (!bool1 || !bool2 || !bool3)
            builder.append("Merci de changer les paramètres en option ou de recharger les fichiers de parametrage.");

        return builder.toString();
    }

    /**
     * Retourne toutes les map de gestions des colonnes pour les fichiers en lecture.
     * 
     * @param colClass
     *                 Enumération correspondante à la map désirée.
     * @param          <T>
     *                 Classe de l'énumération.
     * @return
     *         La map de paramètres désirée.
     */
    @SuppressWarnings(
    { "unchecked" })
    public <T extends ColR> Map<T, String> getEnumMapColR(Class<T> colClass)
    {
        switch (colClass.getName())
        {
            case "model.enums.ColClarity":
                return (Map<T, String>) getMapColsClarity();

            case "model.enums.ColChefServ":
                return (Map<T, String>) getMapColsChefServ();

            case "model.enums.ColEdition":
                return (Map<T, String>) getMapColsEdition();

            case "model.enums.ColAppliDir":
                return (Map<T, String>) getMapColsAppliDir();

            case "model.enums.ColPic":
                return (Map<T, String>) getMapColsPic();

            default:
                throw new TechnicalException("model.parsing.ProprietesXML.getEnumColR - Type non gere :" + colClass, null);
        }
    }

    /**
     * Retourne toutes les map de gestions des colonnes pour les fichiers en écriture.
     * 
     * @param colClass
     *                 Enumération correspondante à la map désirée.
     * @param          <T>
     *                 Classe de l'énumération.
     * @return
     *         La map de paramètres désirée.
     */
    @SuppressWarnings(
    { "unchecked" })
    public <T extends ColW> Map<T, Colonne> getEnumMapColW(Class<T> colClass)
    {
        switch (colClass.getName())
        {
            case "model.enums.ColVul":
                return (Map<T, Colonne>) getMapColsVul();

            case "model.enums.ColCompo":
                return (Map<T, Colonne>) getMapColsCompo();

            case "model.enums.ColRegle":
                return (Map<T, Colonne>) getMapColsRegle();

            default:
                throw new TechnicalException("model.parsing.ProprietesXML.getEnumColW - Type non gere :" + colClass, null);
        }
    }

    /**
     * Retourne la liste des colonnes avec clefs et valeurs inversees
     * 
     * @param colClass
     *                 Enumération correspondante à la map désirée.
     * @param          <T>
     *                 Classe de l'énumération.               
     * @return
     *         La map de paramètres désirée, mais avec les clef/valeur inversées.
     */
    public <T extends Enum<T> & ColR> Map<String, T> getMapColsInvert(Class<T> colClass)
    {
        // Set des valeurs de la map
        Set<Entry<T, String>> set = getEnumMapColR(colClass).entrySet();

        // Map de retour
        Map<String, T> retour = new HashMap<>((int) (set.size() * Statics.RATIOLOAD));

        // Remplissage map de retour avec inversion clefs / valeurs
        for (Entry<T, String> entry : set)
        {
            retour.put((String) entry.getValue(), (T) entry.getKey());
        }
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean controleCols(StringBuilder builder)
    {
        // Parcours de tous les types de colonnes
        StringBuilder builderErreurs = new StringBuilder();

        controleMap(builderErreurs, ColClarity.class, mapColsClarity);
        controleMap(builderErreurs, ColChefServ.class, mapColsChefServ);
        controleMap(builderErreurs, ColEdition.class, mapColsEdition);
        controleMap(builderErreurs, ColAppliDir.class, mapColsAppliDir);
        controleMap(builderErreurs, ColPic.class, mapColsPic);

        // Renvoi du booleen
        if (builderErreurs.length() == 0)
        {
            builder.append("Nom colonnes OK").append(Statics.NL);
            return true;
        }
        else
        {
            builder.append("Certaines colonnes sont mal renseignees :").append(Statics.NL).append(builderErreurs.toString());
            return false;
        }
    }

    /**
     * Methodes génériques pour contrôler les valeur d'une map
     * 
     * @param builderErreurs
     *                       Stringbuilder pour afficher toutes les erreurs.
     * @param col
     *                       Classe de l'énumération correspondante à la map testée.
     * @param          <T>
     *                 Classe de l'énumération.                  
     * @param mapCols
     *                       Map de paramètres à tester.
     */
    private <T extends Enum<T> & ColR> void controleMap(StringBuilder builderErreurs, Class<T> col, Map<T, String> mapCols)
    {
        for (T type : col.getEnumConstants())
        {
            if (mapCols.get(type) == null || mapCols.get(type).isEmpty())
                builderErreurs.append(type.getValeur()).append(Statics.NL);
        }
    }

    /**
     * Contrôle la valorisation de tous les paramètres enregistrés.
     * 
     * @param builder
     *                Stringbuilder pour afficher toutes les erreurs.
     * @return
     *         Vrai si il n'y a aucune erreur.
     */
    private boolean controleParams(StringBuilder builder)
    {
        // Contrôle des paramètres
        StringBuilder builderErreurs = new StringBuilder();

        // Paramètres classiques
        for (Param param : Param.values())
        {
            if (!param.isPerso() && (mapParams.get(param) == null || mapParams.get(param).isEmpty()))
                builderErreurs.append(param.getNom()).append(Statics.NL);
        }

        // Paramètres speciaux
        for (ParamSpec param : ParamSpec.values())
        {
            if (!param.isPerso() && (mapParamsSpec.get(param) == null || mapParamsSpec.get(param).isEmpty()))
                builderErreurs.append(param.getNom()).append(Statics.NL);
        }

        // Paramètres booleens
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
            builder.append("Certains paramètres sont mal renseignes :").append(Statics.NL).append(builderErreurs.toString());
            return false;
        }
    }

    /**
     * Contrôle la valorisation des paramètres des planificateurs.
     * 
     * @param builder
     *                Stringbuilder pour afficher toutes les erreurs.
     * @return
     *         Vrai si il n'y a aucune erreur.
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
            builder.append("Certains planificateurs ne sont pas parametres :").append(Statics.NL).append(builderErreurs.toString());
            return false;
        }
    }

    /**
     * Méthode de protection pour éviter que les paramètres non valorisés n'apparaissent pas dans les options pour les fichiers en lectures.
     * 
     * @param map
     *                map de paramètres a controler.
     * @param          <T>
     *                 Classe de l'énumération.           
     * @param valeurs
     *                valeurs à tester.
     */
    private <T extends ColR> void protectionDonnesManquantesParamR(Map<T, String> map, T[] valeurs)
    {
        for (T col : valeurs)
        {
            map.computeIfAbsent(col, k -> Statics.EMPTY);
        }
    }
    
    /**
     * Méthode de protection pour éviter que les paramètres non valorisés n'apparaissent pas dans les options pour les fichiers en écritures.
     * 
     * @param map
     *                map de paramètres a controler.
     * @param          <T>
     *                 Classe de l'énumération.           
     * @param valeurs
     *                valeurs à tester.
     */
    private <T extends ColW> void protectionDonnesManquantesParamW(Map<T, Colonne> map, T[] valeurs)
    {
        for (T col : valeurs)
        {
            map.computeIfAbsent(col, k -> new Colonne());
        }
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
    @XmlElement(name = "mapColsClarity", required = false)
    private Map<ColClarity, String> getMapColsClarity()
    {
        protectionDonnesManquantesParamR(mapColsClarity, ColClarity.values());
        return mapColsClarity;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsChefServ", required = false)
    private Map<ColChefServ, String> getMapColsChefServ()
    {
        protectionDonnesManquantesParamR(mapColsChefServ, ColChefServ.values());
        return mapColsChefServ;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsEdition", required = false)
    private Map<ColEdition, String> getMapColsEdition()
    {
        protectionDonnesManquantesParamR(mapColsEdition, ColEdition.values());
        return mapColsEdition;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsAppliDir", required = false)
    private Map<ColAppliDir, String> getMapColsAppliDir()
    {
        protectionDonnesManquantesParamR(mapColsAppliDir, ColAppliDir.values());
        return mapColsAppliDir;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsPic", required = false)
    public Map<ColPic, String> getMapColsPic()
    {
        protectionDonnesManquantesParamR(mapColsPic, ColPic.values());
        return mapColsPic;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsVul", required = false)
    private Map<ColVul, Colonne> getMapColsVul()
    {
        protectionDonnesManquantesParamW(mapColsVul, ColVul.values());
        return mapColsVul;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsCompo", required = false)
    private Map<ColCompo, Colonne> getMapColsCompo()
    {
        protectionDonnesManquantesParamW(mapColsCompo, ColCompo.values());
        return mapColsCompo;
    }

    @XmlElementWrapper
    @XmlElement(name = "mapColsRegle", required = false)
    public Map<ColRegle, Colonne> getMapColsRegle()
    {
        protectionDonnesManquantesParamW(mapColsRegle, ColRegle.values());
        return mapColsRegle;
    }
}
