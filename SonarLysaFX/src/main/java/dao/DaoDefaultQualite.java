package dao;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import model.bdd.AbstractBDDModele;
import model.bdd.DefaultQualite;
import model.enums.Matiere;
import model.enums.TypeDonnee;

public class DaoDefaultQualite extends AbstractDao<DefaultQualite> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/

    DaoDefaultQualite()
    {
        typeDonnee = TypeDonnee.ANOMALIE;
    }
    
    DaoDefaultQualite(EntityManager em)
    {
        super(em);
        typeDonnee = TypeDonnee.ANOMALIE;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        return 0;
    }

    @Override
    public DefaultQualite recupEltParCode(String lot)
    {
        List<DefaultQualite> liste = em.createNamedQuery("DefaultQualite" + AbstractBDDModele.FINDINDEX, DefaultQualite.class).setParameter("code", lot).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
    }

    @Override
    public int resetTable()
    {
        int retour = 0;
        em.getTransaction().begin();
        retour = em.createNamedQuery("DefaultQualite.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE defaults_qualite AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    public Map<String, DefaultQualite> readAllMapMatiere(Matiere matiere)
    {
        Map<String, DefaultQualite> retour = new HashMap<>();

        // Itération pour ne prendre que les anomalies de la matière désirée
        for (DefaultQualite dq : readAll())
        {
            if (dq.getLotRTC().getMatieres().contains(matiere))
                retour.put(dq.getMapIndex(), dq);
        }

        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
