package model.parsing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import model.AbstractModele;
import model.enums.InstanceSonar;
import model.enums.QG;
import utilities.Statics;

/**
 * Classe de modele d'un composant crée à partir du fichier JSOM d'extraction de SonarQube.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
@JsonRootName(value = "composant")
public class ComposantJSON extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private QG qualityGate;
    private ProjetJSON projet;
    private BrancheJSON branche;
    private InstanceSonar instance;
    private int numeroLot;
    private String idAnalyse;
    private boolean traite;

    /*---------- CONSTRUCTEURS ----------*/

    public ComposantJSON()
    {
        qualityGate = QG.NONE;
        instance = InstanceSonar.LEGACY;
        idAnalyse = Statics.EMPTY;
    }
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @JsonProperty(value = "statut")
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

    public ProjetJSON getProjet()
    {
        return projet;
    }

    public void setProjet(ProjetJSON projet)
    {
        if (projet != null)
            this.projet = projet;
    }

    public BrancheJSON getBranche()
    {
        return branche;
    }

    public void setBranche(BrancheJSON branche)
    {
        if (branche != null)
            this.branche = branche;
    }

    @JsonProperty(value = "typeSonar")
    public InstanceSonar getInstance()
    {
        if (instance == null)
            instance = InstanceSonar.LEGACY;
        return instance;
    }

    public void setInstance(InstanceSonar instance)
    {
        if (instance != null)
            this.instance = instance;
    }

    @JsonProperty(value = "lot")
    @JsonFormat(shape = Shape.STRING)
    public int getNumeroLot()
    {
        return numeroLot;
    }

    public void setNumeroLot(int numeroLot)
    {
        if (numeroLot != 0)
            this.numeroLot = numeroLot;
    }

    @JsonProperty(value = "uuidAnalyse")
    public String getIdAnalyse()
    {
        if (idAnalyse == null)
            idAnalyse = Statics.EMPTY;
        return idAnalyse;
    }

    public void setIdAnalyse(String idAnalyse)
    {
        if (idAnalyse != null && !idAnalyse.isEmpty())
            this.idAnalyse = idAnalyse;
    }

    @JsonProperty(value = "traite")
    public boolean isTraite()
    {
        return traite;
    }

    public void setTraite(boolean traite)
    {
        this.traite = traite;
    }
}
