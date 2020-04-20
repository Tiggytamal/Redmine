package model.rest.sonarapi;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.AbstractModele;
import model.enums.Metrique;
import model.enums.QG;
import model.enums.TypeObjetSonar;
import utilities.adapter.LocalDateTimeSonarAdapter;
import utilities.adapter.TypeObjetSonarAdapter;

/**
 * Classe de modele représentant un composant en retour du webservice Sonar.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@XmlRootElement(name = "component")
@JsonIgnoreProperties(
{ "isFavorite", "tags", "visibility", "ancestors" })
public class ComposantSonar extends AbstractModele implements ModeleSonar, Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    private String id;
    private String key;
    private String nom;
    private String description;
    private TypeObjetSonar type;
    private String langage;
    private String path;
    private List<Mesure> mesures;
    private String projet;
    private String organisation;
    private QG qualityGate;
    private String branche;
    private LocalDateTime dateAnalyse;
    private LocalDateTime dateLeakPeriod;
    private String version;

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantSonar()
    {
        // Constructeur pour l'initialisation XML avec mise à "master" de la branche.
        branche = "master";
        mesures = new ArrayList<>();
        qualityGate = QG.NONE;
    }

    public static ComposantSonar from(ComposantSonar init)
    {
        ComposantSonar retour = new ComposantSonar();
        retour.setId(init.getId());
        retour.setBranche(init.getBranche());
        retour.setKey(init.getKey());
        retour.setNom(init.getNom());
        retour.setDescription(init.getDescription());
        retour.setType(init.getType());
        retour.setLangage(init.getLangage());
        retour.setPath(init.getPath());
        retour.setMesures(init.getMesures());

        return retour;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet de retourner une map des metriques plutet qu'une liste.<br>
     * clef = identifation du metrique<br>
     * valeur = valeur du metrique
     * 
     * @return la map des metriques
     */
    @XmlTransient
    public Map<Metrique, Mesure> getMapMetriques()
    {
        if (mesures == null)
            return new EnumMap<>(Metrique.class);

        Map<Metrique, Mesure> retour = new EnumMap<>(Metrique.class);

        for (Mesure mesure : mesures)
        {
            if (mesure.getType() != null)
                retour.put(mesure.getType(), mesure);
        }
        return retour;
    }

    /**
     * Remonte la valeur d'une metrique en protegeant des nullPointeur avec une valeur par default.
     * 
     * @param type
     *              type du metrique.
     * @param value
     *              valeur par défaut à remonter en cas de valeur nulle.
     * @return
     *         La valeur du métrique ou une valeur à zéro si elle n'existe pas.
     */
    public String getValueMetrique(Metrique type, String value)
    {
        return getMapMetriques().computeIfAbsent(type, t -> new Mesure(type, value))
                .getValeur();
    }

    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "id", required = false)
    public String getId()
    {
        return getString(id);
    }

    public void setId(String id)
    {
        this.id = getString(id);
    }

    @XmlAttribute(name = "key", required = false)
    public String getKey()
    {
        return getString(key);
    }

    public void setKey(String key)
    {
        this.key = getString(key);
    }

    @XmlAttribute(name = "name", required = false)
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = getString(nom);
    }

    @XmlAttribute(name = "description", required = false)
    public String getDescription()
    {
        return getString(description);
    }

    public void setDescription(String descritpion)
    {
        this.description = getString(descritpion);
    }

    @XmlAttribute(name = "qualifier", required = false)
    @XmlJavaTypeAdapter(value = TypeObjetSonarAdapter.class)
    public TypeObjetSonar getType()
    {
        return type;
    }

    public void setType(TypeObjetSonar type)
    {
        if (type != null)
            this.type = type;
    }

    @XmlAttribute(name = "language", required = false)
    public String getLangage()
    {
        return getString(langage);
    }

    public void setLangage(String langage)
    {
        this.langage = getString(langage);
    }

    @XmlAttribute(name = "path", required = false)
    public String getPath()
    {
        return getString(path);
    }

    public void setPath(String path)
    {
        this.path = getString(path);
    }

    @XmlElementWrapper
    @XmlElement(name = "measures", required = false)
    public List<Mesure> getMesures()
    {
        if (mesures == null)
            mesures = new ArrayList<>();
        return mesures;
    }

    public void setMesures(List<Mesure> mesures)
    {
        if (mesures != null && !mesures.isEmpty())
            this.mesures = mesures;
    }

    @XmlAttribute(name = "project", required = false)
    public String getProjet()
    {
        return getString(projet);
    }

    public void setProjet(String projet)
    {
        this.projet = getString(projet);
    }

    @XmlAttribute(name = "organization", required = false)
    public String getOrganisation()
    {
        return getString(organisation);
    }

    public void setOrganisation(String organisation)
    {
        this.organisation = getString(organisation);
    }

    @XmlTransient
    public QG getQualityGate()
    {
        if (qualityGate == null)
            qualityGate = QG.NONE;
        return qualityGate;
    }

    public void setQualityGate(QG qualityGate)
    {
        if (qualityGate != null)
            this.qualityGate = qualityGate;
    }

    @XmlAttribute(name = "branch", required = false)
    public String getBranche()
    {
        return getString(branche);
    }

    public void setBranche(String branche)
    {
        if (branche != null && !branche.isEmpty())
            this.branche = branche;
    }

    @XmlAttribute(name = "analysisDate", required = false)
    @XmlJavaTypeAdapter(value = LocalDateTimeSonarAdapter.class)
    public LocalDateTime getDateAnalyse()
    {
        return dateAnalyse;
    }

    public void setDateAnalyse(LocalDateTime dateAnalyse)
    {
        if (dateAnalyse != null)
            this.dateAnalyse = dateAnalyse;
    }

    @XmlAttribute(name = "version")
    public String getVersion()
    {
        return getString(version);
    }

    public void setVersion(String version)
    {
        this.version = getString(version);
    }

    @XmlAttribute(name = "leakPeriodDate", required = false)
    @XmlJavaTypeAdapter(value = LocalDateTimeSonarAdapter.class)
    public LocalDateTime getDateLeakPeriod()
    {
        return dateLeakPeriod;
    }

    public void setDateLeakPeriod(LocalDateTime dateLeakPeriod)
    {
        if (dateLeakPeriod != null)
            this.dateLeakPeriod = dateLeakPeriod;
    }
}
