package model;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import junit.JunitBase;
import model.utilities.AbstractModele;
import model.utilities.XML;
import utilities.Statics;

/**
 * Classe permettant d'enregistrer le mot de passe et le pseudo de l'utilisateur
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
    private static final String RESOURCE = "/info.xml";
    
    private String pseudo;
    private String motDePasse;
    private String nom;

    /*---------- CONSTRUCTEURS ----------*/

    Info() { }
    
    /*---------- METHODES PUBLIQUES ----------*/

    public boolean controle()
    {
        return pseudo != null && !pseudo.isEmpty() && motDePasse != null && !motDePasse.isEmpty();
    }

    /*---------- METHODES PRIVEES ----------*/

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

    @Override
    public String controleDonnees()
    {
        if (!controle())
            return "fichiers infos de connexion Connexion vide";
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
        this.pseudo = pseudo;
    }

    @XmlElement(name = "motDepasse", required = true)
    public String getMotDePasse()
    {
        return getString(motDePasse);
    }

    public void setMotDePasse(String motDePasse)
    {
        this.motDePasse = motDePasse;
    }

    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }
}
