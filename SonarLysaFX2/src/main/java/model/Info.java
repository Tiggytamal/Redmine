package model;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import model.bdd.Utilisateur;
import model.parsing.XML;
import utilities.Statics;
import utilities.Utilities;

/**
 * Classe permettant d'enregistrer les informations de connexion de l'utilisateur. (le mot de passe est enregistré crypté).
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
public class Info extends AbstractModele implements XML
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final String NOMFICHIER = "\\info.xml";

    private String pseudo;
    private String motDePasse;
    private String nom;
    private Utilisateur user;
    private File file;

    /*---------- CONSTRUCTEURS ----------*/

    Info()
    {
        file = new File(Statics.JARPATH + NOMFICHIER);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public boolean controle()
    {
        return !getPseudo().isEmpty() && !getMotDePasse().isEmpty();
    }

    /*---------- METHODES PRIVEES ----------*/

    @Override
    public File getFile()
    {
        return file;
    }

    @Override
    public String controleDonnees()
    {
        if (!controle())
            return "Fichier infos de connexion Connexion vide";
        return Statics.EMPTY;
    }
    /*---------- ACCESSEURS ----------*/

    @XmlElement(name = "pseudo", required = true)
    public String getPseudo()
    {
        return getString(pseudo);
    }

    public void setPseudo(String pseudo)
    {
        this.pseudo = getString(pseudo);
    }

    @XmlElement(name = "motDepasse", required = true)
    public String getMotDePasse()
    {
        return Utilities.decrypterValeur(getString(motDePasse));
    }

    public void setMotDePasse(String motDePasse)
    {
        this.motDePasse = Utilities.crypterValeur(getString(motDePasse));
    }

    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = getString(nom);
    }

    @XmlElement(name = "user", required = true)
    public Utilisateur getUser()
    {
        return user;
    }

    public void setUser(Utilisateur user)
    {
        if (user != null)
            this.user = user;
    }
}
