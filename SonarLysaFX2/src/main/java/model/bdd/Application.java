package model.bdd;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import dao.AbstractMySQLDao;
import model.enums.Param;
import utilities.Statics;

/**
 * Classe de modele pour les applications CATS.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
@Entity(name = "Application")
@Access(AccessType.FIELD)
@Table(name = "applications")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Application" + AbstractMySQLDao.FINDALL, query="SELECT a FROM Application a"),
        @NamedQuery(name="Application" + AbstractMySQLDao.FINDINDEX, query="SELECT a FROM Application a WHERE a.code = :index"),
        @NamedQuery(name="Application" + AbstractMySQLDao.RESET, query="DELETE FROM Application")
})
//@formatter:on
public class Application extends AbstractBDDModele<String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final int MAXCODELENGTH = 16;

    @Column(name = "code", nullable = false, length = 128)
    private String code;

    @Column(name = "referentiel", nullable = false)
    private boolean referentiel;

    @Column(name = "direction", nullable = false, length = 64)
    private String direction;

    /*---------- CONSTRUCTEURS ----------*/

    Application()
    {}

    /**
     * Constructeur pour un code application
     * 
     * @param codeAppli
     *                    Le code appli de l'application
     * @param referentiel
     *                    booléen pour savoir si le code application et bien référencé.
     * @return
     *         l'Application
     */
    public static Application getApplication(String codeAppli, boolean referentiel)
    {
        Application retour = new Application();
        retour.setCode(codeAppli);
        retour.setReferentiel(referentiel);
        return retour;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public String getMapIndex()
    {
        return getCode();
    }

    public Application update(Application update)
    {
        referentiel = update.referentiel;
        return this;
    }

    public String getLiens()
    {
        return Statics.proprietesXML.getMapParams().get(Param.URLAPPCATSAPPLI) + getCode();
    }

    @Override
    public int hashCode()
    {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + Objects.hash(code);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      //@formatter:off
        if (!super.equals(obj))
            return false;
        Application other = (Application) obj;
        return Objects.equals(code, other.code) 
                && referentiel == other.referentiel;
        //@formatter:on
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getCode()
    {
        return getString(code);
    }

    public void setCode(String code)
    {
        if (code == null)
            return;
        if (code.length() > MAXCODELENGTH)
            this.code = code.substring(0, MAXCODELENGTH);
        else
            this.code = code;
    }

    public boolean isReferentiel()
    {
        return referentiel;
    }

    public void setReferentiel(boolean referentiel)
    {
        this.referentiel = referentiel;
    }

    public String getDirection()
    {
        return getString(direction);
    }

    public void setDirection(String direction)
    {
        this.direction = getString(direction);
    }
}
