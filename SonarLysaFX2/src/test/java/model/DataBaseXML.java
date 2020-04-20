package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.bdd.Application;
import model.bdd.ChefService;
import model.bdd.ComposantBase;
import model.bdd.ComposantErreur;
import model.bdd.DefautQualite;
import model.bdd.IssueBase;
import model.bdd.LotRTC;
import model.bdd.Utilisateur;
import model.parsing.XML;
import utilities.Statics;

/**
 * Representation XML de la base de données pour les tests
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class DataBaseXML extends AbstractModele implements XML
{

    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final String NOMFICHIER = "\\database.xml";

    // Tables
    private List<ComposantBase> compos;
    private List<DefautQualite> dqs;
    private List<LotRTC> lotsRTC;
    private List<ChefService> chefService;
    private List<ComposantErreur> composErreur;
    private List<KeyDateMEP> keyDateMEPs;
    private List<Utilisateur> users;
    private List<IssueBase> issuesBase;
    private List<Application> apps;

    // Map d'acces aux tables
    private Map<String, ComposantBase> mapCompos;
    private Map<String, LotRTC> mapLotsRTC;
    private Map<String, Utilisateur> mapUsers;

    /*---------- CONSTRUCTEURS ----------*/

    DataBaseXML()
    {
        compos = new ArrayList<>();
        dqs = new ArrayList<>();
        lotsRTC = new ArrayList<>();
        chefService = new ArrayList<>();
        composErreur = new ArrayList<>();
        keyDateMEPs = new ArrayList<>();
        keyDateMEPs = new ArrayList<>();
        users = new ArrayList<>();
        issuesBase = new ArrayList<>();
        apps = new ArrayList<>();

        mapCompos = new HashMap<>();
        mapLotsRTC = new HashMap<>();
        mapUsers = new HashMap<>();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public File getFile()
    {
        return new File(Statics.JARPATH + NOMFICHIER);
    }

    @Override
    public String controleDonnees()
    {
        return Statics.EMPTY;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlElementWrapper(name = "composants")
    @XmlElement(name = "composant", required = false)
    public List<ComposantBase> getCompos()
    {
        if (compos == null)
            compos = new ArrayList<>();
        return compos;
    }

    @Transient
    public Map<String, ComposantBase> getMapCompos()
    {
        if (mapCompos == null)
            mapCompos = new HashMap<>();

        if (mapCompos.isEmpty())
        {
            for (ComposantBase compo : compos)
            {
                mapCompos.put(compo.getNom(), compo);
            }
        }

        return mapCompos;
    }

    @XmlElementWrapper(name = "defautsQualite")
    @XmlElement(name = "defautQualite", required = false)
    public List<DefautQualite> getDqs()
    {
        if (dqs == null)
            dqs = new ArrayList<>();
        return dqs;
    }

    @XmlElementWrapper(name = "lotsRTC")
    @XmlElement(name = "lotRTC", required = false)
    public List<LotRTC> getLotsRTC()
    {
        if (lotsRTC == null)
            lotsRTC = new ArrayList<>();
        return lotsRTC;
    }

    @Transient
    public Map<String, LotRTC> getMapLots()
    {
        if (mapLotsRTC == null)
            mapLotsRTC = new HashMap<>();

        if (mapLotsRTC.isEmpty())
        {
            for (LotRTC lot : lotsRTC)
            {
                mapLotsRTC.put(lot.getNumero(), lot);
            }
        }

        return mapLotsRTC;
    }

    @XmlElementWrapper(name = "chefsService")
    @XmlElement(name = "chefService", required = false)
    public List<ChefService> getChefService()
    {
        if (chefService == null)
            chefService = new ArrayList<>();
        return chefService;
    }

    @XmlElementWrapper(name = "composErreur")
    @XmlElement(name = "compoErreur", required = false)
    public List<ComposantErreur> getComposErreur()
    {
        if (composErreur == null)
            composErreur = new ArrayList<>();
        return composErreur;
    }

    @XmlElementWrapper(name = "keyDateMEPs")
    @XmlElement(name = "keyDateMEP", required = false)
    public List<KeyDateMEP> getKeyDateMEPs()
    {
        if (keyDateMEPs == null)
            keyDateMEPs = new ArrayList<>();
        return keyDateMEPs;
    }

    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user", required = false)
    public List<Utilisateur> getUsers()
    {
        if (users == null)
            users = new ArrayList<>();
        return users;
    }

    @Transient
    public Map<String, Utilisateur> getMapUsers()
    {
        if (mapUsers == null)
            mapUsers = new HashMap<>();

        if (mapUsers.isEmpty())
        {
            for (Utilisateur user : users)
            {
                mapUsers.put(user.getIdentifiant(), user);
            }
        }

        return mapUsers;
    }

    @XmlElementWrapper(name = "IssuesBase")
    @XmlElement(name = "IssueBase", required = false)
    public List<IssueBase> getIssuesBase()
    {
        if (issuesBase == null)
            issuesBase = new ArrayList<>();
        return issuesBase;
    }
    
    @XmlElementWrapper(name = "Apps")
    @XmlElement(name = "App", required = false)
    public List<Application> getApps()
    {
        if (apps == null)
            apps = new ArrayList<>();
        return apps;
    }
}
