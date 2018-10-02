package model.bdd;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import model.utilities.AbstractModele;

@MappedSuperclass
public abstract class AbstractBDDModele extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/
    
    public static final String FINDALL = ".findAll";
    public static final String FINDINDEX = ".findByIndex";
    public static final String RESETTABLE = ".resetTable";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int idBase;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Retourne la valeur servant de clef pour les {@link java.util.Map}
     * 
     * @return
     */
    public abstract String getMapIndex();

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public int getIdBase()
    {
        return idBase;
    }
}
