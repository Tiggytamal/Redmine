package dao;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import model.bdd.DefaultQualite;
import model.enums.Matiere;
import model.enums.TypeDonnee;

public class DaoDefaultQualite extends AbstractDao<DefaultQualite> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "defaults_qualite";

    /*---------- CONSTRUCTEURS ----------*/

    DaoDefaultQualite()
    {
        super(TABLE);
        typeDonnee = TypeDonnee.DEFAULTQUALITE;
    }

    DaoDefaultQualite(EntityManager em)
    {
        super(TABLE, em);
        typeDonnee = TypeDonnee.DEFAULTQUALITE;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        return 0;
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
