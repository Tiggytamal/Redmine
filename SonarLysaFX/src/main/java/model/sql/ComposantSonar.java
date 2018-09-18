package model.sql;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import model.enums.EtatAppli;
import model.enums.QG;
import model.utilities.AbstractModele;

/**
 * Classe de mod�le r�pr�sentant un composant SonarQube en base de donn�es
 * 
 * @author ETP8137 - Gr�goire Mathon
 * @since 1.0
 *
 */
@Entity
@Table(name="composants")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="ComposantSonar.findAll", query="SELECT c FROM ComposantSonar c"),
})
//@formatter:on
public class ComposantSonar extends AbstractModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int idBase;
    
    @Column (name = "nom", nullable = false, length = 128)
    private String nom;
    
    @Column (name = "lot", nullable = true, length = 6)
    private String lot;
    
    @Column (name = "id", nullable = false)
    private String id;
    
    @Column (name = "composantKey", nullable = false, length = 256)
    private String key;
    
    @Column (name = "appli", nullable = true)
    private String appli;
    
    @Column (name = "edition", nullable = true, length = 16)
    private String edition;
    
    @Enumerated (EnumType.STRING)
    @Column (name = "etatAppli", nullable = true)
    private EtatAppli etatAppli;
    
    @Column (name = "ldc", nullable = true)    
    private int ldc;
    
    @Column (name = "securityRating", nullable = true)
    private int securityRating;
    
    @Column (name = "vulnerabilites", nullable = true)
    private int vulnerabilites;
    
    @Column (name = "securite", nullable = true)    
    private boolean securite;
    
    @Enumerated (EnumType.STRING)  
    @Column (name = "qualityGate", nullable = true)
    private QG qualityGate;
    
    @Column (name = "bloquants", nullable = true)     
    private float bloquants;
    
    @Column (name = "critiques", nullable = true)      
    private float critiques;
    
    @Column (name = "duplication", nullable = true)     
    private float duplication;
    
    @Column (name = "versionRelease", nullable = true)      
    private boolean versionRelease;

    /*---------- CONSTRUCTEURS ----------*/

    ComposantSonar()
    {
        if (etatAppli == null)
            etatAppli = EtatAppli.OK;
    }

    ComposantSonar(String id, String key, String nom)
    {
        this.id = id;
        this.key = key;
        this.nom = nom;
    }

    /**
     * Constructeur pour cr�er un clone d'un composant Sonar
     * 
     * @param clone
     */
    ComposantSonar(ComposantSonar clone)
    {
        this.nom = clone.nom;
        this.lot = clone.lot;
        this.key = clone.key;
        this.id = clone.id;
        this.appli = clone.appli;
        this.edition = clone.edition;
        this.etatAppli = clone.etatAppli;
        this.ldc = clone.ldc;
        this.securityRating = clone.securityRating;
        this.vulnerabilites = clone.vulnerabilites;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public int getIdBase()
    {
        return idBase;
    }

    public void setIdBase(int idBase)
    {
        this.idBase = idBase;
    }

    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public String getLot()
    {
        return getString(lot);
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getAppli()
    {
        return getString(appli);
    }

    public void setAppli(String appli)
    {
        this.appli = appli;
    }

    public String getEdition()
    {
        return getString(edition);
    }

    public void setEdition(String edition)
    {
        this.edition = edition;
    }

    public String getId()
    {
        return getString(id);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getLdc()
    {
        return ldc;
    }

    public void setLdc(int ldc)
    {
        this.ldc = ldc;
    }

    /**
     * Setter de ldc qui parse les String en int directement
     * 
     * @param ldc
     */
    public void setLdc(String ldc)
    {
        this.ldc = Integer.parseInt(ldc);
    }

    public int getSecurityRating()
    {
        return securityRating;
    }

    public void setSecurityRating(int securityRating)
    {
        this.securityRating = securityRating;
    }

    public int getVulnerabilites()
    {
        return vulnerabilites;
    }

    public void setVulnerabilites(int vulnerabilites)
    {
        this.vulnerabilites = vulnerabilites;
    }

    /**
     * Setter de vulnerabilite qui parse les String directement en int
     * 
     * @param vulnerabilites
     */
    public void setVulnerabilites(String vulnerabilites)
    {
        this.vulnerabilites = Integer.parseInt(vulnerabilites);
    }

    public EtatAppli getEtatAppli()
    {
        return etatAppli;
    }

    public void setEtatAppli(EtatAppli etatAppli)
    {
        this.etatAppli = etatAppli;
    }

    public boolean isSecurite()
    {
        return securite;
    }

    public void setSecurite(boolean securite)
    {
        this.securite = securite;
    }

    public QG getQualityGate()
    {
        return qualityGate == null ? QG.NONE : qualityGate;
    }

    public void setQualityGate(QG qualityGate)
    {
        this.qualityGate = qualityGate;
    }

    /**
     * Seter du QualityGate depuis une cha�ne de carat�re.
     * 
     * @param qualityGate
     */
    public void setQualityGate(String qualityGate)
    {
        this.qualityGate = QG.from(qualityGate);
    }

    public float getBloquants()
    {
        return bloquants;
    }

    public void setBloquants(float bloquants)
    {
        this.bloquants = bloquants;
    }

    public float getCritiques()
    {
        return critiques;
    }

    public void setCritiques(float critiques)
    {
        this.critiques = critiques;
    }

    public float getDuplication()
    {
        return duplication;
    }

    public void setDuplication(float duplication)
    {
        this.duplication = duplication;
    }

    public boolean isVersionRelease()
    {
        return versionRelease;
    }

    public void setVersionRelease(boolean versionRelease)
    {
        this.versionRelease = versionRelease;
    }
}
