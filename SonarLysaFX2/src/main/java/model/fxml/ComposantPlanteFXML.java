package model.fxml;

import static model.fxml.Styles.TABLEBLUE;
import static model.fxml.Styles.TABLEBROWN;
import static utilities.Statics.EMPTY;

import java.util.Arrays;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import model.bdd.ComposantErreur;

/**
 * Classe représentant l'affichage d'un {@link model.bdd.ComposantErreur}
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class ComposantPlanteFXML extends AbstractFXMLModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String COMPOSANT = "Composant";

    // Donnees
    private final SimpleStringProperty key = new SimpleStringProperty(EMPTY);
    private final ListProperty<String> nom = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final SimpleStringProperty date = new SimpleStringProperty(EMPTY);
    private final SimpleBooleanProperty aPurger = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty existe = new SimpleBooleanProperty(false);
    private final SimpleStringProperty nbrePurge = new SimpleStringProperty(EMPTY);

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantPlanteFXML()
    {
        // Constructeur public sans paramètre pour l'affichage
        super();
        listeGetters = ComposantPlanteFXMLGetters.values();
    }

    private ComposantPlanteFXML(ComposantErreur ce)
    {
        this();
        setKey(ce.getKey());
        setDate(ce.getDateDetection().toString());
        setAPurger(ce.isaPurger());
        setNbrePurge(String.valueOf(ce.getNbrePurge()));
        setNom(Arrays.asList(ce.getNom(), ce.getLiens()));
        setExiste(ce.existe());
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
        return getKey();
    }

    public static ComposantPlanteFXML build(ComposantErreur ce)
    {
        return new ComposantPlanteFXML(ce);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getKey()
    {
        return key.get();
    }

    public final void setKey(String key)
    {
        this.key.set(getString(key));
    }

    public List<String> getNom()
    {
        return nom.get();
    }

    public final void setNom(List<String> nom)
    {
        if (nom != null)
            this.nom.set(FXCollections.observableList(nom));
    }

    public String getDate()
    {
        return date.get();
    }

    public final void setDate(String date)
    {
        this.date.set(getString(date));
    }

    public boolean isAPurger()
    {
        return aPurger.get();
    }

    public final void setAPurger(boolean aPurger)
    {
        this.aPurger.set(aPurger);
    }

    public boolean existe()
    {
        return existe.get();
    }

    public final void setExiste(boolean aPurger)
    {
        this.existe.set(aPurger);
    }

    public String getNbrePurge()
    {
        return nbrePurge.get();
    }

    public final void setNbrePurge(String nbrePurge)
    {
        
        this.nbrePurge.set(getString(nbrePurge));
    }

    /*---------- ENUMERATION INTERNE ----------*/

    /**
     * Enumeration permettant de paramètrer l'affichage sans introduire de specification dans le code par type d'objet.
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 2.0
     *
     */
    public enum ComposantPlanteFXMLGetters implements ListeGetters
    {
        /*---------- ATTRIBUTS ----------*/

        // Selection
        SELECTED(SELECTION, EMPTY, "selected", "getSelected", true, TABLEBROWN),
        KEY(COMPOSANT, "Clef", "key", "getKey", false, TABLEBLUE),
        NOM(COMPOSANT, "Nom", "nom", "getNom", true, TABLEBLUE),
        DATE(COMPOSANT, "Date detection", "date", "getDate", true, TABLEBLUE),
        APURGER(COMPOSANT, "A purger", "aPurger", "getAPurger", true, TABLEBLUE),
        EXISTE(COMPOSANT, "Existe", "existe", "existe", true, TABLEBLUE),
        NBREPURGE(COMPOSANT, "Nbre Purge", "NbrePurge", "getNbrePurge", true, TABLEBLUE);

        private String groupe;
        private String affichage;
        private String nomParam;
        private String nomMethode;
        private String style;
        private boolean isAffParDefaut;

        /*---------- CONSTRUCTEURS ----------*/

        ComposantPlanteFXMLGetters(String groupe, String affichage, String nomParam, String nomMethode, boolean isAffParDefaut, String style)
        {
            this.nomParam = nomParam;
            this.nomMethode = nomMethode;
            this.style = style;
            this.groupe = groupe;
            this.affichage = affichage;
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
