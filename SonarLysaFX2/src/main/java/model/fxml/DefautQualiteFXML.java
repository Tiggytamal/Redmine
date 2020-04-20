package model.fxml;

import static model.fxml.Styles.TABLEBLUE;
import static model.fxml.Styles.TABLEBROWN;
import static model.fxml.Styles.TABLEGREEN;
import static model.fxml.Styles.TABLERED;
import static model.fxml.Styles.TABLEYELLOW;
import static utilities.Statics.EMPTY;

import java.time.Period;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe représentant l'affichage d'un {@link model.bdd.DefautQualite}
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class DefautQualiteFXML extends AbstractFXMLModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques pour la création de l'énumération

    // Groupes
    private static final String PROJETCLARITY = "Projet Clarity";
    private static final String PROJETRTC = "Projet RTC";
    private static final String GROUPECOMPOSANT = "Composant";
    private static final String ANO = "Anomalie";
    private static final String DEFAUT = "Defaut";

    // Index
    private String index;

    // Properties pour pouvoir transférer les données à l'affichage

    // Clarity
    private final SimpleStringProperty direction = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty departement = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty service = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty respServ = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty codeClarity = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty libelleClarity = new SimpleStringProperty(EMPTY);

    // Lot RTC
    private final SimpleStringProperty cpiLot = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty emailCpi = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty edition = new SimpleStringProperty(EMPTY);

    // Création d'une liste pour récupérer en même temps le numero du lot et le liens vers RTC pour transmettre à la cellule
    private final ListProperty<String> lotRTC = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final SimpleStringProperty etatLotRTC = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty codeProjetRTC = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateMajRTC = new SimpleStringProperty(EMPTY);

    // Composant
    // Création d'une liste pour récupérer en même temps le nom du composant et le liens vers SonarQube pour transmettre à la cellule
    private final ListProperty<String> composant = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final SimpleStringProperty version = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty solution = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty matiere = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty qg = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty appliOK = new SimpleStringProperty(EMPTY);

    // Anomalie
    // Création d'une liste pour récupérer en même temps le numero de l'anomalie et le liens vers RTC pour transmettre à la cellule
    private final ListProperty<String> numeroAnoRTC = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final SimpleStringProperty securite = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateDetection = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateCreation = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateRelance = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateReouv = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateReso = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateMepPrev = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dureeAno = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty etatRTC = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty remarque = new SimpleStringProperty(EMPTY);

    // Defaut
    private final SimpleStringProperty etatDefaut = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty typeDefaut = new SimpleStringProperty(EMPTY);

    /*---------- CONSTRUCTEURS ----------*/

    public DefautQualiteFXML()
    {
        // Constructeur public sans paramètres pour l'affichage
        listeGetters = DefautQualiteFXMLGetters.values();
    }

    /**
     * Constructeur avec initialisation des données depuis les informations de la base de données
     * 
     * @param dq
     *           défaut qualité à traiter.
     */
    private DefautQualiteFXML(DefautQualite dq)
    {
        this();

        LotRTC lot = dq.getLotRTC();
        ComposantBase compo = dq.getCompo();

        // Contrôle nullité lot et composant
        if (lot == null || compo == null)
            throw new TechnicalException("model.fxml.DefautQualiteFXML Constructor - le lot et/ou le composant sont nuls :\n- compo null : " + (compo == null) + "\n- lot null : " + (lot == null));

        // Variables sans contrôle
        index = compo.getMapIndex() + lot.getNumero();
        setLotRTC(Arrays.asList(lot.getNumero(), lot.getLiens()));
        setEtatLotRTC(lot.getEtatLot().getValeur());
        setCodeProjetRTC(lot.getProjetRTC());
        setEdition(lot.getEdition().getNom());
        setNumeroAnoRTC(Arrays.asList(String.valueOf(dq.getNumeroAnoRTC()), dq.getLiensAno()));
        setEtatRTC(dq.getEtatAnoRTC());
        setRemarque(dq.getRemarque());
        setVersion(compo.getVersion());
        setComposant(Arrays.asList(String.valueOf(compo.getNom()), compo.getLiens()));
        setQg(compo.getQualityGate().getValeur());
        setAppliOK(dq.getEtatCodeAppli().getValeur());
        setEtatDefaut(dq.getEtatDefaut().toString());
        setTypeDefaut(dq.getTypeDefaut().toString());
        setDateDetection(dq.getDateDetection().toString());

        setSecurite(compo.getSecurityRating());

        initDureeAno(dq);

        // Variables avec contrôle de nullité

        if (lot.getCpiProjet() != null)
        {
            setCpiLot(lot.getCpiProjet().getNom());
            setEmailCpi(lot.getCpiProjet().getEmail());
        }

        if (lot.getProjetClarity() != null)
        {
            ProjetClarity projetClarity = lot.getProjetClarity();
            setCodeClarity(projetClarity.getCode());
            setLibelleClarity(projetClarity.getLibelleProjet());
            setDepartement(projetClarity.getDepartement());
            setDirection(projetClarity.getDirection());
            setService(projetClarity.getService());
            setRespServ(projetClarity.getChefService().getNom());
        }

        if (lot.getDateMajEtat() != null)
            setDateMajRTC(lot.getDateMajEtat().toString());
        if (dq.getDateCreation() != null)
            setDateCreation(dq.getDateCreation().toString());
        if (dq.getDateRelance() != null)
            setDateRelance(dq.getDateRelance().toString());
        if (dq.getDateReso() != null)
            setDateReso(dq.getDateReso().toString());
        if (dq.getDateReouv() != null)
            setDateReouv(dq.getDateReouv().toString());
        if (dq.getDateMepPrev() != null)
            setDateMepPrev(dq.getDateMepPrev().toString());
        if (compo.getMatiere() != null)
            setMatiere(compo.getMatiere().getValeur());
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
        return getString(index);
    }

    public static DefautQualiteFXML build(DefautQualite dq)
    {
        return new DefautQualiteFXML(dq);
    }

    /*---------- METHODES PRIVEES ----------*/

    private void initDureeAno(DefautQualite dq)
    {
        if (dq.getDateReouv() != null)
            setDureeAno(String.valueOf(Period.between(dq.getDateReouv(), Statics.TODAY).getDays()));
        if (dq.getDateCreation() != null)
            setDureeAno(String.valueOf(Period.between(dq.getDateCreation(), Statics.TODAY).getDays()));
    }

    /*---------- ACCESSEURS ----------*/

    public List<String> getLotRTC()
    {
        return lotRTC.get();
    }

    public final void setLotRTC(List<String> lotRTC)
    {
        if (lotRTC != null)
            this.lotRTC.set(FXCollections.observableList(lotRTC));
    }

    public List<String> getNumeroAnoRTC()
    {
        return numeroAnoRTC.get();
    }

    public final void setNumeroAnoRTC(List<String> numeroAnoRTC)
    {
        if (numeroAnoRTC != null)
            this.numeroAnoRTC.set(FXCollections.observableList(numeroAnoRTC));
    }

    public String getEtatRTC()
    {
        return etatRTC.get();
    }

    public final void setEtatRTC(String etatRTC)
    {
        this.etatRTC.set(getString(etatRTC));
    }

    public String getSecurite()
    {
        return securite.get();
    }

    public final void setSecurite(String securite)
    {
        this.securite.set(getString(securite));
    }

    public String getRemarque()
    {
        return remarque.get();
    }

    public final void setRemarque(String remarque)
    {
        this.remarque.set(getString(remarque));
    }

    public String getVersion()
    {
        return version.get();
    }

    public final void setVersion(String version)
    {
        this.version.set(getString(version));
    }

    public String getDateCreation()
    {
        return dateCreation.get();
    }

    public final void setDateCreation(String dateCreation)
    {
        this.dateCreation.set(getString(dateCreation));
    }

    public String getDateDetection()
    {
        return dateDetection.get();
    }

    public final void setDateDetection(String dateDetection)
    {
        this.dateDetection.set(getString(dateDetection));
    }

    public String getDateRelance()
    {
        return dateRelance.get();
    }

    public final void setDateRelance(String dateRelance)
    {
        this.dateRelance.set(getString(dateRelance));
    }

    public String getDateReso()
    {
        return dateReso.get();
    }

    public final void setDateReso(String dateReso)
    {
        this.dateReso.set(getString(dateReso));
    }

    public String getEtatDefaut()
    {
        return etatDefaut.get();
    }

    public final void setEtatDefaut(String etatDefaut)
    {
        this.etatDefaut.set(getString(etatDefaut));
    }

    public String getTypeDefaut()
    {
        return typeDefaut.get();
    }

    public final void setTypeDefaut(String typeDefaut)
    {
        this.typeDefaut.set(getString(typeDefaut));
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

    public String getSolution()
    {
        return solution.get();
    }

    public final void setSolution(String solution)
    {
        this.solution.set(getString(solution));
    }

    public String getMatiere()
    {
        return matiere.get();
    }

    public final void setMatiere(String matiere)
    {
        this.matiere.set(getString(matiere));
    }

    public String getCodeProjetRTC()
    {
        return codeProjetRTC.get();
    }

    public final void setCodeProjetRTC(String codeProjetRTC)
    {
        this.codeProjetRTC.set(getString(codeProjetRTC));
    }

    public String getDateMajRTC()
    {
        return dateMajRTC.get();
    }

    public final void setDateMajRTC(String dateMajRTC)
    {
        this.dateMajRTC.set(getString(dateMajRTC));
    }

    public String getEtatLotRTC()
    {
        return etatLotRTC.get();
    }

    public final void setEtatLotRTC(String etatLotRTC)
    {
        this.etatLotRTC.set(getString(etatLotRTC));
    }

    public String getDateReouv()
    {
        return dateReouv.get();
    }

    public final void setDateReouv(String dateReouv)
    {
        this.dateReouv.set(getString(dateReouv));
    }

    public String getDateMepPrev()
    {
        return dateMepPrev.get();
    }

    public final void setDateMepPrev(String dateMepPrev)
    {
        this.dateMepPrev.set(getString(dateMepPrev));
    }

    public String getDureeAno()
    {
        return dureeAno.get();
    }

    public final void setDureeAno(String dureeAno)
    {
        this.dureeAno.set(getString(dureeAno));
    }

    public List<String> getComposant()
    {
        return composant.get();
    }

    public final void setComposant(List<String> composant)
    {
        if (composant != null)
            this.composant.set(FXCollections.observableList(composant));
    }

    public String getQg()
    {
        return qg.get();
    }

    public final void setQg(String qg)
    {
        this.qg.set(getString(qg));
    }

    public String getAppliOK()
    {
        return appliOK.get();
    }

    public final void setAppliOK(String appliOK)
    {
        this.appliOK.set(getString(appliOK));
    }

    /*---------- ENUMERATION INTERNE ----------*/

    /**
     * Enumeration permettant de paramètrer l'affichage sans introduire de specification dans le code par type d'objet.
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 2.0
     *
     */
    public enum DefautQualiteFXMLGetters implements ListeGetters
    {
        /*---------- ATTRIBUTS ----------*/

        // Selection
        SELECTED(SELECTION, EMPTY, "selected", "getSelected", true, TABLEBROWN),

        // Projet Clarity
        DIRECTION(PROJETCLARITY, "Direction", "direction", "getDirection", false, TABLEBLUE),
        DEPARTEMENT(PROJETCLARITY, "Departement", "departement", "getDepartement", false, TABLEBLUE),
        SERVICE(PROJETCLARITY, "Service", "service", "getService", false, TABLEBLUE),
        RESPSERVICE(PROJETCLARITY, "Responsable Service", "respServ", "getRespServ", false, TABLEBLUE),
        CODECLARITY(PROJETCLARITY, "Code Clarity", "codeClarity", "getCodeClarity", false, TABLEBLUE),
        LIBCLARITY(PROJETCLARITY, "Libelle Clarity", "libelleClarity", "getLibelleClarity", false, TABLEBLUE),

        // Projet RTC
        CPILOT(PROJETRTC, "Cpi du Lot", "cpiLot", "getCpiLot", false, TABLEGREEN),
        EMAILCPI(PROJETRTC, "Email du Cpi", "emailCpi", "getEmailCpi", false, TABLEGREEN),
        EDITION(PROJETRTC, "Edition", "edition", "getEdition", false, TABLEGREEN),
        LOTRTC(PROJETRTC, "Lot", "lotRTC", "getLotRTC", true, TABLEGREEN),
        ETATLOTRTC(PROJETRTC, "Etat Lot RTC", "etatLotRTC", "getEtatLotRTC", false, TABLEGREEN),
        CODEPROJETRTC(PROJETRTC, "Libelle Projet RTC", "codeProjetRTC", "getCodeProjetRTC", false, TABLEGREEN),
        DATEMAJRTC(PROJETRTC, "Date Maj RTC", "dateMajRTC", "getDateMajRTC", false, TABLEGREEN),

        // Composant
        COMPO(GROUPECOMPOSANT, "Nom", "composant", "getComposant", true, TABLERED),
        VERSION(GROUPECOMPOSANT, "Version", "version", "getVersion", false, TABLERED),
        SOLUTION(GROUPECOMPOSANT, "Solution", "solution", "getSolution", false, TABLERED),
        MATIERE(GROUPECOMPOSANT, "Matiere", "matiere", "getMatiere", true, TABLERED),
        QG(GROUPECOMPOSANT, "Quality Gate", "qg", "getQg", true, TABLERED),
        APPLIOK(GROUPECOMPOSANT, "Application OK", "appliOK", "getAppliOK", true, TABLERED),

        // Anomalie
        NUMANO(ANO, "Numero", "numeroAnoRTC", "getNumeroAnoRTC", true, TABLEYELLOW),
        SECURITE(ANO, "Securite", "securite", "getSecurite", true, TABLEYELLOW),
        DATECREA(ANO, "Date création", "dateCreation", "getDateCreation", true, TABLEYELLOW),
        DATEDETECTION(ANO, "Date detection", "dateDetection", "getDateDetection", false, TABLEYELLOW),
        DATERELANCE(ANO, "Date relance", "dateRelance", "getDateRelance", false, TABLEYELLOW),
        DATEREOUV(ANO, "Date Reouverture", "dateReouv", "getDateReouv", false, TABLEYELLOW),
        DATERESO(ANO, "Date resolution", "dateReso", "getDateReso", false, TABLEYELLOW),
        DATEMEPPREV(ANO, "Date mep prévue", "dateMepPrev", "getDateMepPrev", false, TABLEYELLOW),
        DUREEANO(ANO, "Duree anomalie", "dureeAno", "getDureeAno", false, TABLEYELLOW),
        ETATRTC(ANO, "Etat anomalie", "etatRTC", "getEtatRTC", false, TABLEYELLOW),
        REMARQUE(ANO, "Remarques", "remarque", "getRemarque", true, TABLEYELLOW),

        // Defaut
        ETATDEFAUT(DEFAUT, "Etat defaut", "etatDefaut", "getEtatDefaut", false, TABLEBROWN),
        TYPEDEFAUT(DEFAUT, "Type default", "typeDefaut", "getTypeDefaut", true, TABLEBROWN);

        private String groupe;
        private String affichage;
        private String nomParam;
        private String nomMethode;
        private String style;
        private boolean isAffParDefaut;

        /*---------- CONSTRUCTEURS ----------*/

        DefautQualiteFXMLGetters(String groupe, String affichage, String nomParam, String nomMethode, boolean isAffParDefaut, String style)
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
        public String toString()
        {
            return getAffichage();
        }

        @Override
        public boolean isAffParDefaut()
        {
            return isAffParDefaut;
        }
    }
}
