package model.rest.sonarapi;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import model.AbstractModele;
import model.enums.TypeBranche;
import utilities.adapter.LocalDateTimeSonarAdapter;
import utilities.adapter.TypeBrancheAdapter;

/**
 * Classe de modele pour les branches des composants en retour du webservice.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class Branche extends AbstractModele implements ModeleSonar
{
    /*---------- ATTRIBUTS ----------*/

    private String nom;
    private boolean principal;
    private TypeBranche type;
    private StatutBranche statut;
    private LocalDateTime dateAnalyse;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "name", required = true)
    public String getNom()
    {
        return getString(nom);
    }

    public void setNom(String nom)
    {
        this.nom = getString(nom);
    }

    @XmlAttribute(name = "isMain", required = true)
    public boolean isPrincipale()
    {
        return principal;
    }

    public void setPrincipale(boolean principale)
    {
        this.principal = principale;
    }

    @XmlAttribute(name = "type", required = true)
    @XmlJavaTypeAdapter(value = TypeBrancheAdapter.class)
    public TypeBranche getType()
    {
        if (type == null)
            type = TypeBranche.LONG;
        return type;
    }

    public void setType(TypeBranche type)
    {
        if (type != null)
            this.type = type;
    }

    @XmlElement(name = "status", required = true)
    public StatutBranche getStatut()
    {
        return statut;
    }

    public void setStatut(StatutBranche statut)
    {
        if (statut != null)
            this.statut = statut;
    }

    @XmlAttribute(name = "analysisDate")
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
}
