package model.bdd;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import model.AbstractModele;

/**
 * Classe mère abstraite des modèles pour la base de données. Contient la clef primaire, le timestamp et la partie commune pour equals et hashcode.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractBDDModele<T> extends AbstractModele implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDBASE", nullable = false)
    protected int idBase;

    @Column(name = "timestamp", nullable = false)
    protected LocalDateTime timeStamp;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractBDDModele()
    {
        updateTimeStamp();
    }

    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Retourne la valeur servant de clef pour les {@link java.util.Map}
     * 
     * @return
     *         L'index.
     */
    public abstract T getMapIndex();

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        @SuppressWarnings("unchecked")
        AbstractBDDModele<T> other = (AbstractBDDModele<T>) obj;
        return Objects.equals(idBase, other.idBase);
        //@formatter:on
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        return PRIME * result + Objects.hash(idBase);
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public int getIdBase()
    {
        return idBase;
    }

    public LocalDateTime getTimeStamp()
    {
        if (timeStamp == null)
            updateTimeStamp();
        return timeStamp;
    }

    /**
     * Mise à jour du timestamp
     */
    public final void updateTimeStamp()
    {
        timeStamp = LocalDateTime.now();
    }
}
