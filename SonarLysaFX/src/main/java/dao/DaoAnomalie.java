package dao;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.bdd.AbstractBDDModele;
import model.bdd.Anomalie;
import model.enums.Matiere;
import model.enums.TypeDonnee;

public class DaoAnomalie extends AbstractDao<Anomalie> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/

    DaoAnomalie()
    {
        typeDonnee = TypeDonnee.ANOMALIE;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        return 0;
    }

    @Override
    public Anomalie recupEltParCode(String lot)
    {
        List<Anomalie> liste = em.createNamedQuery("Anomalie" + AbstractBDDModele.FINDINDEX, Anomalie.class).setParameter("code", lot).getResultList();
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
        retour = em.createNamedQuery("Anomalie.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE anomalies AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    public Map<String, Anomalie> readAllMapMatiere(Matiere matiere)
    {
        Map<String, Anomalie> retour = new HashMap<>();

        // Itération pour ne prendre que les anomalies de la matière désirée
        for (Anomalie ano : readAll())
        {
            if (ano.getLotRTC().getMatieres().contains(matiere))
                retour.put(ano.getMapIndex(), ano);
        }

        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
