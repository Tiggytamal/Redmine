package model;

/**
 * Classe de modèle pour les informations extraites des traitements à envoyer par mail.
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
public class InfoMail implements Modele
{
    /*---------- ATTRIBUTS ----------*/

    private String lot;
    private String infoSupp;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    public InfoMail(String lot, String infoSupp)
    {
        super();
        this.lot = lot;
        this.infoSupp = infoSupp;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((infoSupp == null) ? 0 : infoSupp.hashCode());
        result = PRIME * result + ((lot == null) ? 0 : lot.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InfoMail other = (InfoMail) obj;
        if (infoSupp == null)
        {
            if (other.infoSupp != null)
                return false;
        }
        else if (!infoSupp.equals(other.infoSupp))
            return false;
        if (lot == null)
        {
            if (other.lot != null)
                return false;
        }
        else if (!lot.equals(other.lot))
            return false;
        return true;
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public String getLot()
    {
        return getString(lot);
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }
    public String getInfoSupp()
    {
        return getString(infoSupp);
    }
    public void setInfoSupp(String infoSupp)
    {
        this.infoSupp = infoSupp;
    }
}
