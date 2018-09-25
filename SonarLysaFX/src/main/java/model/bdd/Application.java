package model.bdd;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Classe de modèle pour les applications CATS
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@Entity
@Table(name = "applications")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Application.findAll", query="SELECT a FROM Application a"),
        @NamedQuery(name="Application.findByCode", query="SELECT a FROM Application a WHERE a.code = :code"),
        @NamedQuery(name="Application.resetTable", query="DELETE FROM Application")
})
//@formatter:on
public class Application extends AbstractBDDModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Column(name = "code", nullable = false, length = 128)
    private String code;

    @Column(name = "actif", nullable = false)
    private boolean actif;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "open", nullable = false)
    private boolean open;

    @Column(name = "mainFrame", nullable = false)
    private boolean mainFrame;

    @Column(name = "referentiel", nullable = false)
    private boolean referentiel;

    @Transient
    private String valSecurite;

    @Transient
    private int nbreVulnerabilites;

    @Transient
    private int ldcSonar;

    @Transient
    private int ldcMainframe;

    /*---------- CONSTRUCTEURS ----------*/

    Application() { }

    /**
     * Constructeur pour un code application inconnu
     * 
     * @param codeAppli
     */
    public static Application getApplicationInconnue(String codeAppli)
    {
        Application retour = new Application();
        retour.code = codeAppli;
        retour.actif = false;
        retour.libelle = "Appli inconnue du référentiel";
        retour.open = true;
        retour.mainFrame = false;
        retour.referentiel = false;
        return retour;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/


    @Override
    public String getMapIndex()
    {
        return getCode();
    }
    
    public Application update(Application update)
    {
        actif = update.actif;
        libelle = update.libelle;
        open = update.open;
        mainFrame = update.mainFrame;
        referentiel = update.referentiel;
        return this;
    }

    /**
     * 
     * @param valeur
     */
    public void ajouterldcSonar(int valeur)
    {
        ldcSonar += valeur;
    }

    /**
     * 
     * @param valeur
     */
    public void ajouterVulnerabilites(int valeur)
    {
        nbreVulnerabilites += valeur;
    }

    /**
     * 
     * @param securite
     */
    public void majValSecurite(String securite)
    {
        if (getValSecurite().compareTo(securite) < 0)
            valSecurite = securite;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getCode()
    {
        return getString(code);
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean isActif()
    {
        return actif;
    }

    public void setActif(boolean actif)
    {
        this.actif = actif;
    }

    public String getLibelle()
    {
        return getString(libelle);
    }

    public void setLibelle(String libelle)
    {
        this.libelle = libelle;
    }

    public boolean isOpen()
    {
        return open;
    }

    public void setOpen(boolean open)
    {
        this.open = open;
    }

    public boolean isMainFrame()
    {
        return mainFrame;
    }

    public void setMainFrame(boolean mainFrame)
    {
        this.mainFrame = mainFrame;
    }

    public String getValSecurite()
    {
        return getString(valSecurite);
    }

    public void setValSecurite(String valSecurite)
    {
        this.valSecurite = valSecurite;
    }

    public int getNbreVulnerabilites()
    {
        return nbreVulnerabilites;
    }

    public void setNbreVulnerabilites(int nbreVulnerabilites)
    {
        this.nbreVulnerabilites = nbreVulnerabilites;
    }

    public int getLdcSonar()
    {
        return ldcSonar;
    }

    public void setLdcSonar(int ldcSonar)
    {
        this.ldcSonar = ldcSonar;
    }

    public int getLdcMainframe()
    {
        return ldcMainframe;
    }

    public void setLdcMainframe(int ldcMainframe)
    {
        this.ldcMainframe = ldcMainframe;
    }

    public boolean isReferentiel()
    {
        return referentiel;
    }

    public void setReferentiel(boolean referentiel)
    {
        this.referentiel = referentiel;
    }
}
