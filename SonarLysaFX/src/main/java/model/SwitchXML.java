package model;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import junit.JunitBase;
import model.enums.Switch;
import model.utilities.AbstractModele;
import model.utilities.XML;
import utilities.Statics;

@XmlRootElement
public class SwitchXML extends AbstractModele implements XML
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final String NOMFICHIER = "\\switch.xml";
    private static final String RESOURCE = "/switch.xml";

    private Switch bddOuXML;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/

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
        switch (bddOuXML)
        {
            case BDD:
                return "Paramétrage base de données";
                
            case XML:
                return "Paramétrage fichiers XML";
                
            default:
                return "Fichier Switch introuvable, merci de le recréer.";
        }
    }

    /*---------- ACCESSEURS ----------*/
    
    /**
     * @return the heure
     */
    @XmlAttribute(name = "switch")
    public Switch getSwitch()
    {
        return bddOuXML;
    }
    
    public void setSwitch(Switch bddOuXML)
    {
        this.bddOuXML = bddOuXML;
    }

}
