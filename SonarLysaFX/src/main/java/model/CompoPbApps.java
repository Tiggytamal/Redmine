package model;

import model.bdd.ProjetClarity;
import model.enums.EtatAppli;
import model.utilities.AbstractModele;

/**
 * Classe de modèle représentant les informations des composants avec un problème de code application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class CompoPbApps extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    private static final String DEPARTVIDE = "Projet sans département";
    private static final String SERVVIDE = "Projet sans service";

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
    public boolean majDepuisClarity(ProjetClarity info)
    {
        // Indique un manque d'info si le département est vide.
        if (info.getDepartement().isEmpty())
            setDepart(DEPARTVIDE);
        else
            setDepart(info.getDepartement());

        // Indique un manque d'info si le service est vide et renvoie "faux".
        if (info.getService().isEmpty())
        {
            setService(SERVVIDE);
            return false;
        }
        setService(info.getDepartement());
        return true;
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
        return lotRTC;
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

    public static String getDepartvide()
    {
        return DEPARTVIDE;
    }

    public static String getServvide()
    {
        return SERVVIDE;
    }
}
