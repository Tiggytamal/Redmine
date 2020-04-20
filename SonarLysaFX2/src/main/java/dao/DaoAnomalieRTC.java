package dao;

import java.util.List;

import model.bdd.AnomalieRTC;

/**
 * DOA pour la classe {@link model.bdd.AnomalieRTC}
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class DaoAnomalieRTC extends AbstractMySQLDao<AnomalieRTC, Integer>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "anomalieRTC";

    /*---------- CONSTRUCTEURS ----------*/

    DaoAnomalieRTC()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public AnomalieRTC recupEltParIndex(Integer index)
    {
        List<AnomalieRTC> liste = em.createNamedQuery(modele.getSimpleName() + FINDINDEX, modele).setParameter(INDEX, index).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
    }

    @Override
    protected void persistImpl(AnomalieRTC t)
    {
        // Pas d'implementation nécessaire
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
