package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import junit.JunitBase;
import model.bdd.ComposantSonar;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.interfaces.AbstractModele;
import model.interfaces.XML;
import utilities.Statics;

/**
 * Représentation XML de la base de données pour les tests
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
@XmlRootElement
public class DataBaseXML extends AbstractModele implements XML
{

    /*---------- ATTRIBUTS ----------*/
    
    // Constantes statiques
    private static final String NOMFICHIER = "\\database.xml";
    private static final String RESOURCE = "/database.xml";
    
    // Tables
    private List<ComposantSonar> compos;
    private List<DefautQualite> dqs;
    private List<LotRTC> lotsRTC;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    DataBaseXML()
    {
        compos = new ArrayList<>();
        dqs = new ArrayList<>();
        lotsRTC = new ArrayList<>();
    }
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public File getFile()
    {
        return new File(Statics.JARPATH + NOMFICHIER);
    }

    @Override
    public File getResource()
    {
        return new File(JunitBase.class.getResource(RESOURCE).getFile());
    }

    @Override
    public String controleDonnees()
    {
        return Statics.EMPTY;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
   
    @XmlElementWrapper
    @XmlElement(name = "composant", required = false)
    public List<ComposantSonar> getCompos()
    {
        return compos;
    }
    
    @XmlElementWrapper
    @XmlElement(name = "defautQualite", required = false)
    public List<DefautQualite> getDqs()
    {
        return dqs;
    }
    
    @XmlElementWrapper
    @XmlElement(name = "lotRTC", required = false)
    public List<LotRTC> getLotsRTC()
    {
        return lotsRTC;
    }
}
