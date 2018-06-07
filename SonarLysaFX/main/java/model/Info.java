package model;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import utilities.Statics;

/**
 * Classe permettant d'enregistrer le mot de passe et le pseudo de l'utilisateur
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
@XmlRootElement
public class Info implements Modele, XML
{
    /*---------- ATTRIBUTS ----------*/

    private String pseudo;
    private String motDePasse;
    private static final String NOMFICHIER = "\\info.xml";
    private static final String RESOURCE = "/resources/info.xml";

    /*---------- CONSTRUCTEURS ----------*/

    Info()
    {
    }
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
        return new File(getClass().getResource(RESOURCE).getFile());
    }

    @Override
    public String controleDonnees()
    {
        if (!controle())
            return "fichiers infos de connexion Connexion vide";
        return "";
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
}