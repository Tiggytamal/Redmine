package model.fxml;

import static model.fxml.Styles.TABLEBROWN;
import static model.fxml.Styles.TABLEYELLOW;
import static utilities.Statics.EMPTY;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import model.bdd.Utilisateur;

/**
 * Classe représentant l'affichage d'un {@link model.bdd.AnomalieRTC}
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class UtilisateurFXML extends AbstractFXMLModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    // Groupes
    private static final String INFORMATIONS = "Informations";

    // Informations
    private final SimpleStringProperty nom = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty identifiant = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty email = new SimpleStringProperty(EMPTY);
    private final SimpleBooleanProperty active = new SimpleBooleanProperty(true);

    /*---------- CONSTRUCTEURS ----------*/

    public UtilisateurFXML()
    {
        // Constructeur public sans paramètres pour l'affichage
        listeGetters = UtilisateurFXMLGetters.values();
    }

    private UtilisateurFXML(Utilisateur u)
    {
        this();

        // Infos
        setNom(u.getNom());
        setIdentifiant(u.getIdentifiant());
        setEmail(u.getEmail());
        setActive(u.isActive());
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public ListeGetters[] getListeGetters()
    {
        return listeGetters;
    }

    @Override
    public String getIndex()
    {
        return getIdentifiant();
    }

    public static UtilisateurFXML build(Utilisateur u)
    {
        return new UtilisateurFXML(u);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getNom()
    {
        return nom.get();
    }

    public final void setNom(String nom)
    {
        this.nom.set(getString(nom));
    }

    public String getIdentifiant()
    {
        return identifiant.get();
    }

    public final void setIdentifiant(String identifiant)
    {
        this.identifiant.set(getString(identifiant));
    }

    public String getEmail()
    {
        return email.get();
    }

    public final void setEmail(String email)
    {
        this.email.set(getString(email));
    }
    
    public boolean isActive()
    {
        return active.get();
    }
    
    public final void setActive(boolean active)
    {
        this.active.set(active);
    }

    /*---------- ENUMERATION INTERNE ----------*/

    /**
     * Enumeration permettant de paramètrer l'affichage sans introduire de specification dans le code par type d'objet.
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 2.0
     *
     */
    public enum UtilisateurFXMLGetters implements ListeGetters
    {
        // Selection
        SELECTED(SELECTION, EMPTY, "selected", "getSelected", true, TABLEBROWN),

        // Informations
        NOM(INFORMATIONS, "Nom", "nom", "getNom", true, TABLEYELLOW),
        IDENTIFIANT(INFORMATIONS, "Identifiant", "identifiant", "getIdentifiant", true, TABLEYELLOW),
        EMAIL(INFORMATIONS, "Email", "email", "getEmaile", true, TABLEYELLOW),
        ACTIVE(INFORMATIONS, "Activé", "active", "isActive", true, TABLEYELLOW);

        private String groupe;
        private String affichage;
        private String nomParam;
        private String nomMethode;
        private String style;
        private boolean isAffParDefaut;

        /*---------- ATTRIBUTS ----------*/
        /*---------- CONSTRUCTEURS ----------*/

        UtilisateurFXMLGetters(String groupe, String affichage, String nomParam, String nomMethode, boolean isAffParDefaut, String style)
        {
            this.groupe = groupe;
            this.affichage = affichage;
            this.nomParam = nomParam;
            this.nomMethode = nomMethode;
            this.style = style;
            this.isAffParDefaut = isAffParDefaut;
        }

        /*---------- METHODES PUBLIQUES ----------*/
        /*---------- METHODES PRIVEES ----------*/
        /*---------- ACCESSEURS ----------*/

        @Override
        public String getNomParam()
        {
            return nomParam;
        }

        @Override
        public String getNomMethode()
        {
            return nomMethode;
        }

        @Override
        public String getStyle()
        {
            return style;
        }

        @Override
        public String getGroupe()
        {
            return groupe;
        }

        @Override
        public String getAffichage()
        {
            return affichage;
        }

        @Override
        public boolean isAffParDefaut()
        {
            return isAffParDefaut;
        }

        @Override
        public String toString()
        {
            return getAffichage();
        }
    }
}
