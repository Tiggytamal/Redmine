package model.fxml;

import static model.fxml.Styles.TABLEBLUE;
import static model.fxml.Styles.TABLEBROWN;
import static model.fxml.Styles.TABLEGREEN;
import static model.fxml.Styles.TABLEYELLOW;
import static utilities.Statics.EMPTY;

import java.util.Arrays;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import model.bdd.AnomalieRTC;
import model.bdd.ProjetClarity;

/**
 * Classe représentant l'affichage d'un {@link model.bdd.AnomalieRTC}
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class AnomalieRTCFXML extends AbstractFXMLModele<Integer>
{
    /*---------- ATTRIBUTS ----------*/

    // Groupes
    private static final String INFORMATIONS = "Informations";
    private static final String DATES = "Dates";
    private static final String PROJETCLARITY = "Projet Clarity";

    // Informations
    private final SimpleStringProperty titre = new SimpleStringProperty(EMPTY);
    private final ListProperty<String> numero = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final SimpleStringProperty etatAno = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty commentaire = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty matiere = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty edition = new SimpleStringProperty(EMPTY);

    // Lot RTC
    private final SimpleStringProperty cpiLot = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty emailCpi = new SimpleStringProperty(EMPTY);

    // Dates
    private final SimpleStringProperty dateCrea = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateReso = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateRelance = new SimpleStringProperty(EMPTY);

    // Clarity
    private final SimpleStringProperty direction = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty departement = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty service = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty respServ = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty codeClarity = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty libelleClarity = new SimpleStringProperty(EMPTY);

    /*---------- CONSTRUCTEURS ----------*/

    public AnomalieRTCFXML()
    {
        // Constructeur public sans paramètres pour l'affichage
        listeGetters = AnomalieRTCFXMLGetters.values();
    }

    private AnomalieRTCFXML(AnomalieRTC ano)
    {
        this();

        // Infos de base
        setTitre(ano.getTitre());
        setEtatAno(ano.getEtatAno());
        setCommentaire(ano.getCommentaire());
        setNumero(Arrays.asList(String.valueOf(ano.getNumero()), ano.getLiens()));
        setEdition(ano.getEdition());
        setMatiere(ano.getMatiere());

        // Cpi Lot
        if (ano.getCpiLot() != null)
        {
            setCpiLot(ano.getCpiLot().getNom());
            setEmailCpi(ano.getCpiLot().getEmail());
        }

        // Dates
        setDateCrea(ano.getDateCreation().toString());
        if (ano.getDateReso() != null)
            setDateReso(ano.getDateReso().toString());
        if (ano.getDateRelance() != null)
            setDateRelance(ano.getDateRelance().toString());

        // Clarity
        if (ano.getClarity() != null)
        {
            ProjetClarity projetClarity = ano.getClarity();
            setCodeClarity(projetClarity.getCode());
            setLibelleClarity(projetClarity.getLibelleProjet());
            setDepartement(projetClarity.getDepartement());
            setDirection(projetClarity.getDirection());
            setService(projetClarity.getService());
            setRespServ(projetClarity.getChefService().getNom());
        }
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public ListeGetters[] getListeGetters()
    {
        return listeGetters;
    }

    @Override
    public Integer getIndex()
    {
        return Integer.valueOf(getNumero().get(0));
    }

    public static AnomalieRTCFXML build(AnomalieRTC ano)
    {
        return new AnomalieRTCFXML(ano);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getTitre()
    {
        return titre.get();
    }

    public final void setTitre(String titre)
    {
        this.titre.set(getString(titre));
    }

    public List<String> getNumero()
    {
        return numero.get();
    }

    public final void setNumero(List<String> numero)
    {
        if (numero != null)
            this.numero.set(FXCollections.observableList(numero));
    }

    public String getEtatAno()
    {
        return etatAno.get();
    }

    public final void setEtatAno(String etatAno)
    {
        this.etatAno.set(getString(etatAno));
    }

    public String getCommentaire()
    {
        return commentaire.get();
    }

    public final void setCommentaire(String commentaire)
    {
        this.commentaire.set(getString(commentaire));
    }

    public String getMatiere()
    {
        return matiere.get();
    }

    public final void setMatiere(String matiere)
    {
        this.matiere.set(getString(matiere));
    }

    public String getCpiLot()
    {
        return cpiLot.get();
    }

    public final void setCpiLot(String cpiLot)
    {
        this.cpiLot.set(getString(cpiLot));
    }

    public String getEmailCpi()
    {
        return emailCpi.get();
    }

    public final void setEmailCpi(String emailCpi)
    {
        this.emailCpi.set(getString(emailCpi));
    }

    public String getEdition()
    {
        return edition.get();
    }

    public final void setEdition(String edition)
    {
        this.edition.set(getString(edition));
    }

    public String getDateCrea()
    {
        return dateCrea.get();
    }

    public final void setDateCrea(String dateCrea)
    {
        this.dateCrea.set(getString(dateCrea));
    }

    public String getDateReso()
    {
        return dateReso.get();
    }

    public final void setDateReso(String dateReso)
    {
        this.dateReso.set(getString(dateReso));
    }

    public String getDateRelance()
    {
        return dateRelance.get();
    }

    public final void setDateRelance(String dateRelance)
    {
        this.dateRelance.set(getString(dateRelance));
    }

    public String getDirection()
    {
        return direction.get();
    }

    public final void setDirection(String direction)
    {
        this.direction.set(getString(direction));
    }

    public String getDepartement()
    {
        return departement.get();
    }

    public final void setDepartement(String departement)
    {
        this.departement.set(getString(departement));
    }

    public String getService()
    {
        return service.get();
    }

    public final void setService(String service)
    {
        this.service.set(getString(service));
    }

    public String getRespServ()
    {
        return respServ.get();
    }

    public final void setRespServ(String respServ)
    {
        this.respServ.set(getString(respServ));
    }

    public String getCodeClarity()
    {
        return codeClarity.get();
    }

    public final void setCodeClarity(String codeClarity)
    {
        this.codeClarity.set(getString(codeClarity));
    }

    public String getLibelleClarity()
    {
        return libelleClarity.get();
    }

    public final void setLibelleClarity(String libelleClarity)
    {
        this.libelleClarity.set(getString(libelleClarity));
    }

    /*---------- ENUMERATION INTERNE ----------*/

    /**
     * Enumeration permettant de paramètrer l'affichage sans introduire de specification dans le code par type d'objet.
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 2.0
     *
     */
    public enum AnomalieRTCFXMLGetters implements ListeGetters
    {
        // Selection
        SELECTED(SELECTION, EMPTY, "selected", "getSelected", true, TABLEBROWN),

        // Identifiants
        TITRE(INFORMATIONS, "Titre", "titre", "getTitre", true, TABLEYELLOW),
        NUMERO(INFORMATIONS, "Numéro", "numero", "getNumero", true, TABLEYELLOW),
        COMM(INFORMATIONS, "Commentaire", "commentaire", "getCommentaire", true, TABLEYELLOW),
        MATIERE(INFORMATIONS, "Matière", "matiere", "getMatiere", false, TABLEYELLOW),
        CPILOT(INFORMATIONS, "Cpi du Lot", "cpiLot", "getCpiLot", false, TABLEYELLOW),
        EMAILCPI(INFORMATIONS, "Email du Cpi", "emailCpi", "getEmailCpi", false, TABLEYELLOW),
        EDITION(INFORMATIONS, "Edition", "edition", "getEdition", false, TABLEYELLOW),

        // Dates
        DATECREA(DATES, "Date création", "dateCrea", "getDateCrea", true, TABLEGREEN),
        DATERESO(DATES, "Date résolution", "dateReso", "getDateReso", true, TABLEGREEN),
        DATERELANCE(DATES, "Date relance", "dateRelance", "getDateRelance", true, TABLEGREEN),

        // Projet Clarity
        DIRECTION(PROJETCLARITY, "Direction", "direction", "getDirection", false, TABLEBLUE),
        DEPARTEMENT(PROJETCLARITY, "Departement", "departement", "getDepartement", false, TABLEBLUE),
        SERVICE(PROJETCLARITY, "Service", "service", "getService", false, TABLEBLUE),
        RESPSERVICE(PROJETCLARITY, "Responsable Service", "respServ", "getRespServ", false, TABLEBLUE),
        CODECLARITY(PROJETCLARITY, "Code Clarity", "codeClarity", "getCodeClarity", false, TABLEBLUE),
        LIBCLARITY(PROJETCLARITY, "Libelle Clarity", "libelleClarity", "getLibelleClarity", false, TABLEBLUE);

        private String groupe;
        private String affichage;
        private String nomParam;
        private String nomMethode;
        private String style;
        private boolean isAffParDefaut;

        /*---------- ATTRIBUTS ----------*/
        /*---------- CONSTRUCTEURS ----------*/

        AnomalieRTCFXMLGetters(String groupe, String affichage, String nomParam, String nomMethode, boolean isAffParDefaut, String style)
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
