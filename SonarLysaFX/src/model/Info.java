package model;

/**
 * Classe permettant d'enregistrer le mot de passe et le pseudo de l'utilisateur
 * 
 * @author ETP8137 - Gr�goire Mathon
 *
 */
public class Info
{
    /*---------- ATTRIBUTS ----------*/

    private String pseudo;
    private String motDePasse;
    
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    
    public boolean controle()
    {
        return pseudo != null && !pseudo.isEmpty() && motDePasse != null && !motDePasse.isEmpty();
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getPseudo()
    {
        return pseudo;
    }
    public void setPseudo(String pseudo)
    {
        this.pseudo = pseudo;
    }
    public String getMotDePasse()
    {
        return motDePasse;
    }
    public void setMotDePasse(String motDePasse)
    {
        this.motDePasse = motDePasse;
    }
    
}