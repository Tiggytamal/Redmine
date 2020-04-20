package model.fxml;

import static model.fxml.Styles.TABLEBLUE;
import static model.fxml.Styles.TABLEBROWN;
import static model.fxml.Styles.TABLEGREEN;
import static model.fxml.Styles.TABLEYELLOW;
import static utilities.Statics.EMPTY;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import model.bdd.Application;
import model.bdd.ComposantBase;
import model.bdd.LotRTC;
import model.bdd.Solution;
import utilities.Statics;

/**
 * Classe représentant l'affichage d'un {@link model.bdd.ComposantBase}
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class ComposantFXML extends AbstractFXMLModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    // Pattern
    public static final Pattern LETTERPATTERN = Pattern.compile("[a-zA-Z]");
    
    // Groupes
    private static final String IDENTIFIANTS = "Identifiants";
    private static final String LIENS = "Liens";
    private static final String INFORMATIONS = "Informations";
    private static final String ETAT = "Etat";

    // Identifiant
    private final SimpleStringProperty key = new SimpleStringProperty(EMPTY);
    private final ListProperty<String> nom = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final SimpleStringProperty branche = new SimpleStringProperty(EMPTY);

    // Liens
    private final ListProperty<String> numeroLot = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<String> codeAppli = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<String> codeSolution = new SimpleListProperty<>(FXCollections.observableArrayList());

    // Informations
    private final SimpleStringProperty qg = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty version = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty versionMax = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty ldc = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty securityRating = new SimpleStringProperty(EMPTY);
    private final SimpleBooleanProperty controleAppli = new SimpleBooleanProperty(true);

    // Etat
    private final SimpleStringProperty matiere = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateMep = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty instance = new SimpleStringProperty(EMPTY);
    private final SimpleBooleanProperty doublon = new SimpleBooleanProperty(false);

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantFXML()
    {
        // Constructeur public sans paramètres pour l'affichage
        listeGetters = ComposantFXMLGetters.values();
    }

    private ComposantFXML(ComposantBase compo)
    {
        this();
        LotRTC lot = compo.getLotRTC();
        Application appli = compo.getAppli();

        if (lot != null)
        {
            setNumeroLot(Arrays.asList(lot.getNumero(), lot.getLiens()));
            if (lot.getDateMep() != null)
                setDateMep(lot.getDateMep().toString());
        }
        else
            setNumeroLot(Arrays.asList(EMPTY, EMPTY));

        // Variables sans contrôle
        setKey(compo.getKey());
        setNom(Arrays.asList(compo.getNom(), compo.getLiens()));
        setBranche(compo.getBranche());
        setQg(compo.getQualityGate().getValeur());
        setVersion(compo.getVersion());
        setVersionMax(compo.getVersionMax());
        setLdc(String.valueOf(compo.getLdc()));
        setSecurityRating(compo.getSecurityRating());
        setMatiere(compo.getMatiere().getValeur());
        setInstance(compo.getInstance().getValeur());
        setDoublon(compo.isDoublon());
        setControleAppli(compo.isControleAppli());

        // Variables avec contrôle nullité

        if (compo.getSolution() != null)
        {
            Solution solution = compo.getSolution();
            String liensProduit = EMPTY;
            if (solution.getProduit() != null)
                liensProduit = compo.getSolution().getProduit().calculLiens();
            setCodeSolution(Arrays.asList(String.valueOf(solution.getNom()), liensProduit));
        }
        else
            setCodeSolution(Arrays.asList(EMPTY, EMPTY));

        if (appli != null)
        {
            if (appli.isReferentiel())
                setCodeAppli(Arrays.asList(String.valueOf(appli.getCode()), appli.getLiens()));
            else
                setCodeAppli(Arrays.asList(String.valueOf(appli.getCode()), EMPTY));
        }
        else
            setCodeAppli(Arrays.asList(EMPTY, EMPTY));

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
        return getKey() + getBranche();
    }

    public static ComposantFXML build(ComposantBase compo)
    {
        return new ComposantFXML(compo);
    }

    public boolean isVersionOK()
    {
        // Protection valeurs vides ou nulles
        if (getVersion().isEmpty() || getVersionMax().isEmpty())
            return true;
        
        String[] vers = LETTERPATTERN.matcher(getVersion().replace("-SNAPSHOT", Statics.EMPTY)).replaceAll(EMPTY).split("\\.");
        String[] max = LETTERPATTERN.matcher(getVersionMax().replace("-SNAPSHOT", Statics.EMPTY)).replaceAll(EMPTY).split("\\.");
        
        // Itération sur chaque numéro de version pour composarer avec la verison max
        for (int i = 0; i < vers.length; i++)
        {
            if (max.length == i)
                return true;
            if (Integer.parseInt(vers[i]) > Integer.parseInt(max[i]))
                return true;
            if (Integer.parseInt(vers[i]) < Integer.parseInt(max[i]))
                return false;
        }
        return true;
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

    public String getBranche()
    {
        return branche.get();
    }

    public final void setBranche(String branche)
    {
        this.branche.set(getString(branche));
    }

    public String getQg()
    {
        return qg.get();
    }

    public final void setQg(String qg)
    {
        this.qg.set(getString(qg));
    }

    public List<String> getNumeroLot()
    {
        return numeroLot.get();
    }

    public final void setNumeroLot(List<String> numeroLot)
    {
        if (numeroLot != null)
            this.numeroLot.set(FXCollections.observableList(numeroLot));
    }

    public List<String> getCodeAppli()
    {
        return codeAppli.get();
    }

    public final void setCodeAppli(List<String> codeAppli)
    {
        if (codeAppli != null)
            this.codeAppli.set(FXCollections.observableList(codeAppli));
    }

    public List<String> getCodeSolution()
    {
        return codeSolution.get();
    }

    public final void setCodeSolution(List<String> codeSolution)
    {
        if (codeSolution != null)
            this.codeSolution.set(FXCollections.observableList(codeSolution));
    }

    public String getVersion()
    {
        return version.get();
    }

    public final void setVersion(String version)
    {
        this.version.set(getString(version));
    }

    public String getVersionMax()
    {
        return versionMax.get();
    }

    public final void setVersionMax(String versionMax)
    {
        this.versionMax.set(getString(versionMax));
    }

    public String getLdc()
    {
        return ldc.get();
    }

    public final void setLdc(String ldc)
    {
        this.ldc.set(getString(ldc));
    }

    public String getSecurityRating()
    {
        return securityRating.get();
    }

    public final void setSecurityRating(String securityRating)
    {
        this.securityRating.set(getString(securityRating));
    }
    
    public boolean isControleAppli()
    {
        return controleAppli.get();
    }
    
    public final void setControleAppli(boolean controleAppli)
    {
        this.controleAppli.set(controleAppli);
    }

    public String getMatiere()
    {
        return matiere.get();
    }

    public final void setMatiere(String matiere)
    {
        this.matiere.set(getString(matiere));
    }

    public String getDateMep()
    {
        return dateMep.get();
    }

    public final void setDateMep(String dateMep)
    {
        this.dateMep.set(getString(dateMep));
    }

    public String getInstance()
    {
        return instance.get();
    }

    public final void setInstance(String instance)
    {
        this.instance.set(getString(instance));
    }

    public boolean isDoublon()
    {
        return doublon.get();
    }

    public final void setDoublon(boolean doublon)
    {
        this.doublon.set(doublon);
    }

    public enum ComposantFXMLGetters implements ListeGetters
    {
        /*---------- ATTRIBUTS ----------*/

        // Selection
        SELECTED(SELECTION, EMPTY, "selected", "getSelected", true, TABLEBROWN),

        // Identifiants
        KEY(IDENTIFIANTS, "Clef", "key", "getKey", false, TABLEBLUE),
        NOM(IDENTIFIANTS, "Nom", "nom", "getNom", true, TABLEBLUE),
        BRANCHE(IDENTIFIANTS, "Branche", "branche", "getBranche", true, TABLEBLUE),

        // Liens
        LOT(LIENS, "Lot", "numeroLot", "getNumeroLot", true, TABLEGREEN),
        APP(LIENS, "Application", "codeAppli", "getCodeAppli", true, TABLEGREEN),
        SOLUTION(LIENS, "Solution", "codeSolution", "getCodeSolution", false, TABLEGREEN),

        // informations
        QG(INFORMATIONS, "Quality Gate", "qg", "getQg", true, TABLEYELLOW),
        VERSION(INFORMATIONS, "Version", "version", "getVersion", true, TABLEYELLOW),
        VERSIONMAX(INFORMATIONS, "VersionMax", "versionMax", "getVersionMax", false, TABLEYELLOW),
        LDC(INFORMATIONS, "Lignes de code", "ldc", "getLdc", false, TABLEYELLOW),
        SECURITE(INFORMATIONS, "Securite", "securityRating", "getSecurityrating", false, TABLEYELLOW),
        CONTROLEAPPLI(INFORMATIONS, "Contrôle Appli", "controleAppli", "isControleAppli", false, TABLEYELLOW),

        // Etat
        MATIERE(ETAT, "Matiere", "matiere", "getMatiere", true, TABLEBROWN),
        DATEMEP(ETAT, "Date Mise en Prod", "dateMep", "getDateMep", false, TABLEBROWN),
        DOUBLON(ETAT, "Doublon", "doublon", "isDoublon", false, TABLEBROWN),
        INSTANCE(ETAT, "Instance", "instance", "getInstance", true, TABLEBROWN);

        private String groupe;
        private String affichage;
        private String nomParam;
        private String nomMethode;
        private String style;
        private boolean isAffParDefaut;

        /*---------- CONSTRUCTEURS ----------*/

        ComposantFXMLGetters(String groupe, String affichage, String nomParam, String nomMethode, boolean isAffParDefaut, String style)
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
