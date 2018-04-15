package model;

/**
 * Classe permettant d'enregistrer le mot de passe et le pseudo de l'utilisateur
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
public class Info implements Modele
{
    /*---------- ATTRIBUTS ----------*/

    private String pseudo;
    private String motDePasse;
    
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
    /*---------- ACCESSEURS ----------*/
    
    public String getPseudo()
    {
        return getString(pseudo);
    }
    public void setPseudo(String pseudo)
    {
        this.pseudo = pseudo;
    }
    public String getMotDePasse()
    {
        return getString(motDePasse);
    }
    public void setMotDePasse(String motDePasse)
    {
        this.motDePasse = motDePasse;
    }
    
}
