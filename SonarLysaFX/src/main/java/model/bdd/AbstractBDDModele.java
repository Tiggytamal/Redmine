package model.bdd;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import model.interfaces.AbstractModele;

@MappedSuperclass
public abstract class AbstractBDDModele extends AbstractModele
{
    /*---------- ATTRIBUTS ----------*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int idBase;
    
    @Column(name = "timestamp", nullable = false)
    protected LocalDateTime timeStamp;

    /*---------- CONSTRUCTEURS ----------*/    
    
    protected AbstractBDDModele()
    {
        initTimeStamp();
    }
    
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
    
    public LocalDateTime getTimeStamp()
    {
        if (timeStamp == null)
            timeStamp = LocalDateTime.now();
        return timeStamp;
    }
    
    public void initTimeStamp()
    {
        timeStamp = LocalDateTime.now();
    }
}
