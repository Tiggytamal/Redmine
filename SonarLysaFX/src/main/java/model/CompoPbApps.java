package model;

import model.bdd.ProjetClarity;
import model.enums.EtatAppli;
import model.interfaces.AbstractModele;
import utilities.Statics;

/**
 * Classe de modèle représentant les informations des composants avec un problème de code application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class CompoPbApps extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private String codeComposant;
    private String codeAppli;
    private String cpiLot;
    private String depart;
    private String service;
    private String chefService;
    private String lotRTC;
    private EtatAppli etatAppli;

    /*---------- CONSTRUCTEURS ----------*/

    CompoPbApps() { }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Met à jour le département et le service depuis les infos Clarity. Renvoie "vrai" si le service est renseigné et "faux" sinon.
     * 
     * @param info
     * @return
     */
    public void majDepuisClarity(ProjetClarity info)
    {
        if (info == null)
            return;

        setDepart(info.getDepartement());
        setService(info.getService());

        if (info.getChefService() != null)
            setChefService(info.getChefService().getNom());
        else
            setChefService(Statics.INCONNU);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getCodeComposant()
    {
        return getString(codeComposant);
    }

    public void setCodeComposant(String codeComposant)
    {
        this.codeComposant = codeComposant;
    }

    public String getCodeAppli()
    {
        return getString(codeAppli);
    }

    public void setCodeAppli(String codeAppli)
    {
        this.codeAppli = codeAppli;
    }

    public String getCpiLot()
    {
        return getString(cpiLot);
    }

    public void setCpiLot(String cpiLot)
    {
        this.cpiLot = cpiLot;
    }

    public String getDepart()
    {
        return getString(depart);
    }

    public void setDepart(String depart)
    {
        this.depart = depart;
    }

    public String getService()
    {
        return getString(service);
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String getChefService()
    {
        return getString(chefService);
    }

    public void setChefService(String chefService)
    {
        this.chefService = chefService;
    }

    public String getLotRTC()
    {
        return getString(lotRTC);
    }

    public void setLotRTC(String lotRTC)
    {
        this.lotRTC = lotRTC;
    }

    public EtatAppli getEtatAppli()
    {
        return etatAppli;
    }

    public void setEtatAppli(EtatAppli etatAppli)
    {
        this.etatAppli = etatAppli;
    }
}
