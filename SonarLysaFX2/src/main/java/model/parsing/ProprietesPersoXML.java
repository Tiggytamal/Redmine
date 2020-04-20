package model.parsing;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.AbstractModele;
import model.enums.Param;
import model.enums.ParamSpec;

@XmlRootElement
public class ProprietesPersoXML extends AbstractModele implements XML
{
    /*---------- ATTRIBUTS ----------*/

    private File file;

    // Map des paramètres personnalisables
    private Map<Param, String> params;
    private Map<ParamSpec, String> paramsSpec;

    /*---------- CONSTRUCTEURS ----------*/

    ProprietesPersoXML()
    {
        params = new EnumMap<>(Param.class);
        paramsSpec = new EnumMap<>(ParamSpec.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String controleDonnees()
    {
        return null;
    }

    public void copie(ProprietesPersoXML propriete)
    {
        this.params = propriete.params;
        this.paramsSpec = propriete.paramsSpec;
        this.file = propriete.file;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Override
    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        if (file != null)
            this.file = file;
    }

    @XmlElementWrapper
    @XmlElement(name = "params", required = false)
    public Map<Param, String> getParams()
    {
        if (params == null)
            params = new EnumMap<>(Param.class);
        return params;
    }

    @XmlElementWrapper
    @XmlElement(name = "paramsSpec", required = false)
    public Map<ParamSpec, String> getParamsSpec()
    {
        if (paramsSpec == null)
            paramsSpec = new EnumMap<>(ParamSpec.class);
        return paramsSpec;
    }
}
